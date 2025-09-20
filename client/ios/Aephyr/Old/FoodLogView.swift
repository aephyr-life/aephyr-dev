import SwiftUI
import AephyrShared

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
        HStack(spacing: 12) {
            VStack(alignment: .leading, spacing: 6) {
                Text(food.name)
                    .font(.headline)
                    .fontWeight(.semibold)

                HStack(spacing: 16) {
                    Text("\(food.calories) kcal")
                    Text("\(food.protein)g protein")
                    if food.carbs > 0 { Text("\(food.carbs)g carbs") }
                    if food.fat > 0 { Text("\(String(format: "%.1f", food.fat))g fat") }
                }
                .font(.subheadline)
                .foregroundColor(.secondary)

                Text("per 100g")
                    .font(.caption)
                    .foregroundColor(.secondary)
            }

            Spacer()

            Button {
                // add item action
            } label: {
                Image(systemName: "plus")
                    .font(.headline)
                    .frame(width: 36, height: 36)
                    .overlay(Circle().stroke(.primary, lineWidth: 2))
                    .contentShape(Circle())
            }
            .buttonStyle(.plain)
            .accessibilityLabel("Add \(food.name)")
        }
        .padding(16)
        .background(Color(.systemGray6))
        .clipShape(RoundedRectangle(cornerRadius: 20, style: .continuous))
    }
}

struct FoodLogView: View {
    @StateObject private var vm = FoodSearchViewModel()
    @State private var searchText: String

    init(initialQuery: String = "") {
        _searchText = State(initialValue: initialQuery)
    }

    var body: some View {
        NavigationStack {
            VStack(spacing: 12) {
                // Search + barcode
                HStack(spacing: 12) {
                    HStack(spacing: 8) {
                        Image(systemName: "magnifyingglass")
                            .foregroundColor(.secondary)
                        TextField("Search foods or scan barcode", text: $searchText)
                            .textInputAutocapitalization(.never)
                            .autocorrectionDisabled()
                            .onSubmit { vm.search(searchText) }
                    }
                    .padding(12)
                    .background(
                        RoundedRectangle(cornerRadius: 12, style: .continuous)
                            .fill(Color(.systemGray5))
                    )

                    Button {
                        vm.search(searchText)
                    } label: {
                        Image(systemName: "barcode.viewfinder")
                            .font(.headline)
                            .padding(12)
                            .background(
                                RoundedRectangle(cornerRadius: 12, style: .continuous)
                                    .fill(Color(.label).opacity(0.9))
                            )
                            .foregroundColor(.white)
                    }
                    .accessibilityLabel("Scan barcode")
                }
                .padding(.horizontal)

                if vm.isLoading {
                    ProgressView("Loading…").padding(.top)
                } else if let error = vm.error {
                    Text("Error: \(error)")
                        .foregroundColor(.red)
                        .padding(.top)
                } else if vm.foods.isEmpty, !searchText.isEmpty {
                    Text("No results found")
                        .foregroundColor(.secondary)
                        .padding(.top)
                }

                ScrollView {
                    LazyVStack(spacing: 14) {
                        ForEach(vm.foods) { food in
                            FoodRowView(food: food)
                        }
                    }
                    .padding(.horizontal)
                    .padding(.top, 4)
                }
            }
            .navigationTitle("Log Food")
            .task(id: searchText) {
                if !searchText.isEmpty {
                    vm.search(searchText)
                }
            }
        }
    }
}

#Preview {
    FoodLogView()
}


