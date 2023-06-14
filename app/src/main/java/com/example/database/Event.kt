package com.example.database

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.example.database.databinding.FragmentEventBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class Event : Fragment() {
    private lateinit var binding: FragmentEventBinding
    private lateinit var database: DatabaseReference
    private lateinit var spinner: Spinner
    private var selectedDate: String? = null
    private var selectedTime: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentEventBinding.inflate(inflater, container, false)
        val view = binding.root

        // Get a reference to the Firebase Realtime Database
        database = FirebaseDatabase.getInstance().reference

        // Set up the spinner for room selection
        setSpinner()

        // Set up the click listeners for date and time selection buttons
        binding.pickDateBtn.setOnClickListener {
            showDatePickerDialog()
        }
        binding.pickTimeBtn.setOnClickListener {
            showTimePickerDialog()
        }

        // Set up the click listener for event creation button
        binding.eventBtn.setOnClickListener {
            createEvent()
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Restore values if available
        selectedDate = savedInstanceState?.getString(KEY_SELECTED_DATE)
        selectedTime = savedInstanceState?.getString(KEY_SELECTED_TIME)
        updateSelectedDateTimeText()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Save the selected date and time values
        outState.putString(KEY_SELECTED_DATE, selectedDate)
        outState.putString(KEY_SELECTED_TIME, selectedTime)
    }

    private fun showTimePickerDialog() {
        // Show a time picker dialog to select the time
        val timePickerDialog = TimePickerDialog(requireContext(), TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            val formattedHourOfDay = hourOfDay.toString().padStart(2, '0')
            val formattedMinute = minute.toString().padStart(2, '0')

            // Store the selected time
            selectedTime = "$formattedHourOfDay:$formattedMinute"
            updateSelectedDateTimeText()
        }, 0, 0, false)

        timePickerDialog.show()
    }

    private fun showDatePickerDialog() {
        // Show a date picker dialog to select the date
        val datePickerDialog = DatePickerDialog(requireContext())
        datePickerDialog.setOnDateSetListener { view, year, month, dayOfMonth ->
            // The month returned by the date picker starts from 0 (January = 0), so increment it by 1
            val formattedMonth = (month + 1).toString().padStart(2, '0')
            val formattedDayOfMonth = dayOfMonth.toString().padStart(2, '0')

            // Store the selected date
            selectedDate = "$year-$formattedMonth-$formattedDayOfMonth"
            updateSelectedDateTimeText()
        }

        datePickerDialog.show()
    }

    private fun createEvent() {
        // Get the input values
        val title = binding.editTextTitle.text.toString().trim()
        val description = binding.editTextDescription.text.toString().trim()
        val room = spinner.selectedItem as String

        // Validate the input fields
        if (title.isEmpty() || description.isEmpty() || selectedDate.isNullOrEmpty() || selectedTime.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "Please fill in all the fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Parse the selected date and time
        val selectedDateTime = "$selectedDate $selectedTime"
        val eventDateTime = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).parse(selectedDateTime)

        // Check if the selected date and time is valid
        if (eventDateTime == null || eventDateTime.before(Calendar.getInstance().time)) {
            Toast.makeText(requireContext(), "Please select a valid date and time", Toast.LENGTH_SHORT).show()
            return
        }

        // Create the event object
        val event = DataEvent(title, description, room, selectedDate!!, selectedTime!!)

        // Generate a unique key for the event in the database
        val eventKey = database.push().key

        if (eventKey != null) {
            // Store the event in the database
            database.child(eventKey).setValue(event)
                .addOnSuccessListener {
                    // Clear the input fields and show a success message
                    binding.editTextTitle.text?.clear()
                    binding.editTextDescription.text?.clear()
                    selectedDate = null
                    selectedTime = null
                    updateSelectedDateTimeText()
                    Toast.makeText(requireContext(), "Event created successfully", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    // Show an error message if event creation fails
                    Toast.makeText(requireContext(), "Failed to create event", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun setSpinner() {
        // Set up the spinner for room selection
        spinner = binding.spinnerRoom
        val roomOptions = listOf("Gerlach", "Rysy", "Kriváň")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, roomOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    private fun updateSelectedDateTimeText() {
        // Update the selected date and time in the text view
        val selectedDateTime = StringBuilder()

        if (!selectedDate.isNullOrEmpty()) {
            selectedDateTime.append("Selected Date: $selectedDate")
        }

        if (!selectedTime.isNullOrEmpty()) {
            if (selectedDateTime.isNotEmpty()) {
                selectedDateTime.append("\n")
            }
            selectedDateTime.append("Selected Time: $selectedTime")
        }

        binding.selectedDateTimeTextView!!.text = selectedDateTime.toString()
    }

    companion object {
        private const val KEY_SELECTED_DATE = "selected_date"
        private const val KEY_SELECTED_TIME = "selected_time"
    }
}
