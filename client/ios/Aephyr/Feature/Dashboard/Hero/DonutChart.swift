//
//  DonutChart.swift
//  Aephyr
//
//  Created by Martin Pallmann on 20.09.25.
//

import SwiftUI

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

    struct ArcStroke: Shape {
        var start: Angle
        var end: Angle
        var inset: CGFloat = 0
        func path(in rect: CGRect) -> Path {
            var p = Path()
            let r = min(rect.width, rect.height) / 2 - inset
            p.addArc(center: CGPoint(x: rect.midX, y: rect.midY),
                     radius: r, startAngle: start, endAngle: end, clockwise: false)
            return p
        }
    }

    var body: some View {
        GeometryReader { geo in
            let line = thickness
            ZStack {
                ForEach(slices) { s in
                    ArcStroke(start: s.start, end: s.end, inset: line/2)
                        .stroke(s.color, style: StrokeStyle(lineWidth: line, lineCap: .butt))
                }
            }
        }
        .aspectRatio(1, contentMode: .fit)
        .background(Color.clear)
        .accessibilityHidden(true)
    }


}
