import SwiftUI

struct DailyView: View {
    let data = DailyHeroData(kcal: 1430, proteinG: 90, fatG: 12, carbsG: 43)

    var body: some View {
        let anchorDate = Date()
        let day = DietDay(anchorDate)
        let sample: [LoggedFood] = [
            .init(id: UUID(), day: day, timeOfDay: Calendar.current.date(bySettingHour: 8, minute: 10, second: 0, of: anchorDate),
                  name: "Greek yogurt", grams: 200, kcal: 150, loggedAt: Date()),
            .init(id: UUID(), day: day, timeOfDay: Calendar.current.date(bySettingHour: 13, minute: 5, second: 0, of: anchorDate),
                  name: "Chicken salad", grams: 350, kcal: 540, loggedAt: Date()),
            .init(id: UUID(), day: day, timeOfDay: nil,
                  name: "Protein bar", grams: 60, kcal: 220, loggedAt: Date())
        ]
        ZStack {
            AephyrBackground()
            ScrollView {
                LazyVStack(spacing: 16, pinnedViews: []) {
                    VStack(alignment: .leading, spacing: 8) {
                        Text("Today")
                            .font(.title3.weight(.semibold))
                            .padding(.horizontal, 16)
                        DailyHeroCard(data: .init(kcal: 1430, proteinG: 90, fatG: 12, carbsG: 43))
                            .padding(.horizontal, 16)
                    }
                    VStack(alignment: .leading, spacing: 8) {
                        Text("Food log")
                            .font(.title3.weight(.semibold))
                            .padding(.horizontal, 16)
                        DailyFoodList(day: day, items: sample, showsBuckets: true)
                            .padding(.horizontal, 16)
                    }
                    
                }
            }
        }
        .navigationBarTitleDisplayMode(.inline)
        .toolbarBackground(.automatic, for: .navigationBar)
        
    }
}
