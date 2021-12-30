package com.example.mvvmtodo.di

import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mvvmtodo.data.TodoDatabase
import com.example.mvvmtodo.data.TodoRepository
import com.example.mvvmtodo.data.TodoRepositoryImp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideTodoDatabase(app: Application): TodoDatabase{

        return Room.databaseBuilder(
            app,
            TodoDatabase::class.java,
            "todo_db"
        ).build()
    }

    @Provides
    @Singleton
    fun providesTodoRepository(db: TodoDatabase): TodoRepository{


        return TodoRepositoryImp(db.dao)
    }
}








