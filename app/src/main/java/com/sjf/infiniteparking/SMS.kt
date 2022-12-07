package com.sjf.infiniteparking

import android.content.Context
import android.telephony.SmsManager

class SMS(context: Context) {
    private val sms = context.getSystemService(SmsManager::class.java)
    fun sendMessage(){
        sms.sendTextMessage("+4369910798173", null, "15", null, null)
//        sms.sendTextMessage("+436646600990", null, "15", null, null)
    }
}