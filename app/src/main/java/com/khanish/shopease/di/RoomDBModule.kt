package com.khanish.shopease.di

import android.content.Context
import androidx.room.Room
import com.khanish.shopease.local.ShopEaseDB
import com.khanish.shopease.local.ShopEaseDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomDBModule {

    @Provides
    @Singleton
    fun provideRoomDb(@ApplicationContext applicationContext: Context): ShopEaseDB {
        return Room.databaseBuilder(applicationContext, ShopEaseDB::class.java, "ShopEase_DB")
            .fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideDatabase(shopEaseDB: ShopEaseDB): ShopEaseDao {
        return shopEaseDB.shopEaseDao()
    }

}