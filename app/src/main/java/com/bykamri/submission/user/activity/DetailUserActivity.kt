package com.bykamri.submission.user.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bykamri.submission.R
import com.bykamri.submission.user.adapter.DetailUserAdapter
import com.bykamri.submission.databinding.ActivityDetailUserBinding
import com.bykamri.submission.server.db.UserFavourite
import com.bykamri.submission.server.model.DetailUser
import com.bykamri.submission.server.modelview.ModelViewFactory
import com.bykamri.submission.server.modelview.ModelViewMain
import com.bykamri.submission.server.modelview.ModelViewUserFavourite
import com.bykamri.submission.server.utils.SettingUtils
import com.bykamri.submission.server.utils.dataStore
import com.google.android.material.tabs.TabLayoutMediator

class DetailUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailUserBinding
    private lateinit var viewModel: ModelViewMain
    private lateinit var favUserViewModel: ModelViewUserFavourite
    private var inFav: Boolean = false
    private var shareText: String = String()
    private var toast: Toast? = null

    companion object {
        const val EXTRA_USERNAME = "extra_username"
        const val EXTRA_TYPE = "extra_type"
        var username = String()
        var type = String()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        setupViewModels()
        setupUsernameAndType()
        setupUserDetailAdapter()
        setupActionBar()
        setupFavoriteUserObserver()

        loadingObserver()
        renderGivenData()
    }

    private fun setupViewModels() {
        val pref = SettingUtils.getInstance(application.dataStore)
        val factory = ModelViewFactory.getInstance(pref, application)
        viewModel = ViewModelProvider(this, factory)[ModelViewMain::class.java]
        favUserViewModel = ViewModelProvider(this, factory)[ModelViewUserFavourite::class.java]
    }

    private fun setupUsernameAndType() {
        username = intent.getStringExtra(EXTRA_USERNAME).toString()
        type = intent.getStringExtra(EXTRA_TYPE).toString()
    }

    private fun setupUserDetailAdapter() {
        val userDetailAdapter = DetailUserAdapter(this)
        binding.viewPager.adapter = userDetailAdapter
        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Followers"
                1 -> "Following"
                else -> throw IllegalArgumentException("Unknown tab position: $position")
            }
        }.attach()
    }

    private fun setupActionBar() {
        supportActionBar?.apply {
            title = username
            setDisplayHomeAsUpEnabled(true)
            setHomeButtonEnabled(true)
        }
    }

    private fun setupFavoriteUserObserver() {
        favUserViewModel.checkFavoriteUser(username).observe(this) { isFavorite ->
            inFav = isFavorite
            val color = if (isFavorite) {
                R.color.colorInfo
            } else {
                R.color.colorNeutralVariant
            }

            with(binding.toolbar) {
                btnAddFavorite.setBackgroundColor(resources.getColor(color, theme))
            }

        }
    }


    private fun renderGivenData() {
        viewModel.getUserDetail(username)
        viewModel.userDetail.observe(this) { detailUser ->
            detailUser?.let {
                setupToolbar(it)
                setupShareButton(it)
                setupFavoriteButton(it)
            }
        }

        setupBackButton()
        setupToolbarTitle()
    }

    private fun setupFavoriteButton(detailUser: DetailUser) {
        with(binding.toolbar) {
            btnAddFavorite.setOnClickListener {
                val favoriteUser = UserFavourite(
                    username = detailUser.login,
                    name = detailUser.name ?: "no name",
                    type = detailUser.type,
                    pictureUrl = detailUser.avatarUrl
                )
                if (inFav) {
                    favUserViewModel.delete(favoriteUser)
                    showToast("$username has been deleted from favourite")
                } else {
                    favUserViewModel.insert(favoriteUser)
                    showToast("$username has been added from favourite")
                }
            }
        }
    }

    private fun showToast(message: String) {
        toast?.cancel()
        toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
        toast?.show()
    }

    private fun setupToolbar(detailUser: DetailUser) {
        with(binding.toolbar) {
            Glide.with(this@DetailUserActivity)
                .load(detailUser.avatarUrl)
                .circleCrop()
                .into(civAvatar)

            if (detailUser.company.isNullOrEmpty()) {
                tvWork.visibility = View.GONE
            }
            if (detailUser.location.isNullOrEmpty()) {
                tvLocation.visibility = View.GONE
            }
            if (detailUser.bio.isNullOrEmpty()) {
                tvDesc.visibility = View.GONE
            }

            tvUsername.text = detailUser.login
            tvName.text = detailUser.name ?: detailUser.login
            tvLocation.text = detailUser.location
            tvDesc.text = detailUser.bio
            tvWork.text = detailUser.company
            tvFollowers.text = detailUser.followers
            tvFollowing.text = detailUser.following
            tvRepos.text = detailUser.publicRepos
        }
    }

    private fun setupShareButton(detailUser: DetailUser) {
        with(binding.toolbar) {
            shareText = "Check out ${detailUser.name} on GitHub\n\n${detailUser.htmlUrl}"

            btnShare.setOnClickListener {
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, shareText)
                    type = "text/plain"
                }
                val shareIntent = Intent.createChooser(sendIntent, null)
                startActivity(shareIntent)
            }
        }
    }


    private fun setupBackButton() {
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun setupToolbarTitle() {
        binding.tvTitleToolbar.text = username
    }


    private fun loadingObserver() {
        viewModel.isLoading.observe(this) {
            if (it) {
                binding.loadingLayout.loadingScreen.visibility = View.VISIBLE
            } else {
                binding.loadingLayout.loadingScreen.visibility = View.GONE
            }
        }
    }
}