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
                // Explicitly cast to the expected Kotlin Flow type
                let flow = facade.observeState() as! Kotlinx_coroutines_coreFlow
                for try await ui in asyncSequence(
                    for: flow,
                    Output.self,
                    Error.self
                ) {
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
        do {
            let suspendFun = facade.refresh() as! KotlinSuspendFunction0
            _ = try await asyncFunction(
                for: suspendFun,
                KotlinUnit.self,
                Error.self
            )
        } catch { /* handle */ }
    }

    func remove(id: String) async {
        do {
            let suspendFun = facade.remove(id: id) as! KotlinSuspendFunction1
            _ = try await asyncFunction(
                for: suspendFun,
                KotlinUnit.self,
                Error.self
            )
        } catch { /* handle */ }
    }
}

