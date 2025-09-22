//
//  AephyrBackground.swift
//  Aephyr
//
//  Created by Martin Pallmann on 19.09.25.
//


import SwiftUI

struct AephyrBackground: View {
    var body: some View {
        ZStack {
            LinearGradient(
                colors: [
                    Color("BackgroundStart"),
                    Color("BackgroundMid"),
                    Color("BackgroundEnd")
                ],
                startPoint: .topLeading,
                endPoint: .bottomTrailing
            )
            .ignoresSafeArea()

            Group {

                Image("white-texture")
                    .resizable(resizingMode: .tile)
                    .renderingMode(.original)
                    .saturation(0)
                    .opacity(0.25)
                    .blendMode(.multiply)
            }
            .ignoresSafeArea()

            // 3) Very soft vignette to add depth (optional)
            LinearGradient(
                colors: [.black.opacity(0.06), .clear, .clear, .black.opacity(0.06)],
                startPoint: .top,
                endPoint: .bottom
            )
            .ignoresSafeArea()
            .allowsHitTesting(false)
        }
    }
}
