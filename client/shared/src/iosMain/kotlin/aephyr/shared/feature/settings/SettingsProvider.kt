package aephyr.shared.feature.settings

import com.russhwolf.settings.Settings
import com.russhwolf.settings.NSUserDefaultsSettings
import platform.Foundation.NSUserDefaults

actual class SettingsProvider {
    actual val settings: Settings by lazy {
        NSUserDefaultsSettings(NSUserDefaults.standardUserDefaults)
    }
}
