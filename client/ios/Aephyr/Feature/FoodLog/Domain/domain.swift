import Foundation

struct SDietMoment: Sendable, Equatable {
    var dayComponents: DateComponents
    var timeMinutes: Int?
}

struct SMacros: Sendable, Equatable {
    let protein: Measurement<UnitMass>
    let fat: Measurement<UnitMass>
    let carb: Measurement<UnitMass>
}

public struct SFoodLogItem: Identifiable, Sendable, Equatable {
    public var id: String
    let itemId: String
    let consumedAt: SDietMoment
    let name: String
    let portion: Measurement<UnitMass>?
    let energy: Measurement<UnitEnergy>?
    let macros: SMacros?
    let notices: [String]
    let loggedAt: Date
}

public struct SFoodLogDay: Identifiable, Sendable, Equatable {
    public let date: DateComponents
    public let items: [SFoodLogItem]
    let totalEnergy: Measurement<UnitEnergy>
    
    public var id: DateComponents { date }
    
    public init(date: DateComponents, items: [SFoodLogItem]) {
        self.date = date
        self.items = items
        self.totalEnergy = SFoodLogDay.en(items: items)
    }
    
    public static func empty(for date: DateComponents) -> SFoodLogDay {
        SFoodLogDay(date: date, items: [])
    }

    public func adding(_ item: SFoodLogItem) -> SFoodLogDay {
        SFoodLogDay(date: date, items: items + [item])
    }

    public func removing(id: SFoodLogItem.ID) -> SFoodLogDay {
        SFoodLogDay(date: date, items: items.filter { $0.id != id })
    }
    
    private static func en(items: [SFoodLogItem]) -> Measurement<UnitEnergy> {
        items.reduce(Measurement(value: 0.0, unit: .kilojoules)) { partial, item in
            let value = item.energy?.converted(to: .kilojoules).value ?? 0.0
            return Measurement(value: partial.value + value, unit: .kilojoules)
        }
    }
}

struct SAddFoodLogItemCommand: Sendable {
    let consumedAt: SDietMoment
    let name: String
    let portion: Measurement<UnitMass>?
    let energy: Measurement<UnitEnergy>?
    let macros: SMacros?
}
