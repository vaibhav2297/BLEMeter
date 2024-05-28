package com.example.blemeter.feature.communication.domain.usecases

import com.example.blemeter.core.ble.data.repository.IBLERepository
import com.example.blemeter.core.ble.domain.bleparsable.MeterDataCommand
import com.example.blemeter.core.ble.domain.model.request.MeterDataRequest
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalUnsignedTypes::class)
class ReadMeterDataUseCase @Inject constructor(
    bleRepository: IBLERepository,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : BaseCommandUseCase(bleRepository) {

    private val pollingInterval = 3L
    private var pollingJob: Job? = null

    private val _isPolling = MutableStateFlow(false)
    val isPolling = _isPolling.asStateFlow()

    fun startMeterDataPolling(request: MeterDataRequest) {
        pollingJob = CoroutineScope(ioDispatcher).launch {
            while (isActive) {
                val baseRequest = getBaseRequest()
                val finalRequest = request.copy(baseRequest = baseRequest)
                val result = writeCharacteristic(MeterDataCommand, finalRequest)
                _isPolling.update { result.isSuccess }
                if (result.isFailure) {
                    cancel("${result.exceptionOrNull()?.message}")
                    break
                }
                delay(pollingInterval)
            }
        }
    }

    fun stopMeterDataPolling() {
        pollingJob?.cancel()
    }
}