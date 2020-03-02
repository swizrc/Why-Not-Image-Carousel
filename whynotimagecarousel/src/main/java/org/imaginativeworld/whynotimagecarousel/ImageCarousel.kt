package org.imaginativeworld.whynotimagecarousel

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DimenRes
import androidx.annotation.Dimension
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import me.relex.circleindicator.CircleIndicator2
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable

class ImageCarousel(
    @NotNull context: Context,
    @Nullable private var attributeSet: AttributeSet?
) : ConstraintLayout(context, attributeSet) {

    private lateinit var adapter: CarouselAdapter

    private val scaleTypeArray = arrayOf(
        ImageView.ScaleType.MATRIX,
        ImageView.ScaleType.FIT_XY,
        ImageView.ScaleType.FIT_START,
        ImageView.ScaleType.FIT_CENTER,
        ImageView.ScaleType.FIT_END,
        ImageView.ScaleType.CENTER,
        ImageView.ScaleType.CENTER_CROP,
        ImageView.ScaleType.CENTER_INSIDE
    )

    var onItemClickListener: OnItemClickListener? = null
        set(value) {
            field = value
            adapter.listener = value
        }

    private lateinit var rvImages: RecyclerView
    private lateinit var tvCaption: TextView
    private lateinit var indicator: CircleIndicator2
    private lateinit var btnPrevious: MaterialButton
    private lateinit var btnNext: MaterialButton
    private lateinit var viewTopShadow: View
    private lateinit var viewBottomShadow: View
    private lateinit var previousButtonContainer: FrameLayout
    private lateinit var nextButtonContainer: FrameLayout

    private var showTopShadow = false
        set(value) {
            field = value
            invalidate()
            requestLayout()
        }

    private var showBottomShadow = false
        set(value) {
            field = value
            invalidate()
            requestLayout()
        }

    private var showCaption = false
        set(value) {
            field = value
            invalidate()
            requestLayout()
        }

    private var showIndicator = false
        set(value) {
            field = value
            invalidate()
            requestLayout()
        }

    private var showNavigationButtons = false
        set(value) {
            field = value
            invalidate()
            requestLayout()
        }

    private var imageScaleType: ImageView.ScaleType = ImageView.ScaleType.CENTER_CROP
        set(value) {
            field = value
            invalidate()
            requestLayout()
        }

    private var carouselBackground: Drawable? = null
        set(value) {
            field = value
            invalidate()
            requestLayout()
        }

    private var imagePlaceholder: Drawable? = null
        set(value) {
            field = value
            invalidate()
            requestLayout()
        }

    @LayoutRes
    private var itemLayout: Int = R.layout.item_carousel
        set(value) {
            field = value
            invalidate()
            requestLayout()
        }

    @IdRes
    private var imageViewId: Int = R.id.img
        set(value) {
            field = value
            invalidate()
            requestLayout()
        }

    @LayoutRes
    private var previousButtonLayout: Int = R.layout.previous_button_layout
        set(value) {
            field = value
            invalidate()
            requestLayout()
        }

    @IdRes
    private var previousButtonId: Int = R.id.btn_next
        set(value) {
            field = value
            invalidate()
            requestLayout()
        }

    @Dimension
    private var previousButtonMargin: Float = 0F
        set(value) {
            field = value
            invalidate()
            requestLayout()
        }

    @LayoutRes
    private var nextButtonLayout: Int = R.layout.next_button_layout
        set(value) {
            field = value
            invalidate()
            requestLayout()
        }

    @IdRes
    private var nextButtonId: Int = R.id.btn_previous
        set(value) {
            field = value
            invalidate()
            requestLayout()
        }

    @Dimension
    private var nextButtonMargin: Float = 0F
        set(value) {
            field = value
            invalidate()
            requestLayout()
        }


    init {
        initAttributes()
        initAdapter()
        initViews()
        initListeners()
    }

    private fun initAdapter() {
        adapter = CarouselAdapter(
            context,
            itemLayout,
            imageViewId,
            onItemClickListener,
            imageScaleType,
            imagePlaceholder
        )
    }

    private fun initViews() {

        val carouselView = LayoutInflater.from(context).inflate(R.layout.image_carousel, this)

        rvImages = carouselView.findViewById(R.id.rv_images)
        tvCaption = carouselView.findViewById(R.id.tv_caption)
        indicator = carouselView.findViewById(R.id.indicator)
        viewTopShadow = carouselView.findViewById(R.id.view_top_shadow)
        viewBottomShadow = carouselView.findViewById(R.id.view_bottom_shadow)
        previousButtonContainer = carouselView.findViewById(R.id.previous_button_container)
        nextButtonContainer = carouselView.findViewById(R.id.next_button_container)

        // Inflate views
        val inflater = LayoutInflater.from(context)
        inflater.inflate(previousButtonLayout, previousButtonContainer, true)
        inflater.inflate(nextButtonLayout, nextButtonContainer, true)

        btnPrevious = carouselView.findViewById(previousButtonId)
        btnNext = carouselView.findViewById(nextButtonId)


        // Init Margins
        val previousButtonParams = LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT
        )
        previousButtonParams.setMargins(previousButtonMargin.toPx(context), 0, 0, 0)
        previousButtonContainer.layoutParams = previousButtonParams

        val nextButtonParams = LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT
        )
        nextButtonParams.setMargins(0, 0, nextButtonMargin.toPx(context), 0)
        nextButtonContainer.layoutParams = nextButtonParams


        // Recycler view
        rvImages.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rvImages.adapter = adapter
        rvImages.background = carouselBackground

        val pagerSnapHelper = PagerSnapHelper()
        pagerSnapHelper.attachToRecyclerView(rvImages)

        indicator.attachToRecyclerView(rvImages, pagerSnapHelper)
        adapter.registerAdapterDataObserver(indicator.adapterDataObserver)


        // Init visibility
        viewTopShadow.visibility = if (showTopShadow) View.VISIBLE else View.GONE
        viewBottomShadow.visibility = if (showBottomShadow) View.VISIBLE else View.GONE
        tvCaption.visibility = if (showCaption) View.VISIBLE else View.GONE
        indicator.visibility = if (showIndicator) View.VISIBLE else View.GONE
        btnPrevious.visibility = if (showNavigationButtons) View.VISIBLE else View.GONE
        btnNext.visibility = if (showNavigationButtons) View.VISIBLE else View.GONE
    }

    private fun initListeners() {
        rvImages.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                val position = indicator.getSnapPosition(rvImages.layoutManager)

                if (position >= 0) {
                    val dataItem = adapter.getItem(position)

                    dataItem?.apply {
                        tvCaption.text = this.caption
                    }
                }

            }
        })

        btnPrevious.setOnClickListener {
            val position = indicator.getSnapPosition(rvImages.layoutManager)

            if (position > 0) {
                rvImages.smoothScrollToPosition(position - 1)
            }
        }

        btnNext.setOnClickListener {
            rvImages.adapter?.apply {
                val position = indicator.getSnapPosition(rvImages.layoutManager)

                if (position < this.itemCount - 1) {
                    rvImages.smoothScrollToPosition(position + 1)
                }
            }
        }
    }

    private fun initAttributes() {
        context.theme.obtainStyledAttributes(
            attributeSet,
            R.styleable.ImageCarousel,
            0,
            0
        ).apply {

            try {
                imageScaleType = scaleTypeArray[
                        getInteger(
                            R.styleable.ImageCarousel_imageScaleType,
                            ImageView.ScaleType.CENTER_CROP.ordinal
                        )
                ]

                showTopShadow = getBoolean(R.styleable.ImageCarousel_showTopShadow, true)

                showBottomShadow = getBoolean(
                    R.styleable.ImageCarousel_showBottomShadow,
                    true
                )

                showCaption = getBoolean(R.styleable.ImageCarousel_showCaption, true)

                showIndicator = getBoolean(R.styleable.ImageCarousel_showIndicator, true)

                showNavigationButtons = getBoolean(
                    R.styleable.ImageCarousel_showNavigationButtons,
                    true
                )

                carouselBackground = getDrawable(
                    R.styleable.ImageCarousel_carouselBackground
                ) ?: ColorDrawable(Color.parseColor("#333333"))

                imagePlaceholder = getDrawable(
                    R.styleable.ImageCarousel_imagePlaceholder
                ) ?: ContextCompat.getDrawable(context, R.drawable.ic_picture)

                itemLayout = getResourceId(
                    R.styleable.ImageCarousel_itemLayout,
                    R.layout.item_carousel
                )

                imageViewId = getResourceId(
                    R.styleable.ImageCarousel_imageViewId,
                    R.id.img
                )

                previousButtonLayout = getResourceId(
                    R.styleable.ImageCarousel_previousButtonLayout,
                    R.layout.previous_button_layout
                )

                previousButtonId = getResourceId(
                    R.styleable.ImageCarousel_previousButtonId,
                    R.id.btn_previous
                )

                previousButtonMargin = getDimension(
                    R.styleable.ImageCarousel_previousButtonMargin,
                    4F
                )

                nextButtonLayout = getResourceId(
                    R.styleable.ImageCarousel_nextButtonLayout,
                    R.layout.next_button_layout
                )

                nextButtonId = getResourceId(
                    R.styleable.ImageCarousel_nextButtonId,
                    R.id.btn_next
                )

                nextButtonMargin = getDimension(
                    R.styleable.ImageCarousel_nextButtonMargin,
                    4F
                )

            } finally {
                recycle()
            }

        }
    }

    // ----------------------------------------------------------------

    public fun addData(data: List<CarouselItem>) {
        adapter.addAll(data)
    }

}