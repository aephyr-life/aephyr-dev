package aephyr.shared.feature.settings

import com.russhwolf.settings.Settings
import com.russhwolf.settings.PreferencesSettings
import java.util.prefs.Preferences

actual class SettingsProvider {
    actual val settings: Settings by lazy {
        // Change the node path if you want a different namespace
        PreferencesSettings(Preferences.userRoot().node("life.aephyr.shared"))
    }
}
