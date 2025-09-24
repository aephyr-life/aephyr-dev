import SwiftUI
import AephyrShared

struct FoodRow: View {
    let row: FoodItem
    @Environment(\.energyUnit) private var energyUnit

    // compute measurements from KMM fields
    private var energy: Measurement<UnitEnergy>? {
        guard let energy = row.energy else { return nil }
        // Assuming K_EnergyUnit is bridged to UnitEnergy, and .kilocalories is the only supported unit for now
        // You may want to map energy.unit to UnitEnergy if needed
        return Measurement(value: energy.value, unit: .kilocalories)
    }
    private var mass: Measurement<UnitMass>? {
        guard let mass = row.mass else { return nil }
        // Assuming K_MassUnit is bridged to UnitMass, and .grams is the only supported unit for now
        // You may want to map mass.unit to UnitMass if needed
        return Measurement(value: mass.value, unit: .grams)
    }

    var body: some View {
        HStack(spacing: 12) {
            // LEFT: name (FoodItem has no time info)
            VStack(alignment: .leading, spacing: 2) {
                Text(row.name)
            }
            Spacer(minLength: 8)

            // RIGHT: energy + grams
            VStack(alignment: .trailing, spacing: 2) {
                Text(energy.map { $0.formatted(using: energyUnit) } ?? "—")
                    .monospacedDigit()
                    .foregroundStyle(.secondary)

                Text(mass.map { $0.formatted() } ?? "–")
                    .font(.caption)
                    .foregroundStyle(.tertiary)
            }
        }
        .contentShape(Rectangle())
    }
}

