package com.example.database.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.database.DataEvent
import com.example.database.databinding.FragmentKrivanBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class Krivan : BaseEventFragment() {
    override lateinit var eventRecyclerView: RecyclerView
    override lateinit var eventArrayList: ArrayList<DataEvent>
    override lateinit var calendarView: CalendarView
    override var selectedDate: String? = null
    override val desiredRoom = "Kriváň"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentKrivanBinding.inflate(inflater, container, false)
        val view = binding.root

        eventRecyclerView = binding.krivanList
        eventRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        eventRecyclerView.setHasFixedSize(true)
        eventArrayList = arrayListOf()
        calendarView = binding.calendar
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val calendar = Calendar.getInstance()
            calendar.set(year, month, dayOfMonth)
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            selectedDate = dateFormat.format(calendar.time)

            getEventData(desiredRoom)
        }

        return view
    }

    override fun getFragmentLayout(): Int {
        TODO("Not yet implemented")
    }
}
