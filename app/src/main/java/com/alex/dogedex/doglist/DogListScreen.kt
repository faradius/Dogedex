package com.alex.dogedex.doglist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.alex.dogedex.R
import com.alex.dogedex.model.Dog

@Composable
fun DogListScreen(dogList: List<Dog>, onDogClicked: (Dog) -> Unit) {
    //LazyColumn significa que no hagas nada hasta que te necesite pero aqui ya es la implementación del recycler view
    LazyColumn {
        items(dogList) {
            DogItem(dog = it, onDogClicked)
        }
    }
}

@Composable
fun DogItem(dog: Dog, onDogClicked: (Dog) -> Unit) {
    if (dog.inColletion) {
        Text(
            modifier = Modifier
                .padding(16.dp)
                .clickable {
                    onDogClicked(dog)
                },
            text = dog.name
        )
    }else{
        Text(
            modifier = Modifier
                .padding(16.dp)
                .background(color = Color.Red),
            text = stringResource(id = R.string.dog_index_format, dog.index))
    }
}