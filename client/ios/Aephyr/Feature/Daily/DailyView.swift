import SwiftUI
import AephyrShared

struct DailyView: View {
    @StateObject private var vm: DailyVM
    @State private var isLoading = true

    init(store: IOSFoodStore = IOSFoodStoreFactory().mock(), day: Date = Date()) {
        _vm = StateObject(wrappedValue: DailyVM(store: store, day: day))
    }

    private func dayTitle(_ date: Date) -> String {
        let cal = Calendar.current
        if cal.isDateInToday(date)     { return NSLocalizedString("Today", comment: "") }
        if cal.isDateInYesterday(date) { return NSLocalizedString("Yesterday", comment: "") }
        if cal.isDateInTomorrow(date)  { return NSLocalizedString("Tomorrow", comment: "") }
        return DateFormatter.dayTitle.string(from: date)
    }

    var body: some View {
        List {
            // Hero row
            Section {
                VStack(alignment: .leading, spacing: 8) {
                    Text(dayTitle(vm.day))
                        .font(.largeTitle.weight(.semibold))
                        .padding(.top, 12)

                    if isLoading {
                        DailyHeroSkeleton()
                    } else if let hero = vm.hero {
                        DailyHeroCard(data: hero)
                    } else {
                        DailyHeroEmpty()
                    }
                }
                .listRowInsets(EdgeInsets(top: 0, leading: 16, bottom: 12, trailing: 16))
                .listRowSeparator(.hidden)
                .listRowBackground(Color.clear)
            }

            // Entries
            Section {
                ForEach(vm.entries, id: \.id) { item in
                    FoodRow(row: item)
                        .listRowInsets(EdgeInsets(top: 8, leading: 16, bottom: 8, trailing: 16))
                        .listRowSeparator(.hidden)
                        .listRowBackground(Color.clear)
                        .swipeActions(edge: .trailing, allowsFullSwipe: true) {
                            Button(role: .destructive) {
                                Task { await vm.remove(id: item.id) }
                            } label: {
                                Label("Delete", systemImage: "trash")
                            }
                        }
                }
            }
        }
        .listStyle(.plain)
        .scrollContentBackground(.hidden)
        .background(AephyrBackground())
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
}

private extension DateFormatter {
    static let dayTitle: DateFormatter = {
        let f = DateFormatter()
        f.dateStyle = .medium
        f.timeStyle = .none
        return f
    }()
}

