package com.example.githubuserplus.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import com.bumptech.glide.Glide
import com.example.githubuserplus.R
import com.example.githubuserplus.utils.User
import com.example.githubuserplus.databinding.ActivityDetailUserBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class DetailUser : AppCompatActivity() {

    private lateinit var binding: ActivityDetailUserBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getDetailUser()

    }

    private fun getDetailUser(){
        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        binding.viewPager.adapter = sectionsPagerAdapter
        binding.progressBar.visibility = View.VISIBLE

        val user = intent.getParcelableExtra<User>(EXTRA_USER) as User
        val client = AsyncHttpClient()

        client.addHeader("Authorization", "token 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")
        client.addHeader("User-Agent", "request")

        sectionsPagerAdapter.followersUrl = user.followers
        sectionsPagerAdapter.followingUrl = user.following

        client.get(user.url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
                // Jika koneksi berhasil
                binding.progressBar.visibility = View.INVISIBLE

                val result = String(responseBody)
                Log.d(TAG, result)
                try {
                    val jsonObject = JSONObject(result)
                    val photo = jsonObject.getString("avatar_url")
                    val username = jsonObject.getString("login")
                    val name = jsonObject.getString("name")
                    val company = jsonObject.getString("company")
                    val location = jsonObject.getString("location")
                    val followers = jsonObject.getString("followers")
                    val following = jsonObject.getString("following")

                    TabLayoutMediator(binding.tabs, binding.viewPager){ tab, position ->
                        when (position){
                            0 -> tab.text = resources.getString(TAB_TITLES[position]) + " (" + followers + ")"
                            1 -> tab.text = resources.getString(TAB_TITLES[position]) + " (" + following + ")"
                        }

                    }.attach()

                    Glide.with(binding.imgItemPhotoDetail)
                        .load(photo)
                        .circleCrop()
                        .into(binding.imgItemPhotoDetail)
                    binding.tvItemUsernameDetail.text = username
                    binding.tvItemNameDetail.text = name
                    binding.tvItemCompanyDetail.text = company
                    binding.tvItemLocationDetail.text = location

                } catch (e: Exception) {
                    Toast.makeText(this@DetailUser, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                // Jika koneksi gagal
                binding.progressBar.visibility = View.INVISIBLE
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Toast.makeText(this@DetailUser, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }


    companion object {
        const val EXTRA_USER = "extra_user"
        private val TAG = DetailUser::class.java.simpleName

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_title_1,
            R.string.tab_title_2
        )
    }

}