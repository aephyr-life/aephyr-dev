import Foundation
import AephyrShared

// MARK: - Interop helpers
@inline(__always) private func asInt32(_ v: Any?) -> Int32? {
    if let i32 = v as? Int32 { return i32 }
    if let ki  = v as? KotlinInt { return ki.int32Value }
    if let i   = v as? Int { return Int32(i) }
    if let n   = v as? NSNumber { return n.int32Value }
    return nil
}
@inline(__always) private func asInt(_ v: Any?) -> Int? {
    if let i = v as? Int { return i }
    if let i32 = v as? Int32 { return Int(i32) }
    if let ki  = v as? KotlinInt { return Int(ki.int32Value) }
    if let n   = v as? NSNumber { return n.intValue }
    return nil
}
@inline(__always) private func asString(_ v: Any) -> String {
    (v as? String) ?? String(describing: v)
}

// MARK: - Swift-facing wrapper
final class FoodStore {

    struct LoggedItem: Identifiable, Equatable {
        let id:         String
        let name:       String
        let consumedAt: DietMoment

        let grams:      Measurement<UnitMass>?
        let energy:     Measurement<UnitEnergy>?    // canonical kJ in value, convert at display

        let protein:    Measurement<UnitMass>?
        let fat:        Measurement<UnitMass>?
        let carb:       Measurement<UnitMass>?

        let loggedAt:   Date
    }

    struct NewItemInput {
        let consumedAt: DietMoment?                 // nil → today/no time
        let name:       String
        let grams:      Measurement<UnitMass>?      // optional
        let energy:     Measurement<UnitEnergy>?    // optional
        let protein:    Measurement<UnitMass>?      // optional
        let fat:        Measurement<UnitMass>?      // optional
        let carb:       Measurement<UnitMass>?      // optional
    }

    // Factory
    nonisolated static func mock() -> FoodStore {
        FoodStore(delegate: K_FoodStoreFactory().mock())
    }

    // Dependencies
    private let delegate: K_FoodStore
    init(delegate: K_FoodStore) { self.delegate = delegate }

    // API
    func entries(for day: Date) async throws -> [LoggedItem] {
        let local = day.toKmmLocalDate()
        let anyList = try await delegate.entriesFor(day: local)
        let items = (anyList as? [K_LoggedFood]) ?? []
        return items.map(mapLoggedFood(_:))
    }

    @discardableResult
    func remove(id: String) async throws -> Bool {
        let any = try await delegate.remove(id: id) as Any
        if let b  = any as? Bool          { return b }
        if let kb = any as? KotlinBoolean { return kb.boolValue }
        if let n  = any as? NSNumber      { return n.boolValue }
        return false
    }

    @discardableResult
    func add(_ input: NewItemInput) async throws -> LoggedItem {
        // consumedAt
        let dm: K_DietMoment = {
            if let m = input.consumedAt {
                return m.toKmm()
            } else {
                return K_DietMoment(day: Date().toKmmLocalDate(), timeMinutes: nil)
            }
        }()

        // Portion (Mass) — K_Quantity.Mass -> K_QuantityMass in Swift
        let portion: K_Quantity? = {
            guard let g = input.grams else { return nil }
            let gramsInt32 = Int32(g.converted(to: .grams).value.rounded())
            return K_QuantityMass(grams: gramsInt32)
        }()

        // Energy — inline Int in Kotlin → Int32 in Swift
        let energyKJ: Int32? = input.energy.map {
            Int32($0.converted(to: .kilojoules).value.rounded())
        }

        // Macros — K_Decigram is inline Int → pass Int32 decigrams directly
        let macros: K_Macros? = {
            guard let p = input.protein, let f = input.fat, let c = input.carb else { return nil }
            let pDG = Int32((p.converted(to: .grams).value * 10.0).rounded())
            let fDG = Int32((f.converted(to: .grams).value * 10.0).rounded())
            let cDG = Int32((c.converted(to: .grams).value * 10.0).rounded())
            return K_Macros(protein: pDG, fat: fDG, carb: cDG)
        }()

        let kmmInput = K_NewLogInput(
            consumedAt: dm,
            name: input.name,          // K_FoodName is inline → String
            portion: portion,
            energy: energyKJ,
            macros: macros
        )

        let created = try await delegate.add(input: kmmInput)
        return mapLoggedFood(created as! K_LoggedFood)
    }

    // MARK: - Mapping KMM -> Swift
    private func mapLoggedFood(_ lf: K_LoggedFood) -> LoggedItem {
        let moment = lf.consumedAt.toSwift()

        // Portion mass → Measurement<UnitMass>?
        var gramsMeas: Measurement<UnitMass>? = nil
        if let mass = lf.portion as? K_QuantityMass, let g = asInt(mass.grams) {
            gramsMeas = Measurement(value: Double(g), unit: .grams)
        }

        // Macros decigrams → grams → Measurement<UnitMass>?
        func dgToMass(_ v: Any?) -> Measurement<UnitMass>? {
            guard let dg = asInt32(v) else { return nil }
            let grams = Double(dg) / 10.0
            return Measurement(value: grams, unit: .grams)
        }
        let proteinM = dgToMass(lf.macros?.protein)
        let fatM     = dgToMass(lf.macros?.fat)
        let carbM    = dgToMass(lf.macros?.carb)

        // Energy (kJ) → Measurement<UnitEnergy>?
        let energyMeas: Measurement<UnitEnergy>? = {
            if let e32 = asInt32(lf.energy) {
                return Measurement(value: Double(e32), unit: .kilojoules)
            }
            if let p = proteinM?.converted(to: .grams).value,
               let f = fatM?.converted(to: .grams).value,
               let c = carbM?.converted(to: .grams).value {
                let kcal = p * 4 + f * 9 + c * 4
                let kj = (kcal * 4.184)
                return Measurement(value: kj.rounded(), unit: .kilojoules)
            }
            return nil
        }()

        return LoggedItem(
            id: asString(lf.id),
            name: asString(lf.name),
            consumedAt: moment,
            grams: gramsMeas,
            energy: energyMeas,
            protein: proteinM,
            fat: fatM,
            carb: carbM,
            loggedAt: lf.loggedAt.toDate()
        )
    }
}

