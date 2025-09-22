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
        // Iterate first emission from the Flow returned by observeToday()
        let moment = DietMoment(day: day, timeMinutes: nil)
        for try await items in asyncSequence(for: delegate.observeToday()) {
            return items.map { mapFoodItem($0, consumedAt: moment) }
        }
        return []
    }

    @discardableResult
    func remove(id: String) async throws -> Bool {
        do {
            try await asyncFunction(for: delegate.remove(id: id))
            return true
        } catch {
            return false
        }
    }

    /// Adds an item using the new KMM API (Mass/Energy/Macros are optional; we send what's provided).
    @discardableResult
    func add(_ input: NewItemInput) async throws -> LoggedItem {
        // Convert optional Foundation Measurements → KMM Mass/Energy/Macros
        let massK: Mass? = input.grams.map { m in
            let g = m.converted(to: .grams).value
            return Mass(value: g, unit: .gram)
        }

        let energyK: Energy? = input.energy.map { e in
            if e.unit == .kilojoules {
                let kJ = e.converted(to: .kilojoules).value
                return Energy(value: kJ, unit: .kilojoule)
            } else {
                let kcal = e.converted(to: .kilocalories).value
                return Energy(value: kcal, unit: .kilocalorie)
            }
        }

        let macrosK: Macros? = {
            guard let p = input.protein, let f = input.fat, let c = input.carb else { return nil }
            return Macros(
                protein: Mass(value: p.converted(to: .grams).value, unit: .gram),
                fat:     Mass(value: f.converted(to: .grams).value, unit: .gram),
                carb:    Mass(value: c.converted(to: .grams).value, unit: .gram)
            )
        }()

        // Call KMM
        let created = try await asyncFunction(
            for: delegate.add(name: input.name, mass: massK, energy: energyK, macros: macrosK)
        )

        // Synthesize timing until KMM provides it
        let consumed = input.consumedAt ?? DietMoment(day: Date(), timeMinutes: nil)
        return mapFoodItem(created, consumedAt: consumed)
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

