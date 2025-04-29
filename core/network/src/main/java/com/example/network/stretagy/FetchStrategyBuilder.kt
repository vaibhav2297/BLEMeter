package com.example.network.stretagy

/**
 * A DSL for deciding whether to fetch data from local cache or remote server.
 */
class FetchStrategyBuilder<T> {

    private var localBlock: (suspend () -> T)? = null
    private var serverBlock: (suspend () -> T)? = null
    private var saveBlock: (suspend (T) -> Unit)? = null
    private var predicate: (() -> Boolean)? = null

    /**
     * Define how to fetch from local cache.
     */
    fun local(block: suspend () -> T) = apply { localBlock = block }

    /**
     * Define how to fetch from remote server.
     */
    fun server(block: suspend () -> T) = apply { serverBlock = block }

    /**
     * Define how to save fresh data into local storage.
     */
    fun save(block: suspend (T) -> Unit) = apply { saveBlock = block }

    /**
     * Predicate to decide whether to fetch from server.
     */
    fun decide(condition: () -> Boolean) = apply { predicate = condition }

    /**
     * Execute the fetch strategy:
     * - If predicate is true, fetch from server, optionally save, then return fresh data.
     * - Otherwise, fetch from local cache.
     */
    suspend fun execute(): T {
        val useServer = predicate?.invoke() ?: true
        return if (useServer) {
            val fresh = serverBlock?.invoke()
                ?: error("Server block is not defined")

            saveBlock?.invoke(fresh)
            fresh
        } else {
            localBlock?.invoke()
                ?: error("Local block is not defined")
        }
    }
}

/**
 * Helper to invoke the DSL in a suspend context.
 */
suspend fun <T> fetchData(
    block: FetchStrategyBuilder<T>.() -> Unit
): T = FetchStrategyBuilder<T>().apply(block).execute()