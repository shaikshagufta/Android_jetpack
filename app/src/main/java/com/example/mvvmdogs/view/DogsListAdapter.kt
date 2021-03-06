package com.example.mvvmdogs.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvmdogs.R
import com.example.mvvmdogs.databinding.ItemDogBinding
import com.example.mvvmdogs.model.DogBreed

//1. Implement DogClickListener
class DogsListAdapter(private val dogList: ArrayList<DogBreed>) : RecyclerView.Adapter<DogsListAdapter.DogViewHolder>(), DogClickListener{

    //to update the list when we get info from backend
    fun updateDogList(newDogList: List<DogBreed>) {
        dogList.clear()
        dogList.addAll(newDogList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DogViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view =
            DataBindingUtil.inflate<ItemDogBinding>(inflater, R.layout.item_dog, parent, false)

        //returning the ViewHolder containing the inflated view as parameter
        return DogViewHolder(view)
    }

    //binding the element of the dogList with position to the vieHolder
    override fun onBindViewHolder(holder: DogViewHolder, position: Int) {

        //attaching the layout variable we created for databinding
        holder.view.dog = dogList[position]
        holder.view.listener = this//this adapter
    }

    override fun getItemCount() = dogList.size


    //inner class we created before inheritance
    inner class DogViewHolder(var view: ItemDogBinding) : RecyclerView.ViewHolder(view.root)

    override fun onDogClicked(v: View, dogUuid: Int) {
        val action = ListFragmentDirections.actionDetailFragment()
        action.dogUuid = dogUuid
        Navigation.findNavController(v).navigate(action)
    }


}