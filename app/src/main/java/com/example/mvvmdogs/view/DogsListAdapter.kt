package com.example.mvvmdogs.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvmdogs.R
import com.example.mvvmdogs.databinding.ItemDogBinding
import com.example.mvvmdogs.model.DogBreed


class DogsListAdapter(private val dogList: ArrayList<DogBreed>) : RecyclerView.Adapter<DogsListAdapter.DogViewHolder>(){

    //private lateinit var binding: ItemDogBinding

    //to update the list when we get info from backend
    fun updateDogList(newDogList: List<DogBreed>) {
        dogList.clear()
        dogList.addAll(newDogList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DogViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        // val view = inflater.inflate(R.layout.item_dog, parent, false)
        val view = DataBindingUtil.inflate<ItemDogBinding>(inflater, R.layout.item_dog, parent, false)

        //binding = ItemDogBinding.inflate(inflater, parent, false)

        //returning the ViewHolder containing the inflated view as parameter
        return DogViewHolder(view)
    }

    //binding the element of the dogList with position to the vieHolder
    override fun onBindViewHolder(holder: DogViewHolder, position: Int) {

        //attaching the layout variable we created for databinding
        holder.view.dog = dogList[position]
        //with data binding we don't need to specify in text which elements we want to update, like we did before
       /* holder.name.text= dogList[position].dogBreed
        holder.lifeSpan.text = dogList[position].lifeSpan
        //nav to detail fragment on click
        holder.view.setOnClickListener {
            val action = ListFragmentDirections.actionDetailFragment()
            action.dogUuid = dogList[position].uuid//so that we get a uuid in 'action'
            Navigation.findNavController(it).navigate(action)
        }
        holder.imageView.loadImage(dogList[position].imageUrl, getProgressDrawable(holder.imageView.context))*/
    }

    override fun getItemCount() = dogList.size

    //inner class we created before inheritance
    //1. add the generated binding layout instead of view and pass the root as the view
    inner class DogViewHolder(var view: ItemDogBinding) : RecyclerView.ViewHolder(view.root){

        //we avoid all this using data binding

        //val name: TextView = view.findViewById(R.id.name)
        //val lifeSpan: TextView = view.findViewById(R.id.lifeSpan)
        //val imageView: ImageView = view.findViewById(R.id.imageView)
    }
}