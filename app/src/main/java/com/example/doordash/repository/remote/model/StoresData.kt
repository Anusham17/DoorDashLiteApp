package com.example.doordash.repository.remote.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
/**
 * Data models for network data
 */

data class Store(
    @Expose val id: Int?,
    @Expose val name: String?,
    @Expose val description: String?,
    @SerializedName("cover_img_url") val coverImageUrl: String?,
    @Expose val status: Status?
)

data class StoreFeed(
    @SerializedName("num_results") val size: Int?,
    @SerializedName("is_first_time_user") val isFirstTimeUser: Boolean?,
    @SerializedName("sort_order") val sortOrder: String?,
    @SerializedName("next_offset") val nextOffset: Int?,
    @SerializedName("show_list_as_pickup") val showListAsPickup: Boolean?,
    @Expose val stores: List<Store>?
)

data class Status(
    @SerializedName("unavailable_reason") val unavailableReason: String?,
    @SerializedName("pickup_available") val isPickupAvailable: Boolean?,
    @SerializedName("asap_available") val isAsapAvailable: Boolean?,
    @SerializedName("asap_minutes_range") val asapRange: IntArray?
)

data class Address(
    @SerializedName("printable_address") val printableAddress: String?
)

data class StoreDetails(
    @Expose val id: Int?,
    @Expose val name: String?,
    @Expose val description: String?,
    @SerializedName("cover_img_url") val coverImageUrl: String?,
    @SerializedName("average_rating") val averageRating: Float?,
    @Expose val status: String?,
    @Expose val address: Address?
)
