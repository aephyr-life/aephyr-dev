//
//  FoodLogBridge.swift
//  Aephyr
//
//  Created by Martin Pallmann on 26.09.25.
//


import Foundation

protocol FoodLogBridge: Sendable {
    func observeDay(date: DateComponents) -> AsyncThrowingStream<SFoodLogDay, Error>
    func add(_ cmd: SAddFoodLogItemCommand) async throws -> SFoodLogItem
    func remove(id: String) async throws
}
