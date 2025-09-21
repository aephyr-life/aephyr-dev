import SwiftUI
import AephyrShared

@MainActor
struct DashboardView: View {
    @StateObject private var vm: DashboardVM

    init(repo: DashboardRepository? = nil) {
        let repository = repo ?? MockDashboardRepository()
        let facade = DashboardFacade(repo: repository) // Kotlin secondary ctor that injects MainScope
        _vm = StateObject(wrappedValue: DashboardVM(facade: facade))
    }

    var body: some View {
        ZStack {
            AephyrBackground()

            content
                .opacity(vm.isLoading ? 0.5 : 1.0)

            if vm.isLoading {
                ProgressView().progressViewStyle(.circular)
            }
        }
        .navigationBarTitleDisplayMode(.inline)
        .navigationTitle(todayTitle)
        .task { @MainActor in await vm.reload() }
        .refreshable { @MainActor in await vm.reload() }
    }

    @ViewBuilder private var content: some View {
        List {
            // HERO — temporarily show empty until we map KMM -> DashboardHero
            Section {
                if vm.isLoading {
                    DashboardHeroSkeleton()
                } else {
                    DashboardHeroEmpty()
                }
            }
            .listRowSeparator(.hidden)

            // ENTRIES — simple KMM adapter row (no detail nav yet)
            Section {
                ForEach(vm.entries, id: \.id) { item in
                    FoodRowKMMAdapter(item: item)
                }
            }
        }
        .listStyle(.plain)
        .scrollContentBackground(.hidden)
        .background(Color.clear)
    }

    private var todayTitle: String {
        let df = DateFormatter()
        df.locale = .current
        df.doesRelativeDateFormatting = true
        df.dateStyle = .medium
        df.timeStyle = .none
        return df.string(from: Date())
    }
}

// Lightweight row that renders the KMM FoodItem directly.
// Replace with your nice FoodRow once mapping is in place.
private struct FoodRowKMMAdapter: View {
    let item: FoodItem
    var body: some View {
        HStack(spacing: 12) {
            VStack(alignment: .leading, spacing: 2) {
                Text(item.name)
                Text("\(item.grams) g")
                    .font(.caption)
                    .foregroundStyle(.secondary)
            }
            Spacer(minLength: 8)
            VStack(alignment: .trailing, spacing: 2) {
                Text("\(item.kcal) kcal")
                    .monospacedDigit()
                    .foregroundStyle(.secondary)
            }
        }
        .contentShape(Rectangle())
    }
}

