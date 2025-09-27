import AephyrShared
import KMPNativeCoroutinesAsync
#if canImport(UIKit)
import UIKit
#endif

@MainActor
final class AppSettings: ObservableObject {
    @Published private(set) var energy: AephyrShared.EnergyUnitPref
    private let repo: SettingsRepository
    private var energyTask: Task<Void, Never>?

    init(repo: SettingsRepository = SettingsRepository(settings: SettingsProvider().settings)) {
        self.repo = repo
        self.energy = repo.currentEnergy

        energyTask = Task { [weak self] in
            do {
                for try await value in asyncSequence(for: repo.energy) {
                    self?.energy = value
                }
            } catch is CancellationError {
                // ignore task cancellation
            } catch {
                // optional: log/report the flow error
                print("Energy flow error: \(error)")
            }
        }

        #if canImport(UIKit)
        NotificationCenter.default.addObserver(
            forName: UIApplication.didBecomeActiveNotification,
            object: nil,
            queue: .main
        ) { [weak self] _ in
            Task { @MainActor in self?.repo.refreshFromSystem() }
        }
        #endif
    }

    func setEnergy(_ pref: AephyrShared.EnergyUnitPref) {
        repo.setEnergy(pref: pref)
    }

    deinit {
        energyTask?.cancel()
        #if canImport(UIKit)
        NotificationCenter.default.removeObserver(self)
        #endif
    }
}

