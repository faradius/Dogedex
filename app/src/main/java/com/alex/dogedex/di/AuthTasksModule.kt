package com.alex.dogedex.di

import com.alex.dogedex.auth.AuthRepository
import com.alex.dogedex.auth.AuthTasks
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthTasksModule {
    @Binds
    abstract fun bindDogTasks(
        authRepository: AuthRepository
    ): AuthTasks
}