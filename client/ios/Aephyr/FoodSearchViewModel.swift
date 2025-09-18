//
//  FoodSearchViewModel.swift
//  Aephyr
//
//  Created by Martin Pallmann on 18.09.25.
//
import Foundation
import AephyrShared

@MainActor
final class FoodSearchViewModel: ObservableObject {
    @Published var foods: [Food] = []
    @Published var isLoading = false
    @Published var error: String?

    private let service = OffFoodService(staging: false)

    func search(_ query: String) {
        guard !query.isEmpty else {
            foods = []
            return
        }
        isLoading = true
        error = nil

        Task {
            do {
                let results = try await service.search(query: query)
                self.foods = results.map { dto in
                    Food(
                        name: dto.name,
                        calories: Int(dto.per100g.kcal),
                        protein: Int(dto.per100g.protein),
                        carbs: Int(dto.per100g.carbs),
                        fat: dto.per100g.fat
                    )
                }
                self.isLoading = false
            } catch {
                self.error = error.localizedDescription
                self.isLoading = false
            }
        }
    }
}

