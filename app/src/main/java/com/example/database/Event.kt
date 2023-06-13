package com.example.database

import android.R
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
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
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEventBinding.inflate(inflater, container, false)
        val view = binding.root

        // Initialize the Firebase database reference
        database = FirebaseDatabase.getInstance().reference

        spinner = binding.spinnerRoom
        val timeOptions = listOf("Gerlach", "Rysy", "Kriváň")
        val adapter = ArrayAdapter(requireContext(), R.layout.simple_spinner_item, timeOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        binding.eventBtn.setOnClickListener {
            createEvent()
        }

        return view
    }

    private fun createEvent() {
        val title = binding.editTextTitle.text.toString()
        val description = binding.editTextDescription.text.toString()
        val room = spinner.selectedItem as String
        val event = DataEvent(title, description, room)
        val eventKey = database.push().key


        if (eventKey != null) {
            database.child(eventKey).setValue(event)
                .addOnSuccessListener {
                    binding.editTextTitle.text?.clear()
                    binding.editTextDescription.text?.clear()
                }
                .addOnFailureListener {

                }
        }
    }


}