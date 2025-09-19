//
//  MacroPill.swift
//  Aephyr
//
//  Created by Martin Pallmann on 19.09.25.
//

import SwiftUI

struct MacroPill: View {
    let label: String
    let value: String
    let icon: String
    var body: some View {
        HStack(spacing: 8) {
            Image(systemName: icon)
            VStack(alignment: .leading, spacing: 2) {
                Text(label).font(.caption).foregroundColor(.secondary)
                Text(value).font(.subheadline).fontWeight(.medium)
            }
        }
        .padding(12)
        .frame(maxWidth: .infinity, alignment: .leading)
        .background(Color(.systemGray6))
        .clipShape(RoundedRectangle(cornerRadius: 14, style: .continuous))
    }
}
