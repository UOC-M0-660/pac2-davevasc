package edu.uoc.pac2.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import edu.uoc.pac2.databinding.ActivityBookDetailBinding

/**
 * An activity representing a single Book detail screen.
 */
class BookDetailActivity : AppCompatActivity() {

    lateinit var binding: ActivityBookDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Support my custom action bar
        setSupportActionBar(findViewById(binding.detailToolbar.id))
        // Show the Up button in the action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Set dummy action to floating button
        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "There is no action yet", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }



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
            val fragment = BookDetailFragment.newInstance(itemID)
            supportFragmentManager.beginTransaction()
                    .add(binding.frameLayout.id, fragment)
                    .commit()
        }
    }

    // TODO: Override finish animation for actionbar back arrow
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }

    // TODO: Override finish animation for phone back button
    override fun onBackPressed() {
        super.onBackPressed()
    }

}