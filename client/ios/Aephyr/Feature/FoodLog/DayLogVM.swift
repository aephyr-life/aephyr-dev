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
