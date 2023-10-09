package com.task.dicoding.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.task.dicoding.R
import com.task.dicoding.adapter.UserAdapter
import com.task.dicoding.viewmodel.FollowViewModel

class FollowFragment : Fragment() {

    private lateinit var viewModel: FollowViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var loadingView: View
    private lateinit var errorView: TextView

    private lateinit var userAdapter: UserAdapter
    private lateinit var username: String
    private var position: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_follow, container, false)

        recyclerView = rootView.findViewById(R.id.rvFollower)
        loadingView = rootView.findViewById(R.id.progressbar_follower)
        errorView = rootView.findViewById(R.id.error_view)

        position = arguments?.getInt(ARG_POSITION, 0) ?: 0
        username = arguments?.getString(ARG_USERNAME, "") ?: ""

        // Inisialisasi ViewModel
        viewModel = ViewModelProvider(this)[FollowViewModel::class.java]

        // Mengamati LiveData untuk followers
        viewModel.follow.observe(viewLifecycleOwner) { followers ->


            if (followers != null) {
                // Lakukan sesuatu dengan data followers yang tidak null
                userAdapter.submitList(followers)
                showRecyclerView()
            } else {
                // Lakukan sesuatu jika data followers adalah null
                showEmptyView()
            }
        }

        // Mengamati LiveData untuk error
        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            val errorText = "Error: $errorMessage"
            errorView.text = errorText
            showErrorView()
        }

        setupRecyclerView()
        fetchData()

        return rootView

    }

    private fun setupRecyclerView() {
        userAdapter = UserAdapter()
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = userAdapter
    }

    private fun fetchData() {
        showLoading()
        if (position == 0) {
            viewModel.fetchFollowers(username)
        } else {
            viewModel.fetchFollowing(username)
        }
    }

    private fun showRecyclerView() {
        recyclerView.visibility = View.VISIBLE
        loadingView.visibility = View.GONE
        errorView.visibility = View.GONE
    }

    private fun showEmptyView() {
        recyclerView.visibility = View.GONE
        loadingView.visibility = View.GONE
        errorView.visibility = View.VISIBLE
    }

    private fun showErrorView() {
        recyclerView.visibility = View.GONE
        loadingView.visibility = View.GONE
        errorView.visibility = View.VISIBLE
    }

    private fun showLoading() {
        recyclerView.visibility = View.GONE
        loadingView.visibility = View.VISIBLE
        errorView.visibility = View.GONE

    }

    companion object {
        const val ARG_POSITION = "position"
        const val ARG_USERNAME = "username"
    }

}
