import SwiftUI

import AephyrShared

struct DailyFoodList: View {
    @ObservedObject var vm: DailyVM
    var showsBuckets: Bool = true

    var body: some View {

        LazyVStack(spacing: 12, pinnedViews: []) {
            ForEach(vm.entries, id: \.id) { item in
                FoodRow(row: item)
            }
        }
        .padding(16)
        .overlay(
            RoundedRectangle(cornerRadius: 20, style: .continuous)
                .strokeBorder(.white.opacity(0.08))
        )
        .background(CardBackground())
    }
}
