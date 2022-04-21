package com.example.mvvmdogs.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.mvvmdogs.databinding.FragmentListBinding

class ListFragment : Fragment() {

    lateinit var binding: FragmentListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
       binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonDetails.setOnClickListener {
            val action = ListFragmentDirections.actionDetailFragment()
            //to use the action we created in the dog_navigation: dogUuid
            // ie, passing the argument in list fragment to retrieve from detail fragment
            action.dogUuid = 5
            Navigation.findNavController(it).navigate(action)
        }
    }
}