package edu.uoc.pac2.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import edu.uoc.pac2.MyApplication
import edu.uoc.pac2.R
import edu.uoc.pac2.data.Book
import edu.uoc.pac2.data.BooksInteractor
import kotlinx.coroutines.launch

/**
 * An activity representing a list of Books.
 */
class BookListActivity : AppCompatActivity() {

    private val TAG = "BookListActivity"
    private lateinit var adapter: BooksListAdapter
    // Declare MyApplication variable
    private var app: MyApplication? = null
    // Declare room data base interactor
    private var interactor: BooksInteractor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_list)

        // Set MyApplication in app variable
        app = (applicationContext as? MyApplication)
        // Get interactor form MyApplication activity
        interactor = app?.getBooksInteractor()

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
        adapter = BooksListAdapter(emptyList()) { item -> doClick(item) }
        recyclerView.adapter = adapter
    }

    // Creating method to make it look simpler
    fun doClick(item: Book) {
        val intent = Intent(this, BookDetailActivity::class.java).apply {
            putExtra(BookDetailFragment.ARG_ITEM_ID, item.uid)
        }
        startActivity(intent)
        // Set animation: Detail is oppening from down to top
        overridePendingTransition(R.anim.translate_in_top, R.anim.translate_out_top)
    }

    // TODO: Get Books and Update UI
    private fun getBooks() {
            // First load local books
        lifecycleScope.launch {
            loadBooksFromLocalDb()
        }
            // If internet connection, then load form firestore and save to room
            app?.hasInternetConnection()?.let {
                if (it) {
                    loadBooksFromOnlineDb()
                }
            }
    }

    // Load Books from Room
    private suspend fun loadBooksFromLocalDb() {
        interactor?.getAllBooks()?.let { adapter.setBooks(it) }
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
                    value?.let{ coll -> val books: List<Book> = coll.documents.mapNotNull { doc -> doc.toObject(Book::class.java) }
                        // Reload recycler view again with new book list
                        adapter.setBooks(books)
                        // Load firestore online books into local Room database
                        lifecycleScope.launch {
                            restartBooksInToLocalDatabase()
                            saveBooksToLocalDatabase(books)
                        }
                    }
                }
    }

    // Save Books to Local Storage
    private suspend fun saveBooksToLocalDatabase(books: List<Book>) {
        interactor?.saveBooks(books)
    }

    // Restart Books to Local Storage
    private suspend fun restartBooksInToLocalDatabase() {
        interactor?.resetBooks()
    }
}