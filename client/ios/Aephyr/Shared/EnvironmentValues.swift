//
//  EnvironmentValues.swift
//  Aephyr
//
//  Created by Martin Pallmann on 21.09.25.
//

import Foundation
import SwiftUI

private struct EnergyUnitKey: EnvironmentKey {
  static let defaultValue: EnergyUnit = .kilocalories
}

extension EnvironmentValues {
  var energyUnit: EnergyUnit {
    get { self[EnergyUnitKey.self] }
    set { self[EnergyUnitKey.self] = newValue }
  }
}

