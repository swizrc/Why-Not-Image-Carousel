package org.imaginativeworld.whynotimagecarousel

import androidx.recyclerview.widget.RecyclerView

interface CarouselOnOverScrollListener {

    fun onLeftOverScroll(recycler: RecyclerView.Recycler,
                         state: RecyclerView.State?)

    fun onRightOverScroll(recycler: RecyclerView.Recycler,
                          state: RecyclerView.State?)
}