//
//  RingSlice.swift
//  Aephyr
//
//  Created by Martin Pallmann on 20.09.25.
//

import SwiftUI

struct RingSlice: Shape {
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
