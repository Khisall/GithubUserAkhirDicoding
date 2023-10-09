package com.task.dicoding.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.task.dicoding.R
import com.task.dicoding.adapter.FavoriteAdapter
import com.task.dicoding.adapter.UserAdapter
import com.task.dicoding.database.AppDatabase
import com.task.dicoding.database.FavoriteRepository
import com.task.dicoding.database.FavoriteUser
import com.task.dicoding.databinding.ActivityFavoriteBinding
import com.task.dicoding.viewmodel.FavoriteViewModel
import com.task.dicoding.viewmodel.FavoriteViewModelFactory

class FavoriteActivity : AppCompatActivity() {

    private lateinit var adapter: FavoriteAdapter
    private lateinit var favoriteViewModel: FavoriteViewModel
    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val recyclerView = findViewById<RecyclerView>(R.id.rvFavorite)
        val progressBar = findViewById<View>(R.id.progressbar_favorite)

        adapter = FavoriteAdapter()
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // Inisialisasi AppDatabase
        database = AppDatabase.getDatabase(this)

        val repository = FavoriteRepository(database.favoriteUserDao())
        favoriteViewModel = ViewModelProvider(
            this,
            FavoriteViewModelFactory(repository)
        )[FavoriteViewModel::class.java]

        favoriteViewModel.favoriteUsers.observe(this) { users ->
            // Tidak perlu menginisialisasi FavoriteUser dengan data statis di sini

            // Logging data pengguna favorit
            users.forEach { favoriteUser ->
                Log.d("FavoriteUser", users.toString())
            }

            // Menyampaikan data pengguna favorit ke adapter
            adapter.submitList(users)
        }
        progressBar.visibility = View.GONE
    }
    private fun showSelectedUser(user: FavoriteUser) {
        val intentToDetail = Intent(this@FavoriteActivity, DetailUser::class.java)
        intentToDetail.putExtra("DATA", user.username)
        startActivity(intentToDetail)
    }
}
