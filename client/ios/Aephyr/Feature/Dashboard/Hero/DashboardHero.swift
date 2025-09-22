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
        
        func totalEnergy(in unit: UnitEnergy) -> Measurement<UnitEnergy> {
          let zero = Measurement(value: 0, unit: unit)
          return items.reduce(into: zero) { sum, item in
            guard let e = item.energy else { return }
            sum = sum + e.converted(to: unit)
          }
        }
        
        struct Totals {
          var carb    = Measurement<UnitMass>(value: 0, unit: .grams)
          var protein = Measurement<UnitMass>(value: 0, unit: .grams)
          var fat     = Measurement<UnitMass>(value: 0, unit: .grams)
        }

        let totals = items.reduce(into: Totals()) { acc, item in
          if let m = item.carb?.converted(to: .grams)    { acc.carb.value    += m.value }
          if let p = item.protein?.converted(to: .grams) { acc.protein.value += p.value }
          if let f = item.fat?.converted(to: .grams)     { acc.fat.value     += f.value }
        }
        
        return DashboardHero(
            energy: totalEnergy(in: UnitEnergy.kilojoules), // TODO use user preference
            macros: MacrosSummary(protein: totals.protein, fat: totals.fat, carb: totals.carb)
        )
    }
}

