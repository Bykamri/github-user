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
import com.bykamri.submission.user.adapter.UserFavouriteAdapter
import com.bykamri.submission.databinding.FragmentUserFavouriteBinding
import com.bykamri.submission.server.db.UserFavourite
import com.bykamri.submission.user.activity.DetailUserActivity
import com.bykamri.submission.server.modelview.ModelViewFactory
import com.bykamri.submission.server.modelview.ModelViewUserFavourite
import com.bykamri.submission.server.utils.SettingUtils
import com.bykamri.submission.server.utils.dataStore

class FragmentUserFavourite : Fragment() {

    private lateinit var binding: FragmentUserFavouriteBinding
    private lateinit var viewModel: ModelViewUserFavourite
    private val adapter: UserFavouriteAdapter = UserFavouriteAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserFavouriteBinding.inflate(layoutInflater)
        val pref = SettingUtils.getInstance(requireContext().dataStore)
        val factory = ModelViewFactory(pref, requireActivity().application)
        viewModel = ViewModelProvider(this, factory)[ModelViewUserFavourite::class.java]

        viewModel.getAll().observe(viewLifecycleOwner) { favoriteUser ->
            if (!favoriteUser.isNullOrEmpty()) {
                adapter.setData(favoriteUser as ArrayList<UserFavourite>)
                binding.rvUser.visibility = View.VISIBLE
                binding.emptyLayout.emptyScreen.visibility = View.GONE
            } else {
                binding.rvUser.visibility = View.GONE
                binding.emptyLayout.emptyScreen.visibility = View.VISIBLE
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
    }

    private fun setupRecyclerView() {

        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.rvUser.layoutManager = GridLayoutManager(requireContext(), 2)
        } else {
            binding.rvUser.layoutManager = LinearLayoutManager(requireContext())
        }
        binding.rvUser.visibility = View.VISIBLE
        binding.rvUser.adapter = adapter
        adapter.setOnItemClickCallback {
            val intent = Intent(requireContext(), DetailUserActivity::class.java)
            intent.putExtra(DetailUserActivity.EXTRA_USERNAME, it.username)
            intent.putExtra(DetailUserActivity.EXTRA_TYPE, it.type)
            startActivity(intent)
        }
    }
}