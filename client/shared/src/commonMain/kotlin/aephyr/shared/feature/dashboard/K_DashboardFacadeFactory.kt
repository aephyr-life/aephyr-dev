package aephyr.shared.feature.dashboard

class K_DashboardFacadeFactory {
    fun mock() : DashboardFacade {
        return DashboardFacade(MockRepo())
    }

    private class MockRepo() : DashboardRepository {
        override suspend fun fetchToday(): DashboardData {
            return DashboardData(
                hero = Hero("Today"),
                entries = emptyList()
            )
        }

        override suspend fun remove(id: String) {

        }

    }
}
