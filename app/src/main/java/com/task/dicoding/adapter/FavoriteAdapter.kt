package com.task.dicoding.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.task.dicoding.database.FavoriteUser
import com.task.dicoding.databinding.ItemFavoriteBinding
import com.task.dicoding.ui.DetailUser

class FavoriteAdapter : ListAdapter<FavoriteUser, FavoriteAdapter.FavoriteViewHolder>(DIFF_CALLBACK) {

    class FavoriteViewHolder(private val binding: ItemFavoriteBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(user: FavoriteUser){
            binding.tvUsername.text = user.username
            Log.d("FavoriteAdapter",user.avatarUrl.toString())
            Glide.with(binding.root)
                .load(user.avatarUrl)
                .into(binding.tvProfilePicture)
        }
    }
    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<FavoriteUser>() {
            override fun areItemsTheSame(oldItem: FavoriteUser, newItem: FavoriteUser): Boolean {
                return oldItem.username == newItem.username
            }

            override fun areContentsTheSame(oldItem: FavoriteUser, newItem: FavoriteUser): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = ItemFavoriteBinding.inflate(LayoutInflater.from(parent.context),
            parent, false)
        return FavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val favoriteUser = getItem(position)
        holder.bind(favoriteUser)

        // Menambahkan Aksi Tekan
        holder.itemView.setOnClickListener {
            val intent = Intent(it.context, DetailUser::class.java)
            intent.putExtra("username", favoriteUser.username)
            it.context.startActivity(intent)
        }
    }
}