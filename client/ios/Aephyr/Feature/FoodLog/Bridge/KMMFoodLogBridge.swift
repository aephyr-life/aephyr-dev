//
//  KMMFoodLogBridge.swift
//  Aephyr
//
//  Created by Martin Pallmann on 26.09.25.
//


//import AephyrShared
//import KMPNativeCoroutinesAsync
//
//final class KMMFoodLogBridge: FoodLogBridge {
//    private let port: FoodLogPort
//
//    init(port: FoodLogPort) { self.port = port }
//
//    func observeDay(date: DateComponents) -> AsyncThrowingStream<SFoodLogDay, Error> {
//        AsyncThrowingStream { continuation in
//            Task {
//                do {
//                    let kDate = try KMMDate.toKotlinLocalDate(date)
//                    for try await kDay in asyncSequence(for: port.observeDay(date: kDate)) {
//                        continuation.yield(SFoodLogDay(kDay))
//                    }
//                    continuation.finish()
//                } catch {
//                    continuation.finish(throwing: error)
//                }
//            }
//        }
//    }
//
//    func add(_ cmd: SAddFoodLogItemCommand) async throws -> SFoodLogItem {
//        let kCmd = try AddFoodLogItemCommand.from(cmd)
//        let added = try await port.add(command: kCmd)
//        return SFoodLogItem(added)
//    }
//
//    func remove(id: SFoodLogItemID) async throws {
//        try await port.remove(id: FoodLogItemId.from(id))
//    }
//}
