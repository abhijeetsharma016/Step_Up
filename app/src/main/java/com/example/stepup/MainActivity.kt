package com.example.stepup

import android.content.Context
import android.content.SharedPreferences
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity(), SensorEventListener {
    private var sensorManager: SensorManager? = null
    private var running = false
    private var totalSteps = 0f
    private var previousTotalSteps = 0f
    var tv_stepsTaken = findViewById<TextView>(R.id.tv_stepsTaken)
    var progress_circular = findViewById<CircularProgressView>(R.id.progress_circular)
    var tv_goal = findViewById<TextView>(R.id.tv_goal)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

    }

    override fun onResume() {
        super.onResume()
        running = true
        val stepSensor: Sensor? = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        if(stepSensor == null) {
            Toast.makeText(this, "No Sensor detected on this device", Toast.LENGTH_SHORT).show()
        }
        else{
            sensorManager?.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI)
        }
    }
    override fun onSensorChanged(event: SensorEvent?) {
        if(running) {
            totalSteps = event!!.values[0]
            val currentSteps = totalSteps.toInt() - previousTotalSteps.toInt()
            tv_stepsTaken.text = ("$currentSteps")


            progress_circular.apply {
                setProgressWithAnimation(currentSteps.toFloat())
            }
        }
    }
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        TODO("Not yet implemented")
    }

    private fun resetSteps(){
        tv_stepsTaken.setOnClickListener{
            Toast.makeText(this, "Long Tap to reset Steps", Toast.LENGTH_SHORT).show()
        }

        tv_stepsTaken.setOnLongClickListener{
            previousTotalSteps = totalSteps
            tv_stepsTaken.text = 0.toString()
            saveData()
            Toast.makeText(this, "Long Tap to reset Steps", Toast.LENGTH_SHORT).show()
            true
        }
    }
    private fun setProgressWithAnimation(toFloat: Float) {
        val sharedPreferences: SharedPreferences =
            getSharedPreferences("my Preferences", Context.MODE_PRIVATE)
        val editor:SharedPreferences.Editor = sharedPreferences.edit()
        editor.putFloat("Key1", previousTotalSteps)
        editor.apply()
    }

}