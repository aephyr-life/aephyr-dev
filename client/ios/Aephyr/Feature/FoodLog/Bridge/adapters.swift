import AephyrShared
import Foundation

// MARK: - Energy

extension Measurement where UnitType == UnitEnergy {
    init(kmm e: Energy) {
        // Pick a UnitEnergy based on the EnergyUnit’s conversion factor.
        // kcal has kJPerUnit ≈ 4.184; kJ has kJPerUnit = 1
        let kJPerUnit = e.unit.kJPerUnit
        if abs(kJPerUnit - 4.184) < 0.001 {
            self.init(value: e.value, unit: .kilocalories)
        } else if abs(kJPerUnit - 1.0) < 0.000_001 {
            self.init(value: e.value, unit: .kilojoules)
        } else {
            // Fallback: represent as kilojoules
            self.init(value: e.value * kJPerUnit, unit: .kilojoules)
        }
    }
}

extension Energy {
    static func from(_ m: Measurement<UnitEnergy>) -> Energy {
        // Map Measurement to KMM’s Energy (prefer KILOCALORIE or KILOJOULE)
        switch m.unit {
        case .kilocalories: return Energy(value: m.value, unit: EnergyUnit.kilocalorie)
        case .kilojoules:   return Energy(value: m.value, unit: EnergyUnit.kilojoule)
        default:
            // Convert unknown UnitEnergy to kilocalories
            let kcal = m.converted(to: .kilocalories).value
            return Energy(value: kcal, unit: EnergyUnit.kilocalorie)
        }
    }
}

// MARK: - Mass

extension Measurement where UnitType == UnitMass {
    init(kmm m: Mass) {
        let gPerUnit = m.unit.gramsPerUnit
        switch gPerUnit {
        case 1.0e-6: self.init(value: m.value, unit: .micrograms)
        case 1.0e-3: self.init(value: m.value, unit: .milligrams)
        case 1.0:    self.init(value: m.value, unit: .grams)
        case 1_000:  self.init(value: m.value, unit: .kilograms)
        default:
            // Fallback: express as grams
            self.init(value: m.value * gPerUnit, unit: .grams)
        }
    }
}

extension Mass {
    static func from(_ m: Measurement<UnitMass>) -> Mass {
        switch m.unit {
        case .micrograms: return Mass(value: m.value, unit: MassUnit.microgram)
        case .milligrams: return Mass(value: m.value, unit: MassUnit.milligram)
        case .grams:      return Mass(value: m.value, unit: MassUnit.gram)
        case .kilograms:  return Mass(value: m.value, unit: MassUnit.kilogram)
        default:
            // Convert unknown UnitMass to grams
            let grams = m.converted(to: .grams).value
            return Mass(value: grams, unit: MassUnit.gram)
        }
    }
}

// MARK: - Macros

extension SMacros {
    init(kmm m: Macros) {
        protein = Measurement<UnitMass>(kmm: m.protein)
        fat     = Measurement<UnitMass>(kmm: m.fat)
        carb    = Measurement<UnitMass>(kmm: m.carb)
    }
}

extension Macros {
    static func from(_ s: SMacros) -> Macros {
        Macros(
            protein: Mass.from(s.protein),
            fat:     Mass.from(s.fat),
            carb:    Mass.from(s.carb)
        )
    }
}

// DietMoment — until you share its shape, use .description
extension String {
    init(kmm moment: DietMoment) { self = "\(moment)" }
}

// Instant mapping
extension Date {
    init(kmmInstantMillis epochMs: Int64) { self = Date(timeIntervalSince1970: TimeInterval(epochMs) / 1000.0) }
}

enum KMMDateBridge {
    /// Swift `DateComponents` (Y/M/D) -> Kotlinx_datetime `LocalDate` (numeric ctor).
    static func toLocalDate(_ comps: DateComponents) throws -> Kotlinx_datetimeLocalDate {
        guard let y = comps.year, let m = comps.month, let d = comps.day else {
            throw NSError(domain: "Bridge", code: 1,
                          userInfo: [NSLocalizedDescriptionKey: "Missing year/month/day in DateComponents"])
        }
        return Kotlinx_datetimeLocalDate(year: Int32(y), month: Int32(m), day: Int32(d))
    }

