//
//  DayLogVM.swift
//  Aephyr
//
//  Created by Martin Pallmann on 26.09.25.
//


import SwiftUI

@MainActor
final class DayLogVM: ObservableObject {
    @Published private(set) var day: SFoodLogDay?
    private let bridge: FoodLogBridge
    private var task: Task<Void, Never>?

    init(bridge: FoodLogBridge) { self.bridge = bridge }

    func start(for date: DateComponents) {
        stop()
        task = Task {
            do {
                for try await d in bridge.observeDay(date: date) {
                    self.day = d
                }
            } catch is CancellationError {} catch {
                print("observe failed:", error)
            }
        }
    }

    func stop() { task?.cancel(); task = nil }

    func addSample() async {
        do {
            let portion = Measurement(value: 170, unit: UnitMass.grams)
            let energy  = Measurement(value: 100, unit: UnitEnergy.kilocalories) // or .kilojoules
            _ = try await bridge.add(.init(
                date: Calendar.current.dateComponents([.year, .month, .day], from: Date()),
                name: "Greek Yogurt",
                portion: portion,
                energy: energy
            ))
        } catch { print("add failed:", error) }
    }
}

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
