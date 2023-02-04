package com.alex.dogedex.di

import com.alex.dogedex.doglist.DogRepository
import com.alex.dogedex.doglist.DogTasks
import com.alex.dogedex.machinelearning.ClassifierRepository
import com.alex.dogedex.machinelearning.ClassifierTasks
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ClassifierModule {
    @Binds
    abstract fun bindClassifierTasks(
        classifierRepository: ClassifierRepository
    ): ClassifierTasks
}