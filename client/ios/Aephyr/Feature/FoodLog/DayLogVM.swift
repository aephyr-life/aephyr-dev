//
//  DayLogVM.swift
//  Aephyr
//
//  Created by Martin Pallmann on 26.09.25.
//


import SwiftUI

@MainActor
final class DayLogVM: ObservableObject {
    @Published private(set) var day: SFoodLogDay
    private let bridge: FoodLogBridge
    private var task: Task<Void, Never>?

    init(bridge: FoodLogBridge) {
        self.bridge = bridge
        self.day = .empty(for: Calendar.current.dateComponents([.year,.month,.day], from: Date()))
    }

    func start(for date: DateComponents) {
        stop()
        task = Task {
            do {
                for try await d in bridge.observeDay(date: date) {
                    self.day = d
                }
            } catch is CancellationError {
            } catch {
                print("observe failed:", error)
            }
        }
    }

    func stop() { task?.cancel(); task = nil }

    // generic add (you can keep addSample for quick testing)
    func add(_ cmd: SAddFoodLogItemCommand) async {
        do { _ = try await bridge.add(cmd) }
        catch { print("add failed:", error) }
    }
    
    func remove(_ id: String) async {
        do { _ = try await bridge.remove(id: id) }
        catch { print("remove failed:", error) }
    }
}
