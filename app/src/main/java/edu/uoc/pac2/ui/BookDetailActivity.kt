package edu.uoc.pac2.ui

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import edu.uoc.pac2.R
import edu.uoc.pac2.data.Book
import edu.uoc.pac2.databinding.ActivityBookDetailBinding

/**
 * An activity representing a single Book detail screen.
 */
class BookDetailActivity : AppCompatActivity() {

    // Declare binding for content view
    private lateinit var binding: ActivityBookDetailBinding
    var book: Book = Book()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            val itemID = intent.getIntExtra(BookDetailFragment.ARG_ITEM_ID, -1)
            val fragment = BookDetailFragment.newInstance(itemID) { item ->
                // Load current book to book variable
                book = item
                // Load my custom action bar with current book image and parallax special effect
                setMyActionBar()
                // Share current book when clcik on floating button
                binding.fab.setOnClickListener { shareData() }
            }
            supportFragmentManager.beginTransaction()
                    .add(binding.frameLayout.id, fragment)
                    .commit()
        }
    }

    private fun setMyActionBar() {
        setSupportActionBar(binding.detailToolbar)
        // Show the Up button in the action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        // Set book title in the action bar
        binding.toolbarLayout.title = book.title
        // Set book image into actionbar with paralax effect
        Picasso.get().load(book.urlImage).into(binding.ivToolBarImage)
    }

    // Share like text, book title and image url when push share button
    private fun shareData() {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "Title: ${book.title}\nImageURL: ${book.urlImage}")
            type = "text/plain"
        }
        startActivity(Intent.createChooser(sendIntent, "Share to other apps..."))
    }

    // Override finish animation for actionbar back arrow
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    // Override finish animation for phone back button
    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.translate_in_bottom, R.anim.translate_out_bottom)
    }

}