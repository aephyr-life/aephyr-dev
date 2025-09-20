import SwiftUI

struct FoodRow: View {
    let row: LoggedFood
    var body: some View {
        HStack(spacing: 12) {
            VStack(alignment: .leading, spacing: 2) {
                Text(row.name)
                if let t = row.timeOfDay {
                    Text(t, style: .time)
                        .font(.caption)
                        .foregroundStyle(.secondary)
                }
            }
            Spacer(minLength: 8)
            VStack(alignment: .trailing, spacing: 2) {
                Text("\(row.kcal) kcal")
                    .monospacedDigit()
                    .foregroundStyle(.secondary)
                if row.grams > 0 {
                    Text("\(row.grams) g")
                        .font(.caption)
                        .foregroundStyle(.tertiary)
                }
            }
        }
        .contentShape(Rectangle())
    }
}
