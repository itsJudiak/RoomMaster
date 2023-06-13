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
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.example.database.databinding.FragmentEventBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Event.newInstance] factory method to
 * create an instance of this fragment.
 */
class Event : Fragment() {
    private lateinit var binding: FragmentEventBinding
    private lateinit var database: DatabaseReference
    private lateinit var spinner: Spinner
    private lateinit var selectedDate: String
    private lateinit var selectedTime: String
    private var selectedDateText: TextView? = null
    private var selectedTimeText: TextView? = null
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

    private fun showTimePickerDialog() {
        val timePickerDialog = TimePickerDialog(requireContext(), TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            val formattedHourOfDay = hourOfDay.toString().padStart(2, '0')
            val formattedMinute = minute.toString().padStart(2, '0')

            selectedTime = "$formattedHourOfDay:$formattedMinute"
            selectedTimeText?.text = "Selected Time: $selectedTime"
        }, 0, 0, false)

        timePickerDialog.show()
    }

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

    private fun createEvent() {
        val title = binding.editTextTitle.text.toString()
        val description = binding.editTextDescription.text.toString()
        val room = spinner.selectedItem as String
        val event = DataEvent(title, description, room, selectedDate, selectedTime)
        val eventKey = database.push().key

        if (title.isEmpty() || description.isEmpty() || selectedDate.isEmpty() || selectedTime.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill in all the fields", Toast.LENGTH_SHORT).show()
            return
        }

        if (eventKey != null) {
            database.child(eventKey).setValue(event)
                .addOnSuccessListener {
                    binding.editTextTitle.text?.clear()
                    binding.editTextDescription.text?.clear()
                    binding.editTextTitle.text?.clear()
                    binding.editTextDescription.text?.clear()
                    selectedDateText?.text = null
                    selectedTimeText?.text = null
                    Toast.makeText(requireContext(), "Event created successfully", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "Failed to create Event", Toast.LENGTH_SHORT).show()
                }
        }
    }
    private fun setSpinner() {
        spinner = binding.spinnerRoom
        val timeOptions = listOf("Gerlach", "Rysy", "Kriváň")
        val adapter = ArrayAdapter(requireContext(), R.layout.simple_spinner_item, timeOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }


}