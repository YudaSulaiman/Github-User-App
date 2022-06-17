package com.example.githubuserplus.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubuserplus.R
import com.example.githubuserplus.data.local.entity.UserEntity
import com.example.githubuserplus.databinding.ItemRowUsersBinding
import com.example.githubuserplus.ui.RoomUserAdapter.MyViewHolder
import com.example.githubuserplus.utils.User

class RoomUserAdapter(private val onStarredClick: (UserEntity) -> Unit) : ListAdapter<UserEntity, MyViewHolder>(DIFF_CALLBACK) {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemRowUsersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)

        val ivStarred = holder.binding.imgItemStarred
        if (user.isStarred) {
            ivStarred.setImageDrawable(ContextCompat.getDrawable(ivStarred.context, R.drawable.ic_baseline_star_24))
        } else {
            ivStarred.setImageDrawable(ContextCompat.getDrawable(ivStarred.context, R.drawable.ic_baseline_star_border_24))
        }
        ivStarred.setOnClickListener {
            onStarredClick(user)
        }
    }

    class MyViewHolder(val binding: ItemRowUsersBinding) : RecyclerView.ViewHolder(
        binding.root
    ) {
        fun bind(user: UserEntity) {
            binding.tvItemUsername.text = user.username
            binding.tvItemName.text = user.name
            Glide.with(itemView.context)
                .load(user.photo)
                .circleCrop()
                .into(binding.imgItemPhoto)
            itemView.setOnClickListener{
                val moveToDetailUser = Intent(itemView.context, DetailUser::class.java)
                val userAttribute = User(user.username, user.name, user.photo, "Repo", user.followers_url,
                    user.following_url, "Company", "Location", user.userUrl, user.isStarred)
                moveToDetailUser.putExtra(DetailUser.EXTRA_USER, userAttribute)
                Log.i("SAKIII", "$user")
                itemView.context.startActivity(moveToDetailUser)
            }
        }
    }


    interface OnItemClickCallback {
        fun onItemClicked(data: UserEntity)
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<UserEntity> =
            object : DiffUtil.ItemCallback<UserEntity>() {
                override fun areItemsTheSame(oldUser: UserEntity, newUser: UserEntity): Boolean {
                    return oldUser.username == newUser.username
                }

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(oldUser: UserEntity, newUser: UserEntity): Boolean {
                    return oldUser == newUser
                }
            }
    }
}