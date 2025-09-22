import SwiftUI
import AephyrShared

struct DashboardHeroCard: View {
    let data: DashboardHero
    @Environment(\.energyUnit) private var energyUnit

    // Gram values (Double) for convenience
    private var pG: Double { data.macros.protein.converted(to: .grams).value }
    private var fG: Double { data.macros.fat.converted(to: .grams).value }
    private var cG: Double { data.macros.carb.converted(to: .grams).value }
    private var hasMacros: Bool { (pG + fG + cG) > 0.0001 }

    private var donutSegments: [DonutChart.Segment] {
        [
            .init(label: "Protein", value: pG, color: Color("MacroProtein")),
            .init(label: "Fat",     value: fG, color: Color("MacroFat")),
            .init(label: "Carbs",   value: cG, color: Color("MacroCarbs"))
        ]
    }

    var body: some View {
        HStack(alignment: .center, spacing: 16) {
            // Donut: segmented when macros > 0, otherwise neutral ring
            Group {
                if hasMacros {
                    DonutChart(segments: donutSegments, thickness: 20)
                } else {
                    EmptyDonut(thickness: 20)
                }
            }
            .frame(width: 120, height: 120)
            .background(Color.clear)

            // Textual summary
            VStack(alignment: .leading, spacing: 14) {
                // Big kcal line
                HStack(alignment: .firstTextBaseline, spacing: 4) {
                    let e = data.energy.converted(to: energyUnit.unit)
                    Text("\(Int(e.value.rounded()))")
                        .font(.system(size: 44, weight: .bold))
                        .monospacedDigit()
                    Text(e.unit.symbol)
                        .font(.title3.weight(.semibold))
                        .foregroundStyle(.secondary)
                }

                // Protein / Fat / Carbs
                HStack(alignment: .top) {
                    StatColumn(title: "Protein",
                               value: Int32(pG.rounded()),
                               unit: "g",
                               color: Color("MacroProtein"))
                    Spacer(minLength: 12)
                    StatColumn(title: "Fat",
                               value: Int32(fG.rounded()),
                               unit: "g",
                               color: Color("MacroFat"))
                    Spacer(minLength: 12)
                    StatColumn(title: "Carbs",
                               value: Int32(cG.rounded()),
                               unit: "g",
                               color: Color("MacroCarbs"))
                }
            }
        }
        .padding(16)
        //.background(CardBackground())
        .clipShape(RoundedRectangle(cornerRadius: 20, style: .continuous))
        .overlay(
            RoundedRectangle(cornerRadius: 20, style: .continuous)
                .strokeBorder(.white.opacity(0.08))
        )
        .accessibilityElement(children: .combine)
        .accessibilityLabel("Daily summary")
    }
}

// Neutral, always-visible donut ring (no progress/segments)
private struct EmptyDonut: View {
    var thickness: CGFloat = 20
    var body: some View {
        Circle()
            .stroke(lineWidth: thickness)
            .opacity(0.12)
    }
}

