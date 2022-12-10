package com.sjf.infiniteparking

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import java.util.*

class Alarm : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val calendar = Calendar.getInstance()

    }
    fun startAlarm(context: Context, time: Long, intent: Intent?) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val newIntent = intent ?: Intent(context, Alarm::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, newIntent, PendingIntent.FLAG_IMMUTABLE)
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, time, pendingIntent)
    }
}