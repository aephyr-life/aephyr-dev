//
//  FoodSearchScreen.swift
//  Aephyr
//
//  Created by Martin Pallmann on 27.09.25.
//


import SwiftUI
import VisionKit

struct FoodSearchScreen: View {
    @State private var query = ""
    @State private var isSearching = false
    @State private var scope: Scope = .all
    @State private var showScanner = false

    enum Scope: String, CaseIterable, Identifiable {
        case all = "All", favorites = "Favorites", brand = "Brand"
        var id: String { rawValue }
    }

    var body: some View {
        NavigationStack {
            List {
                // Results
                Section {
                    ForEach(results(for: query, scope: scope), id: \.id) { item in
                        Button { confirm(item) } {
                            VStack(alignment: .leading) {
                                Text(item.name)
                                Text(item.subtitle).font(.caption).foregroundStyle(.secondary)
                            }
                        }
                    }
                }

                // Inline quick add (native row, no custom chrome)
                if !query.isEmpty {
                    Section {
                        Button {
                            quickAdd(query)
                        } label: {
                            Label("Add quick entry “\(query)”", systemImage: "plus.circle")
                        }
                    }
                }
            }
            .navigationTitle("Add Food")
            // Native expanding search bar in the nav area
            .searchable(text: $query,
                        isPresented: $isSearching,
                        placement: .navigationBarDrawer(displayMode: .always),
                        prompt: "Search foods")
            .searchSuggestions {
                // Recents / suggestions (tap to fill the field)
                ForEach(recentQueries(), id: \.self) { q in
                    Text(q).searchCompletion(q)
                }
                if !query.isEmpty {
                    // Also expose quick add as a suggestion
                    Text("Add quick entry “\(query)”").onTapGesture { quickAdd(query) }
                }
            }
            .searchScopes($scope) {
                ForEach(Scope.allCases) { s in
                    Text(s.rawValue).tag(s)
                }
            }
            .onSubmit(of: .search) {
                performSearch(query, scope: scope)
            }
            .toolbar {
                // A plain, stock toolbar button for barcode scanning
                ToolbarItem(placement: .topBarTrailing) {
                    Button {
                        showScanner = true
                    } label: {
                        Image(systemName: "barcode.viewfinder")
                    }
                    .accessibilityLabel("Scan barcode")
                }
            }
            .sheet(isPresented: $showScanner) {
                // Stock barcode via VisionKit wrapper
                BarcodeScannerSheet { code in
                    handleScanned(code)
                }
            }
        }
    }

    // MARK: intents (replace with your VM/bridge)
    func performSearch(_ q: String, scope: Scope) { /* debounce + API */ }
    func results(for q: String, scope: Scope) -> [Food] { [] }
    func quickAdd(_ text: String) { /* create lightweight entry + undo */ }
    func confirm(_ item: Food) { /* portion/time confirm sheet */ }
    func recentQueries() -> [String] { ["banana 120g", "oats 80g", "yogurt"] }
    func handleScanned(_ code: String) { /* lookup by barcode, fallback to quick add */ }
}
