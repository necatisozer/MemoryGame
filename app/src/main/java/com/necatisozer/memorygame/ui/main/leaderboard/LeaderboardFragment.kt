package com.necatisozer.memorygame.ui.main.leaderboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.necatisozer.memorygame.R
import com.necatisozer.memorygame.data.entity.User
import com.necatisozer.memorygame.databinding.FragmentLeaderboardBinding
import com.necatisozer.memorygame.di.injector
import com.necatisozer.memorygame.extension.viewModels
import com.necatisozer.memorygame.ui.base.BaseFragment
import splitties.arch.lifecycle.observeNotNull

class LeaderboardFragment : BaseFragment() {
    private lateinit var binding: FragmentLeaderboardBinding
    private val viewModel by viewModels { injector.leaderboardViewModel }

    private val leaderboardAdapter: LeaderboardAdapter by lazy { LeaderboardAdapter() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_leaderboard, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViews()
        observeViewModel()
    }

    private fun initViews() {
        binding.toolbar.toolbar.setupWithNavController(findNavController())

        binding.recyclerViewLeaderboard.apply {
            adapter = leaderboardAdapter
            addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.observeNotNull(viewModel.leaderboardLiveData()) { showLeaderboard(it) }
    }

    private fun showLeaderboard(leaderboard: List<User>) {
        leaderboardAdapter.submitList(leaderboard)
    }
}