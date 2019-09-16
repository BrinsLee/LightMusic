package com.brins.lightmusic.ui.base.adapter

import android.content.Context

abstract class CommonViewAdapter<T>(context: Context, var layoutId: Int, var list: ArrayList<T>) :
    BaseRecyclerAdapter<T>(context, list) {

    companion object {
        val TAG: String = "TreeRecyclerViewAdapter"
    }

    init {
        addItemViewDelegate(object : ItemViewDelegate<T> {
            override fun getItemViewLayoutId(): Int {
                return layoutId
            }

            override fun isForViewType(item: T, position: Int): Boolean {
                return true
            }

            override fun convert(holder: ViewHolder, t: T, position: Int) {
                converted(holder, t, position)
            }

        })
    }

    protected abstract fun converted(holder: ViewHolder, t: T, position: Int)


   /* private fun declareType() {
        if (list.isNotEmpty()) {
            if (list[0] is UserPlayListBean) {
                className = "UserPlayListBean"

            }
            if (list[0] is Music) {
                className = "OnlineMusic"
            }
            if (list[0] is Item) {
                className = "Item"
            }
        }
        Log.d(TAG, className)

    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val id =
            if (className == "Item") R.layout.item_login_selector else R.layout.item_local_music
        val itemView = LayoutInflater.from(parent.context).inflate(
            id,
            parent,
            false
        )
        val holder = SecondViewHolder(itemView)
        if (mItemClickListener != null) {
            itemView.setOnClickListener {
                mItemClickListener!!.onItemClick(it.tag as Int)
            }

        }
        return holder
    }

    fun setData(arrayList: ArrayList<T>) {
        this.list = arrayList
    }

    override fun getItemCount(): Int {
        return list.size
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (list.size != 0) {
            holder.itemView.tag = position
            when (className) {
                "UserPlayListBean" -> {
                    val playlist = (list[position] as UserPlayListBean)
                    Glide.with(BaseApplication.getInstance().baseContext)
                        .load(playlist.coverImgUrl)
                        .into((holder as CommonViewAdapter<*>.SecondViewHolder).imgCover)
                    holder.tvName.text = playlist.name
                    holder.tvAccount.text = "共${playlist.trackCount}首"
                }
                "OnlineMusic" -> {
                    val playlist = (list[position] as Music)
                    Glide.with(BaseApplication.getInstance().baseContext)
                        .load(playlist.album.picUrl)
                        .into((holder as CommonViewAdapter<*>.SecondViewHolder).imgCover)
                    (holder as CommonViewAdapter<*>.SecondViewHolder).tvName.text =
                        playlist.name
                    holder.tvAccount.text = playlist.artistBeans!![0].name
                }
                "Item" -> {
                    val playList = (list[position] as Item)
                    (holder as CommonViewAdapter<*>.SecondViewHolder).imgCover.setImageResource(
                        playList.icon
                    )
                    (holder as CommonViewAdapter<*>.SecondViewHolder).tvName.text = playList.name
                }
            }
        }

    }


    private inner class SecondViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemRoot = itemView.findViewById<RoundConstraintLayout>(R.id.itemRoot)
        val imgCover = itemView.findViewById<ImageView>(R.id.imgCover)
        val tvName = itemView.findViewById<TextView>(R.id.textViewName)
        val tvAccount = itemView.findViewById<TextView>(R.id.textViewArtist)
    }*/

}