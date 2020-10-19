package edu.uoc.pac2.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Room Application Database
 */
@Database(entities = [Book::class], version = 1)
abstract class ApplicationDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao
/**
    companion object {
        @Volatile private var INSTANCE: ApplicationDatabase? = null

        fun getInstance(context: Context): ApplicationDatabase =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
                }

        private fun buildDatabase(context: Context) =
                Room.databaseBuilder(context,
                        ApplicationDatabase::class.java, "Sample.db")
                        .build()
    }
*/
}