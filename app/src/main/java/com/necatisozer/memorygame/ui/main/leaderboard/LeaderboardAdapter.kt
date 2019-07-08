package com.necatisozer.memorygame.ui.main.leaderboard

import android.view.ViewGroup
import com.necatisozer.memorygame.data.entity.User
import com.necatisozer.memorygame.databinding.ItemLeaderboardBinding
import com.necatisozer.memorygame.extension.inflater
import com.necatisozer.memorygame.extension.loadUrlAsCircle
import com.necatisozer.memorygame.ui.base.BaseAdapter
import com.necatisozer.memorygame.ui.base.BaseViewHolder


class LeaderboardAdapter : BaseAdapter<User, LeaderboardViewHolder>() {
    override fun onCreateViewHolder(root: ViewGroup) =
        LeaderboardViewHolder(ItemLeaderboardBinding.inflate(root.inflater, root, false))
}

class LeaderboardViewHolder(binding: ItemLeaderboardBinding) :
    BaseViewHolder<User, ItemLeaderboardBinding>(binding) {

    override fun bindData(data: User) {
        data.photoUrl?.let { binding.imageViewProfilePhoto.loadUrlAsCircle(it) }
        binding.textViewUsername.text = data.username
        binding.textViewHighestScore.text = data.highestScore.toString()
    }
}