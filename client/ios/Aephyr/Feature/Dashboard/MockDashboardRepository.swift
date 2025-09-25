import Foundation
import AephyrShared

/// A quick mock implementation for SwiftUI previews or dev.
final class MockDashboardRepository: DashboardRepository {
    func fetchToday() async throws -> DashboardData {
        // simulate network delay
        try await Task.sleep(nanoseconds: 300_000_000)
        let hero = Hero(title: "Welcome back", subtitle: "Test")
        return DashboardData(hero: hero, entries: [])
    }

    func remove(id: String) async throws {
        try await Task.sleep(nanoseconds: 150_000_000)
    }
}
