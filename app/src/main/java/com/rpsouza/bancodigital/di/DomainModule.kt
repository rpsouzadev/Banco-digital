package com.rpsouza.bancodigital.di

import com.rpsouza.bancodigital.data.repository.auth.AuthFirebaseDataSourceImpl
import com.rpsouza.bancodigital.data.repository.auth.IAuthFirebaseDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class DomainModule {

  @Binds
  abstract fun bindsAuthRepository(
    authFirebaseDataSourceImpl: AuthFirebaseDataSourceImpl
  ): IAuthFirebaseDataSource



}