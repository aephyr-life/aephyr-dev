import Foundation
import SwiftUI

@MainActor
final class DashboardVM: ObservableObject {
    @Published var day: Date
    @Published var hero: DashboardHero?
    @Published var entries: [FoodStore.LoggedItem] = []

    private let store: FoodStore
    private let roundingKcalStep: Int = 50

    init(store: FoodStore, day: Date) {
        self.store = store
        self.day = day
    }

    func load() async {
        do {
            let list = try await store.entries(for: day)
            self.entries = list
            self.hero = DashboardHero.make(from: list)
        } catch {
            self.entries = []
            self.hero = nil
            print("Failed to load day: \(error)")
        }
    }

    /// Optimistic delete with rollback on failure.
    @discardableResult
    func remove(id: String) async -> Bool {
        let before = entries
        withAnimation { entries.removeAll { $0.id == id } }
        do {
            let ok = try await store.remove(id: id)   // Bool from wrapper
            if ok {
                // recompute hero from current snapshot
                self.hero = DashboardHero.make(from: entries)
                return true
            } else {
                withAnimation { self.entries = before }
                return false
            }
        } catch {
            withAnimation { self.entries = before }
            return false
        }
    }
}

