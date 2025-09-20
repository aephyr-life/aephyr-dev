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
