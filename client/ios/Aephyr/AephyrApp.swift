//
//  AephyrApp.swift
//  Aephyr
//
//  Created by Martin Pallmann on 14.08.25.
//

import SwiftUI

@main
struct AephyrApp: App {
    
    @AppStorage("energyUnit")
    private var selectedRaw: String = EnergyUnit.kilocalories.rawValue
    
    var body: some Scene {
        WindowGroup {
            DashboardView()
        }
    }
}
