package com.example.dictionaryapp.di

import androidx.room.Room
import com.example.dictionaryapp.MainViewModel
import com.example.dictionaryapp.data.api.DictionaryApi
import com.example.dictionaryapp.data.database.DictionaryDao
import com.example.dictionaryapp.data.database.DictionaryDatabase
import com.example.dictionaryapp.data.datasource.local.LocalDataSource
import com.example.dictionaryapp.data.datasource.local.LocalDataSourceImpl
import com.example.dictionaryapp.data.datasource.remote.RemoteDataSource
import com.example.dictionaryapp.data.datasource.remote.RemoteDataSourceImpl
import com.example.dictionaryapp.data.network.RetrofitClient
import com.example.dictionaryapp.data.repository.DictionaryRepository
import com.example.dictionaryapp.data.repository.DictionaryRepositoryImpl
import com.example.dictionaryapp.domain.GetWordInfoUsecase
import com.example.dictionaryapp.domain.GetWordInfoUsecaseImpl
import com.squareup.moshi.Moshi
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val appModule = module {

    single {
        RetrofitClient()
    }

    single<Retrofit> {
        val client: RetrofitClient = get()
        client.buildRetrofitInstance(get())
    }

    single<Moshi> {
        val client: RetrofitClient = get()
        client.buildMoshiInstance()
    }

    single {
        val retrofit: Retrofit = get()
        retrofit.create(DictionaryApi::class.java)
    }

    single<RemoteDataSource> {
        val api: DictionaryApi = get()
        RemoteDataSourceImpl(api)
    }

    single<LocalDataSource> {
        LocalDataSourceImpl(dao = get())
    }

    single<DictionaryDao> {
        val db: DictionaryDatabase = get()
        db.dictionaryDao()
    }

    single<DictionaryDatabase> {
        Room.databaseBuilder(
            context = androidContext(),
            klass = DictionaryDatabase::class.java,
            name = "dictionary_db",
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    single<DictionaryRepository> {
        DictionaryRepositoryImpl(
            remoteDataSource = get(),
            localDataSource = get()
        )
    }

    single<GetWordInfoUsecase> {
        GetWordInfoUsecaseImpl(repository = get())
    }

    viewModel {
        MainViewModel(getWordInfoUsecase = get())
    }
}