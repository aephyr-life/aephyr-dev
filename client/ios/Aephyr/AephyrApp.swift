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
    
    var body: some Scene {
        WindowGroup {
            DayLogView(bridge: KMMFoodLogBridge(port: FoodLogFactory.shared.local()))
        }
    }
}
