package aephyr.shared.feature.settings

import com.russhwolf.settings.Settings

// Expect the platform to provide a concrete Settings
expect class SettingsProvider {
    val settings: Settings
}
