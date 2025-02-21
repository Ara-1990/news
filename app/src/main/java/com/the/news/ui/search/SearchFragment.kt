package com.the.news.ui.search


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.SearchView

import com.the.news.databinding.FragmentSearchBinding
import com.the.news.ui.BaseMviFragment
import com.the.news.ui.details.SearchableAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel


class SearchFragment : BaseMviFragment<State, Effect, Event, SearchViewModel>() {


    override val viewModel: SearchViewModel by viewModel()
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var searchAdapter: SearchableAdapter



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun initViews() {
        with(binding) {
            recycler.apply {
                setHasFixedSize(true)
                searchAdapter = SearchableAdapter { title -> }
                adapter = searchAdapter
            }
            search.apply {
                setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(p0: String?): Boolean {
                        return true
                    }

                    override fun onQueryTextChange(p0: String?): Boolean {
                        viewModel.process(Event.Search(p0 ?: ""))
                        return true
                    }
                })
            }
        }
    }

    override fun renderViewState(state: State) {
        with(binding) {
            searchAdapter.setListItems(state.articles)
        }
    }

    override fun renderViewEffect(effect: Effect) {
    }
}
