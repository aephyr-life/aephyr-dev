//
//  FoodDetailView.swift
//  Aephyr
//
//  Created by Martin Pallmann on 20.09.25.
//


import SwiftUI
import AephyrShared

struct FoodDetailView: View {
    let item: IOSLoggedFood
    let onDelete: () async -> Bool

    @Environment(\.dismiss) private var dismiss
    @State private var confirming = false
    @State private var deleting = false
    @State private var error: String?

    var body: some View {
        List {
            Section(header: Text("Food")) {
                HStack { Text("Name"); Spacer(); Text(item.name).foregroundStyle(.secondary) }
                if let g = item.grams as? Int {
                    HStack { Text("Portion"); Spacer(); Text("\(g) g").foregroundStyle(.secondary) }
                }
            }
            Section(header: Text("Energy")) {
                if let kcal = item.energyKcalRounded as? Int {
                    HStack { Text("Calories"); Spacer(); Text("\(kcal) kcal").foregroundStyle(.secondary) }
                }
                if let kj = item.energyKJ as? Int {
                    HStack { Text("Kilojoules"); Spacer(); Text("\(kj) kJ").foregroundStyle(.secondary) }
                }
            }
            Section(header: Text("Macros")) {
                if let p = item.protein_g as? Double {
                    row("Protein", "\(Int(p)) g")
                }
                if let f = item.fat_g as? Double {
                    row("Fat", "\(Int(f)) g")
                }
                if let c = item.carb_g as? Double {
                    row("Carbs", "\(Int(c)) g")
                }
            }
            Section {
                Button(role: .destructive) {
                    confirming = true
                } label: {
                    HStack {
                        Spacer()
                        if deleting { ProgressView() }
                        Text("Delete Entry")
                        Spacer()
                    }
                }
                .disabled(deleting)
                if let e = error {
                    Text(e).foregroundStyle(.red).font(.footnote)
                }
            }
        }
        .navigationTitle("Details")
        .navigationBarTitleDisplayMode(.inline)
        .confirmationDialog("Delete this entry?", isPresented: $confirming, titleVisibility: .visible) {
            Button("Delete", role: .destructive) {
                Task {
                    deleting = true; error = nil
                    if await onDelete() { dismiss() }
                    else { error = "Couldn’t delete. Please try again." }
                    deleting = false
                }
            }
            Button("Cancel", role: .cancel) {}
        }
    }

    private func row(_ a: String, _ b: String) -> some View {
        HStack { Text(a); Spacer(); Text(b).foregroundStyle(.secondary) }
    }
}
