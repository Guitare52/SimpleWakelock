package com.example.simplewakelock

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PowerManager
import android.widget.Button
import android.widget.TextView

/**
 * Simple class used to demonstrate how to turn off screen
 * when proximity sensor senses something near him
 * (aka your head when making a call)
 */
class MainActivity : AppCompatActivity() {

    // Power manager will provide the lock
    private lateinit var powerManager: PowerManager
    // The lock will handle himself the behaviour we want
    private lateinit var lock: PowerManager.WakeLock

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Setting listeners on buttons
        findViewById<Button>(R.id.button_activate).setOnClickListener {
            enableBehaviour()
            findViewById<TextView>(R.id.textview).text = "Behaviour Enabled"
        }
        findViewById<Button>(R.id.button_disable).setOnClickListener {
            disableBehaviour()
            findViewById<TextView>(R.id.textview).text = "Behaviour Disabled"
        }

        // Declaring needed elements
        powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        // Create a new wake lock relying on the proximity sensor
        // You should make further research about that tag thing if you want to use other locks
        lock = powerManager.newWakeLock(PowerManager.PROXIMITY_SCREEN_OFF_WAKE_LOCK,"simplewakelock:wakelocktag")
    }

    // Be sure to disable when exiting / pausing activity
    // Otherwise it will continue even if your app is in background
    override fun onPause() {
        super.onPause()
        disableBehaviour()
    }

    /* Note that i suppress the "WakelockTimeout" warning because i’m releasing it when i’m exiting the activity.
    You could also acquire the lock for a predefined period of time. Ex :
    lock.acquire(10*60*1000L /*10 minutes*/)   */
    @SuppressLint("WakelockTimeout")
    private fun enableBehaviour(){
        // Acquire the lock if it was not already acquired
        if(!lock.isHeld) lock.acquire()
    }

    private fun disableBehaviour(){
        // Release the lock if it was not already released
        if(lock.isHeld) lock.release()
    }
}