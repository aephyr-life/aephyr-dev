//
//  DailyVM.swift
//  Aephyr
//
//  Created by Martin Pallmann on 20.09.25.
//

import Foundation
import AephyrShared

@MainActor
final class DailyVM: ObservableObject {
    @Published var day: Date
    @Published var hero: IOSAggregateHero?
    @Published var entries: [IOSLoggedFood] = []

    private let store: IOSFoodStore

    init(store: IOSFoodStore, day: Date) {
        self.store = store
        self.day = day
    }

    func load() async {
        do {
            if let arr = try await store.entriesForDTO(day: day, roundingKcalStep: 50) as? [IOSLoggedFood] {
                self.entries = arr
            } else {
                self.entries = []
            }

            self.hero = try await store.heroForDTO(day: day, roundingKcalStep: 50)
        } catch {
            self.entries = []
            self.hero = nil
            print("Failed to load day: \(error)")
        }
    }

//    func go(offsetDays: Int) async {
//        // shift LocalDate in Kotlin (no minusDays on Swift side)
//        self.day = AephyrSharedUtilKt.shiftDay(base: day, days: Int32(offsetDays))
//        await load()
//    }
}

