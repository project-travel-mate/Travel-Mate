package objects

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class RestaurantDetails(@SerializedName("restaurant_id") var id: Int,
                             @SerializedName("restaurant_name") var name: String,
                             @SerializedName("restaurant_url") var url: String,
                             @SerializedName("restaurant_address") var address: String,
                             @SerializedName("restaurant_longitude") var longitude: Double,
                             @SerializedName("restaurant_latitude") var latitude: Double,
                             @SerializedName("avg_cost_2") var avgCost: Int,
                             @SerializedName("price_range") var priceRange: Int,
                             @SerializedName("currency") var currency: String,
                             @SerializedName("featured_image") var featuredImage: String,
                             @SerializedName("user_aggregate_rating") var rating: String,
                             @SerializedName("user_rating_votes") var votes: String,
                             @SerializedName("is_delivering_now") var isDeliveringNow: Boolean,
                             @SerializedName("has_table_booking") var hasTableBooking: Boolean,
                             @SerializedName("cuisines") var cuisines: String,
                             @SerializedName("phone_numbers") var phoneNumbers: Any): Serializable