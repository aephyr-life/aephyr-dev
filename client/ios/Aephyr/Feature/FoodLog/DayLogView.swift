import SwiftUI

struct DayLogView: View {
    @StateObject private var vm: DayLogVM
    @State private var energyPrefs: EnergyPrefs = .kilocalories

    init(bridge: FoodLogBridge) {
        _vm = StateObject(wrappedValue: DayLogVM(bridge: bridge))
    }

    var body: some View {
        List {
            if let day = vm.day {
                Section(header: Text(formatted(day.date))) {
                    ForEach(day.items) { item in
                        HStack {
                            Text(item.name)
                            Spacer()
                            // TEMP: if you haven’t added formatting helpers yet:
                            Text(item.energy.map { MeasurementFormatter().string(from: $0) } ?? "—")
                                .monospacedDigit()
                                .foregroundStyle(.secondary)
                        }
                    }
                    Text("Total: \(day.totalEnergyString(energyPrefs))").bold()
                }
            } else {
                Text("Loading…")
            }
        }
        .task { @MainActor in
            let today = Calendar.current.dateComponents([.year,.month,.day], from: Date())
            vm.start(for: today)
        }
    }

    private func formatted(_ comps: DateComponents) -> String {
        let cal = Calendar.current
        guard let date = cal.date(from: comps) else { return "—" }
        let f = DateFormatter(); f.dateStyle = .medium; return f.string(from: date)
    }
}
