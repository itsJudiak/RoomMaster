package com.example.database.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.database.databinding.FragmentKrivanBinding

class Krivan : Fragment() {
    private lateinit var binding: FragmentKrivanBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentKrivanBinding.inflate(inflater, container, false)
        return binding.root
    }
}
