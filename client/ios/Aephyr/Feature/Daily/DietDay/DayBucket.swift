import Foundation

enum DayBucket: String, CaseIterable {
    case morning, afternoon, evening, night, unspecified
}

func bucket(for t: Date?, cal: Calendar = .current) -> DayBucket {
    guard let t else { return .unspecified }
    let h = cal.component(.hour, from: t)
    switch h {
    case 0..<6:   return .night
    case 6..<12:  return .morning
    case 12..<17: return .afternoon
    default:      return .evening
    }
}
