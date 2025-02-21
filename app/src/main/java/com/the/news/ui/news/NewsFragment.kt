package com.the.news.ui.news


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.the.news.databinding.FragmentNewsBinding
import com.the.news.ui.BaseMviFragment
import com.the.news.ui.news.Event.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewsFragment : BaseMviFragment<State, Effect, Event, NewsViewModel>() {

    override val viewModel: NewsViewModel by viewModel()
    private var _binding: FragmentNewsBinding? = null
    private val binding get() = _binding!!
    private lateinit var articleAdapter: ArticleAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {


        _binding = FragmentNewsBinding.inflate(inflater, container, false)


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
                    onlyCached = false,
                    onViewContent = { title -> viewModel.process(ViewDetails(title)) },
                    onRemoveArticle = { title -> viewModel.process(RemoveFromFavourites(title)) },
                    onSaveArticle = { title -> viewModel.process(MarkAsFavourite(title)) },


                )
                adapter = articleAdapter
            }
        }
    }

    override fun renderViewState(state: State) {
        with(binding) {
            articleAdapter.setListItems(state.articles)
        }
    }

    override fun renderViewEffect(effect: Effect) {
        when (effect) {
            is Effect.NavigateTo.Details -> {
                findNavController().navigate(
                    NewsFragmentDirections.actionNavNewsToNavDetails(effect.article)

                )
            }

            else -> {
                Toast.makeText(context, "error", Toast.LENGTH_SHORT).show()
            }

        }
    }
}