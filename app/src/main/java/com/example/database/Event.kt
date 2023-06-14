package com.example.database

import android.R
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

/**
 * Fragment Event is used for creating events and storing them in the Firebase Realtime Database.
 */
class Event : Fragment() {
    private lateinit var binding: FragmentEventBinding
    private lateinit var database: DatabaseReference
    private lateinit var spinner: Spinner
    private lateinit var selectedDate: String
    private lateinit var selectedTime: String
    private var selectedDateText: TextView? = null
    private var selectedTimeText: TextView? = null
    private var storedTitle: String = ""
    private var storedDescription: String = ""
    private var storedSelectedDate: String = ""
    private var storedSelectedTime: String = ""

    /**
     * Creates the view of the fragment.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEventBinding.inflate(inflater, container, false)
        val view = binding.root

        database = FirebaseDatabase.getInstance().reference

        setSpinner()

        binding.pickDateBtn.setOnClickListener {
            showDatePickerDialog()
        }
        binding.pickTimeBtn.setOnClickListener{
            showTimePickerDialog()
        }
        selectedDateText = binding.selectedDateTextView
        selectedTimeText = binding.selectedTimeTextView

        binding.eventBtn.setOnClickListener {
            createEvent()
        }

        return view
    }

    /**
     * Restores the values of the views when the view is created.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Restore values if available
        binding.editTextTitle.setText(storedTitle)
        binding.editTextDescription.setText(storedDescription)
        selectedDateText?.text = storedSelectedDate
        selectedTimeText?.text = storedSelectedTime
    }

    /**
     * Stores the values of the views before the view is destroyed.
     */
    override fun onDestroyView() {
        super.onDestroyView()

        // Store values before view is destroyed
        storedTitle = binding.editTextTitle.text.toString()
        storedDescription = binding.editTextDescription.text.toString()
        storedSelectedDate = selectedDateText?.text.toString()
        storedSelectedTime = selectedTimeText?.text.toString()
    }

    /**
     * Shows a time picker dialog to select the time and stores the selected time.
     */
    private fun showTimePickerDialog() {
        val timePickerDialog = TimePickerDialog(requireContext(), TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            val formattedHourOfDay = hourOfDay.toString().padStart(2, '0')
            val formattedMinute = minute.toString().padStart(2, '0')

            selectedTime = "$formattedHourOfDay:$formattedMinute"
            selectedTimeText?.text = "Selected Time: $selectedTime"
        }, 0, 0, false)

        timePickerDialog.show()
    }

    /**
     * Shows a date picker dialog to select the date and stores the selected date.
     */
    private fun showDatePickerDialog() {
        val datePickerDialog = DatePickerDialog(requireContext())
        datePickerDialog.setOnDateSetListener { view, year, month, dayOfMonth ->
            // The month returned by the date picker starts from 0 (January = 0), so increment it by 1
            val formattedMonth = (month + 1).toString().padStart(2, '0')
            val formattedDayOfMonth = dayOfMonth.toString().padStart(2, '0')

            selectedDate = "$year-$formattedMonth-$formattedDayOfMonth"
            selectedDateText?.text = "Selected Date: $selectedDate"
        }

        datePickerDialog.show()
    }

    /**
     * Creates the event based on the input fields.
     * Checks if all fields are filled, validates the selected date and time, and saves the event to the database.
     */
    private fun createEvent() {
        val title = binding.editTextTitle.text.toString().trim()
        val description = binding.editTextDescription.text.toString().trim()
        val room = spinner.selectedItem as String

        if (title.isEmpty() || description.isEmpty() || selectedDate.isNullOrEmpty() || selectedTime.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "Please fill in all the fields", Toast.LENGTH_SHORT).show()
            return
        }

        val selectedDateTime = "$selectedDate $selectedTime"
        val currentDate = getCurrentDateTime()

        val eventDateTime = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).parse(selectedDateTime)
        val currentDateTime = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).parse(currentDate)

        if (eventDateTime == null || currentDateTime == null || eventDateTime.before(currentDateTime)) {
            Toast.makeText(requireContext(), "Please select a valid date and time", Toast.LENGTH_SHORT).show()
            return
        }

        val event = DataEvent(title, description, room, selectedDate!!, selectedTime!!)
        val eventKey = database.push().key

        if (eventKey != null) {
            database.child(eventKey).setValue(event)
                .addOnSuccessListener {
                    binding.editTextTitle.text?.clear()
                    binding.editTextDescription.text?.clear()
                    selectedDateText?.text = null
                    selectedTimeText?.text = null
                    Toast.makeText(requireContext(), "Event created successfully", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "Failed to create event", Toast.LENGTH_SHORT).show()
                }
        }
    }

    /**
     * Sets the values for the room selection spinner.
     */
    private fun setSpinner() {
        spinner = binding.spinnerRoom
        val roomOptions = listOf("Gerlach", "Rysy", "Kriváň")
        val adapter = ArrayAdapter(requireContext(), R.layout.simple_spinner_item, roomOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    /**
     * Returns the current date and time as a formatted string.
     */
    private fun getCurrentDateTime(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        val currentDateTime = Calendar.getInstance().time
        return dateFormat.format(currentDateTime)
    }
}
