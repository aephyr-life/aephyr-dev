import Foundation
import AephyrShared
import KMPNativeCoroutinesAsync

@MainActor
final class DashboardVM: ObservableObject {
    @Published var isLoading = true
    @Published var hero: Hero?
    @Published var entries: [FoodItem] = []

    private let facade: DashboardFacade
    private var bindTask: Task<Void, Never>?

    init(facade: DashboardFacade) {
        self.facade = facade
        bindState()
    }

    deinit { bindTask?.cancel() }

    private func bindState() {
        bindTask = Task { @MainActor [weak self, facade] in
            do {
                // Let the type infer to NativeFlowAsyncSequence
                for try await ui in asyncSequence(for: facade.observeState()) {
                    guard let self else { return }
                    self.isLoading = ui.isLoading
                    self.hero = ui.hero
                    self.entries = ui.entries
                }
            } catch {
                // optional: log/show error
            }
        }
    }

    func reload() async {
        do { try await asyncFunction(for: facade.refresh()) } catch { /* handle */ }
    }

    func remove(id: String) async {
        do { try await asyncFunction(for: facade.remove(id: id)) } catch { /* handle */ }
    }
}

