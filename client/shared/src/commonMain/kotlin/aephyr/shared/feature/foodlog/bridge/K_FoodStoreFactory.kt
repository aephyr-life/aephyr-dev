package aephyr.shared.feature.foodlog.bridge

import aephyr.shared.feature.foodlog.data.MockFoodStore

class K_FoodStoreFactory {
    fun instance(): K_FoodStore =
        K_FoodStore(MockFoodStore())
}
