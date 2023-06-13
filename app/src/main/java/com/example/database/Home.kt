package com.example.database

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.database.fragments.Gerlach
import com.example.database.fragments.Rysy
import com.example.database.fragments.Krivan
import com.example.database.databinding.FragmentHomeBinding

class Home : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var fragmentManager: FragmentManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fragmentManager = childFragmentManager

        // Initialize the first nested fragment
        val initialFragment = Gerlach()
        replaceFragment(initialFragment)

        // Set up the bottom navigation view
        binding.bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_gerlach -> replaceFragment(Gerlach())
                R.id.menu_rysy -> replaceFragment(Rysy())
                R.id.menu_krivan -> replaceFragment(Krivan())
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        fragmentManager.beginTransaction()
            .replace(binding.fragmentContainer.id, fragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
