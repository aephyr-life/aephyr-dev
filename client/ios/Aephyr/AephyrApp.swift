//
//  AephyrApp.swift
//  Aephyr
//
//  Created by Martin Pallmann on 14.08.25.
//

import AephyrShared
import SwiftUI

@main
struct AephyrApp: App {
    
    @StateObject private var settings = AppSettings()
    
    var body: some Scene {
        let bridge = KMMFoodLogBridge(port: FoodLogFactory.shared.local())
        WindowGroup {
            DayLogView(vm: DayLogVM(bridge: bridge))
                .environmentObject(settings)
        }
    }
}
