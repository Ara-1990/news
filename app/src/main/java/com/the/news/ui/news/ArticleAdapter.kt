package com.the.news.ui.news

import android.annotation.SuppressLint
import android.content.Context
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog

import com.squareup.picasso.Picasso
import com.the.news.domain.model.Article
import com.the.news.R
import com.the.news.databinding.ListItemArticleBinding
import com.the.news.utils.px
import com.the.news.utils.setOnDebouncedClickListener

class ArticleAdapter(
    private val onlyCached: Boolean,
    private val onViewContent: (String) -> Unit,
    private val onRemoveArticle: (String) -> Unit,
    private val onSaveArticle: ((String) -> Unit)? = null, ) : RecyclerView.Adapter<ArticleAdapter.ViewHolder>() {

    private var articles: List<Article> = listOf()
    private var updatedPosition: Int? = null
    lateinit var context: Context



    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {



        private val binding = ListItemArticleBinding.bind(view)
        fun bind(item: Article) {
            binding.run {
                tvTitle.text = item.title
                tvDate.text = item.pubDate
                tvName.text = item.source_name



                    if (item.title.isNotEmpty() && item.description.isNotEmpty()) {
                        try {
                            val icPlaceholderRes = R.drawable.ic_news
                            Picasso.get()
                                .load(item.image_url)
                                .centerCrop(Gravity.TOP)
                                .resize(60.px, 60.px)
                                .error(icPlaceholderRes)
                                .placeholder(icPlaceholderRes)
                                .into(ivPicture)
                        }
                        catch (e: Exception) {


                        }

                    } else {
                        val icPlaceholderRes = R.drawable.ic_news
                        Picasso.get()
                            .load(R.drawable.ic_news)
                            .centerCrop(Gravity.TOP)
                            .resize(60.px, 60.px)
                            .placeholder(icPlaceholderRes)
                            .into(ivPicture)


                    }

                icFavourite.run {
                    val bottomSheetDialog = BottomSheetDialog(context, R.style.BottomSheetTheme)
                    var view: View = LayoutInflater.from(context)
                        .inflate(R.layout.dialog_layout, null)
                    bottomSheetDialog.setContentView(view)
                    var save: Button = view.findViewById(R.id.saveTv)
                    var saveArtical: TextView = view.findViewById(R.id.saved_article)
                    var delete_saved_article: ImageView = view.findViewById(R.id.delete_saved_article)


                    val iconRes = when {
                        onlyCached -> R.drawable.ic_close
                        else -> R.drawable.ic_more_vert_24

                    }

                    setImageResource(iconRes)
                    setOnDebouncedClickListener {

                        articles[layoutPosition].run {
                            updatedPosition = layoutPosition
                            if (isFavourite) {
                                save.visibility = View.GONE
                                saveArtical.visibility = View.VISIBLE
                                delete_saved_article.visibility = View.VISIBLE

                            }
                            if (onlyCached) {
                                onRemoveArticle.invoke(title)
                            } else {

                                bottomSheetDialog.show()
                                save.setOnClickListener {
                                    onSaveArticle?.invoke(title)
                                    bottomSheetDialog.dismiss()

                                }
                                delete_saved_article.setOnClickListener {
                                    onRemoveArticle.invoke(title)
                                    bottomSheetDialog.dismiss()
                                }

                            }
                        }
                    }
                }
                layContainer.setOnDebouncedClickListener {
                    onViewContent.invoke(articles[layoutPosition].title)

                }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_article, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = articles.size

    @SuppressLint("NotifyDataSetChanged")
    fun setListItems(articles: List<Article>) {
        this.articles = articles
        updatedPosition?.let {
            if (onlyCached) {
                notifyItemRemoved(it)
            } else {
                notifyItemChanged(it)
            }
        }
            ?: notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(articles[position])

    }



}