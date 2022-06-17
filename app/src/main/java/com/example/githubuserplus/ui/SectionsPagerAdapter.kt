package com.example.githubuserplus.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class SectionsPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {

    var followersUrl: String = ""
    var followingUrl: String = ""

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> {
                fragment = FollowersFragment()
                fragment.arguments = Bundle().apply {
                    putString(FollowersFragment.ARG_NAME, followersUrl)
                }
            }

            1 -> {
                fragment = FollowingFragment()
                fragment.arguments = Bundle().apply {
                    putString(FollowingFragment.ARG_NAME, followingUrl)
                }
            }

        }
        return fragment as Fragment
    }


    override fun getItemCount(): Int {
        return 2
    }

}