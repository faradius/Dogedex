package com.alex.dogedex.doglist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import coil.annotation.ExperimentalCoilApi
import com.alex.dogedex.R
import com.alex.dogedex.api.ApiResponseStatus
import com.alex.dogedex.databinding.ActivityDogListBinding
//import com.alex.dogedex.dogdetail.DogDetailActivity
//import com.alex.dogedex.dogdetail.DogDetailActivity.Companion.DOG_KEY
import com.alex.dogedex.dogdetail.DogDetailComposeActivity
import com.alex.dogedex.dogdetail.ui.theme.DogedexTheme
import com.alex.dogedex.model.Dog
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalMaterialApi
@ExperimentalFoundationApi
@ExperimentalCoilApi
@AndroidEntryPoint
class DogListActivity : ComponentActivity() {

    //El by hace funciona como un lateinit, osea que se declara y que se va usar despues
    //private val viewModel:DogListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{
//            val status = viewModel.status
            DogedexTheme {
//                val dogList = viewModel.dogList
                DogListScreen(
                    onNavigationIconClick = ::onNavigationIconClick,
//                    dogList = dogList.value,
                    onDogClicked = ::openDogDetailActivity, //se puede simplificar porque recibe en ambos lados un dog
//                status = status.value,
//                    onDialogErrorDismiss = ::resetApiResponseStatus
                )
            }
        }

        //Esto era cuando se implementaba con xml
/*        val binding = ActivityDogListBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        val loadingWheel = binding.loadingWheel
//
//        val rvDog = binding.rvDog
//        rvDog.layoutManager = GridLayoutManager(this, GRID_SPAN_COUNT)
//
//        val adapter = DogAdapter()
//        adapter.setOnItemClickListener {
//            //Pasar el dog a DogDetailActivity
//            val intent = Intent(this, DogDetailComposeActivity::class.java)
//            intent.putExtra(DogDetailComposeActivity.DOG_KEY, it)
//            startActivity(intent)
//        }
//
//        rvDog.adapter = adapter
//
//        dogListViewModel.dogList.observe(this){ dogList ->
//            adapter.submitList(dogList)
//        }
//
//        dogListViewModel.status.observe(this){ status->
//            when(status){
//                is ApiResponseStatus.Error -> {
//                    loadingWheel.visibility = View.GONE
//                    Toast.makeText(this@DogListActivity, status.messageId, Toast.LENGTH_SHORT).show()
//                }
//                is ApiResponseStatus.Loading -> loadingWheel.visibility = View.VISIBLE
//                is ApiResponseStatus.Success -> loadingWheel.visibility = View.GONE
//                else -> {Toast.makeText(this@DogListActivity, R.string.unknown_error, Toast.LENGTH_SHORT).show()}
//            }
//        }
*/

    }

    private fun openDogDetailActivity(dog: Dog){
        val intent = Intent(this, DogDetailComposeActivity::class.java)
        intent.putExtra(DogDetailComposeActivity.DOG_KEY, dog)
        startActivity(intent)
    }

    private fun onNavigationIconClick(){
        finish()
    }

//    private fun resetApiResponseStatus() {
//        viewModel.resetApiResponseStatus()
//    }

}