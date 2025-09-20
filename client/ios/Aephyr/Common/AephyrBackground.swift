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
            // 1) Exaggerated gradient (so you SEE it first)
            LinearGradient(
                colors: [
                    Color("BackgroundStart"),   // e.g. #DED7CE
                    Color("BackgroundEnd")     // e.g. #D3CBBF (a touch darker/greener)
                ],
                startPoint: .topLeading,
                endPoint: .bottomTrailing
            )
            .ignoresSafeArea()

            // 2) Texture – make it obvious first, then reduce
            Group {
                // TEMP: show a checker if the asset is missing/misspelled
                // Comment this out when your Paper image shows.
                //Image(systemName: "checkmark.seal.fill")
                //  .resizable().scaledToFill().opacity(0.3)

                Image("white-texture")                    // EXACT asset name
                    .resizable(resizingMode: .tile)
                    .renderingMode(.original)
                    .saturation(0)                // pure grayscale
                    .opacity(0.22)                // start high so it’s visible
                    .blendMode(.multiply)         // sits naturally on beige
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
