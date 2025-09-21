//
//  MeasurementExtension.swift
//  Aephyr
//
//  Created by Martin Pallmann on 21.09.25.
//

import Foundation

extension Measurement where UnitType == UnitEnergy {
  func formatted(using pref: EnergyUnit) -> String {
    let m = self.converted(to: pref.unit)
    let fmt = MeasurementFormatter()
    fmt.unitOptions = .providedUnit
    fmt.unitStyle   = .short
    return fmt.string(from: m)
  }
}
