import SwiftUI

import AephyrShared

struct FoodRow: View {
    let row: IOSLoggedFood
    var body: some View {
        HStack(spacing: 12) {
            VStack(alignment: .leading, spacing: 2) {
                Text(row.name)
                let t = row.consumedDate
                Text(t, style: .time)
                    .font(.caption)
                    .foregroundStyle(.secondary)
                
            }
            Spacer(minLength: 8)
            VStack(alignment: .trailing, spacing: 2) {
                Text("\(row.energyKcalRounded) kcal")
                    .monospacedDigit()
                    .foregroundStyle(.secondary)
                if let g = row.grams.asInt {
                    Text("\(g) g")
                        .font(.caption)
                        .foregroundStyle(.tertiary)
                }
            }
        }
        .contentShape(Rectangle())
    }
}
