package com.the.news.ui.favourite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.the.news.databinding.FragmentFavouriteBinding
import com.the.news.ui.BaseMviFragment
import com.the.news.ui.news.ArticleAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.the.news.ui.favourite.Event.*

class FavouriteFragment : BaseMviFragment<State, Effect, Event, FavouriteViewModel>() {

    override val viewModel: FavouriteViewModel by viewModel()
    private var _binding: FragmentFavouriteBinding? = null
    private val binding get() = _binding!!
    private lateinit var articleAdapter: ArticleAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentFavouriteBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun initViews() {
        with(binding) {
            recyclerArticles.apply {
                setHasFixedSize(true)
                articleAdapter = ArticleAdapter(
                    onlyCached = true,
                    onViewContent = { title -> viewModel.process(ViewDetails(title)) },
                    onRemoveArticle = { title -> viewModel.process(Remove(title)) },

                )
                adapter = articleAdapter
            }
        }
    }

    override fun renderViewState(state: State) {
        with(binding) {
            articleAdapter.setListItems(state.savedArticled)

        }
    }

    override fun renderViewEffect(effect: Effect) {
        when (effect) {
            is Effect.NavigateTo.Details -> {
                findNavController().navigate(
                    FavouriteFragmentDirections.actionNavFavouriteToNavDetails(effect.article)
                )
            }
            else -> {
                Toast.makeText(context, "error", Toast.LENGTH_SHORT).show()
            }
        }

    }
}
