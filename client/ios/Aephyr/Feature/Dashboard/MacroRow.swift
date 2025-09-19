//
//  MacroRow.swift
//  Aephyr
//
//  Created by Martin Pallmann on 19.09.25.
//

import SwiftUI

struct MacroRow: View {
    let protein: Int
    let carbs: Int
    let fat: Int
    var body: some View {
        HStack(spacing: 12) {
            MacroPill(label: "Protein", value: "\(protein) g", icon: "bolt.fill")
            MacroPill(label: "Carbs", value: "\(carbs) g", icon: "leaf.fill")
            MacroPill(label: "Fat", value: "\(fat) g", icon: "drop.fill")
        }
    }
}
