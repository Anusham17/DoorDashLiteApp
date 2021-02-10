package com.example.doordash.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.doordash.R
import com.example.doordash.repository.remote.model.Store
import com.example.doordash.viewmodel.StoreFeedViewModel

class StoresAdapter(
    private val storeFeedViewModel: StoreFeedViewModel,
    private var storesList: List<Store> = emptyList()
) : RecyclerView.Adapter<StoresAdapter.StoresViewHolder>() {


    fun setData(storesList: List<Store>) {
        this.storesList = storesList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoresViewHolder =
        StoresViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.store_item, parent, false),
            storeFeedViewModel
        )

    override fun getItemCount() = if (storesList.isNullOrEmpty()) 0 else storesList.size

    override fun onBindViewHolder(holder: StoresViewHolder, position: Int) {
        val store = storesList[position]

        store.coverImageUrl?.let {
            holder.storeImage?.let { image ->
                Glide.with(holder.itemView.context).load(store.coverImageUrl).into(
                    image
                )
            }
        }

        holder.storeName?.text = store.name
        holder.storeDesc?.text = store.description
        holder.storeStatus?.text = "${store.status?.asapRange?.get(0)} Mins"

        holder.itemView.setOnClickListener {
            store.id?.let { id -> storeFeedViewModel.onStoreItemClick(id) }
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val visibleItems: Int? = recyclerView.layoutManager?.childCount
                val totalItems: Int? = recyclerView.layoutManager?.itemCount
                val scrolledItems: Int? =
                    (recyclerView.layoutManager as? LinearLayoutManager)?.findFirstVisibleItemPosition()
                if (visibleItems == null || totalItems == null || scrolledItems == null) return
                if (visibleItems + scrolledItems >= totalItems) {
                    storeFeedViewModel.getStores(storesList.size)
                }
            }
        })
    }

    class StoresViewHolder(view: View, private val viewModel: StoreFeedViewModel) :
        RecyclerView.ViewHolder(view) {
        val storeImage: ImageView? = view.findViewById(R.id.store_image)
        val storeName: TextView? = view.findViewById(R.id.store_name)
        val storeDesc: TextView? = view.findViewById(R.id.store_description)
        val storeStatus: TextView? = view.findViewById(R.id.store_status)
    }
}