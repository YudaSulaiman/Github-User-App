package com.example.githubuserplus.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuserplus.data.local.entity.UserEntity
import com.example.githubuserplus.databinding.ActivityStarredBinding

class StarredActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStarredBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityStarredBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val layoutManager = LinearLayoutManager(this)
        binding.rvStarred.setLayoutManager(layoutManager)

        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvStarred.addItemDecoration(itemDecoration)

        // nyoba room
        val factory: UserViewModelFactory = UserViewModelFactory.getInstance(this)
        val viewModel by viewModels<RoomUserViewModel>{
            factory
        }

        val roomUserAdapter = RoomUserAdapter { user ->
            if (user.isStarred){
                viewModel.deleteUser(user)
            } else {
                viewModel.saveUser(user)
            }
        }

        binding.rvStarred.adapter = roomUserAdapter

        viewModel.getStarredUser().observe(this) { starredUser ->
            binding.progressBar.visibility = View.GONE
            roomUserAdapter.submitList(starredUser)
        }

        roomUserAdapter.setOnItemClickCallback(object : RoomUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: UserEntity){
                val moveToDetailUser = Intent(this@StarredActivity, DetailUser::class.java)
                moveToDetailUser.putExtra(DetailUser.EXTRA_USER, data.userUrl)
                Log.i("SAKIII", "$data")
                startActivity(moveToDetailUser)
            }
        })
    }
}