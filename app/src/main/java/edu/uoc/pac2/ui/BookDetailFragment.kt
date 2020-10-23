package edu.uoc.pac2.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.squareup.picasso.Picasso
import edu.uoc.pac2.MyApplication
import edu.uoc.pac2.R
import edu.uoc.pac2.data.Book
import edu.uoc.pac2.data.BooksInteractor
import edu.uoc.pac2.databinding.ActivityBookDetailBinding
import edu.uoc.pac2.databinding.FragmentBookDetailBinding
import kotlinx.coroutines.launch

/**
 * A fragment representing a single Book detail screen.
 * This fragment is contained in a [BookDetailActivity].
 */
class BookDetailFragment : Fragment() {

    // Declare MyApplication variable
    private var app: MyApplication? = null
    // Declare room data base interactor
    private var interactor: BooksInteractor? = null

    private lateinit var binding: FragmentBookDetailBinding
    private lateinit var bindingActivity: ActivityBookDetailBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Get this view associating with the correct layout
        binding = FragmentBookDetailBinding.inflate(inflater, container, false)
        bindingActivity = ActivityBookDetailBinding.inflate(inflater, container, false)

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
            if (book != null) {
                initUI(book)
            }
        }
    }

    // TODO: Init UI with book details
    private fun initUI(book: Book?) {
        activity?.findViewById<CollapsingToolbarLayout>(bindingActivity.toolbarLayout.id)?.title = book?.title
        Picasso.get().load(book?.urlImage).into(binding.ivUrl)
        binding.tvAutor.text = book?.author
        binding.tvFecha.text = book?.publicationDate
        binding.tvDesc.text = book?.description
    }

    // TODO: Share Book Title and Image URL
    private fun shareContent(book: Book) {

    }

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