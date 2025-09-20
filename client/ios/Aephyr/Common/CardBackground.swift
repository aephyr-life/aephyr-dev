//
//  CardBackground.swift
//  Aephyr
//
//  Created by Martin Pallmann on 20.09.25.
//

import SwiftUI

struct CardBackground: View {
    var body: some View {
        let shape = RoundedRectangle(cornerRadius: 20, style: .continuous)
        return ZStack {
            shape.fill(.ultraThinMaterial)
            shape.fill(Color.white.opacity(0.15))
        }
    }
}
