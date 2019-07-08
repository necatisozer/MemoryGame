package com.necatisozer.memorygame.ui.main.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.firebase.ui.auth.AuthUI
import com.necatisozer.memorygame.R
import com.necatisozer.memorygame.data.entity.User
import com.necatisozer.memorygame.databinding.FragmentMainBinding
import com.necatisozer.memorygame.di.injector
import com.necatisozer.memorygame.extension.loadUrlAsCircle
import com.necatisozer.memorygame.extension.viewModels
import com.necatisozer.memorygame.ui.base.BaseFragment
import com.necatisozer.memorygame.ui.game.GameActivity
import com.necatisozer.memorygame.ui.splash.SplashActivity
import splitties.arch.lifecycle.observeNotNull
import splitties.fragments.start
import splitties.views.onClick


class MainFragment : BaseFragment() {
    companion object {
        const val HIGHEST_SCORE_REQUEST_CODE = 9999
    }

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
        binding.layoutMainBody.buttonPlay.onClick { navigateToGame() }
        binding.layoutMainBody.buttonLeaders.onClick { navigateToLeaderboard() }
        binding.layoutMainBody.buttonSignOut.onClick { signOut() }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.observeNotNull(viewModel.userLiveData()) { showUserData(it) }
    }

    private fun showUserData(user: User) {
        user.photoUrl?.let { binding.layoutProfile.imageViewProfilePhoto.loadUrlAsCircle(it) }
        binding.layoutProfile.textViewUsername.text = user.username
        binding.layoutProfile.textViewHighestScore.text =
            getString(R.string.main_highest_score, user.highestScore)
    }

    private fun navigateToGame() {
        startActivityForResult(Intent(activity, GameActivity::class.java), HIGHEST_SCORE_REQUEST_CODE)
    }

    private fun navigateToLeaderboard() {
        val direction = MainFragmentDirections.actionMainFragmentToLeaderboardFragment()
        findNavController().navigate(direction)
    }

    private fun signOut() {
        AuthUI.getInstance().signOut(context!!).addOnCompleteListener { openSplash() }
    }

    private fun openSplash() {
        activity?.finish()
        start<SplashActivity>()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == HIGHEST_SCORE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                val highestScore = data?.getIntExtra("highest_score", 0)
                binding.layoutProfile.textViewHighestScore.text = getString(R.string.main_highest_score, highestScore)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}