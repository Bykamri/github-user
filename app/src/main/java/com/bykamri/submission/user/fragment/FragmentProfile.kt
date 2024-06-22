package com.bykamri.submission.user.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bykamri.submission.databinding.FragmentProfileBinding
import com.bykamri.submission.server.modelview.ModelViewFactory
import com.bykamri.submission.server.modelview.ModelViewMain
import com.bykamri.submission.server.utils.SettingUtils
import com.bykamri.submission.server.utils.dataStore

class FragmentProfile : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var viewModel: ModelViewMain

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(layoutInflater)
        val pref = SettingUtils.getInstance(requireContext().dataStore)
        viewModel = ViewModelProvider(
            this, ModelViewFactory.getInstance(pref, requireActivity().application)
        )[ModelViewMain::class.java]


        if (viewModel.userDetail.value == null) {
            viewModel.getUserDetail("Bykamri")
        }

        viewModel.getThemeSettings().observe(viewLifecycleOwner) { isDarkModeActive ->
            AppCompatDelegate.setDefaultNightMode(if (isDarkModeActive) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO)
            binding.switchDarkMode.isChecked = isDarkModeActive
        }

        binding.switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            viewModel.saveThemeSettings(isChecked)
        }
        loadingObserver()
        renderProfile()
        return binding.root
    }

    private fun loadingObserver() {
        viewModel.isLoading.observe(viewLifecycleOwner) {
            if (it) {
                binding.loadingLayout.loadingScreen.visibility = View.VISIBLE
            } else {
                binding.loadingLayout.loadingScreen.visibility = View.GONE
            }
        }
    }

    private fun renderProfile() {
        viewModel.userDetail.observe(viewLifecycleOwner) { userDetail ->
            if (userDetail != null) {
                with(binding.header) {
                    Glide.with(this@FragmentProfile).load(userDetail.avatarUrl).circleCrop()
                        .into(civAvatar)
                    tvName.text = userDetail.name
                    tvUsername.text = userDetail.login
                    tvWork.text = userDetail.company
                    tvLocation.text = userDetail.location
                    tvDesc.text = userDetail.bio
                    tvRepos.text = userDetail.publicRepos
                    tvFollowers.text = userDetail.followers
                    tvFollowing.text = userDetail.following
                    btnShare.visibility = View.GONE
                    btnAddFavorite.visibility = View.GONE
                }
            }
        }
    }
}