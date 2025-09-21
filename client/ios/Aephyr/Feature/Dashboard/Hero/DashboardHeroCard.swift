//
//  DailyHeroCard.swift
//  Aephyr
//
//  Created by Martin Pallmann on 20.09.25.
//


import SwiftUI
import AephyrShared

struct DashboardHeroCard: View {
    let data: DashboardHero
    @Environment(\.energyUnit) private var energyUnit

    private var donutSegments: [DonutChart.Segment] {
        let p = data.macros.protein.converted(to: .grams).value
        let f = data.macros.fat.converted(to: .grams).value
        let c = data.macros.carb.converted(to: .grams).value
        return [
            .init(label: "Protein", value: p, color: Color("MacroProtein")),
            .init(label: "Fat",     value: f, color: Color("MacroFat")),
            .init(label: "Carbs",   value: c, color: Color("MacroCarbs"))
        ]
    }

    var body: some View {
        
        HStack(alignment: .center, spacing: 16) {
            // Donut
            DonutChart(segments: donutSegments, thickness: 20)
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

                // Three columns: protein / fat / carbs
                HStack(alignment: .top) {
                    StatColumn(
                        title: "Protein",
                        value: Int32(data.macros.protein.converted(to: .grams).value.rounded()),
                        unit: "g",
                        color: Color("MacroProtein")
                    )
                    Spacer(minLength: 12)
                    StatColumn(
                        title: "Fat",
                        value: Int32(data.macros.fat.converted(to: .grams).value.rounded()),
                        unit: "g",
                        color: Color("MacroFat")
                    )
                    Spacer(minLength: 12)
                    StatColumn(
                        title: "Carbs",
                        value: Int32(data.macros.carb.converted(to: .grams).value.rounded()),
                        unit: "g",
                        color: Color("MacroCarbs")
                    )
                }
            }
        }
        .padding(16)
        //.background(CardBackground())
        .overlay(
            RoundedRectangle(cornerRadius: 20, style: .continuous)
                .strokeBorder(.white.opacity(0.08))
        )
        .accessibilityElement(children: .combine)
        .accessibilityLabel("Daily summary")
       // .accessibilityValue("\(data.kcal) kilocalories. Protein \(Int(data.proteinG)) grams, fat (Int(data.fatG)) grams, carbs \(Int(data.carbsG)) grams.")
    }
}
