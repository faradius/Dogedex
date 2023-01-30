package com.alex.dogedex.di

import com.alex.dogedex.doglist.DogRepository
import com.alex.dogedex.doglist.DogTasks
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class) //Va hacer vista desde toda la aplicación
abstract class DogTaksModule {

    @Binds
    abstract fun bindDogTasks(
        dogRepository: DogRepository
    ): DogTasks
}