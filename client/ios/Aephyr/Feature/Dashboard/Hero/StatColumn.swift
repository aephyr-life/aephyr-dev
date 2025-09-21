//
//  StatColumn.swift
//  Aephyr
//
//  Created by Martin Pallmann on 20.09.25.
//


import SwiftUI

struct StatColumn: View {
    let title: String
    let value: Int32
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
