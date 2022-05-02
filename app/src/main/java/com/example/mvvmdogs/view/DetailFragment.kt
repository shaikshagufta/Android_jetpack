package com.example.mvvmdogs.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.mvvmdogs.databinding.FragmentDetailBinding
import com.example.mvvmdogs.viewmodel.DetailViewModel

class DetailFragment : Fragment() {

    private lateinit var viewModel: DetailViewModel

    private var dogUuid = 0

    private lateinit var binding: FragmentDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        //binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)//can be either way
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //retrieving the argument we passed in list-fragment to detail-fragment if its not null
        arguments?.let {
            dogUuid = DetailFragmentArgs.fromBundle(it).dogUuid//we already have a uuid here unlike DOgsListAdapter
        }

        //instantiating DetailViewModel
        viewModel = ViewModelProvider(this).get(DetailViewModel::class.java)
        viewModel.fetch(dogUuid)

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.dogLiveData.observe(viewLifecycleOwner, Observer { dog ->
            dog?.let {
                binding.dog = dog
            }
        })
    }

}