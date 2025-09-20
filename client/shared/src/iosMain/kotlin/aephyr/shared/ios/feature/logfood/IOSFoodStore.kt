package aephyr.shared.ios.feature.logfood

import aephyr.shared.feature.logfood.data.FoodStore
import aephyr.shared.feature.logfood.data.aggregateHero
import aephyr.shared.ios.util.IOSDateBridge.nsDateToLocalDate
import platform.Foundation.NSArray
import platform.Foundation.NSDate
import platform.Foundation.NSMutableArray

class IOSFoodStore(private val delegate: FoodStore) {
    suspend fun entriesForDTO(day: NSDate, roundingKcalStep: Int = 50): NSArray {
        val arr = NSMutableArray()
        val localDate = nsDateToLocalDate(day)
        delegate.entriesFor(localDate)
            .sorted() // by your Comparable (consumedAt, loggedAt, name, id)
            .forEach { arr.addObject(it.toDTO(roundingKcalStep = roundingKcalStep)) }
        return arr
    }

    suspend fun heroForDTO(day: NSDate, roundingKcalStep: Int = 50): IOSAggregateHero {
        val localDate = nsDateToLocalDate(day)
        return delegate.aggregateHero(localDate, roundingKcalStep).toDTO()
    }
}
