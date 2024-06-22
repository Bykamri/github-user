package com.bykamri.submission.user.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bykamri.submission.user.fragment.FragmentFollowUser

class DetailUserAdapter (activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int) = when (position) {
        0 -> FragmentFollowUser("followers")
        else -> FragmentFollowUser("following")
    }
}