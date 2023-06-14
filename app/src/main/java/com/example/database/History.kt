package com.example.database

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.database.databinding.FragmentHistoryBinding
import com.google.firebase.database.*

class History : Fragment() {

    private lateinit var binding: FragmentHistoryBinding
    private lateinit var database: DatabaseReference
    private lateinit var eventRecyclerView: RecyclerView
    private lateinit var eventArrayList: ArrayList<DataEvent>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHistoryBinding.inflate(inflater, container, false)

        eventRecyclerView = binding.eventList
        eventRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        eventRecyclerView.setHasFixedSize(true)

        eventArrayList = arrayListOf<DataEvent>()
        getUserData()


        return binding.root
    }

    private fun getUserData() {
        database = FirebaseDatabase.getInstance().reference
        database.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (eventSnapshot in snapshot.children) {
                        val event = eventSnapshot.getValue(DataEvent::class.java)
                        eventArrayList.add(event!!)
                    }
                    eventRecyclerView.adapter = HistoryAdapter(eventArrayList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }


}
