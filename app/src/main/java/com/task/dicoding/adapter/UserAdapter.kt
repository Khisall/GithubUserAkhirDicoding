package com.task.dicoding.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.task.dicoding.data.response.User
import com.task.dicoding.databinding.ListUserBinding
import com.task.dicoding.ui.DetailUser

class UserAdapter : ListAdapter<User, UserAdapter.MyViewHolder>(DIFF_CALLBACK){

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<User>(){
            override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAdapter.MyViewHolder {
        val binding = ListUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserAdapter.MyViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)

        // Menambahkan Aksi Tekan
        holder.itemView.setOnClickListener {
            val intent = Intent(it.context, DetailUser::class.java)
            intent.putExtra("username", user.login)
            it.context.startActivity(intent)
        }
    }

    class MyViewHolder(private val binding: ListUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            binding.tvUsername.text = user.login

            Glide.with(binding.root)
                .load(user.avatarUrl)
                .into(binding.tvProfilePicture)
        }
    }
}