package me.goldze.mvvmhabit.utils.swipehelper

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.*
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import me.goldze.mvvmhabit.binding.viewadapter.recyclerview.DividerLine

class SwipeWindowHelper() : Handler() {
    private val MSG_ACTION_DOWN = 1 //点击事件
    private val MSG_ACTION_MOVE = 2 //滑动事件
    private val MSG_ACTION_UP = 3  //点击结束
    private val MSG_SLIDE_CANCEL = 4 //开始滑动，不返回前一个页面
    private val MSG_SLIDE_CANCELED = 5  //结束滑动，不返回前一个页面
    private val MSG_SLIDE_PROCEED = 6 //开始滑动，返回前一个页面
    private val MSG_SLIDE_FINISHED = 7//结束滑动，返回前一个页面

    val MOVING = 100
    val CANCEL = 101
    val FINISH = 102

    private val TAG = "SwipeWindowHelper"

    private val SHADOW_WIDTH = 50 //px 阴影宽度

    private var mOnSwipeListener: OnSwipeListener? = null


    fun setOnSwipeListener(onSwipeListener: OnSwipeListener) {

    }

    private lateinit var mActivity: Activity

    private var mIsSupportSlideBack: Boolean = false

    private lateinit var mCurrentContentView: FrameLayout

    private lateinit var mViewManager: ViewManager

    private var mTouchSlop: Int = 0

    private var mEdgeSize: Int = 0

    constructor(slideBackManager: SlideBackManager) : this() {
        if (slideBackManager == null || slideBackManager.getSlideActivity() == null) {
            throw RuntimeException("Neither SlideBackManager nor the method 'getSlideActivity()' can be null!")
        }
        mActivity = slideBackManager.getSlideActivity()
        mIsSupportSlideBack = slideBackManager.supportSlideBack()
        mCurrentContentView = getContentView(mActivity)
        mViewManager = ViewManager()

        mTouchSlop = ViewConfiguration.get(mActivity).scaledTouchSlop
        mEdgeSize = DividerLine.dip2px(mActivity,20f)
    }

    private fun getContentView(mActivity: Activity): FrameLayout {
        return mActivity.findViewById(Window.ID_ANDROID_CONTENT)
    }


    private var mIsSlideAnimPlaying: Boolean = false

    private var mLastPointX: Float = 0.0f

    private var mIsInThresholdArea: Boolean = false

    private var mIsSliding: Boolean = false

    private val CURRENT_POINT_X = "currentPointX" //点击事件

    private var mDistanceX: Float = 0f

