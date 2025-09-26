import Foundation

enum EnergyPrefs {
    case kilocalories, kilojoules
    var unit: UnitEnergy {
        self == .kilocalories ? .kilocalories : .kilojoules
    }
}

private let energyFormatter: MeasurementFormatter = {
    let f = MeasurementFormatter()
    f.unitStyle = .short
    f.unitOptions = .providedUnit
    f.numberFormatter.maximumFractionDigits = 0
    return f
}()

extension Measurement where UnitType == UnitEnergy {
    func formatted(_ prefs: EnergyPrefs) -> String {
        energyFormatter.string(from: converted(to: prefs.unit))
    }
}
