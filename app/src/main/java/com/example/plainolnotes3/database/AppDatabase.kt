package com.example.plainolnotes3.database

import android.content.Context
import androidx.room.*
import com.example.plainolnotes3.utilities.SingletonHolder

/**
 * @see https://github.com/android/architecture-components-samples/blob/main/BasicRxJavaSampleKotlin/app/src/main/java/com/example/android/observability/persistence/UsersDatabase.kt
 * @see https://bladecoder.medium.com/kotlin-singletons-with-argument-194ef06edd9e
 *
 */
@Database(entities = [NoteEntity::class], version = 1)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {

    // Abstract method for Room to implement DAO
    abstract fun noteDao(): NoteDao

    // Companion object to provide static thread-safe version of getInstance()
    companion object : SingletonHolder<AppDatabase, Context>({
        Room.databaseBuilder(it.applicationContext, AppDatabase::class.java, "AppDatabase.db").build()
    })

}