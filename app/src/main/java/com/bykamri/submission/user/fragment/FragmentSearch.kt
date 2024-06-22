package com.bykamri.submission.user.fragment

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bykamri.submission.user.activity.DetailUserActivity
import com.bykamri.submission.user.adapter.SearchUserAdapter
import com.bykamri.submission.databinding.FragmentSearchBinding
import com.bykamri.submission.server.modelview.ModelViewFactory
import com.bykamri.submission.server.modelview.ModelViewMain
import com.bykamri.submission.server.utils.SettingUtils
import com.bykamri.submission.server.utils.dataStore

class FragmentSearch : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var viewModel: ModelViewMain
    private val adapter: SearchUserAdapter by lazy { SearchUserAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(layoutInflater)
        val pref = SettingUtils.getInstance(requireContext().dataStore)
        viewModel = ViewModelProvider(
            this, ModelViewFactory.getInstance(pref, requireActivity().application)
        )[ModelViewMain::class.java]

        checkSearchResult()
        setupSearchView()
        loadingObserver()

        return binding.root
    }

    private fun setupSearchView() {
        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView.editText.setOnEditorActionListener { _, _, _ ->
                searchBar.setText(searchView.text)
                viewModel.searchUser(searchView.text.toString())
                searchView.hide()
                checkSearchResult()
                false
            }
        }
    }


    private fun checkSearchResult() {
        viewModel.searchList.observe(viewLifecycleOwner) { searchResult ->
            if (!searchResult.isNullOrEmpty()) {
                binding.emptyLayout.emptyScreen.visibility = View.GONE
                binding.rvUser.visibility = View.VISIBLE
                adapter.setData(searchResult)
                renderRecyclerView()
            } else {
                binding.emptyLayout.emptyScreen.visibility = View.VISIBLE
                binding.rvUser.visibility = View.GONE
            }
        }
    }

    private fun renderRecyclerView() {
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.rvUser.layoutManager = GridLayoutManager(requireContext(), 2)
        } else {
            binding.rvUser.layoutManager = LinearLayoutManager(requireContext())
        }
        binding.rvUser.visibility = View.VISIBLE
        binding.rvUser.adapter = adapter
        adapter.setOnItemClickCallback {
            val intent = Intent(requireContext(), DetailUserActivity::class.java)
            intent.putExtra(DetailUserActivity.EXTRA_USERNAME, it.login)
            intent.putExtra(DetailUserActivity.EXTRA_TYPE, it.type)
            startActivity(intent)
        }
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
}