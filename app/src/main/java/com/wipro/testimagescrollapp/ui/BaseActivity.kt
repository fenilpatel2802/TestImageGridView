package com.wipro.testimagescrollapp.ui

import androidx.appcompat.app.AppCompatActivity
import com.wipro.testimagescrollapp.domain.repository.RetrofitRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
open class BaseActivity : AppCompatActivity() {

    @Inject
    lateinit var retrofitRepository: RetrofitRepository

}