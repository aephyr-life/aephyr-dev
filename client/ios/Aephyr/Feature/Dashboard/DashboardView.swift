//
//  DashboardView.swift
//  Aephyr
//
//  Created by Martin Pallmann on 19.09.25.
//

import SwiftUI

struct DashboardView: View {
    @StateObject private var vm = DashboardViewModel()
    @State private var showLogSheet = false

    var body: some View {
        NavigationStack {
            ScrollView {
                VStack(spacing: 16) {
                    SummaryCard(
                        title: "Today",
                        subtitle: "Calories",
                        value: "\(vm.todayCalories) kcal"
                    )

                    MacroRow(protein: vm.proteinG, carbs: vm.carbsG, fat: vm.fatG)

                    RecentList(items: vm.recent)
                }
                .padding()
            }
            .navigationTitle("Today")
            .navigationBarTitleDisplayMode(.large)
            .onAppear { vm.load() }

            // Floating Add button
            .overlay(alignment: .bottomTrailing) {
                Button {
                    showLogSheet = true
                } label: {
                    Image(systemName: "plus")
                        .font(.title2)
                        .foregroundColor(.white)
                        .frame(width: 56, height: 56)
                        .background(Circle().fill(Color.accentColor))
                        .shadow(radius: 6, y: 3)
                }
                .padding(.trailing, 20)
                .padding(.bottom, 24)
                .accessibilityLabel("Add food")
            }

            // Present the FoodLogView in a sheet
            .sheet(isPresented: $showLogSheet) {
                NavigationStack {
                    FoodLogView()
                }
            }
        }
    }
}

