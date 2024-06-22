package com.bykamri.submission.user.fragment

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bykamri.submission.user.adapter.SearchUserAdapter
import com.bykamri.submission.databinding.FragmentFollowUserBinding
import com.bykamri.submission.user.activity.DetailUserActivity
import com.bykamri.submission.server.model.FollowUsers
import com.bykamri.submission.server.model.GithubUser
import com.bykamri.submission.server.modelview.ModelViewFactory
import com.bykamri.submission.server.modelview.ModelViewMain
import com.bykamri.submission.server.utils.SettingUtils
import com.bykamri.submission.server.utils.dataStore

class FragmentFollowUser(private val type: String) : Fragment() {

    private lateinit var binding: FragmentFollowUserBinding
    private lateinit var viewModel: ModelViewMain
    private val adapter: SearchUserAdapter = SearchUserAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowUserBinding.inflate(layoutInflater)
        val pref = SettingUtils.getInstance(requireContext().dataStore)
        viewModel = ViewModelProvider(
            this, ModelViewFactory(pref, requireActivity().application)
        )[ModelViewMain::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        loadingObserver()
    }

    private fun setupRecyclerView() {
        binding.rvUser.adapter = adapter
        if (type == "followers") {
            viewModel.getUserFollowers(DetailUserActivity.username)
            viewModel.userFollowers.observe(viewLifecycleOwner) { followers ->
                if (!followers.isNullOrEmpty()) {
                    renderer(convertToItems(followers))
                }
            }
        } else {
            viewModel.getUserFollowings(DetailUserActivity.username)
            viewModel.userFollowing.observe(viewLifecycleOwner) { following ->
                if (!following.isNullOrEmpty()) {
                    renderer(convertToItems(following))
                }
            }
        }

        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.rvUser.layoutManager = GridLayoutManager(context, 2)
        } else {
            binding.rvUser.layoutManager = LinearLayoutManager(context)
        }

        binding.rvUser.adapter = adapter
        adapter.setOnItemClickCallback { items -> selectedUser(items) }
    }


    private fun selectedUser(data: GithubUser) {
        val intent = Intent(activity, DetailUserActivity::class.java)
        intent.putExtra(DetailUserActivity.EXTRA_USERNAME, data.login)
        intent.putExtra(DetailUserActivity.EXTRA_TYPE, data.type)
        startActivity(intent)
    }

    private fun convertToItems(following: ArrayList<FollowUsers>): ArrayList<GithubUser> {
        val listItems = ArrayList<GithubUser>()
        for (item in following) {
            val items = GithubUser(
                login = item.login,
                avatarUrl = item.avatarUrl,
                htmlUrl = item.htmlUrl,
                type = item.type
            )
            listItems.add(items)
        }
        return listItems
    }

    private fun renderer(follow: ArrayList<GithubUser>?) {
        if (!follow.isNullOrEmpty()) {
            binding.emptyLayout.emptyScreen.visibility = View.GONE
            binding.rvUser.visibility = View.VISIBLE
            adapter.setData(follow)
        } else {
            binding.emptyLayout.emptyScreen.visibility = View.VISIBLE
            binding.rvUser.visibility = View.GONE
        }
    }

    private fun loadingObserver() {
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.loadingLayout.loadingScreen.visibility = View.VISIBLE
            } else {
                binding.loadingLayout.loadingScreen.visibility = View.GONE
            }
        }
    }
}