//
//  DietMoment.swift
//  Aephyr
//
//  Created by Martin Pallmann on 21.09.25.
//


import Foundation
import AephyrShared

struct DietMoment: Equatable, Hashable, Comparable {
    /// Local-day anchored at midnight (normalized).
    let day: Date
    /// Minutes since midnight; nil means “unspecified”.
    let timeMinutes: Int?

    init(day: Date, timeMinutes: Int? = nil, calendar: Calendar = .current) {
        self.day = calendar.startOfDay(for: day)
        self.timeMinutes = timeMinutes
    }

    // Same ordering as KMM: by day, then time; nil time goes last.
    static func < (lhs: DietMoment, rhs: DietMoment) -> Bool {
        if lhs.day != rhs.day { return lhs.day < rhs.day } // both are midnight
        let t1 = lhs.timeMinutes ?? .max
        let t2 = rhs.timeMinutes ?? .max
        return t1 < t2
    }

    var clockDate: Date? {
        guard let m = timeMinutes else { return nil }
        return Calendar.current.date(byAdding: .minute, value: m, to: day)
    }

    // Handy labels
    var isToday: Bool { Calendar.current.isDateInToday(day) }
    var isYesterday: Bool { Calendar.current.isDateInYesterday(day) }
    var isTomorrow: Bool { Calendar.current.isDateInTomorrow(day) }

    func dayTitle() -> String {
        if isToday { return NSLocalizedString("Today", comment: "") }
        if isYesterday { return NSLocalizedString("Yesterday", comment: "") }
        if isTomorrow { return NSLocalizedString("Tomorrow", comment: "") }
        return DateFormatter.dayTitle.string(from: day)
    }

    var timeString: String? {
        guard let d = clockDate else { return nil }
        return DateFormatter.timeShort.string(from: d)
    }
}

private extension DateFormatter {
    static let dayTitle: DateFormatter = {
        let f = DateFormatter()
        f.dateStyle = .medium; f.timeStyle = .none
        return f
    }()
    static let timeShort: DateFormatter = {
        let f = DateFormatter()
        f.dateStyle = .none; f.timeStyle = .short
        return f
    }()
}
