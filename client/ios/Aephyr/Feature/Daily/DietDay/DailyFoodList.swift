import SwiftUI

struct DailyFoodList: View {
    let day: DietDay
    let items: [LoggedFood]
    var showsBuckets: Bool = true   // set false to show a flat list

    private let order: [DayBucket] = [.morning, .afternoon, .evening, .night, .unspecified]

    var body: some View {
        let todays = items.filter { $0.day == day }.sorted { a, b in
            switch (a.timeOfDay, b.timeOfDay) {
            case let (ta?, tb?): return ta < tb
            case (_?, nil):      return true
            case (nil, _?):      return false
            case (nil, nil):     return a.loggedAt < b.loggedAt
            }
        }

        LazyVStack(spacing: 12, pinnedViews: []) {
            ForEach(todays) { FoodRow(row: $0) }
        }
        .padding(16)
        .overlay(
            RoundedRectangle(cornerRadius: 20, style: .continuous)
                .strokeBorder(.white.opacity(0.08))
        )
        .background(CardBackground())
    }

    private func title(for b: DayBucket) -> String {
        switch b {
        case .morning: "Morning"
        case .afternoon: "Afternoon"
        case .evening: "Evening"
        case .night: "Night"
        case .unspecified: "Unspecified"
        }
    }
}
