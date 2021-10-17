package com.suonk.whatsapp_clone.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    fun provideFirebase() = FirebaseFirestore.getInstance()

    @Provides
    fun provideFirebaseAuth() = FirebaseAuth.getInstance()
}