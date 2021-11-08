package com.example.hilttutorial

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(ActivityComponent::class)
object MainModule {

    //@Singleton은 applicationComponent에만 해당
    //동등한게 ActivityScope

    @ActivityScoped
    @Provides
    @Named("String1")
    fun provideTestString(
       @ApplicationContext context: Context,
       @Named("String2")testString2: String
    ) = "${context.getString(R.string.string_to_inject)} - $testString2"

}