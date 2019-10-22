package io.github.project_travel_mate.destinations.description

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import com.squareup.picasso.Picasso
import io.github.project_travel_mate.R
import io.github.project_travel_mate.utilities.gone
import kotlinx.android.synthetic.main.activity_restaurant_details.*
import objects.RestaurantDetails

class RestaurantDetailsActivity : AppCompatActivity() {

    companion object {
        private const val EXTRA_DETAILS = "extra_details"
        val TAG = "RestaurantDetails"
        @JvmStatic
        fun newInstance(context: Activity, restaurantDetails: RestaurantDetails) {
            val intent = Intent(context, RestaurantDetailsActivity::class.java)
            intent.putExtra(EXTRA_DETAILS, restaurantDetails)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_details)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        if (!intent.hasExtra(EXTRA_DETAILS)) {
            Log.e(TAG, "something went wrong!")
            finish()
        }

        val details = intent.getSerializableExtra(EXTRA_DETAILS) as RestaurantDetails

        title = details.name

        populateViews(details)
    }

    private fun populateViews (restaurant: RestaurantDetails) {
        if (restaurant.featuredImage.trim().isNotEmpty()) {
            Picasso.with(this).load(restaurant.featuredImage).into(image_view)
        }

        address.text = getString(R.string.rest_address, restaurant.address)
        avg_cost.text = getString(R.string.avg_cost, restaurant.avgCost.toString())
        price_range.text = getString(R.string.price_range, restaurant.priceRange.toString())
        currency.text = getString(R.string.rest_currency, restaurant.currency)
        user_rating.text = getString(R.string.user_rating, restaurant.rating)
        user_votes.text = getString(R.string.user_votes, restaurant.votes)

        if (restaurant.hasTableBooking) {
            table_booking.text = getString(R.string.has_table_booking)
        } else {
            table_booking_content.gone()
        }
        cuisines.text = getString(R.string.cuisines, restaurant.cuisines)

        val phoneNo = restaurant.phoneNumbers as String?
        if (phoneNo.isNullOrEmpty()) {
            phone_numbers_content.gone()
        } else {
            phone_numbers.text = getString(R.string.phone_no, phoneNo)
        }
    }
}
