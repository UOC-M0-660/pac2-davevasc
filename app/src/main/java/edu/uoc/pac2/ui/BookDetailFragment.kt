package edu.uoc.pac2.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import edu.uoc.pac2.MyApplication
import edu.uoc.pac2.data.Book
import edu.uoc.pac2.data.BooksInteractor
import edu.uoc.pac2.databinding.FragmentBookDetailBinding
import kotlinx.coroutines.launch

/**
 * A fragment representing a single Book detail screen.
 * This fragment is contained in a [BookDetailActivity].
 */
class BookDetailFragment: Fragment() {

    private lateinit var callback: OnBookSelectedListener

    // Link to BookActiviy
    fun setOnBookSelectedListener(callback: OnBookSelectedListener) {
        this.callback = callback
    }

    // This interface can be implemented by the Activity, parent Fragment,
    // or a separate test implementation.
    // Interface for communication between Fragment to Activity
    interface OnBookSelectedListener {
        fun onBookSelected(book: Book)
    }

    // Declare MyApplication variable
    private var app: MyApplication? = null
    // Declare room data base interactor
    private var interactor: BooksInteractor? = null

    private lateinit var binding: FragmentBookDetailBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Get this view associating with the correct layout
        binding = FragmentBookDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set MyApplication in app variable
        app = (activity?.applicationContext as? MyApplication)
        // Get interactor form MyApplication activity
        interactor = app?.getBooksInteractor()

        // Get Book for this detail screen
        loadBook()
    }

    // Get Book for the given {@param ARG_ITEM_ID} Book id
    private fun loadBook() {
        val id = arguments?.getInt(ARG_ITEM_ID)
        lifecycleScope.launch {
            val book = id?.let { interactor?.getBookById(it) }
            book?.let {
                callback.onBookSelected(it)
                initUI(it)
            }
        }
    }

    // Init UI with book details
    private fun initUI(book: Book?) {
        binding.tvAutor.text = book?.author
        binding.tvFecha.text = book?.publicationDate
        binding.tvDesc.text = book?.description
    }

    // Done better in the BookDetailActivity
    //private fun shareContent(book: Book) {

    //}

    companion object {
        /**
         * The fragment argument representing the item title that this fragment
         * represents.
         */
        const val ARG_ITEM_ID = "itemIdKey"

        fun newInstance(itemId: Int): BookDetailFragment {
            val fragment = BookDetailFragment()
            val arguments = Bundle()
            arguments.putInt(ARG_ITEM_ID, itemId)
            fragment.arguments = arguments
            return fragment
        }
    }
}