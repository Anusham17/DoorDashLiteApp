package com.example.doordash.ui.fragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.doordash.DoorDashApplication
import com.example.doordash.R
import com.example.doordash.repository.DoorDashRepository
import com.example.doordash.ui.adapter.StoresAdapter
import com.example.doordash.viewmodel.StoreFeedViewModel
import javax.inject.Inject

class StoreFeedFragment : Fragment(R.layout.fragment_store_feed) {

    private lateinit var storesAdapter: StoresAdapter
    private lateinit var storeFeedViewModel: StoreFeedViewModel

    @Inject
    lateinit var repository: DoorDashRepository

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity?.application as DoorDashApplication).doorDashComponent.inject(this)

        storeFeedViewModel = ViewModelProvider(
            this,
            StoreFeedViewModel.Factory(repository)
        ).get(StoreFeedViewModel::class.java)
        val storesRecyclerView = view.findViewById<RecyclerView>(R.id.stores_recycler_view)
        storesAdapter = StoresAdapter(storeFeedViewModel)

        val layoutManager = LinearLayoutManager(requireContext())
        storesRecyclerView.layoutManager = layoutManager
        val dividerItemDecoration =
            DividerItemDecoration(storesRecyclerView.context, layoutManager.orientation)
        storesRecyclerView.addItemDecoration(dividerItemDecoration)

        storesRecyclerView.adapter = storesAdapter
        storeFeedViewModel.storesLiveData.observe(
            viewLifecycleOwner,
            Observer { storesAdapter.setData(it) })

        storeFeedViewModel.errorLiveData.observe(viewLifecycleOwner, Observer {
            Toast.makeText(requireContext(), it.errorMessage, Toast.LENGTH_SHORT).show()
        })

        storeFeedViewModel.storeItemClickLiveData.observe(viewLifecycleOwner, Observer { event ->
            event.getContentIfNotHandled()?.let {
                activity?.supportFragmentManager?.beginTransaction()
                    ?.add(R.id.container, StoreDetailFragment.newInstance(it))
                    ?.addToBackStack("storeDetails")
                    ?.commit()
            }
        })

        storeFeedViewModel.getStores(0)
    }
}