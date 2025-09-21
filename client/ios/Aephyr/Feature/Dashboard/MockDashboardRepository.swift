import Foundation
import AephyrShared

/// A quick mock implementation for SwiftUI previews or dev.
final class MockDashboardRepository: AephyrSharedDashboardRepository {
    func fetchToday() async throws -> AephyrSharedDashboardData {
        // simulate network delay
        try await Task.sleep(nanoseconds: 300_000_000)
        let hero = AephyrSharedHero(title: "Welcome back")
        let items = [
            AephyrSharedFoodItem(id: UUID().uuidString, name: "Greek Yogurt", grams: 200, kcal: 120),
            AephyrSharedFoodItem(id: UUID().uuidString, name: "Banana", grams: 120, kcal: 105)
        ]
        return AephyrSharedDashboardData(hero: hero, entries: items)
    }

    func remove(id: String) async throws {
        try await Task.sleep(nanoseconds: 150_000_000)
    }
}
