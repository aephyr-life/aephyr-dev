import SwiftUI
import AephyrShared

final class HealthViewModel: ObservableObject {
    @Published var status: String = "…"
    @Published var error: String?

    private let api = ApiClient(baseUrl: "https://dev.aephyr.life")

    func load() {
        api.health { [weak self] text, err in
            DispatchQueue.main.async {
                if let err = err {
                    self?.status = "error"
                    self?.error = String(describing: err)
                } else {
                    self?.status = text ?? "(empty)"
                    self?.error = nil
                }
            }
        }
    }
}
