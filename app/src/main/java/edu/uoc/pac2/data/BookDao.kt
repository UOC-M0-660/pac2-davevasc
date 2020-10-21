package edu.uoc.pac2.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * Book Dao (Data Access Object) for accessing Book Table functions.
 */
@Dao
interface BookDao {
    @Query("SELECT * FROM books_table WHERE isActive = 1")
    //@Query("SELECT * FROM books_table")
    suspend fun getAllBooks(): List<Book>

    @Query("SELECT * FROM books_table WHERE uid = :id")
    suspend fun getBookById(id: Int): Book?

    @Query("SELECT * FROM books_table WHERE title = :titleBook")
    suspend fun getBookByTitle(titleBook: String): Book?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveBook(book: Book): Long

    @Query("UPDATE books_table SET isActive = 0")
    suspend fun resetBooks(): Int
}