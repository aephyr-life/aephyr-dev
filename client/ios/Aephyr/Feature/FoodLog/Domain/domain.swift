import Foundation

struct SDietMoment: Sendable {
    var dayComponents: DateComponents          // only Y-M-D
    var timeMinutes: Int?            // minutes since midnight, local
}

struct SMacros: Sendable {
    let protein: Measurement<UnitMass>
    let fat: Measurement<UnitMass>
    let carb: Measurement<UnitMass>
}

struct SFoodLogItem: Identifiable, Sendable {
    var id: String
    let itemId: String
    let consumedAt: SDietMoment
    let name: String
    let portion: Measurement<UnitMass>?
    let energy: Measurement<UnitEnergy>?
    let macros: SMacros?
    let notices: [String]
    let loggedAt: Date
}

struct SFoodLogDay: Sendable {
    let date: DateComponents
    let items: [SFoodLogItem]
    let totalEnergy: Measurement<UnitEnergy> // kcal by default
}

struct SAddFoodLogItemCommand: Sendable {
    let consumedAt: SDietMoment
    let name: String
    let portion: Measurement<UnitMass>?
    let energy: Measurement<UnitEnergy>?
    let macros: SMacros?
}
