package com.bykamri.submission.server.modelview

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.bykamri.submission.server.model.DetailUser
import com.bykamri.submission.server.model.FollowUsers
import com.bykamri.submission.server.model.GithubUser
import com.bykamri.submission.server.model.SearchUser
import com.bykamri.submission.server.network.NetworkConfig
import com.bykamri.submission.server.utils.SettingUtils
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ModelViewMain (private val pref: SettingUtils) : ViewModel() {

    private val _searchList = MutableLiveData<ArrayList<GithubUser>?>(null)
    val searchList: LiveData<ArrayList<GithubUser>?> = _searchList

    private val _userDetail = MutableLiveData<DetailUser?>(null)
    val userDetail: LiveData<DetailUser?> = _userDetail

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _userFollowers = MutableLiveData<ArrayList<FollowUsers>?>(null)
    val userFollowers: LiveData<ArrayList<FollowUsers>?> = _userFollowers

    private val _userFollowing = MutableLiveData<ArrayList<FollowUsers>?>(null)
    val userFollowing: LiveData<ArrayList<FollowUsers>?> = _userFollowing

    fun searchUser(username: String) {
        Log.d("MainViewModel", "Searching for user $username")
        _isLoading.value = true
        val client = NetworkConfig.getNetworkService().searchUser(username)
        client.enqueue(object : Callback<SearchUser> {
            override fun onResponse(
                call: Call<SearchUser>,
                response: Response<SearchUser>
            ) {
                val responseBody = response.body()
                Log.d("MainViewModel", "onResponse: $responseBody")
                if (response.isSuccessful && responseBody != null) {
                    _searchList.value = ArrayList(responseBody.items)
                    _isLoading.value = false
                } else {
                    _isLoading.value = false
                }
            }

            override fun onFailure(call: Call<SearchUser>, t: Throwable) {
                _isLoading.value = false
                Log.e("MainViewModel", "onFailure: ${t.message}")
            }
        })
    }

    fun getUserDetail(username: String) {
        _isLoading.value = true
        val client = NetworkConfig.getNetworkService().getDetailUser(username)
        client.enqueue(object : Callback<DetailUser> {
            override fun onResponse(
                call: Call<DetailUser>,
                response: Response<DetailUser>
            ) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    _userDetail.value = responseBody
                    _isLoading.value = false
                }
            }

            override fun onFailure(call: Call<DetailUser>, t: Throwable) {
                _isLoading.value = false
                Log.e("MainViewModel", "onFailure: ${t.message}")
            }
        })
    }

    fun getUserFollowers(username: String) {
        _isLoading.value = true
        val client = NetworkConfig.getNetworkService().getFollowersUser(username)
        client.enqueue(object : Callback<ArrayList<FollowUsers>> {
            override fun onResponse(
                call: Call<ArrayList<FollowUsers>>,
                response: Response<ArrayList<FollowUsers>>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    _userFollowers.value = response.body()
                    _isLoading.value = false
                }
            }

            override fun onFailure(call: Call<ArrayList<FollowUsers>>, t: Throwable) {
                _isLoading.value = false
                Log.e("MainViewModel", "onFailure: ${t.message}")
            }
        })
    }

    fun getUserFollowings(username: String) {
        _isLoading.value = true
        val client = NetworkConfig.getNetworkService().getFollowingUser(username)
        client.enqueue(object : Callback<ArrayList<FollowUsers>> {
            override fun onResponse(
                call: Call<ArrayList<FollowUsers>>,
                response: Response<ArrayList<FollowUsers>>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    _userFollowing.value = response.body()
                    _isLoading.value = false
                }
            }

            override fun onFailure(call: Call<ArrayList<FollowUsers>>, t: Throwable) {
                _isLoading.value = false
                Log.e("MainViewModel", "onFailure: ${t.message}")
            }
        })
    }

    fun getThemeSettings(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }

    fun saveThemeSettings(isDarkModeActive: Boolean) {
        viewModelScope.launch { pref.saveThemeSetting(isDarkModeActive) }
    }
}