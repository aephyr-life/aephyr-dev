//
//  SummaryCard.swift
//  Aephyr
//
//  Created by Martin Pallmann on 19.09.25.
//

import SwiftUI

struct SummaryCard: View {
    let title: String
    let subtitle: String
    let value: String
    var body: some View {
        VStack(alignment: .leading, spacing: 8) {
            Text(title).font(.headline)
            HStack {
                Text(subtitle).foregroundColor(.secondary)
                Spacer()
                Text(value).font(.title2).fontWeight(.semibold)
            }
        }
        .padding()
        .background(Color(.systemGray6))
        .clipShape(RoundedRectangle(cornerRadius: 16, style: .continuous))
    }
}
