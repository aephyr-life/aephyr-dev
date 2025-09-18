//
//  AephyrApp.swift
//  Aephyr
//
//  Created by Martin Pallmann on 14.08.25.
//

import SwiftUI

@main
struct AephyrApp: App {
    init() { print("AephyrApp.init") }
    var body: some Scene {
        WindowGroup {
            FoodLogView()
//            ContentView()
//                .onAppear { print("ContentView appeared") }
        }
    }
}
