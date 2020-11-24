package org.imaginativeworld.whynotimagecarousel

import android.graphics.Bitmap
import android.widget.ImageView

interface CarouselOnImageLoadListener {

    fun onImageLoad(view: ImageView,resource: Bitmap?)

}