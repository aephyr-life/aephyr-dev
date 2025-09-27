//
//  QuickAddSheet.swift
//  Aephyr
//

import SwiftUI
import AephyrShared   // <- for EnergyUnitPref extension below

struct QuickAddSheet: View {
    @EnvironmentObject var settings: AppSettings
    @Environment(\.dismiss) private var dismiss

    @State private var name: String = ""
    @State private var gramsText: String = ""
    @State private var energyText: String = ""   // was `kcalText`
    @State private var proteinText: String = ""
    @State private var fatText: String = ""
    @State private var carbText: String = ""

    let onAdd: (
        _ name: String,
        _ mass: Measurement<UnitMass>,
        _ energy: Measurement<UnitEnergy>?,
        _ protein: Measurement<UnitMass>,
        _ fat: Measurement<UnitMass>,
        _ carb: Measurement<UnitMass>
    ) async -> Void

    var body: some View {
        NavigationStack {
            Form {
                TextField("Name", text: $name)
                    .textInputAutocapitalization(.words)

                TextField("Grams (optional)", text: $gramsText)
                    .keyboardType(.decimalPad)

                // Use the shared enum via AppSettings.energy
                TextField("\(settings.energy.label) (optional)", text: $energyText)
                    .keyboardType(.decimalPad)

                TextField("protein (optional)", text: $proteinText)
                    .keyboardType(.decimalPad)

                TextField("fat (optional)", text: $fatText)
                    .keyboardType(.decimalPad)

                TextField("carb (optional)", text: $carbText)
                    .keyboardType(.decimalPad)
            }
            .navigationTitle("Quick Add")
            .toolbar {
                ToolbarItem(placement: .cancellationAction) {
                    Button("Cancel") { dismiss() }
                }
                ToolbarItem(placement: .confirmationAction) {
                    Button("Add") {
                        Task {
                            let grams   = parseLocalizedDouble(gramsText).map { Measurement(value: $0, unit: UnitMass.grams) }
                            let energy  = parseLocalizedDouble(energyText).map { Measurement(value: $0, unit: settings.energy.unit) }
                            let protein = parseLocalizedDouble(proteinText).map { Measurement(value: $0, unit: UnitMass.grams) }
                            let fat     = parseLocalizedDouble(fatText).map { Measurement(value: $0, unit: UnitMass.grams) }
                            let carb    = parseLocalizedDouble(carbText).map { Measurement(value: $0, unit: UnitMass.grams) }

                            let portion = grams ?? Measurement(value: 0, unit: UnitMass.grams)
                            let mProtein = protein ?? Measurement(value: 0, unit: UnitMass.grams)
                            let mFat     = fat     ?? Measurement(value: 0, unit: UnitMass.grams)
                            let mCarb    = carb    ?? Measurement(value: 0, unit: UnitMass.grams)

                            await onAdd(
                                name.trimmingCharacters(in: .whitespacesAndNewlines),
                                portion, energy, mProtein, mFat, mCarb
                            )
                            dismiss()
                        }
                    }
                    .disabled(name.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty)
                }
            }
        }
        .presentationDetents([.medium, .large])
    }
}

private func parseLocalizedDouble(_ text: String) -> Double? {
    let trimmed = text.trimmingCharacters(in: .whitespacesAndNewlines)
    guard !trimmed.isEmpty else { return nil }
    let formatter = NumberFormatter()
    formatter.locale = .current
    formatter.numberStyle = .decimal
    if let n = formatter.number(from: trimmed) {
        return n.doubleValue
    }
    let fallback = trimmed.replacingOccurrences(of: ",", with: ".")
    return Double(fallback)
}

// MARK: - Bridging helpers for the shared enum

extension AephyrShared.EnergyUnitPref {
    var label: String {
        switch self {
        case .kj:   return "kJ"
        case .kcal: return "kcal"
        default:    return "kcal" // defensive for future cases
        }
    }
    var unit: UnitEnergy {
        switch self {
        case .kj:   return .kilojoules
        case .kcal: return .kilocalories
        default:    return .kilocalories
        }
    }
}
