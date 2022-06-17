package com.example.githubuserplus.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuserplus.R
import com.example.githubuserplus.databinding.ActivityMainBinding
import com.example.githubuserplus.data.Result
import com.example.githubuserplus.data.local.entity.UserEntity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)

        val pref = SettingPreferences.getInstance(dataStore)
        val settingViewModel = ViewModelProvider(this, ViewModelFactory(pref)).get(
            SettingViewModel::class.java
        )

        settingViewModel.getThemeSettings().observe(
            this
        ) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        val layoutManager = LinearLayoutManager(this)
        binding.rvUsers.setLayoutManager(layoutManager)

        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvUsers.addItemDecoration(itemDecoration)

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

        binding.rvUsers.adapter = roomUserAdapter

        binding.progressBar.visibility = View.GONE

        binding.btnSend.setOnClickListener { view ->
            val text = binding.edReview.text.toString()
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
            binding.tvCariUser.visibility = View.GONE
            viewModel.getRoomUser(text).observe(this) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }
                        is Result.Success -> {
                            binding.progressBar.visibility = View.GONE
                            val userData = result.data
                            roomUserAdapter.submitList(userData)
                        }
                        is Result.Error -> {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(
                                this,
                                "Terjadi kesalahan" + result.error,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }

        roomUserAdapter.setOnItemClickCallback(object : RoomUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: UserEntity){
                val moveToDetailUser = Intent(this@MainActivity, DetailUser::class.java)
                moveToDetailUser.putExtra(DetailUser.EXTRA_USER, data.userUrl)
                Log.i("SAKIII", "$data")
                startActivity(moveToDetailUser)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_setting -> {
                val moveToSettingActivity = Intent(this, SettingActivity::class.java)
                startActivity(moveToSettingActivity)
                true
            }
            R.id.menu_starred -> {
                val moveToStarredActivity = Intent(this, StarredActivity::class.java)
                startActivity(moveToStarredActivity)
                true
            }
            else -> true
        }
    }

}