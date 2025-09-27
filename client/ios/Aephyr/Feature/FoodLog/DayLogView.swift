import SwiftUI

struct DayLogView: View {
    @EnvironmentObject var settings: AppSettings
    @StateObject var vm: DayLogVM
    @State private var showQuickAdd = false
    @State private var today = Calendar.current.dateComponents([.year, .month, .day], from: Date())

    /// Local formatter to avoid calling a member on the EnvironmentObject.
    private func energyString(_ energy: Measurement<UnitEnergy>?) -> String {
        let formatter = MeasurementFormatter()
        formatter.unitStyle = .medium
        formatter.numberFormatter.numberStyle = .decimal
        return energy.map { m in formatter.string(from: m) } ?? ""
    }

    @ViewBuilder
    private func row(for item: SFoodLogItem) -> some View {
        HStack {
            VStack(alignment: .leading) {
                Text(item.name).font(.headline)
                if let portion = item.portion {
                    Text(portion.formatted())
                        .font(.subheadline)
                        .foregroundStyle(.secondary)
                        .opacity(portion.value == 0 ? 0 : 1) // or .hidden()
                }
            }
            Spacer()
            Text(energyString(item.energy))
                .font(.subheadline)
                .foregroundStyle(.secondary)
        }
    }
    
    @ViewBuilder
    private func list(vm: DayLogVM) -> some View {
        List {
            ForEach(Array(vm.day.items.enumerated()), id: \.offset) { _, item in
                row(for: item)
            }
            .onDelete { offsets in
                Task { @MainActor in
                    for index in offsets {
                        let itemId = vm.day.items[index].id
                        await vm.remove(itemId)
                    }
                }
            }
        }
    }

    var body: some View {
        NavigationStack {
            Group {
                if vm.day.items.isEmpty {
                    ContentUnavailableView(
                        "No items yet",
                        systemImage: "fork.knife",
                        description: Text("Tap + to add your first item.")
                    )
                } else {
                    list(vm: vm)
                }
            }
            .navigationTitle("Today")
            .toolbar {
                ToolbarItem(placement: .primaryAction) {
                    Button { showQuickAdd = true } label: { Image(systemName: "plus") }
                }
            }
            .sheet(isPresented: $showQuickAdd) {
                QuickAddSheet { name, mass, energy, protein, fat, carb in
                    await vm.add(.init(
                        date: today,
                        name: name,
                        portion: mass,
                        energy: energy,
                        macros: SMacros(protein: protein, fat: fat, carb: carb)
                    ))
                }
            }
        }
        .onAppear { vm.start(for: today) }
        .onDisappear { vm.stop() }
    }
}

