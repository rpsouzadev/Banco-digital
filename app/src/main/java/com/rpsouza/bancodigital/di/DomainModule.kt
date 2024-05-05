package com.rpsouza.bancodigital.di

import com.rpsouza.bancodigital.data.repository.auth.AuthFirebaseDataSourceImpl
import com.rpsouza.bancodigital.data.repository.auth.IAuthFirebaseDataSource
import com.rpsouza.bancodigital.data.repository.deposit.DepositDataSourceImpl
import com.rpsouza.bancodigital.data.repository.deposit.IDepositDataSource
import com.rpsouza.bancodigital.data.repository.transaction.ITransactionDataSource
import com.rpsouza.bancodigital.data.repository.transaction.TransactionDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class DomainModule {

  @Binds
  abstract fun bindsAuthDataSource(
    authFirebaseDataSourceImpl: AuthFirebaseDataSourceImpl
  ): IAuthFirebaseDataSource

  @Binds
  abstract fun bindsDepositDataSource(
    depositDataSourceImpl: DepositDataSourceImpl
  ): IDepositDataSource

  @Binds
  abstract fun bindsTransactionDataSource(
    transactionDataSourceImpl: TransactionDataSourceImpl
  ): ITransactionDataSource
}