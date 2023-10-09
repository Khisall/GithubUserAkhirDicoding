package com.task.dicoding.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.task.dicoding.R
import com.task.dicoding.adapter.SectionsPagerAdapter
import com.task.dicoding.data.response.User
import com.task.dicoding.data.retrofit.ApiService
import com.task.dicoding.database.FavoriteUser
import com.task.dicoding.databinding.DetailUserBinding
import com.task.dicoding.viewmodel.DetailUserViewModel
import com.task.dicoding.viewmodel.DetailUserViewModelFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DetailUser : AppCompatActivity() {

    private val _name = MutableLiveData<String?>()
    val name: LiveData<String?> = _name

    private lateinit var binding: DetailUserBinding
    private lateinit var username: String
    private lateinit var viewModel: DetailUserViewModel
    private lateinit var progressBar: ProgressBar
    private var user: User? = null // Menggunakan tipe User? (nullable)
    private var favoriteUser: FavoriteUser? = null
    private var favoriteUsersList: List<FavoriteUser> =
        emptyList() // Variabel untuk menyimpan daftar pengguna favorit

    object ApiServiceFactory {
        private const val BASE_URL = "https://api.github.com/"

        fun create(): ApiService {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(ApiService::class.java)
        }
    }

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.followers,
            R.string.following
        )

        const val EXTRA_LOGIN = "extra_login"
        const val EXTRA_USERNAME = "extra_username"
        const val EXTRA_AVATAR_URL = "extra_avatar_url"

    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        username = intent.getStringExtra("username") ?: ""

        progressBar = findViewById(R.id.progressbar_detail)
        viewModel = ViewModelProvider(this)[DetailUserViewModel::class.java]
        viewModel.fetchUserDetail(username)

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        sectionsPagerAdapter.username = username // Kirim data username ke adapter

        val viewPager: ViewPager2 = findViewById(R.id.viewpager)
        viewPager.adapter = sectionsPagerAdapter

        val tabs: TabLayout = findViewById(R.id.tabs)

        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        supportActionBar?.hide()

        viewModel.user.observe(this) { fetchedUser ->
            if (fetchedUser != null) {
                user = fetchedUser // Inisialisasi user saat data user tersedia
                Log.d("AvatarUrl", "Avatar URL: ${user?.avatarUrl}")
            }
            binding.apply {
                tvName.text = user?.name
                tvUsername.text = user?.login
                tvFollower.text =
                    "Followers: ${user?.followers}" // Ganti dengan field yang sesuai dari User
                tvFollowing.text =
                    "Following: ${user?.following}" // Ganti dengan field yang sesuai dari User

                // Sembunyikan ProgressBar setelah data user dimuat
                progressBar.visibility = View.GONE

                if (user != null) {
                    Glide.with(this@DetailUser)
                        .load(user?.avatarUrl)
                        .into(tvProfilePicture)
                }
            }
            // Sembunyikan ProgressBar setelah data user dimuat
            progressBar.visibility = View.GONE
        }

        viewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) {
                progressBar.visibility = View.VISIBLE // Tampilkan ProgressBar saat isLoading true
            } else {
                progressBar.visibility = View.GONE // Sembunyikan ProgressBar saat isLoading false
            }
        }
        viewModel = ViewModelProvider(
            this,
            DetailUserViewModelFactory(application)
        )[DetailUserViewModel::class.java]

        // Tambahkan LiveData untuk data favorit
        val favoriteUserLiveData = viewModel.getFavoriteUser(username)
        favoriteUserLiveData.observe(this) { favUser ->
            favoriteUser = favUser
            updateFavoriteUsersList()
            updateFavoriteButton()
        }
    }

    private fun updateFavoriteButton() {

        val favoriteButton = findViewById<FloatingActionButton>(R.id.btnFavorite)

        // Set OnClickListener untuk tombol Favorite
        favoriteButton.setOnClickListener {
            if (favoriteUser in favoriteUsersList) {
                // Hapus data favorit

                viewModel.deleteFavoriteUser(favoriteUser ?: FavoriteUser(username, avatarUrl = user?.avatarUrl))
                favoriteButton.setImageResource(R.drawable.baseline_favorite_border_24)
            } else {
                // Tambahkan data favorit
                viewModel.insertFavoriteUser(favoriteUser ?: FavoriteUser(username, avatarUrl = user?.avatarUrl))
                favoriteButton.setImageResource(R.drawable.baseline_favorite_24)
            }
            updateFavoriteUsersList()
        }

        // Perbarui ikon Favorite sesuai status
        if (favoriteUser in favoriteUsersList) {
            favoriteButton.setImageResource(R.drawable.ic_baseline_favorite_24)
        } else {
            favoriteButton.setImageResource(R.drawable.baseline_favorite_border_24)
        }
    }

    private fun updateFavoriteUsersList() {
        val favoriteUserLiveData = viewModel.getAllFavoriteUsers()
        favoriteUserLiveData.observe(this) { favoriteUsers ->
            favoriteUsersList = favoriteUsers
            updateFavoriteButton()
        }
    }
}