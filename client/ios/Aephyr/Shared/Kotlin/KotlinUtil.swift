//
//  KotlinUtil.swift
//  Aephyr
//
//  Created by Martin Pallmann on 20.09.25.
//

import Foundation
import AephyrShared

// Date ⇄ LocalDate
extension Date {
    func toKmmLocalDate(calendar: Calendar = .current) -> Kotlinx_datetimeLocalDate {
        let c = calendar.dateComponents([.year, .month, .day], from: self)
        return Kotlinx_datetimeLocalDate(
            year: Int32(c.year ?? 1970),
            monthNumber: Int32(c.month ?? 1),
            dayOfMonth: Int32(c.day ?? 1)
        )
    }
}

extension Kotlinx_datetimeLocalDate {
    func toDateAtMidnight(calendar: Calendar = .current) -> Date {
        var comps = DateComponents()
        comps.year  = Int(self.year)
        comps.month = Int(self.monthNumber)
        comps.day   = Int(self.dayOfMonth)
        comps.hour = 0; comps.minute = 0; comps.second = 0
        return calendar.date(from: comps) ?? Date()
    }
}

// Instant → Date
extension Kotlinx_datetimeInstant {
    func toDate() -> Date {
        let msAny = self.toEpochMilliseconds() as Any
        let ms: Int64
        if let k = msAny as? KotlinLong { ms = k.int64Value }
        else if let i = msAny as? Int64 { ms = i }
        else { ms = 0 }
        return Date(timeIntervalSince1970: TimeInterval(ms) / 1000.0)
    }
}

// Swift Int? → KotlinInt?
extension Optional where Wrapped == Int {
    var kInt: KotlinInt? {
        guard let v = self else { return nil }
        // KotlinInt conforms to ExpressibleByIntegerLiteral on the Swift side,
        // and its integerLiteral init takes a Swift Int.
        return KotlinInt(integerLiteral: v)
    }
}

// KMM <-> Swift
extension K_DietMoment {
    func toSwift() -> DietMoment {
        DietMoment(
            day: day.toDateAtMidnight(),
            timeMinutes: timeMinutes.map { Int($0.int32Value) }

        )
    }
}
extension DietMoment {
    func toKmm() -> K_DietMoment {
        K_DietMoment(day: day.toKmmLocalDate(), timeMinutes: timeMinutes.kInt)
    }
}

@inline(__always) func KInt(_ v: KotlinInt?) -> Int? { v.map { Int($0.int32Value) } }
@inline(__always) func KDouble(_ v: KotlinDouble?) -> Double? { v.map { $0.doubleValue } }
// If you ever use Long?/Float?:
@inline(__always) func KLong(_ v: KotlinLong?) -> Int64? { v.map { $0.int64Value } }
@inline(__always) func KFloat(_ v: KotlinFloat?) -> Float? { v.map { v in v.floatValue } }

extension Optional where Wrapped == KotlinInt {
    var asInt: Int? { self.map { Int($0.int32Value) } }
    var orZero: Int { asInt ?? 0 }
}

extension Optional where Wrapped == KotlinDouble {
    var asDouble: Double? { self.map { $0.doubleValue } }
}
