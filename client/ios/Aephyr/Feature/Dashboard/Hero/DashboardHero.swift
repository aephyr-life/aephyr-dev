import Foundation

// MARK: - Macros

enum Macro: String { case protein, fat, carb }

struct MacroSlice: Equatable {
    let kind: Macro
    let mass: Measurement<UnitMass>  // keep the measurement (grams)
    let percent: Double              // 0…100 of macro calories
}

struct MacrosSummary: Equatable {
    let protein: Measurement<UnitMass>
    let fat: Measurement<UnitMass>
    let carb: Measurement<UnitMass>

    // Convenience init from grams
    init(proteinG: Double, fatG: Double, carbG: Double) {
        self.protein = Measurement(value: proteinG, unit: .grams)
        self.fat     = Measurement(value: fatG,     unit: .grams)
        self.carb    = Measurement(value: carbG,    unit: .grams)
    }

    // Grams as Doubles (converted for safety if units ever change)
    private var proteinG: Double { protein.converted(to: .grams).value }
    private var fatG: Double     { fat.converted(to: .grams).value }
    private var carbG: Double    { carb.converted(to: .grams).value }

    private var kcalTotal: Double {
        let p = proteinG * 4
        let f = fatG     * 9
        let c = carbG    * 4
        return max(0.0, p + f + c)
    }

    var slices: [MacroSlice] {
        let total = kcalTotal
        func pct(_ grams: Double, _ kcalPerG: Double) -> Double {
            guard total > 0 else { return 0 }
            return grams * kcalPerG / total * 100.0
        }
        return [
            MacroSlice(kind: .protein, mass: protein, percent: pct(proteinG, 4)),
            MacroSlice(kind: .fat,     mass: fat,     percent: pct(fatG, 9)),
            MacroSlice(kind: .carb,    mass: carb,    percent: pct(carbG, 4)),
        ]
    }
}

// MARK: - Dashboard hero aggregate

struct DashboardHero: Equatable {
    let energy: Measurement<UnitEnergy>  // canonical kJ; convert at display time
    let macros: MacrosSummary
}

extension DashboardHero {
    static func make(from items: [FoodStore.LoggedItem]) -> DashboardHero {
        // Sum energy in kJ (fallback: 0 if missing)
        let totalKJ = items
            .compactMap { $0.energy?.converted(to: .kilojoules).value }
            .reduce(0, +)
        let energy = Measurement(value: totalKJ, unit: .kilojoules)

        // Sum macros in grams (nil → 0)
        func sum(_ key: (FoodStore.LoggedItem) -> Double?) -> Double {
            items.compactMap(key).reduce(0.0, +)
        }
        let p = sum { $0.proteinG }
        let f = sum { $0.fatG }
        let c = sum { $0.carbG }

        return DashboardHero(
            energy: energy,
            macros: MacrosSummary(proteinG: p, fatG: f, carbG: c)
        )
    }
}

