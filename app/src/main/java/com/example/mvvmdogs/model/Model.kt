package com.example.mvvmdogs.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

//marking this as a class or DS that can be put in the db
@Entity//(tableName = " ")- we can set a table name or leave it so the class-name is the table name
data class DogBreed(
    @ColumnInfo(name = "breed_id")//changing the column name
    @SerializedName("id")
    val breedId: String?,

    @ColumnInfo(name = "dog_name")
    @SerializedName("name")
    val dogBreed: String?,

    @ColumnInfo(name = "life_span")
    @SerializedName("life_span")
    val lifeSpan: String?,

    @ColumnInfo(name = "breed_group")
    @SerializedName("breed_group")
    val breedGroup: String?,

    @ColumnInfo(name = "bred_for")
    @SerializedName("bred_for")
    val bredFor: String?,

    @SerializedName("temperament")
    val temperament: String?,

    @ColumnInfo(name = "dog_url")
    @SerializedName("url")
    val imageUrl: String?
) {
// we don't set any of the parameters as primary key because
// 1. none of them are unique as they are from backend and can repeat
// 2. we don't want an id to be generated every time an objet of dog breed data class is generated
//so we generate a primary key in the body of the data class

    //instantiating var that will hold the primary key
    @PrimaryKey(autoGenerate = true)
    var uuid: Int = 0//by default
//so every time the @Entity is put inside the db, Room library will generate a primary-key and store it with data
}