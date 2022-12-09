package com.sjf.infiniteparking

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import java.util.*

class Alarm : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

    }
    fun startAlarm(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val calendar: Calendar = Calendar.getInstance()
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 0, 17, 0)

//        val intent = Intent(this, MyAlarm::class.java)
//        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0)
//        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
    }
}