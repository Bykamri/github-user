package com.bykamri.submission.server.network

import com.bykamri.submission.server.model.DetailUser
import com.bykamri.submission.server.model.FollowUsers
import com.bykamri.submission.server.model.SearchUser
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface NetworkService {

    @GET("search/users")
    fun searchUser(
        @Query("q") username: String
    ): Call<SearchUser>

    @GET("users/{username}")
    fun getDetailUser(
        @Path("username") username: String
    ): Call<DetailUser>

    @GET("users/{username}/followers")
    fun getFollowersUser(
        @Path("username") username: String
    ): Call<ArrayList<FollowUsers>>

    @GET("users/{username}/following")
    fun getFollowingUser(
        @Path("username") username: String
    ): Call<ArrayList<FollowUsers>>

}