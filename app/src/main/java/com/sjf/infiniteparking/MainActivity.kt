/* Data to be saved in shared preference
*   last alarm set
*   target time
* */

package com.sjf.infiniteparking

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import java.util.Calendar

class MainActivity : AppCompatActivity() {
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
        val startButton = findViewById<Button>(R.id.Start)
        val stopButton = findViewById<Button>(R.id.Stop)
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

        startButton.setOnClickListener{
            val targetTime = Calendar.getInstance()
            targetTime.add(Calendar.HOUR, hoursPicker.value)
            targetTime.add(Calendar.MINUTE, minutesPicker.value)
            val sharedPref = this.getSharedPreferences(getString(R.string.pref), Context.MODE_PRIVATE)
            sharedPref.edit().putLong(getString(R.string.pref_targetTime), targetTime.timeInMillis).apply()

            val lastAlarmSet = Calendar.getInstance()
            lastAlarmSet.timeInMillis = sharedPref.getLong(getString(R.string.pref_lastAlarmSet), 0)
            lastAlarmSet.add(Calendar.MINUTE, this.resources.getInteger(R.integer.alarmInterval))
            val nextAlarm = Calendar.getInstance()
            if (nextAlarm.after(lastAlarmSet)) {
                sharedPref.edit().putLong(getString(R.string.pref_lastAlarmSet), nextAlarm.timeInMillis).apply()
                nextAlarm.add(Calendar.MINUTE, this.resources.getInteger(R.integer.alarmInterval))
                Alarm().startAlarm(this, nextAlarm.timeInMillis, null)
                SMS(this).sendMessage()
            }
        }
        stopButton.setOnClickListener {
            this.getSharedPreferences(getString(R.string.pref), Context.MODE_PRIVATE).edit().putLong(getString(R.string.pref_targetTime), 0).apply()
        }
    }

    fun redrawTime(millisUntilFinished: Long, textView: TextView) {
        textView.text = String.format("%02d:%02d:%02d", millisUntilFinished / 3600000 % 24, millisUntilFinished / 60000 % 60, millisUntilFinished / 1000 % 60)
    }
}