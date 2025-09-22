package aephyr.shared.core.bridge

import kotlinx.coroutines.Job

interface K_Cancellable { fun cancel() }

internal class JobCancellable(private val job: Job) : K_Cancellable {
    override fun cancel() = job.cancel()
}
