import Foundation
import AephyrShared

/// A quick mock implementation for SwiftUI previews or dev.
final class MockDashboardRepository: DashboardRepository {
    func fetchToday() async throws -> DashboardData {
        // simulate network delay
        try await Task.sleep(nanoseconds: 300_000_000)
        let hero = Hero(title: "Welcome back", subtitle: "Test")
        let items = [
            FoodItem(id: UUID().uuidString, name: "Greek Yogurt", grams: 200, kcal: 120),
            FoodItem(id: UUID().uuidString, name: "Banana", grams: 120, kcal: 105)
        ]
        return DashboardData(hero: hero, entries: items)
    }

    func remove(id: String) async throws {
        try await Task.sleep(nanoseconds: 150_000_000)
    }
}
