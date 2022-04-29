package com.example.mvvmdogs.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvmdogs.R
import com.example.mvvmdogs.databinding.ItemDogBinding
import com.example.mvvmdogs.model.DogBreed


class DogsListAdapter(private val dogList: ArrayList<DogBreed>) : RecyclerView.Adapter<DogsListAdapter.DogViewHolder>(){

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

    }

    override fun getItemCount() = dogList.size

    //inner class we created before inheritance
    inner class DogViewHolder(var view: ItemDogBinding) : RecyclerView.ViewHolder(view.root)
}