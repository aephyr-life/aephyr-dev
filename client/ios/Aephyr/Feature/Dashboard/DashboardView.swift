import SwiftUI
import AephyrShared

@MainActor
struct DashboardView: View {

    @StateObject private var vm: DashboardVM
    @State private var isLoading = true

    init(repo: AephyrSharedDashboardRepository? = nil) {
        let repository = repo ?? MockDashboardRepository()
        let facade = AephyrSharedDashboardFacade(repo: repository)
        _vm = StateObject(wrappedValue: DashboardVM(facade: facade))
    }

    var body: some View {
        ZStack {
            AephyrBackground()

            content
                .opacity(isLoading ? 0.5 : 1.0)

            if isLoading {
                ProgressView()
                    .progressViewStyle(.circular)
            }
        }
        .navigationBarTitleDisplayMode(.inline)
        .navigationTitle(todayTitle)
        .task { @MainActor in
            await reload()
        }
        .refreshable { @MainActor in
            await reload()
        }
    }

    // MARK: - UI
    @ViewBuilder private var content: some View {
        List {
            // HERO
            Section {
                if isLoading {
                    DashboardHeroSkeleton()
                } else if let hero = vm.hero {
                    DashboardHeroCard(data: hero)
                } else {
                    DashboardHeroEmpty()
                }
            }
            .listRowSeparator(.hidden)

            // ENTRIES (navigate to detail; delete there)
            Section {
                ForEach(vm.entries, id: \.id) { item in
                    NavigationLink {
                        FoodDetailView(
                            item: item,
                            onDelete: { await vm.remove(id: item.id) }
                        )
                    } label: {
                        FoodRow(row: item)
                    }
                }
            }
        }
        .listStyle(.plain)
        .scrollContentBackground(.hidden)
        .background(Color.clear)
    }

    // MARK: - Helpers
    private var todayTitle: String {
        let df = DateFormatter()
        df.locale = .current
        df.doesRelativeDateFormatting = true
        df.dateStyle = .medium
        df.timeStyle = .none
        return df.string(from: Date()) // always “Today” (localized) when applicable
    }

    private func reload() async {
        precondition(Thread.isMainThread, "reload() not on main thread")
        isLoading = true
        await vm.load()
        isLoading = false
    }
}

