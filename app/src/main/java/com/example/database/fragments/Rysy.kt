package com.example.database.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.database.databinding.FragmentRysyBinding

class Rysy : Fragment() {
    private lateinit var binding: FragmentRysyBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRysyBinding.inflate(inflater, container, false)
        return binding.root
    }
}
