//
//  FoodDetailView.swift
//  Aephyr
//
//  Created by Martin Pallmann on 20.09.25.
//


import SwiftUI
import AephyrShared

struct FoodDetailView: View {
    let item: FoodStore.LoggedItem
    let onDelete: () async -> Bool

    @Environment(\.dismiss)    private var dismiss
    @Environment(\.energyUnit) private var energyUnit
    @State private var confirming = false
    @State private var deleting = false
    @State private var error: String?

    var body: some View {
        List {
            Section(header: Text("Food")) {
                HStack {
                    Text("Name")
                    Spacer()
                    Text(item.name)
                        .foregroundStyle(.secondary)
                }
                if let mass = item.mass {
                    HStack {
                        Text("Portion")
                        Spacer()
                        Text(
                            mass.converted(to: .grams),
                            format: .measurement(width: .narrow, usage: .asProvided)
                        )
                        .foregroundStyle(.secondary)
                    }
                }
            }
            Section(header: Text("Energy")) {
                if let energy = item.energy {
                    HStack {
                        Text("Energy")
                        Spacer()
                        Text(
                            energy.converted(to: energyUnit.unit),
                            format: .measurement(width: .narrow, usage: .asProvided)
                        )
                        .foregroundStyle(.secondary)
                    }
                }
            }
            Section(header: Text("Macros")) {
                if let protein = item.protein {
                    row("Protein", protein)
                }
                if let fat = item.fat {
                    row("Fat", fat)
                }
                if let carb = item.carb {
                    row("Carbs", carb)
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

    @ViewBuilder
    private func row(_ label: String, _ measurement: Measurement<UnitMass>) -> some View {
        HStack {
            Text(label)
            Spacer()
            Text(
                measurement.converted(to: .grams),
                format: .measurement(width: .narrow, usage: .asProvided)
            )
            .foregroundStyle(.secondary)
            .monospacedDigit()
        }
    }
}
