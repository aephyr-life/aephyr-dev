import AephyrShared
import KMPNativeCoroutinesAsync

final class KMMFoodLogBridge: FoodLogBridge {
    private let port: FoodLogPort
    init(port: FoodLogPort) { self.port = port }

    func observeDay(date: DateComponents) -> AsyncThrowingStream<SFoodLogDay, Error> {
        AsyncThrowingStream { continuation in
            let task = Task {
                do {
                    let kDate = try KMMDateBridge.toLocalDate(date)
                    for try await kDay in asyncSequence(for: port.observeDay(date: kDate)) {
                        continuation.yield(SFoodLogDay(from: kDay))
                    }
                    continuation.finish()
                } catch { continuation.finish(throwing: error) }
            }
            continuation.onTermination = { _ in task.cancel() }
        }
    }

    // Wrap the triple-callback suspend export with a continuation
    func add(_ cmd: SAddFoodLogItemCommand) async throws -> SFoodLogItem {
        let kCmd = try AddFoodLogItemCommand.from(cmd)

        let added: FoodLogItem = try await withCheckedThrowingContinuation { cont in
            // 1) Get the callback-taking function
            let op = port.add(command: kCmd)

            // 2) Call it with (success, failure, cancellation) -> canceller
            _ = op(
                { item, _ in
                    cont.resume(returning: item)
                    return KotlinUnit()   // satisfy KotlinUnit return
                },
                { error, _ in
                    cont.resume(throwing: error)
                    return KotlinUnit()
                },
                { error, _ in           // cancellation callback (sometimes nil)
                    cont.resume(throwing: error) //  ?? CancellationError()
                    return KotlinUnit()
                }
            )
        }

        return SFoodLogItem(from: added)
    }

    func remove(id: String) async throws {

        try await withCheckedThrowingContinuation { (cont: CheckedContinuation<Void, Error>) in
            let op = port.remove(id: id)
            _ = op(
                { _, _ in               // success for Unit
                    cont.resume(returning: ())
                    return KotlinUnit()
                },
                { error, _ in
                    cont.resume(throwing: error)
                    return KotlinUnit()
                },
                { error, _ in
                    cont.resume(throwing: error)
                    return KotlinUnit()
                }
            )
        }
    }
}

