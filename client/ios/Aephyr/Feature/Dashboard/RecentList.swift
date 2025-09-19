//
//  RecentList.swift
//  Aephyr
//
//  Created by Martin Pallmann on 19.09.25.
//

import SwiftUI

struct RecentList: View {
    let items: [LoggedFood]
    var body: some View {
        VStack(alignment: .leading, spacing: 8) {
            Text("Recent").font(.headline)
            ForEach(items) { item in
                HStack {
                    VStack(alignment: .leading) {
                        Text(item.name).font(.subheadline).fontWeight(.medium)
                        Text("\(item.grams) g").font(.caption).foregroundColor(.secondary)
                    }
                    Spacer()
                    Text("\(item.kcal) kcal").font(.subheadline)
                }
                .padding(12)
                .background(Color(.systemGray6))
                .clipShape(RoundedRectangle(cornerRadius: 12, style: .continuous))
            }
        }
    }
}
