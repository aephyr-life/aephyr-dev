import SwiftUI

struct Food: Identifiable {
    let id = UUID()
    let name: String
    let calories: Int
    let protein: Int
    let carbs: Int
    let fat: Double
}

struct FoodRowView: View {
    let food: Food

    var body: some View {
        HStack {
            VStack(alignment: .leading, spacing: 4) {
                Text(food.name)
                    .font(.headline)
                Text("\(food.calories) kcal \(food.protein)g protein \(food.carbs)g carbs \(String(format: \"%.1f\", food.fat))g fat per 100g")
                    .font(.subheadline)
                    .foregroundColor(.secondary)
                    .lineLimit(1)
            }
            Spacer()
            Button(action: {
                // Aktion hinzufügen
            }) {
                Image(systemName: "plus")
                    .foregroundColor(.white)
                    .padding(8)
                    .background(Circle().foregroundColor(.blue))
            }
            .accessibilityLabel("Add \(food.name)")
        }
        .padding()
        .background(Color(.systemGray6))
        .cornerRadius(8)
    }
}

struct LogFoodView: View {
    @State private var searchText = ""
    private let foods = Food.mockData

    private var filteredFoods: [Food] {
        if searchText.isEmpty {
            return foods
        } else {
            return foods.filter { $0.name.localizedCaseInsensitiveContains(searchText) }
        }
    }

    var body: some View {
        NavigationStack {
            VStack(spacing: 0) {
                HStack {
                    Image(systemName: "magnifyingglass")
                        .foregroundColor(.secondary)
                    TextField("Search foods or scan barcode", text: $searchText)
                        .accessibilityLabel("Search foods or scan barcode")
                    Button(action: {
                        // Barcode scannen
                    }) {
                        Image(systemName: "barcode.viewfinder")
                            .foregroundColor(.blue)
                    }
                    .accessibilityLabel("Scan barcode")
                }
                .padding(10)
                .background(Color(.systemGray5))
                .cornerRadius(10)
                .padding(.horizontal)
                .padding(.top)

                ScrollView {
                    LazyVStack(spacing: 12) {
                        ForEach(filteredFoods) { food in
                            FoodRowView(food: food)
                        }
                    }
                    .padding()
                }
            }
            .navigationTitle("Log Food")
        }
    }
}

extension Food {
    static let mockData: [Food] = [
        .init(name: "Chicken Breast, Grilled / Cooked", calories: 165, protein: 31, carbs: 0, fat: 3.6),
        .init(name: "Salmon, Atlantic, Farmed", calories: 208, protein: 20, carbs: 0, fat: 13),
        .init(name: "Broccoli, Raw", calories: 34, protein: 2, carbs: 7, fat: 0.4),
        .init(name: "Almonds, Raw", calories: 579, protein: 21, carbs: 22, fat: 50)
    ]
}

struct LogFoodView_Previews: PreviewProvider {
    static var previews: some View {
        LogFoodView()
    }
}
