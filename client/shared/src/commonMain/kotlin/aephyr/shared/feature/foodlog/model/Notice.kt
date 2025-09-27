package aephyr.shared.feature.foodlog.model

data class Notice(
    val severity: Severity,
    val code: String,        // e.g. "sodium_high", "protein_low", "missing_energy"
    val message: String      // short, user-friendly text
) {
    enum class Severity { Info, Warning, Alert }
}
