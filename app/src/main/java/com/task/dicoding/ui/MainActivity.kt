package com.task.dicoding.ui

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.switchmaterial.SwitchMaterial
import com.task.dicoding.R
import com.task.dicoding.adapter.SettingPreferences
import com.task.dicoding.adapter.UserAdapter
import com.task.dicoding.databinding.ActivityMainBinding
import com.task.dicoding.setting.SettingActivity
import com.task.dicoding.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var userAdapter: UserAdapter
    //private lateinit var viewModel: MainViewModel


    private val viewModel by viewModels<MainViewModel> {
        MainViewModel.Factory(SettingPreferences(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        supportActionBar?.show()

        // Konfigurasi RecyclerView
        val layoutManager = LinearLayoutManager(this)
        binding.rvUser.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvUser.addItemDecoration(itemDecoration)

        // Inisialisasi UserAdapter
        userAdapter = UserAdapter()

        binding.rvUser.adapter = userAdapter

        // Memuat data pengguna saat aktivitas dimulai
        viewModel.searchUser("q")

        // Memuat data pengguna saat aktivitas dimulai
        viewModel.userList.observe(this) { users ->
            userAdapter.submitList(users)
        }

        viewModel.getTheme().observe(this) {
            if (it) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }


        // Inisialisasi SearchView
        val searchView = binding.searchMenu

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.searchUser(query ?: "")
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Implementasikan pencarian berdasarkan `newText` di sini
                return true
            }
        })

        // Observer untuk mengamati perubahan pada data pengguna
        viewModel.userList.observe(this) { users ->
            userAdapter.submitList(users)
        }

        // Observer untuk mengamati perubahan status loading
        viewModel.loading.observe(this) { isLoading ->
            showLoading(isLoading)
        }

        val topAppBar = findViewById<MaterialToolbar>(R.id.topAppBar)
        setSupportActionBar(topAppBar)

        topAppBar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_favorite -> {
                    val intent = Intent(this, FavoriteActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.setting -> {
                    val intent = Intent(this, SettingActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        // Menampilkan atau menyembunyikan ProgressBar sesuai dengan status isLoading
        if (isLoading) {
            binding.progressbarHome.visibility = View.VISIBLE
        } else {
            binding.progressbarHome.visibility = View.GONE
        }
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
            menuInflater.inflate(R.menu.top_bar_menu, menu)
            val isDarkMode = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES
            val favoriteIconResId = if (isDarkMode) R.drawable.ic_baseline_favorite_24 else R.drawable.baseline_favorite_24
            val settingsIconResId = if (isDarkMode) R.drawable.ic_setting_darkmode else R.drawable.ic_setting

            val menuFavorite = menu.findItem(R.id.action_favorite)
            val menuSettings = menu.findItem(R.id.setting)

            menuFavorite.setIcon(favoriteIconResId)
            menuSettings.setIcon(settingsIconResId)

            return true
        }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_favorite     -> {
                val intent = Intent(this, FavoriteActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.setting -> {
                val intent = Intent(this, SettingActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}