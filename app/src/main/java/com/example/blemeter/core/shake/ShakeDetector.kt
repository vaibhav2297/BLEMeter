package com.example.blemeter.core.shake

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.example.blemeter.config.constants.ShakeDetectorConstant
import kotlin.math.sqrt

class ShakeDetector(
    private val context: Context
) : SensorEventListener {

    private var listener: OnShakeListener? = null

    private var sensorManager: SensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private var accelerometer: Sensor? = null

    init {
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    }

    fun startListening(listener: OnShakeListener) {
        this.listener = listener
        val sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        if (sensor != null) {
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    fun stopListening() {
        sensorManager.unregisterListener(this)
        listener = null
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                // Calculate the acceleration force
                val x = event.values[0]
                val y = event.values[1]
                val z = event.values[2]
                val accelerationForce = sqrt((x * x + y * y + z * z).toDouble())

                if (accelerationForce > ShakeDetectorConstant.SHAKE_THRESHOLD) {
                    // Device was shaken
                    listener?.onShake()
                }
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    interface OnShakeListener {
        fun onShake()
    }
}