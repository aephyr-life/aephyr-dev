import SwiftUI
import AephyrShared

struct DailyView: View {
    @StateObject private var vm: DailyVM
    @State private var isLoading = true
    
    init(store: IOSFoodStore = IOSFoodStoreFactory().mock(), day: Date = Date()) {
        _vm = StateObject(wrappedValue: DailyVM(store: store, day: day))
    }

    var body: some View {
       
        ZStack {
            AephyrBackground()
            ScrollView {
                LazyVStack(spacing: 16, pinnedViews: []) {
                    VStack(alignment: .leading, spacing: 8) {
                        Text("Today")
                            .font(.largeTitle.weight(.semibold))
                            .padding(.horizontal, 16)
                            .padding(.vertical, 16)
                        Group {
                           if isLoading {
                               DailyHeroSkeleton()
                           } else if let hero = vm.hero {
                               DailyHeroCard(data: hero)
                           } else {
                               DailyHeroEmpty()
                           }
                        }
                        .padding(.horizontal, 16)
                        .task {
                                    isLoading = true
                                    await vm.load()
                                    isLoading = false
                        }
                    }
                    VStack(alignment: .leading, spacing: 8) {
                        DailyFoodList(vm: vm, showsBuckets: true)
                            .padding(.horizontal, 16)
                    }
                    
                }
            }
        }
        .navigationBarTitleDisplayMode(.inline)
        .toolbarBackground(.automatic, for: .navigationBar)
        .task { await vm.load() }
        .refreshable { await vm.load() }
        
    }
}
