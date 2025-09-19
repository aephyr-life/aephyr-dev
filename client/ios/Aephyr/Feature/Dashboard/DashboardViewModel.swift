//
//  DashboardViewModel.swift
//  Aephyr
//
//  Created by Martin Pallmann on 19.09.25.
//

import SwiftUI

@MainActor
final class DashboardViewModel: ObservableObject {
    @Published var todayCalories: Int = 0
    @Published var proteinG: Int = 0
    @Published var carbsG: Int = 0
    @Published var fatG: Int = 0
    @Published var recent: [LoggedFood] = []

    func load() {
        // TODO hook up to KMM diary later
        todayCalories = 1875
        proteinG = 120
        carbsG = 180
        fatG = 60
        recent = [
            .init(name: "Chicken Breast", grams: 150, kcal: 248),
            .init(name: "Rice (cooked)", grams: 200, kcal: 260),
            .init(name: "Apple", grams: 150, kcal: 78)
        ]
    }
}

struct LoggedFood: Identifiable {
    let id = UUID()
    let name: String
    let grams: Int
    let kcal: Int
}
