package com.necatisozer.memorygame.ui.main.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.necatisozer.memorygame.R
import com.necatisozer.memorygame.data.entity.User
import com.necatisozer.memorygame.databinding.FragmentMainBinding
import com.necatisozer.memorygame.di.injector
import com.necatisozer.memorygame.extension.loadUrlAsCircle
import com.necatisozer.memorygame.extension.viewModels
import com.necatisozer.memorygame.ui.base.BaseFragment
import splitties.arch.lifecycle.observeNotNull

class MainFragment : BaseFragment() {
    private lateinit var binding: FragmentMainBinding
    private val viewModel by viewModels { injector.mainViewModel }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViews()
        observeViewModel()
    }

    private fun initViews() {

    }

    private fun observeViewModel() {
        viewLifecycleOwner.observeNotNull(viewModel.userLiveData()) { showUserData(it) }
    }

    private fun showUserData(user: User) {
        user.photoUrl?.let { binding.layoutProfile.imageViewProfilePhoto.loadUrlAsCircle(it) }
        binding.layoutProfile.textViewUsername.text = user.username
        binding.layoutProfile.textViewHighestScore.text = getString(R.string.main_highest_score, user.highestScore)
    }
}