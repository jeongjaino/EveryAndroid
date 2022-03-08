package com.example.hilttutorial

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class) //scope의 크기를 결정
object AppModule {
    //retrofit, room시 di 사용

    //singleton 어노테이션은 싱글턴컴포넌트와 작동함.
    @Singleton // singleton을 사용안하면 매번 새로 instance를 생성해서 주입함
    @Provides
    @Named("String1") // named를 통해 어떤걸 주입할지 결정
    fun provideTestString() = "This is String inject"

}
