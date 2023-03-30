package com.example.digitalenvoy.di

import com.example.digitalenvoy.repository.LocationRepository
import com.example.digitalenvoy.repository.LocationRepositoryImp
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LocationModule {

    @Binds
    @Singleton
    abstract fun bindLocationServices(locationRepositoryImp: LocationRepositoryImp) : LocationRepository
}