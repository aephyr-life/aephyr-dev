import SwiftUI
import AephyrShared

struct DashboardView: View {

    @StateObject private var vm: DashboardVM
    @State private var isLoading = true
    

    init(store: FoodStore = FoodStore.mock(), day: Date = Date()) {
        _vm = StateObject(wrappedValue: DashboardVM(store: store, day: day))
    }

    var body: some View {
        ZStack {
            AephyrBackground()

            // page content keyed by day (so it transitions)
            content
                .id(vm.day)
                .animation(isLoading ? .none : .easeInOut(duration: 0.40), value: vm.day)

        }
        .navigationBarTitleDisplayMode(.inline)
        .task {
            isLoading = true
            await vm.load()
            isLoading = false
        }
        .refreshable {
            isLoading = true
            await vm.load()
            isLoading = false
        }
    }

    // MARK: - UI
    @ViewBuilder private var content: some View {
        VStack(alignment: .leading, spacing: 8) {
            Text("Today")
                .font(.largeTitle.weight(.semibold))
                .padding(.horizontal, 16)
                .padding(.top, 12)

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
                //.listRowBackground(Color.clear)
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
                        //.listRowBackground(Color.clear)
                        //.listRowSeparator(.hidden)
                    }
                }
                //.listRowBackground(Color.clear)
            }
            //.listStyle(.plain)
            .scrollContentBackground(.hidden)
            .background(Color.clear)
        }
    }
}

