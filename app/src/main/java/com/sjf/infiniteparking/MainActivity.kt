package com.sjf.infiniteparking

import android.Manifest.permission.SEND_SMS
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_DENIED
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.Settings
import android.telephony.SmsManager
import android.util.Log
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import java.util.*
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    var timerCurrent: CountDownTimer? = null
    private val sms = SmsManager.getDefault()
    private lateinit var total: TextView

    private var count: Long = 0
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
        total = findViewById<TextView>(R.id.TimeTotal)
        val current = findViewById<TextView>(R.id.TimeCurrent)
        val start = findViewById<Button>(R.id.Start)
        val stop = findViewById<Button>(R.id.Stop)
        var timerTotal: CountDownTimer? = null
//        sms = SmsManager.getDefault()

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
            startAlarm()
        }

    }

    fun redrawTime(millisUntilFinished: Long, textView: TextView) {
        textView.text = String.format("%02d:%02d:%02d", millisUntilFinished / 3600000 % 24, millisUntilFinished / 60000 % 60, millisUntilFinished / 1000 % 60)
    }

    fun sendMessage(){
        sms.sendTextMessage("+436646600990", null, "15", null, null)
    }

    fun startAlarm(){
        count++
        redrawTime(count * 1000, total)
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val calendar: Calendar = Calendar.getInstance()
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 0, 1, 0)

        val intent = Intent(this, MyAlarm::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, FLAG_IMMUTABLE)
        alarmManager.setExact(AlarmManager.ELAPSED_REALTIME, 0, pendingIntent)
    }

    inner class MyAlarm() : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
//            sendMessage()
            Log.d("Alarm Bell", "Alarm just fired")
//            startAlarm()
        }
    }
}