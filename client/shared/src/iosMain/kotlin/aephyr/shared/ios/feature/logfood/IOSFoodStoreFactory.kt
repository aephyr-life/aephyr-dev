package aephyr.shared.ios.feature.logfood

import aephyr.shared.feature.logfood.data.MockFoodStore

class IOSFoodStoreFactory {
    fun mock(): IOSFoodStore {
        return IOSFoodStore(MockFoodStore())
    }
}
