import SwiftUI

struct FoodRow: View {
    let row: FoodStore.LoggedItem
    /// Pick your default; swap to .kJ if you prefer
    var energyUnit: EnergyUnit = .kcal

    private func timeDate(from dm: DietMoment) -> Date? {
        guard let mins = dm.timeMinutes else { return nil }
        let h = mins / 60, m = mins % 60
        return Calendar.current.date(
            bySettingHour: h, minute: m, second: 0, of: dm.day
        )
    }

    var body: some View {
        HStack(spacing: 12) {
            // LEFT: name + (optional) time
            VStack(alignment: .leading, spacing: 2) {
                Text(row.name)
                if let t = timeDate(from: row.consumedAt) {
                    Text(t, style: .time)
                        .font(.caption)
                        .foregroundStyle(.secondary)
                }
            }
            Spacer(minLength: 8)

            // RIGHT: energy + grams
            VStack(alignment: .trailing, spacing: 2) {
                if let e = row.energy {
                    let display = (energyUnit == .kcal)
                        ? e.converted(to: .kilocalories)
                        : e.converted(to: .kilojoules)
                    Text(display, format: .measurement(width: .narrow, usage: .asProvided))
                        .monospacedDigit()
                        .foregroundStyle(.secondary)
                } else {
                    Text("—")
                        .foregroundStyle(.tertiary)
                }

                if let g = row.grams {
                    Text(g, format: .measurement(width: .narrow, usage: .nutrition)) // e.g. "60 g"
                        .font(.caption)
                        .foregroundStyle(.tertiary)
                }
            }
        }
        .contentShape(Rectangle()) // keep whole row tappable
    }
}

