package com.example.database

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEventBinding.inflate(inflater, container, false)
        val view = binding.root

        // Initialize the Firebase database reference
        database = FirebaseDatabase.getInstance().reference

        binding.eventBtn.setOnClickListener {
            createEvent()
        }

        return view
    }

    private fun createEvent() {
        val title = binding.editTextTitle.text.toString()
        val description = binding.editTextDescription.text.toString()
        val time = binding.editTextTime.text.toString()

        val event = DataEvent(title, description, time)
        val eventKey = database.push().key

        // Add the event data to the "events" node using the generated key
        if (eventKey != null) {
            database.child(eventKey).setValue(event)
                .addOnSuccessListener {
                    binding.editTextTitle.text?.clear()
                    binding.editTextDescription.text?.clear()
                    binding.editTextTime.text?.clear()
                }
                .addOnFailureListener {

                }
        }
    }


}