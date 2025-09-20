//
//  DietDay.swift
//  Aephyr
//
//  Created by Martin Pallmann on 19.09.25.
//


import SwiftUI

// MARK: - Day key + model (lean)

struct DietDay: Hashable, Codable {
    var year: Int; var month: Int; var day: Int
}

extension DietDay {
    init(_ date: Date, cal: Calendar = .current) {
        let c = cal.dateComponents([.year, .month, .day], from: date)
        self.year = c.year!; self.month = c.month!; self.day = c.day!
    }
}

extension Calendar {
    func date(from day: DietDay) -> Date {
        date(from: DateComponents(year: day.year, month: day.month, day: day.day))!
    }
}

struct LoggedFood: Identifiable, Codable {
    let id: UUID
    var day: DietDay               // logical consumption day (anchor in UI)
    var timeOfDay: Date?           // optional
    var name: String
    var grams: Int
    var kcal: Int
    let loggedAt: Date             // audit
}

// MARK: - Sorting + (optional) buckets

private enum DayBucket: String, CaseIterable {
    case morning, afternoon, evening, night, unspecified
}

private func bucket(for t: Date?, cal: Calendar = .current) -> DayBucket {
    guard let t else { return .unspecified }
    let h = cal.component(.hour, from: t)
    switch h {
    case 0..<6:   return .night
    case 6..<12:  return .morning
    case 12..<17: return .afternoon
    default:      return .evening
    }
}

private extension Array where Element == LoggedFood {
    func sortedForDisplay() -> [LoggedFood] {
        sorted { a, b in
            switch (a.timeOfDay, b.timeOfDay) {
            case let (ta?, tb?): return ta < tb
            case (_?, nil):      return true
            case (nil, _?):      return false
            case (nil, nil):     return a.loggedAt < b.loggedAt
            }
        }
    }
}

// MARK: - Public list view

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
        .padding(.horizontal, 16)
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

// MARK: - Row

private struct FoodRow: View {
    let row: LoggedFood
    var body: some View {
        HStack(spacing: 12) {
            VStack(alignment: .leading, spacing: 2) {
                Text(row.name)
                if let t = row.timeOfDay {
                    Text(t, style: .time)
                        .font(.caption)
                        .foregroundStyle(.secondary)
                }
            }
            Spacer(minLength: 8)
            VStack(alignment: .trailing, spacing: 2) {
                Text("\(row.kcal) kcal")
                    .monospacedDigit()
                    .foregroundStyle(.secondary)
                if row.grams > 0 {
                    Text("\(row.grams) g")
                        .font(.caption)
                        .foregroundStyle(.tertiary)
                }
            }
        }
        .contentShape(Rectangle())
    }
}
