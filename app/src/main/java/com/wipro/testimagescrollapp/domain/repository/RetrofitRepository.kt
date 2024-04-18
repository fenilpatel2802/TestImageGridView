package com.wipro.testimagescrollapp.domain.repository

import com.demo.mvvm.utils.BaseApiResponse
import com.wipro.shopease.utils.Resource
import com.wipro.testimagescrollapp.common.Const.API_CLIENT_ID
import com.wipro.testimagescrollapp.domain.model.ImageListResponse
import com.wipro.testimagescrollapp.domain.rest.RetrofitService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class RetrofitRepository @Inject constructor(
    private val service: RetrofitService
) : BaseApiResponse() {

    // product list
    suspend fun getImageListApi(
        page: Int,
        perPage: Int
    ): Flow<Resource<List<ImageListResponse>>> {
        return flow {
            emit(safeApiCall {
                service.getImageList(API_CLIENT_ID, page, perPage)
            })
        }.flowOn(Dispatchers.IO)
    }

}