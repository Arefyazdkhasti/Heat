package com.example.heat.ui

import android.app.*
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.BuildCompat
import androidx.navigation.fragment.findNavController
import androidx.preference.*
import com.example.heat.R
import com.example.heat.util.UiUtils.Companion.showToast
import com.example.heat.util.broadcastReceiver.*
import com.example.heat.util.setLocale
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.util.*


const val breakfast = "breakfast"
const val lunch = "lunch"
const val dinner = "dinner"
const val snack = "snack"

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
        }

        val themePreference = findPreference<ListPreference>("theme")
        themePreference?.onPreferenceChangeListener = themeChangeListener

        val languagePreference = findPreference<ListPreference>("language")
        languagePreference?.onPreferenceChangeListener = languageChangeListener

        val breakfastPreference = findPreference<SwitchPreferenceCompat>("breakfast_notification")
        breakfastPreference?.onPreferenceChangeListener = breakfastChangedListener

        val lunchPreference = findPreference<SwitchPreferenceCompat>("lunch_notification")
        lunchPreference?.onPreferenceChangeListener = lunchChangedListener

        val dinnerPreference = findPreference<SwitchPreferenceCompat>("dinner_notification")
        dinnerPreference?.onPreferenceChangeListener = dinnerChangedListener

        val snackPreference = findPreference<SwitchPreferenceCompat>("snack_notification")
        snackPreference?.onPreferenceChangeListener = snackChangedListener

        val showNotification = findPreference<SwitchPreferenceCompat>("show_notification")
        showNotification?.onPreferenceChangeListener = showNotificationChangedListener

        val breakfastTime = findPreference<Preference>("breakfast_time")
        breakfastTime?.setOnPreferenceClickListener {
            showTimePickerDialog(it, breakfast)
            true
        }

        val lunchTime = findPreference<Preference>("lunch_time")
        lunchTime?.setOnPreferenceClickListener {
            showTimePickerDialog(it, lunch)
            true
        }

        val dinnerTime = findPreference<Preference>("dinner_time")
        dinnerTime?.setOnPreferenceClickListener {
            showTimePickerDialog(it, dinner)
            true
        }


        val snackTime = findPreference<Preference>("snack_time")
        snackTime?.setOnPreferenceClickListener {
            showTimePickerDialog(it, snack)
            true
        }
    }


    private val languageChangeListener =
        Preference.OnPreferenceChangeListener { _, newValue ->
            Log.i("newValue", newValue.toString())
            newValue as? String
            when (newValue) {
                getString(R.string.english) -> {
                    setLocale(requireActivity(), "en")
                }
                getString(R.string.persian) -> {
                    setLocale(requireActivity(), "fa")
                }
                else -> {
                    setLocale(requireActivity(), "en")
                }
            }
            findNavController().navigateUp()
            true
        }

    private val themeChangeListener =
        Preference.OnPreferenceChangeListener { _, newValue ->
            Log.i("newValue", newValue.toString())
            newValue as? String
            when (newValue) {
                getString(R.string.dark) -> {
                    updateTheme(AppCompatDelegate.MODE_NIGHT_YES)
                }
                getString(R.string.light) -> {
                    updateTheme(AppCompatDelegate.MODE_NIGHT_NO)
                }
                else -> {
                    if (BuildCompat.isAtLeastQ()) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                    } else {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY)
                    }
                }
            }
            //findNavController().navigateUp()
            true
        }

    private val breakfastChangedListener =
        Preference.OnPreferenceChangeListener { _, newValue ->
            if (newValue.toString() == "true")
                scheduleNotification(breakfast)
            else
                cancelAlarm(breakfast)

            true
        }

    private val lunchChangedListener =
        Preference.OnPreferenceChangeListener { _, newValue ->
            if (newValue.toString() == "true")
                scheduleNotification(lunch)
            else
                cancelAlarm(lunch)

            true
        }

    private val dinnerChangedListener =
        Preference.OnPreferenceChangeListener { _, newValue ->
            if (newValue.toString() == "true")
                scheduleNotification(dinner)
            else
                cancelAlarm(dinner)

            true
        }

    private val snackChangedListener =
        Preference.OnPreferenceChangeListener { _, newValue ->
            if (newValue.toString() == "true")
                scheduleNotification(snack)
            else
                cancelAlarm(snack)

            true
        }

    private val showNotificationChangedListener =
        Preference.OnPreferenceChangeListener { _, newValue ->
                if(newValue.toString() == "false"){
                    cancelAlarm(breakfast)
                    cancelAlarm(lunch)
                    cancelAlarm(dinner)
                    cancelAlarm(snack)
                }
                true
            }


    private fun updateTheme(nightMode: Int): Boolean {
        AppCompatDelegate.setDefaultNightMode(nightMode)
        requireActivity().recreate()
        return true
    }

    private fun scheduleNotification(meal: String) {
        val intent = Intent(requireContext(), NotificationBroadcastReceiver::class.java)

        intent.putExtra(titleExtra, "Heat Reminder")
        intent.putExtra(messageExtra, "Don't forget to eat you $meal. Have a wonderful meal :)")

        val notificationID = when (meal) {
            breakfast -> breakfastNotificationID
            lunch -> lunchNotificationID
            dinner -> dinnerNotificationID
            snack -> snackNotificationID
            else -> otherNotificationID
        }

        val pendingIntent = PendingIntent.getBroadcast(
            requireContext(),
            notificationID,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val calendar = Calendar.getInstance()
        calendar[Calendar.HOUR_OF_DAY] = when (meal) {
            breakfast -> 7
            lunch -> 13
            dinner -> 20
            snack -> 17
            else -> 0
        }
        calendar[Calendar.MINUTE] = 30
        calendar[Calendar.SECOND] = 0

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            24 * 60 * 60 * 1000,
            pendingIntent
        ); //Repeat every 24 hours
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val name = "Meal Reminder Channel"
        val desc = "meal reminder from Heat Application"

        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelID, name, importance)
        channel.description = desc
        val notificationManager =
            requireActivity().getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun cancelAlarm(meal: String) {
        val alarmManager = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val myIntent = Intent(
            requireContext(),
            NotificationBroadcastReceiver::class.java
        )
        val notificationID = when (meal) {
            breakfast -> breakfastNotificationID
            lunch -> lunchNotificationID
            dinner -> dinnerNotificationID
            snack -> snackNotificationID
            else -> otherNotificationID
        }
        val pendingIntent = PendingIntent.getBroadcast(
            requireContext(), notificationID, myIntent, PendingIntent.FLAG_NO_CREATE
        )

        pendingIntent?.let { _pendingIntent ->
            alarmManager.cancel(_pendingIntent)
        }
    }

    private fun showTimePickerDialog(preference: Preference, meal: String) {
        val hour = when (meal) {
            breakfast -> 7
            lunch -> 13
            dinner -> 20
            snack -> 15
            else -> 0
        }
        val picker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_12H)
            .setHour(hour)
            .setMinute(30)
            .setTitleText("Select $meal Reminder time")
            .build()

        picker.addOnPositiveButtonClickListener {
            val pickedHour = picker.hour
            val pickedMinute = picker.minute
            val formattedTime: String = when {
                pickedHour > 12 -> {
                    if (pickedMinute < 10) {
                        "${picker.hour - 12}:0${picker.minute} pm"
                    } else {
                        "${picker.hour - 12}:${picker.minute} pm"
                    }
                }
                pickedHour == 12 -> {
                    if (pickedMinute < 10) {
                        "${picker.hour}:0${picker.minute} pm"
                    } else {
                        "${picker.hour}:${picker.minute} pm"
                    }
                }
                pickedHour == 0 -> {
                    if (pickedMinute < 10) {
                        "${picker.hour + 12}:0${picker.minute} am"
                    } else {
                        "${picker.hour + 12}:${picker.minute} am"
                    }
                }
                else -> {
                    if (pickedMinute < 10) {
                        "${picker.hour}:0${picker.minute} am"
                    } else {
                        "${picker.hour}:${picker.minute} am"
                    }
                }
            }
            preference.title = formattedTime
        }
        picker.show(requireActivity().supportFragmentManager, "TimePicker")
    }
}