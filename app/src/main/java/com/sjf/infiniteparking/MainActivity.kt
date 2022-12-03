package com.sjf.infiniteparking

import android.Manifest.permission.SEND_SMS
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_DENIED
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.Settings
import android.telephony.SmsManager
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    var timerCurrent: CountDownTimer? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val request = registerForActivityResult(ActivityResultContracts.RequestPermission(), fun(isGranted) {
            if (!isGranted) {
                Toast.makeText(this, "Gib SMS Permissions", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + packageName))
                startActivity(intent)
            }
        })
        setContentView(R.layout.activity_main)
        val minutesPicker = findViewById<NumberPicker>(R.id.Minutes)
        val hoursPicker = findViewById<NumberPicker>(R.id.Hours)
        val total = findViewById<TextView>(R.id.TimeTotal)
        val current = findViewById<TextView>(R.id.TimeCurrent)
        val start = findViewById<Button>(R.id.Start)
        val stop = findViewById<Button>(R.id.Stop)
        var timerTotal: CountDownTimer? = null
        val sms = SmsManager.getDefault()

        minutesPicker.setFormatter(fun(value: Int):String {
            return String.format("%02d", value)
        })
        minutesPicker.maxValue = 59
        minutesPicker.minValue = 0

        hoursPicker.setFormatter(fun(value: Int):String {
            return String.format("%02d", value)
        })
        hoursPicker.maxValue = 23
        hoursPicker.minValue = 0

        start.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, SEND_SMS) == PERMISSION_DENIED) {
                request.launch(SEND_SMS)
            } else {
                val time = (hoursPicker.value * 3600000 + minutesPicker.value * 60000).toLong()
                if (timerTotal !== null) {
                    timerTotal!!.cancel()
                }
                timerTotal = object: CountDownTimer(time, 1000){
                    override fun onTick(millisUntilFinished: Long) {
                        redrawTime(millisUntilFinished, total)
                    }
                    override fun onFinish() {
                        if (timerCurrent !== null) {
                            timerCurrent!!.cancel()
                        }
                    }
                }.start()
                if (timerCurrent !== null) {
                    timerCurrent!!.cancel()
                }
                startRecursiveCurrentTimer(1020000, 1000, current, sms)
                sms.sendTextMessage("+436646600990", null, "15", null, null)
                redrawTime(time, total)
            }
        }
        stop.setOnClickListener {
            if (timerTotal !== null) {
                timerTotal!!.cancel()
                redrawTime(0, total)
                redrawTime(0, current)
            }
            if (timerCurrent !== null) {
                timerCurrent!!.cancel()
            }
        }
        redrawTime(0, total)
        redrawTime(0, current)
    }

    fun redrawTime(millisUntilFinished: Long, textView: TextView) {
        textView.text = String.format("%02d:%02d:%02d", millisUntilFinished / 3600000 % 24, millisUntilFinished / 60000 % 60, millisUntilFinished / 1000 % 60)
    }
    fun startRecursiveCurrentTimer(millisInFuture: Long, countDownInterval: Long, textView: TextView, sms: SmsManager) {
        timerCurrent = object: CountDownTimer(millisInFuture + Random.nextLong(30000, 60000), countDownInterval){
            override fun onTick(millisUntilFinished: Long) {
                redrawTime(millisUntilFinished, textView)
            }
            override fun onFinish() {
                startRecursiveCurrentTimer(millisInFuture, countDownInterval, textView, sms)
                sms.sendTextMessage("+436646600990", null, "15", null, null)
            }
        }.start()
    }
}