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


/*Fragment Event slúži pre vytváranie udalosti a následne ukladanie do RealTime databazy Firebase*/

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
    //Metoda pre zobrazenie dialogoveho okna pre vyberanie času a potom uloženie hodnoty do premennej
    private fun showTimePickerDialog() {
        val timePickerDialog = TimePickerDialog(requireContext(), TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            val formattedHourOfDay = hourOfDay.toString().padStart(2, '0')
            val formattedMinute = minute.toString().padStart(2, '0')

            selectedTime = "$formattedHourOfDay:$formattedMinute"
            selectedTimeText?.text = "Selected Time: $selectedTime"
        }, 0, 0, false)

        timePickerDialog.show()
    }
    //Metoda pre zobrazenie dialogoveho okna pre vyberanie datumu a potom uloženie hodnoty do premennej
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

    // Metoda ktorá sa volá po kliknutí na tlačitko eventBtn
    // Ukladanie do premennych z vstupných poli, Kontrola či su polia vyplnene, Zostavenie Reťazca z datumu a času ...
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
    // Priradenie hodnôt do rozbalovacieho formulara pre volbu miestnosti
    private fun setSpinner() {
        spinner = binding.spinnerRoom
        val timeOptions = listOf("Gerlach", "Rysy", "Kriváň")
        val adapter = ArrayAdapter(requireContext(), R.layout.simple_spinner_item, timeOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    //Metoda ktora vrati aktualny datum a čas
    private fun getCurrentDateTime(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        val currentDateTime = Calendar.getInstance().time
        return dateFormat.format(currentDateTime)
    }


}