package com.wipro.testimagescrollapp.domain.rest

import com.wipro.testimagescrollapp.domain.model.ImageListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitService {

    // image list
    @GET("photos")
    suspend fun getImageList(
        @Query("client_id") clientId: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): Response<List<ImageListResponse>>



}