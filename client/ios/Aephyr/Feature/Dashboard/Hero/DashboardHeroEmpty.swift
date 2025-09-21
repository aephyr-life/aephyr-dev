//
//  DailyHeroSkeleton.swift
//  Aephyr
//
//  Created by Martin Pallmann on 20.09.25.
//



import SwiftUI

struct DashboardHeroEmpty: View {

    var body: some View {
    
        
        HStack(alignment: .center, spacing: 16) {
           

            // Textual summary
            VStack(alignment: .leading, spacing: 14) {
                // Big kcal line
                HStack(alignment: .firstTextBaseline, spacing: 4) {
                  
                }

                // Three columns: protein / fat / carbs
                HStack(alignment: .top) {
                   
                }
            }
        }
        .padding(16)
        .background(CardBackground())
        .overlay(
            RoundedRectangle(cornerRadius: 20, style: .continuous)
                .strokeBorder(.white.opacity(0.08))
        )
        .accessibilityElement(children: .combine)
        .accessibilityLabel("Daily summary")
       // .accessibilityValue("\(data.kcal) kilocalories. Protein \(Int(data.proteinG)) grams, fat (Int(data.fatG)) grams, carbs \(Int(data.carbsG)) grams.")
    }
}

