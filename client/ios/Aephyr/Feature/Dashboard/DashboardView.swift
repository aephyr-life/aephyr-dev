import SwiftUI
import AephyrShared

@MainActor
struct DashboardView: View {
    @StateObject private var vm: DashboardVM

    init() {
        let factory = K_FoodStoreFactory()
        let foodSvc = factory.mock()   // KMM seeded service
        let facade  = DashboardFacade(food: foodSvc)
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

    // MARK: - UI
    @ViewBuilder private var content: some View {
        ScrollView {
            VStack(spacing: 16) {
                // HERO card (builds DashboardHero from entries)
                if let hero = makeDashboardHero(from: vm.entries) {
                    Card {
                        DashboardHeroCard(data: hero)
                    }
                } else {
                    Card { DashboardHeroSkeleton() }
                }

                // ENTRIES card
                Card {
                    if vm.entries.isEmpty && !vm.isLoading {
                        Text("No foods logged yet.")
                            .foregroundStyle(.secondary)
                            .frame(maxWidth: .infinity, alignment: .center)
                            .padding(.vertical, 24)
                    } else {
                        VStack(spacing: 12) {
                            ForEach(vm.entries, id: \.id) { item in
                                FoodRowKMMAdapter(item: item)
                                Divider().opacity(0.08)
                            }
                        }
                    }
                }
            }
            .padding(.vertical, 12)
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

    private struct Card<Content: View>: View {
        @ViewBuilder var content: () -> Content
        var body: some View {
            VStack(alignment: .leading, spacing: 12) {
                content()
            }
            .padding(16)
            .frame(maxWidth: .infinity, alignment: .leading)
            .background(CardBackground())
            .clipShape(RoundedRectangle(cornerRadius: 20, style: .continuous))
            .overlay(
                RoundedRectangle(cornerRadius: 20, style: .continuous)
                    .strokeBorder(.white.opacity(0.08))
            )
            .padding(.horizontal, 16)
        }
    }


    // MARK: - Helpers
    private var todayTitle: String {
        let df = DateFormatter()
        df.locale = .current
        df.doesRelativeDateFormatting = true
        df.dateStyle = .medium
        df.timeStyle = .none
        return df.string(from: Date())
    }

    /// Build the donut’s data from KMM entries (macros are zero until you track them).
    private func makeDashboardHero(from items: [FoodItem]) -> DashboardHero? {
        guard !items.isEmpty else { return nil }

        let totalKcal = items.reduce(0) { $0 + Int($1.kcal) }
        let energy = Measurement<UnitEnergy>(value: Double(totalKcal), unit: .kilocalories)

        // Adjust if your MacrosSummary initializer differs.
        let zero = Measurement(value: 0, unit: UnitMass.grams)
        let macros = MacrosSummary(protein: zero, fat: zero, carb: zero)

        return DashboardHero(energy: energy, macros: macros)
    }
}

// Lightweight row to render a KMM FoodItem directly.
// Swap back to your app’s FoodRow(LoggedItem) once you add a mapper.
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

