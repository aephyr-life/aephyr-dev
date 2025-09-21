import SwiftUI

struct FoodRow: View {
    let row: FoodStore.LoggedItem
    @Environment(\.energyUnit) private var energyUnit

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
                
                Text(row.energy.map { $0.formatted(using: energyUnit) } ?? "—")
                      .monospacedDigit()
                      .foregroundStyle(.secondary)
                
                Text(row.mass.map { $0.formatted() } ?? "–")
                    .font(.caption)
                    .foregroundStyle(.tertiary)

            }
        }
        .contentShape(Rectangle()) // keep whole row tappable
    }
}

