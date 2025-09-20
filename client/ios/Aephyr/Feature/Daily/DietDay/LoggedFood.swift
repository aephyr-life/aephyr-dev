import Foundation

struct LoggedFood: Identifiable, Codable {
    let id: UUID
    var day: DietDay               // logical consumption day (anchor in UI)
    var timeOfDay: Date?           // optional
    var name: String
    var grams: Int
    var kcal: Int
    let loggedAt: Date             // audit
}

extension Array where Element == LoggedFood {
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