    /// Kotlinx_datetime `LocalDate` -> Swift `DateComponents` (no reflection).
    static func toDateComponents(_ k: Kotlinx_datetimeLocalDate,
                                 calendar: Calendar = .current) -> DateComponents {

        let monthNumber = Int(k.month.ordinal + 1)
        let dayNumber   = Int(k.day)

        return DateComponents(
            calendar: calendar,
            timeZone: calendar.timeZone,
            year: Int(k.year),
            month: monthNumber,
            day: dayNumber
        )
    }
}


extension SDietMoment {
    init(_ k: DietMoment) {
        self.dayComponents = KMMDateBridge.toDateComponents(k.day)
        // Kotlin nullable Int -> Swift: KotlinInt?
        self.timeMinutes = (k.timeMinutes as NSNumber?)?.intValue
    }
}

extension DietMoment {
    static func from(_ s: SDietMoment) throws -> DietMoment {
        let kDay = try KMMDateBridge.toLocalDate(s.dayComponents) // uses year/month/day Int32s
        let kMinutes: KotlinInt? = s.timeMinutes.map { KotlinInt(value: Int32($0)) }
        return DietMoment(day: kDay, timeMinutes: kMinutes)
    }
}

extension SFoodLogItem {
    init(from k: FoodLogItem) {
        // value classes -> Any in Swift; cast to String
        self.id         = (k.id   as? String) ?? String(describing: k.id)
        self.itemId     = self.id
        self.name       = (k.name as? String) ?? String(describing: k.name)

        self.consumedAt = SDietMoment(k.consumedAt)

        self.portion    = k.mass   .map { Measurement<UnitMass>(kmm: $0) }
        self.energy     = k.energy .map { Measurement<UnitEnergy>(kmm: $0) }
        self.macros     = k.macros .map(SMacros.init)
        self.notices    = k.notices.map { "\($0)" }

        // you can map Instant precisely now:
        self.loggedAt   = Date(timeIntervalSince1970:
                               TimeInterval(k.loggedAt.toEpochMilliseconds()) / 1000)
    }
}


extension SFoodLogDay {
    init(from k: FoodLogDay) {
        self.date  = KMMDateBridge.toDateComponents(k.date)
        self.items = k.items.map { SFoodLogItem(from: $0) }

        // If your KMM day has a total, prefer it. Otherwise sum best-effort:
        let kcal = k.items
            .compactMap { $0.energyBestEffort() }
            .map { Measurement<UnitEnergy>(kmm: $0).converted(to: .kilocalories).value }
            .reduce(0, +)
        self.totalEnergy = Measurement(value: kcal, unit: .kilocalories)
    }
    
    /// Render day total with prefs.
    func totalEnergyString(_ prefs: EnergyPrefs) -> String {
        totalEnergy.formatted(prefs)
    }
}

extension SAddFoodLogItemCommand {
    /// Convenience ctor using Measurements (no kcal literals).
    init(date: DateComponents,
         name: String,
         portion: Measurement<UnitMass>?,
         energy: Measurement<UnitEnergy>?,
         macros: SMacros? = nil) {
        self.init(
            consumedAt: SDietMoment(dayComponents: date, timeMinutes: nil),
            name: name,
            portion: portion,
            energy: energy,
            macros: macros
        )
    }
}

extension AddFoodLogItemCommand {
    static func from(_ s: SAddFoodLogItemCommand) throws -> AddFoodLogItemCommand {
        AddFoodLogItemCommand(
            sourceId: nil,
            name: s.name,
            mass: s.portion .map(Mass.from),
            energy:  s.energy .map(Energy.from),
            macros:  s.macros .map(Macros.from),
            consumedAt: try DietMoment.from(s.consumedAt)
        )
    }
}

