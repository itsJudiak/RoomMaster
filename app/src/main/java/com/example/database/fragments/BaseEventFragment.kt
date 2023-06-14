package com.example.database.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.database.DataEvent
import com.example.database.HistoryAdapter
import com.example.database.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

abstract class BaseEventFragment : Fragment() {

    protected lateinit var database: DatabaseReference
    protected open lateinit var eventRecyclerView: RecyclerView
    protected open lateinit var eventArrayList: ArrayList<DataEvent>
    protected open lateinit var calendarView: CalendarView
    protected open var selectedDate: String? = null
    protected abstract val desiredRoom: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = inflater.inflate(getFragmentLayout(), container, false)

        eventRecyclerView = view.findViewById(R.id.eventList)
        eventRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        eventRecyclerView.setHasFixedSize(true)

        eventArrayList = arrayListOf()

        calendarView = view.findViewById(R.id.calendar)
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val calendar = Calendar.getInstance()
            calendar.set(year, month, dayOfMonth)
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            selectedDate = dateFormat.format(calendar.time)

        }

        return view
    }

    protected abstract fun getFragmentLayout(): Int


    // Služí na ziskavanie eventov z databazy, tie uklada do arrayListu na zaklade miestnosti a vybraného dátumu
    protected fun getEventData(desiredRoom: String) {
        database = FirebaseDatabase.getInstance().reference
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    eventArrayList.clear()
                    for (eventSnapshot in snapshot.children) {
                        val event = eventSnapshot.getValue(DataEvent::class.java)
                        if (event?.room == desiredRoom && event?.date == selectedDate) {
                            eventArrayList.add(event)
                        }
                    }
                    eventRecyclerView.adapter = HistoryAdapter(eventArrayList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

}
