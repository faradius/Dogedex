package com.alex.dogedex.dogdetail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import coil.load
import com.alex.dogedex.model.Dog
import com.alex.dogedex.R
import com.alex.dogedex.api.ApiResponseStatus
import com.alex.dogedex.databinding.ActivityDogDetailBinding

class DogDetailActivity : AppCompatActivity() {}
//
//    companion object{
//        const val DOG_KEY = "dog"
//        const val IS_RECOGNITION_KEY = "is_recognition"
//    }
//
//    private val viewModel: DogDetailViewModel by viewModels()
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        val binding = ActivityDogDetailBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        val dog = intent?.extras?.getParcelable<Dog>(DOG_KEY)
//        val isRecognition = intent?.extras?.getBoolean(IS_RECOGNITION_KEY, false) ?: false
//
//        if (dog == null){
//            Toast.makeText(this, R.string.error_showing_dog_not_found, Toast.LENGTH_SHORT).show()
//            finish()
//            return
//        }
//
//        binding.dogIndex.text = getString(R.string.dog_index_format, dog.index)
//        binding.lifeExpectancy.text = getString(R.string.dog_life_expectancy_format, dog.lifeExpectancy)
//
//        binding.dog = dog
//        binding.dogImage.load(dog.imageUrl)
//
//        viewModel.status.observe(this){ status->
//            when(status){
//                is ApiResponseStatus.Error -> {
//                    binding.loadingWheel.visibility = View.GONE
//                    Toast.makeText(this@DogDetailActivity, status.messageId, Toast.LENGTH_SHORT).show()
//                }
//                is ApiResponseStatus.Loading -> binding.loadingWheel.visibility = View.VISIBLE
//                is ApiResponseStatus.Success -> {
//                    binding.loadingWheel.visibility = View.GONE
//                    finish()
//                }
//                else -> {Toast.makeText(this@DogDetailActivity, R.string.unknown_error, Toast.LENGTH_SHORT).show()}
//            }
//        }
//
//        binding.closeButton.setOnClickListener {
//            if (isRecognition){
//                viewModel.addDogToUser(dog.id)
//            }else{
//                //Nos cierra la pantalla y regresa a la anterior
//                finish()
//            }
//
//
//        }
//    }
//}