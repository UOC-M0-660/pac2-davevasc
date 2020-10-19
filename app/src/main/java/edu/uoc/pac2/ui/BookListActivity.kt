package edu.uoc.pac2.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import edu.uoc.pac2.MyApplication
import edu.uoc.pac2.R
import edu.uoc.pac2.data.Book
import edu.uoc.pac2.data.BooksInteractor

/**
 * An activity representing a list of Books.
 */
class BookListActivity : AppCompatActivity() {

    private val TAG = "BookListActivity"
    private lateinit var adapter: BooksListAdapter

    private lateinit var interactor: BooksInteractor

    // Declare room data base interactor


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_list)

        val prueba = MyApplication().hasInternetConnection()

        val interactor = MyApplication().getBooksInteractor()

        // Init UI
        initToolbar()
        initRecyclerView()

        // Get Books
        getBooks()

        // Add books data to Firestore [Use once for new projects with empty Firestore Database]
        // Run just once for add books to firestore db,
        // Correctly added to firestore, so, not need run again
        //addBooksDataToFirestoreDatabase()
    }

    // Init Top Toolbar
    private fun initToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.title = title
    }

    // Init RecyclerView
    private fun initRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.book_list)
        // Set Layout Manager
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        // Init Adapter
        adapter = BooksListAdapter(emptyList())
        recyclerView.adapter = adapter
    }

    // TODO: Get Books and Update UI
    private fun getBooks() {
        // First load local books
        loadBooksFromLocalDb()
        // If internet connection, then load form firestore and save to room
        if (MyApplication().hasInternetConnection()) {
            loadBooksFromOnlineDb()
        }
    }

    // Load Books from Room
    private fun loadBooksFromLocalDb() {
        adapter.setBooks(interactor.getAllBooks())
        throw NotImplementedError()
    }

    // Load Books from Firestore
    private fun loadBooksFromOnlineDb() {
        // define the firestore instance
        val dbF = FirebaseFirestore.getInstance()

        // Call to firestore data base and get books
        dbF.collection("books")
                .addSnapshotListener { value, e ->
                    if (e != null) {
                        Log.w(TAG, "Listen failed.", e)
                        return@addSnapshotListener
                    }
                    // Save books in kotlin structure
                    val books: List<Book> = value!!.documents.mapNotNull { it.toObject(Book::class.java) }
                    // Reload recycler view again with new book list
                    adapter.setBooks(books)
                    // Load firestore online books into local Room database
                    saveBooksToLocalDatabase(books)
                }
    }

    // Save Books to Local Storage
    private fun saveBooksToLocalDatabase(books: List<Book>) {
        interactor.saveBooks(books)
        throw NotImplementedError()
    }
}