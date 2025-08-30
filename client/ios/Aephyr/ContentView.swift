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
    
    @StateObject private var vm = HealthViewModel()
    
    init() {
        print("👉 ContentView.init called")
    }
    
    var body: some View {
        VStack(spacing: 12) {
            Text("Health: \(vm.status)")
                .font(.title3).bold()

            if let err = vm.error {
                Text(err)
                    .foregroundStyle(.red)
                    .font(.footnote)
                    .multilineTextAlignment(.center)
            }

            Button("Ping") { vm.load() }
                .buttonStyle(.borderedProminent)
        }
        .padding()
        .onAppear { vm.load() }
    }
}

#Preview {
    ContentView()
}
