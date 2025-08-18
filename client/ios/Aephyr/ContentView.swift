//
//  ContentView.swift
//  Aephyr
//
//  Created by Martin Pallmann on 14.08.25.
//

import SwiftUI
import AephyrShared

struct ContentView: View {
    
    let greeting = Greeting().text()
    
    var body: some View {
        ZStack {
            Color.yellow.ignoresSafeArea()
            VStack(spacing: 16) {
                Text("SwiftUI is alive ✅").font(.title).bold()
                Text(greeting) // <- from Kotlin
            }
        }
    }
}

#Preview {
    ContentView()
}
