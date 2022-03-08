package com.example.hilttutorial

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(ActivityComponent::class) //scope의 크기를 결정
object MainModule {
    //retrofit, room시 di 사용

    @ActivityScoped //activityScope만큼 존재하고, 그동안 recreate 되지않음.
    @Provides
    @Named("String2") // named를 통해 어떤걸 주입할지 결정
    fun provideTestString2(
        @ApplicationContext context: Context, //이전에 정의한 MyApplication의 application context를 가져옴.
        @Named("String1") testString1: String
    ) = "${context.getString(R.string.string_to_inject)} - $testString1"

}
