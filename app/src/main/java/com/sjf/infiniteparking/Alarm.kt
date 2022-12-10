package com.sjf.infiniteparking

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import java.util.*

class Alarm : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null){
            val nextAlarm = Calendar.getInstance()
            val targetTime = Calendar.getInstance()
            val sharedPref = context.getSharedPreferences(context.getString(R.string.pref), Context.MODE_PRIVATE)
            targetTime.timeInMillis = sharedPref.getLong(context.getString(R.string.pref_targetTime), 0)
            nextAlarm.add(Calendar.MINUTE, context.resources.getInteger(R.integer.alarmInterval))
            if (nextAlarm.before(targetTime)) {
                sharedPref.edit().putLong(context.getString(R.string.pref_lastAlarmSet), nextAlarm.timeInMillis).apply()
                startAlarm(context, nextAlarm.timeInMillis, intent)
            }
        }
    }
    fun startAlarm(context: Context, time: Long, intent: Intent?) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val newIntent = intent ?: Intent(context, Alarm::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, newIntent, PendingIntent.FLAG_IMMUTABLE)
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, time, pendingIntent)
    }
}