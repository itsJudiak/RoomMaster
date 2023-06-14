package com.example.database

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.database.fragments.Rysy
import com.example.database.fragments.Krivan
import com.example.database.databinding.FragmentHomeBinding
import com.example.database.fragments.Gerlach

/*
* Fragment home zobrazuje domovku stranku
* */

class Home : Fragment() {
    private var binding: FragmentHomeBinding? = null

    private lateinit var fragmentManager: FragmentManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fragmentManager = childFragmentManager

        // Inicializovanie prvého fragmentu
        val initialFragment = Gerlach()
        replaceFragment(initialFragment)

        // Navigation view
        binding?.bottomNavigationView?.setOnNavigationItemSelectedListener { menuItem ->
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
            .replace(binding!!.fragmentContainer.id, fragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
    }

}
