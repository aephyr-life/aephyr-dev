//
//  EnergyUnit.swift
//  Aephyr
//
//  Created by Martin Pallmann on 21.09.25.
//

import Foundation

enum EnergyUnit: String, CaseIterable, Identifiable {
  case kilocalories, kilojoules
  var id: String { rawValue }

  // der zugehörige Foundation-Typ
  var unit: UnitEnergy {
    switch self {
      case .kilocalories: return .kilocalories
      case .kilojoules:  return .kilojoules
    }
  }
}
