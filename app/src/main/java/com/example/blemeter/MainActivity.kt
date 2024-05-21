package com.example.blemeter

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.content.FileProvider
import com.example.blemeter.app.BLEMeterApp
import com.example.blemeter.core.file.FileService
import com.example.blemeter.core.file.IFileService
import com.example.blemeter.core.shake.ShakeDetector
import com.example.blemeter.utils.Extras
import com.example.blemeter.utils.ShakeDetectorConstant
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
@AndroidEntryPoint
class MainActivity : ComponentActivity(), ShakeDetector.OnShakeListener {

    @Inject
    lateinit var shakeDetector: ShakeDetector

    @Inject
    lateinit var fileService: IFileService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initShakeDetector()

        enableEdgeToEdge()
        setContent {
            BLEMeterApp()
        }
    }

    private fun initShakeDetector() {
        if (ShakeDetectorConstant.IS_SHAKE_DETECTOR_ENABLED) {
            shakeDetector.startListening(this)
        }
    }

    override fun onShake() {
        val file = fileService.getDebugLogFile(fileService.getTodayLogFileName())
        file?.let { f ->
            val uri = FileProvider.getUriForFile(this, this.packageName + ".provider", f)
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = Extras.MIME_TEXT_FILE
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
            startActivity(Intent.createChooser(shareIntent, "Share debug logs via"))
        }
    }

    override fun onDestroy() {
        shakeDetector.stopListening()
        super.onDestroy()
    }
}

