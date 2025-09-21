import Foundation
import Combine
import AephyrShared
import KMPNativeCoroutinesCore
import KMPNativeCoroutinesAsync

@MainActor
final class DashboardVM: ObservableObject {
    @Published var isLoading: Bool = true
    @Published var hero: AephyrSharedHero?
    @Published var entries: [AephyrSharedFoodItem] = []

    private let facade: AephyrSharedDashboardFacade
    private var bindTask: Task<Void, Never>?

    init(facade: AephyrSharedDashboardFacade) {
        self.facade = facade
        bindState()
    }

    deinit {
        bindTask?.cancel()
    }

    private func bindState() {
        bindTask = Task {
            for try await ui in asyncSequence(for: facade.state) {
                self.isLoading = ui.isLoading
                self.hero = ui.hero
                self.entries = ui.entries
            }
        }
    }

    /// Trigger a manual refresh
    func reload() async {
        do {
            try await asyncFunction(for: facade.refresh())
        } catch {
            // handle error if needed
        }
    }

    /// Remove an item by id
    func remove(id: String) async {
        do {
            try await asyncFunction(for: facade.remove(id: id))
        } catch {
            // handle error if needed
        }
    }
}

