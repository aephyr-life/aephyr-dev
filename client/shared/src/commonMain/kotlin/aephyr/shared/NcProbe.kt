package aephyr.shared

import com.rickclephas.kmp.nativecoroutines.NativeCoroutines

@NativeCoroutines
suspend fun ncPing(): String = "ok"
