//
//  KotlinUtil.swift
//  Aephyr
//
//  Created by Martin Pallmann on 20.09.25.
//

import AephyrShared

@inline(__always) func KInt(_ v: KotlinInt?) -> Int? { v.map { Int($0.int32Value) } }
@inline(__always) func KDouble(_ v: KotlinDouble?) -> Double? { v.map { $0.doubleValue } }
// If you ever use Long?/Float?:
@inline(__always) func KLong(_ v: KotlinLong?) -> Int64? { v.map { $0.int64Value } }
@inline(__always) func KFloat(_ v: KotlinFloat?) -> Float? { v.map { v in v.floatValue } }

extension Optional where Wrapped == KotlinInt {
    var asInt: Int? { self.map { Int($0.int32Value) } }
    var orZero: Int { asInt ?? 0 }
}

extension Optional where Wrapped == KotlinDouble {
    var asDouble: Double? { self.map { $0.doubleValue } }
}