    fun processTouchEvent(ev: MotionEvent): Boolean {
        if (!mIsSupportSlideBack) { //不支持slideback交给上级处理
            return false
        }

        if (mIsSlideAnimPlaying) { //正在滑动直接消费事件
            return true;
        }

        val action = ev.action and MotionEvent.ACTION_MASK
        if (action == MotionEvent.ACTION_DOWN) {
            mLastPointX = ev.rawX
            mIsInThresholdArea = mLastPointX >= 0 && mLastPointX <= mEdgeSize
        }

        if (!mIsInThresholdArea) {  //不满足滑动区域，不做处理
            return false
        }

        val actionIndex = ev.actionIndex
        when (action) {
            MotionEvent.ACTION_DOWN -> {
                sendEmptyMessage(MSG_ACTION_DOWN)
            }
            MotionEvent.ACTION_POINTER_DOWN -> {
                return mIsSliding //触发滑动后屏蔽其他手指
            }
            MotionEvent.ACTION_MOVE -> {
                //一旦触发滑动后拦截其他手指的滑动事件
                if (actionIndex != 0) {
                    return mIsSliding
                }
                val curPointX = ev.rawX
                val isSliding = mIsSliding
                if (!isSliding) {
                    if (Math.abs(curPointX - mLastPointX) < mEdgeSize) {
                        return false
                    } else {
                        mIsSliding = true
                    }
                }

                val bundle = Bundle()
                bundle.putFloat(CURRENT_POINT_X, curPointX)
                val message = obtainMessage()
                message.what = MSG_ACTION_MOVE
                message.data = bundle
                sendMessage(message)

                if (isSliding == mIsSliding) {
                    return true
                } else {
                    val cancelEvent = MotionEvent.obtain(ev) //首次判定为滑动需要修正事件：手动修改事件为 ACTION_CANCEL，并通知底层View
                    cancelEvent.action = MotionEvent.ACTION_CANCEL
                    mActivity.window.superDispatchTouchEvent(cancelEvent)
                    return true
                }


            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP, MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_OUTSIDE -> {
                if (mDistanceX == 0f) {
                    mIsSliding = false
                    sendEmptyMessage(MSG_ACTION_UP)
                    return false
                }

                if (mIsSliding && actionIndex == 0) {
                    mIsSliding = false
                    sendEmptyMessage(MSG_ACTION_UP)
                    return true
                } else if (mIsSliding && actionIndex != 0) {
                    return true
                }
            }
            else -> {
                mIsSliding = false
            }
        }
        return false
    }

    val inputMethod by lazy { mActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager }
    val screenwidth by lazy {
        mActivity.resources.displayMetrics.widthPixels
    }

    override fun handleMessage(msg: Message) {
        super.handleMessage(msg)
        when (msg.what) {
            MSG_ACTION_DOWN -> {
                // hide input method
                val view = mActivity.currentFocus
                if (view != null) {
                    inputMethod.hideSoftInputFromWindow(view.windowToken, 0)
                }

                if (!mViewManager.addViewFromPreviousActivity()) return

                mViewManager.addShadowView();
                if (mCurrentContentView.childCount >= 3) {
                    var v: View? = mViewManager.getDisplayView()
                    if (v?.background == null) {
                        v?.setBackgroundColor(getWindowBackgroundColor())
                    }
                }
            }

            MSG_ACTION_MOVE -> {
                val curPointX: Float = msg.data.getFloat(CURRENT_POINT_X)
                onSliding(curPointX)
                mOnSwipeListener?.onSwipe(MOVING)
            }

            MSG_ACTION_UP -> {
                if (mDistanceX == 0f) {
                    if (mCurrentContentView.childCount >= 3) {
                        mViewManager.removeShadowView()
                        mViewManager.resetPreviousView()
                    }
                } else if (mDistanceX > screenwidth / 4) {
                    sendEmptyMessage(MSG_SLIDE_PROCEED)
                } else {
                    sendEmptyMessage(MSG_SLIDE_CANCEL)
                }

            }

            MSG_SLIDE_PROCEED -> {
                startSlideAnim(false)
            }

            MSG_SLIDE_CANCEL -> {
                mOnSwipeListener?.let {
                    it.onSwipe(CANCEL)
                }
                startSlideAnim(true)
            }

            MSG_SLIDE_CANCELED -> {
                mDistanceX = 0f
                mIsSliding = false
                mViewManager.removeShadowView()
                mViewManager.resetPreviousView()
                mOnSwipeListener?.let {
                    it.onSwipe(CANCEL)
                }
            }

            MSG_SLIDE_FINISHED -> {
                mViewManager.addCacheView()
                mViewManager.removeShadowView()
                mViewManager.resetPreviousView()
                mOnSwipeListener?.let { it.onSwipe(FINISH) }

                var activity = mActivity
                activity.finish()
                //if set windowIsTranslucent is true you must cancel the window anim
                activity.window.setWindowAnimations(0)
                activity.overridePendingTransition(0, 0)

            }

            else -> {

            }

        }
    }

    private fun startSlideAnim(slideCanceled: Boolean) {
        val previewView: ViewGroup? = mViewManager.mPreviousContentView
        val shadowView = mViewManager.mShadowView
        val currentView = mViewManager.getDisplayView()

        if (previewView == null || currentView == null) {
            return
        }

        val interpolator: Interpolator = DecelerateInterpolator(2f)

        var previewViewAnim: ObjectAnimator = ObjectAnimator()
        previewViewAnim.interpolator = interpolator
        previewViewAnim.setProperty(View.TRANSLATION_X)
        var preViewStart = getPreViewContentPosition()
        var preViewStop = if (slideCanceled) -screenwidth / 3f else 0f
        previewViewAnim.setFloatValues(preViewStart, preViewStop)
        previewViewAnim.target = previewView

        var shadowAnim: ObjectAnimator = ObjectAnimator()
        shadowAnim.interpolator = interpolator
        shadowAnim.setProperty(View.TRANSLATION_X)
        var shadowAnimStart = mDistanceX - SHADOW_WIDTH
        var shadowAnimStop = if (slideCanceled) -SHADOW_WIDTH else screenwidth - SHADOW_WIDTH
        shadowAnim.setFloatValues(shadowAnimStart, shadowAnimStop.toFloat())
        shadowAnim.target = shadowView

        var currentAnim: ObjectAnimator = ObjectAnimator()
        currentAnim.interpolator = interpolator
        currentAnim.setProperty(View.TRANSLATION_X)
        currentAnim.setFloatValues(mDistanceX, if (slideCanceled) 0f else screenwidth.toFloat())
        currentAnim.target = currentView

        var mAnimSet: AnimatorSet = AnimatorSet()
        mAnimSet.setDuration(if (slideCanceled) 150 else 300)
        mAnimSet.playTogether(previewViewAnim, shadowAnim, currentAnim)
        mAnimSet.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                if (slideCanceled) {
                    mIsSlideAnimPlaying = false
                    previewView.translationX = 0f
                    if (shadowView != null) {
                        shadowView.translationX = (-SHADOW_WIDTH).toFloat()
                    }
                    currentView.translationX = 0f
                    sendEmptyMessage(MSG_SLIDE_CANCELED)
                } else {
                    sendEmptyMessage(MSG_SLIDE_FINISHED)
                }
            }
        })

        mIsSlideAnimPlaying = true
        mAnimSet.start()

    }

    fun getPreViewContentPosition(): Float {
        return -screenwidth / 3 + mDistanceX / 3
    }

    /**
     * 手动处理滑动事件
     */
    @Synchronized
    fun onSliding(curPointX: Float) {
        var preViewContent: View? = mViewManager.mPreviousContentView
        var shadowView = mViewManager.mShadowView
        var curViewContent = mViewManager.getDisplayView()

        if (preViewContent == null || shadowView == null || curViewContent == null) {
            sendEmptyMessage(MSG_SLIDE_CANCEL)
            return
        }

        mIsSliding = true
        val distanceX = curPointX - mLastPointX
        mLastPointX = curPointX
        mDistanceX = mDistanceX + distanceX
        if (mDistanceX < 0) {
            mDistanceX = 0f
        }

        preViewContent.translationX = getPreViewContentPosition()
        shadowView.translationX = mDistanceX - SHADOW_WIDTH
        curViewContent.translationX = mDistanceX

    }

    fun getWindowBackgroundColor(): Int {
        var array: TypedArray? = null
        try {
            array = mActivity.theme.obtainStyledAttributes(intArrayOf(android.R.attr.windowBackground))
            return array.getColor(0, ContextCompat.getColor(mActivity, android.R.color.transparent))
        } finally {
            if (array != null) {
                array.recycle()
            }
        }
    }

    inner class ViewManager {

        /**
         * Remove view from previous Activity and add into current Activity
         * @return Is view added successfully
         */
        fun addViewFromPreviousActivity(): Boolean {
            if (mCurrentContentView.getChildCount() == 0) {
                mPreviousActivity = null
                mPreviousContentView = null
                return false
            }
            mPreviousActivity = AppManager.instance.getPreActivity()

            if (mPreviousActivity == null) {
                mPreviousActivity = null
                mPreviousContentView = null
                return false
            }
            //Previous activity not support to be swipeBack...
            if (mPreviousActivity is SlideBackManager && !(mPreviousActivity as SlideBackManager).canBeSlideBack()) {
                mPreviousActivity = null
                mPreviousContentView = null
                return false
            }

            var preActivityContainer: ViewGroup = getContentView(mPreviousActivity!!)
            if (preActivityContainer == null || preActivityContainer.childCount == 0) {
                mPreviousActivity = null
                mPreviousContentView = null
                return false
            }

            mPreviousContentView = preActivityContainer.getChildAt(0) as ViewGroup?
            preActivityContainer.removeView(mPreviousContentView)
            mCurrentContentView.addView(mPreviousContentView, 0)
            return true
        }

        /**
         * add shadow view on the left of content view
         */
        @Synchronized
        fun addShadowView() {
            if (mShadowView == null) {
                mShadowView = ShadowView(mActivity).apply {
                    layoutParams = FrameLayout.LayoutParams(SHADOW_WIDTH, FrameLayout.LayoutParams.MATCH_PARENT)
                }
                (mShadowView as ShadowView).translationX = (-SHADOW_WIDTH).toFloat()
            }

            val contentView = mCurrentContentView
            if (mShadowView!!.parent == null) {
                contentView.addView(mShadowView, 1)
            } else {
                removeShadowView()
                addShadowView()
            }
        }

        @Synchronized
        fun removeShadowView() {
            if (mShadowView == null) return
            val contentView = getContentView(mActivity)
            contentView.removeView(mShadowView)
            mShadowView = null
        }

        fun getDisplayView(): View? {
            var index = 0
            if (mViewManager.mPreviousContentView != null) {
                index = index + 1
            }

            if (mViewManager.mShadowView != null) {
                index = index + 1
            }
            return mCurrentContentView.getChildAt(index)
        }

        fun resetPreviousView() {
            if (mPreviousContentView == null) return
            var view: ViewGroup = mPreviousContentView!!
            view.translationX = 0f
            var curlayout = mCurrentContentView
            curlayout.removeView(view)
            mPreviousContentView = null

            mCurrentContentView.removeView(view)

            if (mPreviousActivity == null || mPreviousActivity!!.isFinishing) return
            var preActivity = mPreviousActivity!!
            getContentView(preActivity).addView(view, 0)
            mPreviousActivity = null
        }

        fun addCacheView() {
            val contentView = mCurrentContentView
            val preView = mPreviousContentView
            preView?.let {
                var prePageView: PreviousPageView = PreviousPageView(mActivity)
                contentView.addView(prePageView, 0)
                prePageView.setCacheView(preView)
            }

        }


        var mPreviousActivity: Activity? = null
        var mPreviousContentView: ViewGroup? = null
        var mShadowView: View? = null

    }

    interface OnSwipeListener {
        fun onSwipe(state: Int)
    }

    interface SlideBackManager {
        fun getSlideActivity(): Activity
        fun supportSlideBack(): Boolean
        fun canBeSlideBack(): Boolean
    }

    class ShadowView @JvmOverloads constructor(
            context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
    ) : View(context, attrs, defStyleAttr) {
        var mDrawable: Drawable? = null
//        by lazy {
//            val colors = intArrayOf(0x00000000, 0x17000000, 0x43000000)//分别为开始颜色，中间夜色，结束颜色
//            GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colors)
//        }

        override fun onDraw(canvas: Canvas?) {
            super.onDraw(canvas)
            if (mDrawable == null) {
                val colors = intArrayOf(0x00000000, 0x17000000, 0x43000000)//分别为开始颜色，中间夜色，结束颜色
                mDrawable = GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colors)
            }

            mDrawable?.setBounds(0, 0, measuredWidth, measuredHeight)
            mDrawable?.draw(canvas)
        }
    }

    class PreviousPageView @JvmOverloads constructor(
            context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
    ) : View(context, attrs, defStyleAttr) {
        private var cacheView: View? = null

        fun setCacheView(v: View) {
            this.cacheView = v
            invalidate()
        }

        override fun onDraw(canvas: Canvas?) {
            super.onDraw(canvas)
            cacheView?.let {
                it.draw(canvas)
                cacheView = null
            }
        }
    }
}

