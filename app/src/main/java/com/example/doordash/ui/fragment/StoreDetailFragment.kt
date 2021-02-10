package com.example.doordash.ui.fragment

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.doordash.DoorDashApplication
import com.example.doordash.R
import com.example.doordash.repository.DoorDashRepository
import com.example.doordash.viewmodel.StoreDetailViewModel
import javax.inject.Inject

class StoreDetailFragment : Fragment(R.layout.fragment_store_details) {

    private lateinit var storeDetailViewModel: StoreDetailViewModel
    @Inject
    lateinit var repository: DoorDashRepository

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity?.application as DoorDashApplication).doorDashComponent.inject(this)
        storeDetailViewModel = ViewModelProvider(this, StoreDetailViewModel.Factory(repository)).get(
            StoreDetailViewModel::class.java)

        storeDetailViewModel = ViewModelProvider(this).get(StoreDetailViewModel::class.java)
        val storeImage = view.findViewById<ImageView>(R.id.store_detail_image_view)
        val storeTitle = view.findViewById<TextView>(R.id.store_title)
        val storeRatingBar = view.findViewById<RatingBar>(R.id.store_rating)
        val storeMenu = view.findViewById<TextView>(R.id.store_menu)
        val storeAddress = view.findViewById<TextView>(R.id.store_address)
        val storeStatus = view.findViewById<TextView>(R.id.store_status)

        storeDetailViewModel.storeDetailLiveData.observe(viewLifecycleOwner, Observer { store ->
            store.coverImageUrl?.let {
                Glide.with(view.context).load(it).into(storeImage)
            }
            storeTitle.text = store.name
            store.averageRating?.let {
                storeRatingBar.rating = it
            }
            storeMenu.text = store.description
            storeAddress.text = "Address: ${store.address?.printableAddress}"
            storeStatus.text = "Delivery Time: ${store.status}"
        })

        storeDetailViewModel.errorLiveData.observe(viewLifecycleOwner, Observer {
            Toast.makeText(requireContext(), it.errorMessage, Toast.LENGTH_SHORT).show()
        })

        arguments?.getInt(STORE_ID)?.let { storeDetailViewModel.getStoreDetails(it) }
    }

    companion object {
        const val STORE_ID = "id"

        @JvmStatic
        fun newInstance(id: Int): StoreDetailFragment {
            return StoreDetailFragment().apply {
                arguments = Bundle().apply {
                    putInt(STORE_ID, id)
                }
            }

        }
    }
}