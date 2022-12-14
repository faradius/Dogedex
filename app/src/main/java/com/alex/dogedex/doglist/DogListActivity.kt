package com.alex.dogedex.doglist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.alex.dogedex.Dog
import com.alex.dogedex.R
import com.alex.dogedex.databinding.ActivityDogListBinding

class DogListActivity : AppCompatActivity() {

    //El by hace funciona como un lateinit, osea que se declara y que se va usar despues
    private val dogListViewModel:DogListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityDogListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val rvDog = binding.rvDog
        rvDog.layoutManager = LinearLayoutManager(this)

        val adapter = DogAdapter()
        rvDog.adapter = adapter

        dogListViewModel.dogList.observe(this){ dogList ->
            adapter.submitList(dogList)
        }

    }
}