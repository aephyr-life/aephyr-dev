//
//  DailyHeroCard.swift
//  Aephyr
//
//  Created by Martin Pallmann on 20.09.25.
//


import SwiftUI
import AephyrShared

struct DailyHeroCard: View {
    let data: IOSAggregateHero

    private var donutSegments: [DonutChart.Segment] {
            return [
                .init(label: "Protein", value: data.protein_pct, color: Color("MacroProtein")),
                .init(label: "Fat",     value: data.fat_pct, color: Color("MacroFat")),
                .init(label: "Carbs",   value: data.carb_pct, color: Color("MacroCarbs"))
            ]
        }

    var body: some View {
        
//        let proteinG: Int32 = vmhero?.protein_g ?? 0
//        let fatG:     Int32 = vm.hero?.fat_g ?? 0
//        let carbG:    Int32 = vm.hero?.carb_g ?? 0
//        let kcal = vm.hero.map { Int($0.totalEnergyKcalRounded) } ?? 0
        
        HStack(alignment: .center, spacing: 16) {
            // Donut
            DonutChart(segments: donutSegments, thickness: 20)
                .frame(width: 120, height: 120)

            // Textual summary
            VStack(alignment: .leading, spacing: 14) {
                // Big kcal line
                HStack(alignment: .firstTextBaseline, spacing: 4) {
                    Text("\(data.totalEnergyKcalRounded)")
                        .font(.system(size: 44, weight: .bold))
                        .monospacedDigit()
                    Text("kcal")
                        .font(.title3.weight(.semibold))
                        .foregroundStyle(.secondary)
                }

                // Three columns: protein / fat / carbs
                HStack(alignment: .top) {
                    StatColumn(title: "protein", value: data.protein_g, unit: "g", color: Color("MacroProtein"))
                    Spacer(minLength: 12)
                    StatColumn(title: "fats", value: data.fat_g, unit: "g", color: Color("MacroFat"))
                    Spacer(minLength: 12)
                    StatColumn(title: "carbs", value: data.carb_g, unit: "g", color: Color("MacroCarbs"))
                }
            }
        }
        .padding(16)
        .background(CardBackground())
        .overlay(
            RoundedRectangle(cornerRadius: 20, style: .continuous)
                .strokeBorder(.white.opacity(0.08))
        )
        .accessibilityElement(children: .combine)
        .accessibilityLabel("Daily summary")
       // .accessibilityValue("\(data.kcal) kilocalories. Protein \(Int(data.proteinG)) grams, fat (Int(data.fatG)) grams, carbs \(Int(data.carbsG)) grams.")
    }
}
