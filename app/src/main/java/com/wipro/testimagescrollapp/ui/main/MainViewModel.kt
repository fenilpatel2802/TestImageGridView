package com.wipro.testimagescrollapp.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wipro.shopease.utils.Resource
import com.wipro.testimagescrollapp.domain.model.ImageListResponse
import com.wipro.testimagescrollapp.domain.repository.RetrofitRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: RetrofitRepository
) : ViewModel() {

    var page: Int = 1
    private val perPage: Int = 20

    var isLoading = false
    var isLastPage = false

    private val _response: MutableLiveData<Resource<List<ImageListResponse>>> =
        MutableLiveData()
    val response: LiveData<Resource<List<ImageListResponse>>> = _response
    val productList: MutableList<ImageListResponse?> = mutableListOf()


    init {
        callImageApi()
    }

    fun callImageApi() = viewModelScope.launch {
        if (isLoading || isLastPage) return@launch

        isLoading = true
        _response.postValue(Resource.loading())
        Log.e("TAG", "---->> callImageApi : PageNo : $page")
        repository.getImageListApi(page, perPage).collect { values ->
            _response.value = values
            isLoading = false
        }
    }

}