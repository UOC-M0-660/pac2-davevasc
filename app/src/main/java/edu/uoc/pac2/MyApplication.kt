package edu.uoc.pac2

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.room.Room
import edu.uoc.pac2.data.*

/**
 * Entry point for the Application.
 */
class MyApplication : Application() {

    private lateinit var booksInteractor: BooksInteractor

    override fun onCreate() {
        super.onCreate()
        // Init Room Database
        val dbR = Room.databaseBuilder(
                applicationContext,
                ApplicationDatabase::class.java, "books-database").build()
        // Init BooksInteractor
        //val dbR = ApplicationDatabase.getInstance(applicationContext)
        booksInteractor = BooksInteractor(dbR.bookDao())
    }

    fun getBooksInteractor(): BooksInteractor {
        return booksInteractor
    }

    fun hasInternetConnection(): Boolean {
        // Add Internet Check logic.
        val connectivityManager =
                getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        //val activeNetwork = connectivityManager.activeNetwork
        val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                return true
            }
        }
        return false
    }
}