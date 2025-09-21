import SwiftUI
import AephyrShared

struct FoodRow: View {
    let row: FoodItem
    @Environment(\.energyUnit) private var energyUnit

    // compute measurements from KMM fields
    private var energy: Measurement<UnitEnergy>? {
        row.kcal > 0 ? Measurement(value: Double(row.kcal), unit: .kilocalories) : nil
    }
    private var mass: Measurement<UnitMass>? {
        row.grams > 0 ? Measurement(value: Double(row.grams), unit: .grams) : nil
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

