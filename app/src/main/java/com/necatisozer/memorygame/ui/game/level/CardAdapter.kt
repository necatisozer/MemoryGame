package com.necatisozer.memorygame.ui.game.level

import android.view.ViewGroup
import com.necatisozer.memorygame.databinding.ItemCardBinding
import com.necatisozer.memorygame.extension.inflater
import com.necatisozer.memorygame.extension.loadAsset
import com.necatisozer.memorygame.ui.base.BaseAdapter
import com.necatisozer.memorygame.ui.base.BaseViewHolder
import com.necatisozer.memorygame.util.Card


class CardAdapter : BaseAdapter<Card, CardViewHolder>() {
    override fun onCreateViewHolder(root: ViewGroup) =
        CardViewHolder(ItemCardBinding.inflate(root.inflater, root, false))
}

class CardViewHolder(binding: ItemCardBinding) :
    BaseViewHolder<Card, ItemCardBinding>(binding) {

    override fun bindData(data: Card) {
        val assetPath = when (data.state) {
            Card.State.FRONT -> "material_cards/card_${data.id}.png"
            Card.State.BACK -> "material_cards/card_back.png"
        }

        binding.imageViewCard.loadAsset(assetPath)
    }
}