//
//  DailyHeroData.swift
//  Aephyr
//
//  Created by Martin Pallmann on 19.09.25.
//

import SwiftUI

// MARK: - Model you pass in
struct DailyHeroData {
    var kcal: Int
    var proteinG: Double
    var fatG: Double
    var carbsG: Double
}

// MARK: - View

struct DailyHeroCard: View {
    let data: DailyHeroData

    private var donutSegments: [DonutChart.Segment] {
        [
            .init(label: "Protein", value: data.proteinG, color: Color("MacroProtein")),
            .init(label: "Fat",     value: data.fatG,     color: Color("MacroFat")),
            .init(label: "Carbs",   value: data.carbsG,   color: Color("MacroCarbs"))
        ]
    }

    var body: some View {
        HStack(alignment: .center, spacing: 16) {
            // Donut
            DonutChart(segments: donutSegments, thickness: 20)
                .frame(width: 120, height: 120)

            // Textual summary
            VStack(alignment: .leading, spacing: 14) {
                // Big kcal line
                HStack(alignment: .firstTextBaseline, spacing: 4) {
                    Text("\(data.kcal)")
                        .font(.system(size: 44, weight: .bold))
                        .monospacedDigit()
                    Text("kcal")
                        .font(.title3.weight(.semibold))
                        .foregroundStyle(.secondary)
                }

                // Three columns: protein / fat / carbs
                HStack(alignment: .top) {
                    StatColumn(title: "protein", value: Int(data.proteinG), unit: "g", color: Color("MacroProtein"))
                    Spacer(minLength: 12)
                    StatColumn(title: "fats", value: Int(data.fatG), unit: "g", color: Color("MacroFat"))
                    Spacer(minLength: 12)
                    StatColumn(title: "carbs", value: Int(data.carbsG), unit: "g", color: Color("MacroCarbs"))
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
        .accessibilityValue("\(data.kcal) kilocalories. Protein \(Int(data.proteinG)) grams, fat \(Int(data.fatG)) grams, carbs \(Int(data.carbsG)) grams.")
    }
}

// Small helper for one stat
private struct StatColumn: View {
    let title: String
    let value: Int
    let unit: String
    let color: Color

    var body: some View {
        VStack(alignment: .leading, spacing: 2) {
            Text(title).font(.subheadline).foregroundStyle(.secondary)
            HStack(alignment: .firstTextBaseline, spacing: 2) {
                Text("\(value)").font(.headline.weight(.semibold)).monospacedDigit().foregroundStyle(color)
                Text(unit).font(.headline.weight(.semibold)).monospacedDigit().foregroundStyle(color)
            }
        }
    }
}

// MARK: - Donut chart (precomputed, compiler-friendly)

struct DonutChart: View {
    struct Segment: Identifiable {
        let id = UUID()
        let label: String
        let value: Double
        let color: Color
    }

    let segments: [Segment]
    var thickness: CGFloat = 18

    private var total: Double {
        max(segments.reduce(0) { $0 + $1.value }, 0.001)
    }

    private var slices: [Slice] {
        var out: [Slice] = []
        var cursor: Double = -90
        for seg in segments {
            let sweep = (seg.value / total) * 360.0
            out.append(.init(start: Angle(degrees: cursor),
                             end: Angle(degrees: cursor + sweep),
                             color: seg.color))
            cursor += sweep
        }
        return out
    }

    private struct Slice: Identifiable {
        let id = UUID()
        let start: Angle
        let end: Angle
        let color: Color
    }

    var body: some View {
        ZStack {
            ForEach(slices) { s in
                RingSlice(startAngle: s.start, endAngle: s.end, thickness: thickness)
                    .fill(s.color)
            }
            Circle().fill(.thinMaterial).scaleEffect(0.60)
        }
        .accessibilityHidden(true)
    }
}

private struct RingSlice: Shape {
    let startAngle: Angle
    let endAngle: Angle
    let thickness: CGFloat

    func path(in rect: CGRect) -> Path {
        let R = min(rect.width, rect.height) / 2
        let center = CGPoint(x: rect.midX, y: rect.midY)
        let r = max(R - thickness, 0)

        var p = Path()
        p.addArc(center: center, radius: R, startAngle: startAngle, endAngle: endAngle, clockwise: false)
        p.addArc(center: center, radius: r, startAngle: endAngle, endAngle: startAngle, clockwise: true)
        p.closeSubpath()
        return p
    }
}
