package com.example.githubuserplus.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuserplus.BuildConfig
import com.example.githubuserplus.utils.User
import com.example.githubuserplus.databinding.FragmentFollowingBinding
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray

class FollowingFragment : Fragment() {

    private lateinit var binding: FragmentFollowingBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentFollowingBinding.inflate(inflater, container, false)
        val view: View = binding.root

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = LinearLayoutManager(activity)
        binding.rvFollowing.setLayoutManager(layoutManager)

        getFollowList()
    }



    fun getFollowList(){
        binding.progressBar.visibility = View.VISIBLE
        val client = AsyncHttpClient()
        client.addHeader("Authorization", BuildConfig.Authorization)
        client.addHeader("User-Agent", "request")
        var url = arguments?.getString(ARG_NAME)
        if (url != null) {
            url = url.dropLast(13)
            Log.i("linkFollowing", url)
        }
        else {
            Log.i("link following", "yahh null cok")
        }
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
                // Jika koneksi berhasil
                binding.progressBar.visibility = View.INVISIBLE

                val listUser = ArrayList<User>()
                val result = String(responseBody)
                Log.d(TAG, result)
                try {
                    val jsonArray = JSONArray(result)
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val photo = jsonObject.getString("avatar_url")
                        val username = jsonObject.getString("login")
                        val followers = jsonObject.getString("followers_url")
                        val following = jsonObject.getString("following_url")
                        val userUrl = jsonObject.getString("url")

                        val user = User(username, username, photo, "Repo", followers, following, "Company", "Location", userUrl, false)
                        listUser.add(user)
                    }
                    val adapter = ListUserAdapter(listUser)
                    binding.rvFollowing.adapter = adapter
                    Log.d("KEIKOOO", "$listUser")
                    adapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
                        override fun onItemClicked(data: User){
                            val moveToDetailUser = Intent(activity, DetailUser::class.java)
                            moveToDetailUser.putExtra(DetailUser.EXTRA_USER, data)
                            Log.i("SAKIII", "$data")
                            startActivity(moveToDetailUser)
                        }
                    })
                } catch (e: Exception) {
                    Toast.makeText(activity, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray,
                error: Throwable
            ) {
                // Jika koneksi gagal
                binding.progressBar.visibility = View.INVISIBLE
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }

    companion object {
        private val TAG = FollowingFragment::class.java.simpleName
        const val ARG_NAME = "followingUrl"
    }
}