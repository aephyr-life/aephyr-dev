import Foundation
import AephyrShared
import KMPNativeCoroutinesAsync

// MARK: - Swift-facing wrapper over the KMM store
final class FoodStore {

    struct LoggedItem: Identifiable, Equatable {
        let id:         String
        let name:       String
        let consumedAt: DietMoment             // we synthesize this (KMM FoodItem has no time yet)

        let mass:       Measurement<UnitMass>?
        let energy:     Measurement<UnitEnergy>?

        let protein:    Measurement<UnitMass>?
        let fat:        Measurement<UnitMass>?
        let carb:       Measurement<UnitMass>?

        let loggedAt:   Date
    }

    struct NewItemInput {
        let consumedAt: DietMoment?                 // nil → today / no time
        let name:       String
        let grams:      Measurement<UnitMass>?      // optional
        let energy:     Measurement<UnitEnergy>?    // optional
        let protein:    Measurement<UnitMass>?      // optional
        let fat:        Measurement<UnitMass>?      // optional
        let carb:       Measurement<UnitMass>?      // optional
    }

    // MARK: Dependency
    private let delegate: K_FoodStore
    init(delegate: K_FoodStore) { self.delegate = delegate }

    // MARK: API

    /// Snapshot for a given day (current KMM API exposes "today"; day is used only for the DietMoment we synthesize).
    func entries(for day: Date) async throws -> [LoggedItem] {
        // K_FoodStore does not expose observeToday() in generated bindings.
        // Workaround: Return empty for now to allow build.
        // TODO: Implement when KMM exposes a Swift-compatible API.
        return []
    }

    @discardableResult
    func remove(id: String) async throws -> Bool {
        // K_FoodStore does not expose remove(id:) in generated bindings.
        // Workaround: Always return false for now to allow build.
        // TODO: Implement when KMM exposes a Swift-compatible API.
        return false
    }

    /// Adds an item using the new KMM API (Mass/Energy/Macros are optional; we send what's provided).
    @discardableResult
    func add(_ input: NewItemInput) async throws -> LoggedItem {
        // K_FoodStore does not expose add(...) in generated bindings.
        // Workaround: Throw an error for now to allow build.
        // TODO: Implement when KMM exposes a Swift-compatible API.
        throw NSError(domain: "FoodStore", code: -1, userInfo: [NSLocalizedDescriptionKey: "add() not implemented"])
    }

    // MARK: - Mapping KMM -> Swift presentation

    private func mapFoodItem(_ fi: FoodItem, consumedAt: DietMoment) -> LoggedItem {
        // KMM Mass -> Measurement<UnitMass>
        let mass: Measurement<UnitMass>? = {
            guard let m = fi.mass else { return nil }
            switch m.unit {
            case .milligram: return Measurement(value: m.value, unit: .milligrams)
            case .gram:      return Measurement(value: m.value, unit: .grams)
            case .kilogram:  return Measurement(value: m.value, unit: .kilograms)
            default:         return Measurement(value: m.value, unit: .grams)
            }
        }()

        // KMM Energy -> Measurement<UnitEnergy>
        let energy: Measurement<UnitEnergy>? = {
            guard let e = fi.energy else { return nil }
            switch e.unit {
            case .kilojoule:   return Measurement(value: e.value, unit: .kilojoules)
            case .kilocalorie: return Measurement(value: e.value, unit: .kilocalories)
            default:           return Measurement(value: e.value, unit: .kilocalories)
            }
        }()

        // If you already added KMM `Macros`, you can convert them similarly:
        // let protein = fi.macros?.protein.map { ... }  // remove the ? if protein is non-null inside Macros
        // For now we leave macros nil until you wire them up.
        return LoggedItem(
            id: fi.id,
            name: fi.name,
            consumedAt: consumedAt,
            mass: mass,
            energy: energy,
            protein: nil,
            fat: nil,
            carb: nil,
            loggedAt: Date()
        )
    }

}

