package com.necatisozer.memorygame.ui.game.level

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.necatisozer.memorygame.R
import com.necatisozer.memorygame.databinding.FragmentLevelBinding
import com.necatisozer.memorygame.di.injector
import com.necatisozer.memorygame.extension.activityViewModels
import com.necatisozer.memorygame.extension.viewModels
import com.necatisozer.memorygame.ui.base.BaseFragment
import com.necatisozer.memorygame.util.Card
import splitties.arch.lifecycle.observeNotNull
import kotlin.math.ceil
import kotlin.math.sqrt

class LevelFragment : BaseFragment() {
    private lateinit var binding: FragmentLevelBinding
    private val viewModel by viewModels { injector.levelViewModel }
    private val activityViewModel by activityViewModels { injector.gameViewModel }
    private val cardAdapter: CardAdapter by lazy {
        CardAdapter().apply {
            clickListenerIndexed = { index, card ->
                if (card.state == Card.State.BACK) viewModel.onCardClick(index)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_level, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeViewModel()
    }

    private fun observeViewModel() {
        viewLifecycleOwner.observeNotNull(viewModel.cardsLiveData()) { showCards(it) }
        viewLifecycleOwner.observeNotNull(viewModel.addScoreEvent()) { activityViewModel.addScore(it) }
        viewModel.proceedToNextLevelEvent().observe(viewLifecycleOwner, Observer { proceedToNextLevel(it) })
        viewModel.gameOverEvent().observe(viewLifecycleOwner, Observer { activityViewModel.gameOver(success = true) })
    }

    private fun proceedToNextLevel(numberOfCards: Int) {
        val spanCount = ceil(sqrt(numberOfCards.toDouble())).toInt()

        binding.recyclerViewCards.apply {
            layoutManager = GridLayoutManager(context, spanCount)
            adapter = cardAdapter
        }
    }

    private fun showCards(cards: List<Card>) {
        binding.recyclerViewCards.adapter = cardAdapter // TODO: List is not updating. This is a workaround. Fix it.
        cardAdapter.submitList(cards)
    }
}