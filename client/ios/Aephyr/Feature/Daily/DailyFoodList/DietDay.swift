import Foundation

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
