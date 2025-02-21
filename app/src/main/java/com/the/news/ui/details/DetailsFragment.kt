package com.the.news.ui.details


import android.os.Bundle
import android.view.Gravity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.squareup.picasso.Picasso
import com.the.news.R
import com.the.news.databinding.FragmentDetailsBinding
import com.the.news.ui.BaseMviFragment
import com.the.news.utils.px
import org.koin.androidx.viewmodel.ext.android.viewModel





class DetailsFragment : BaseMviFragment<State, Effect, Event, DetailsViewModel>()
{

    override val viewModel: DetailsViewModel by viewModel()
    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private val args: DetailsFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun initViews() {
        viewModel.process(Event.InitScreen(args.article))
    }

    override fun renderViewState(state: State) {
        with(binding) {
            state.article?.run {

                tvTitle.text = title
                tvContent.text = description
                tvSource.text = source_name
                tvDate.text = pubDate

                if (title.isNotEmpty() && description.isNotEmpty()) {
                    try {
                        val icPlaceholderRes = R.drawable.ic_news
                        Picasso.get()
                            .load(image_url)
                            .centerCrop(Gravity.TOP)
                            .resize(200.px, 200.px)
                            .error(icPlaceholderRes)
                            .placeholder(icPlaceholderRes)
                            .into(ivPicture)
                    } catch (e: Exception) {

                    }
                }
                else{
                    val icPlaceholderRes = R.drawable.ic_news
                    Picasso.get()
                        .load(R.drawable.ic_news)
                        .centerCrop(Gravity.TOP)
                        .resize(200.px, 200.px)
                        .error(icPlaceholderRes)
                        .placeholder(icPlaceholderRes)
                        .into(ivPicture)
                }
            }
        }
    }




    override fun renderViewEffect(effect: Effect) {
    }



}