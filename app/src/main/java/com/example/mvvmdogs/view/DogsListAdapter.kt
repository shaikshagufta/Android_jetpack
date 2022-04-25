package com.example.mvvmdogs.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvmdogs.R
import com.example.mvvmdogs.databinding.ItemDogBinding
import com.example.mvvmdogs.model.DogBreed


class DogsListAdapter(val dogList: ArrayList<DogBreed>) : RecyclerView.Adapter<DogsListAdapter.DogViewHolder>(){

    lateinit var binding: ItemDogBinding

    //inner class we created before inheritance
    inner class DogViewHolder(var view: View) : RecyclerView.ViewHolder(view){

        val name = view.findViewById<TextView>(R.id.name)
        val lifeSpan = view.findViewById<TextView>(R.id.lifeSpan)
    }

    //to update the list when we get info from backend
    fun updateDogList(newDogList: List<DogBreed>) {
        dogList.clear()
        dogList.addAll(newDogList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DogViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_dog, parent, false)

        binding = ItemDogBinding.inflate(inflater, parent, false)

        //returning the ViewHolder containing the inflated view as parameter
        return DogViewHolder(view)
    }

    //binding the element of the dogList with position to the vieHolder
    override fun onBindViewHolder(holder: DogViewHolder, position: Int) {

        holder.name.text = dogList.get(position).dogBreed
        holder.lifeSpan.text = dogList.get(position).lifeSpan
    }

    override fun getItemCount() = dogList.size
}