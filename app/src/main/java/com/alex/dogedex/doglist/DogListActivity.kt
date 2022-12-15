package com.alex.dogedex.doglist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.alex.dogedex.databinding.ActivityDogListBinding
import com.alex.dogedex.dogdetail.DogDetailActivity
import com.alex.dogedex.dogdetail.DogDetailActivity.Companion.DOG_KEY

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
        adapter.setOnItemClickListener {
            //Pasar el dog a DogDetailActivity
            val intent = Intent(this, DogDetailActivity::class.java)
            intent.putExtra(DOG_KEY, it)
            startActivity(intent)
        }
        rvDog.adapter = adapter

        dogListViewModel.dogList.observe(this){ dogList ->
            adapter.submitList(dogList)
        }

    }
}