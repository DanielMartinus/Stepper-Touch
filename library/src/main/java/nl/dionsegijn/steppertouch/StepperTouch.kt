package nl.dionsegijn.steppertouch

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.graphics.drawable.GradientDrawable
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import android.util.AttributeSet
import android.view.*
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.widget.FrameLayout
import android.widget.TextView


/**
 * Created by dionsegijn on 3/19/17.
 */
class StepperTouch : FrameLayout, OnStepCallback {
    constructor(context: Context) : super(context) { prepareElements() }
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) { handleAttrs(attrs) }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) { handleAttrs(attrs) }

    // Stepper view
    lateinit var stepper: Stepper
    private lateinit var viewStepper: StepperCounter
    private lateinit var textViewNegative: TextView
    private lateinit var textViewPositive: TextView

    // Drawing properties
    private val clipPath = Path()
    private var rect: RectF? = null

    private val defaultHeight: Int = pxFromDp(40f).toInt()
    private var newHeight: Int = 0

    // Animation properties
    private val stiffness: Float = 200f
    private val damping: Float = 0.6f
    private var startX: Float = 0f

    // Style properties
    private var stepperBackground = R.color.stepper_background
    private var stepperActionColor = R.color.stepper_actions
    private var stepperActionColorDisabled = R.color.stepper_actions_disabled
    private var stepperTextColor = R.color.stepper_text
    private var stepperButtonColor = R.color.stepper_button
    private var stepperTextSize = 20
    private var allowNegativeStepper = true
    private var allowPositiveStepper = true

    // Indication if tapping positive and negative sides is allowed
    private var isTapEnabled: Boolean = false
    private var isTapped: Boolean = false

    private fun handleAttrs(attrs: AttributeSet) {
        val styles: TypedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.StepperTouch, 0, 0)

        try {
            stepperBackground = styles.getResourceId(R.styleable.StepperTouch_stepperBackgroundColor, R.color.stepper_background)
            stepperActionColor = styles.getResourceId(R.styleable.StepperTouch_stepperActionsColor, R.color.stepper_actions)
            stepperActionColorDisabled = styles.getResourceId(R.styleable.StepperTouch_stepperActionsDisabledColor, R.color.stepper_actions_disabled)
            stepperTextColor = styles.getResourceId(R.styleable.StepperTouch_stepperTextColor, R.color.stepper_text)
            stepperButtonColor = styles.getResourceId(R.styleable.StepperTouch_stepperButtonColor, R.color.stepper_button)
            stepperTextSize = styles.getDimensionPixelSize(R.styleable.StepperTouch_stepperTextSize, R.dimen.st_textsize)
            allowNegativeStepper = styles.getBoolean(R.styleable.StepperTouch_stepperAllowNegativeStepper, true)
            allowPositiveStepper = styles.getBoolean(R.styleable.StepperTouch_stepperAllowPositiveStepper, true)
        } finally {
            styles.recycle()
            prepareElements()
        }
    }

    init {
        clipChildren = true
    }

    private fun prepareElements() {
        // Set width based on height
        newHeight = if (height == 0) defaultHeight else height

        // Set radius based on height
        val parentRadius = getRadiusBackgroundShape(newHeight.toFloat())
        this.background = parentRadius
        parentRadius.setColor(ContextCompat.getColor(context, stepperBackground))

        textViewNegative = createTextView("-", Gravity.START, stepperActionColorDisabled)
        addView(textViewNegative)
        enableSideTapForView(textViewNegative)

        textViewPositive = createTextView("+", Gravity.END, stepperActionColor)
        addView(textViewPositive)
        enableSideTapForView(textViewPositive)

        refreshNegativeVisibility()
        refreshPositiveVisibility()

        // Add draggable viewStepper to the container
        viewStepper = createStepper()
        addView(viewStepper)
    }

    private fun refreshNegativeVisibility() {
        if (!allowNegativeStepper) {
            textViewNegative.visibility = View.INVISIBLE
        } else {
            textViewNegative.visibility = View.VISIBLE
        }
    }

    private fun refreshPositiveVisibility() {
        if (!allowPositiveStepper) {
            textViewPositive.visibility = View.INVISIBLE
        } else {
            textViewPositive.visibility = View.VISIBLE
        }
    }

    fun enableSideTapForView(textView: View) {
        textView.setOnTouchListener { v, event ->
            if(event.action == MotionEvent.ACTION_DOWN) {
                if(isTapEnabled) {
                    isTapped = true
                    viewStepper.x = v.x
                }
            }
            false
        }
    }

    /**
    * Allow interact with negative section, if you disallow, the negative section will hide,
    * and it's not working
    * @param [allow] true if allow to use negative, false to disallow
    * */
    fun allowNegativeStepper(allow: Boolean) {
        allowNegativeStepper = allow
        refreshNegativeVisibility()
    }

    /**
     * Allow interact with positive section, if you disallow, the positive section will hide,
     * and it's not working
     * @param [allow] true if allow to use positive, false to disallow
     * */
    fun allowPositiveStepper(allow: Boolean) {
        allowPositiveStepper = allow
        refreshPositiveVisibility()
    }

    /**
     * Enable interaction when tapping on the left or right side of the widget.
     * @param [enable] true if allowed to update the widget
     */
    fun enableSideTap(enable: Boolean) {
        isTapEnabled = enable
    }

    /**
     * If tapping on the right or left side will trigger the widget to react.
     * @return boolean if the widget will update the count when tapping on one of the sides
     */
    fun getIsEnabled() : Boolean {
        return isTapEnabled
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                if(!isTapped) {
                    startX = event.x
                }
                startX = event.x
                parent.requestDisallowInterceptTouchEvent(true)
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                if(!isTapped) {
                    viewStepper.translationX = event.x - startX
                }
                return true
            }
            MotionEvent.ACTION_UP -> {
                isTapped = false
                if (viewStepper.translationX > viewStepper.width * 0.5 && allowPositiveStepper) viewStepper.add()
                else if (viewStepper.translationX < -(viewStepper.width * 0.5) && allowNegativeStepper) viewStepper.subtract()

                if (viewStepper.translationX != 0f) {
                    val animX = SpringAnimation(viewStepper, SpringAnimation.TRANSLATION_X, 0f)
                    animX.spring.stiffness = stiffness
                    animX.spring.dampingRatio = damping
                    animX.start()
                }
                return true
            }
            else -> {
                parent.requestDisallowInterceptTouchEvent(false)
                return false
            }
        }
    }

    private fun createStepper(): StepperCounter {
        val view = LayoutInflater.from(context).inflate(R.layout.view_step_counter, null) as StepperCounter
        setStepperSize(view)
        view.addStepCallback(this)
        view.setStepperTextColor(ContextCompat.getColor(context, stepperTextColor))
        // Set stepper interface
        this.stepper = view
        return view
    }

    private fun setStepperSize(view: StepperCounter) {
        view.layoutParams = FrameLayout.LayoutParams(newHeight, newHeight, Gravity.CENTER)
        val stepperRadius = getRadiusBackgroundShape(newHeight.toFloat())
        stepperRadius.setColor(ContextCompat.getColor(context, stepperButtonColor))
        view.background = stepperRadius
    }

    override fun onStep(value: Int, positive: Boolean) {
        textViewNegative.setTextColor(ContextCompat.getColor(context,
                if(value == viewStepper.minValue) stepperActionColorDisabled else stepperActionColor)
        )
        textViewPositive.setTextColor(ContextCompat.getColor(context,
                if(value == viewStepper.maxValue) stepperActionColorDisabled else stepperActionColor)
        )
    }

    private fun createTextView(text: String, gravity: Int, @ColorRes color: Int): TextView {
        val textView: TextView = TextView(context)
        textView.text = text
        textView.textSize = 20f
        textView.setTextColor(ContextCompat.getColor(context, color))
        val paramsTextView = FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, gravity)
        textView.layoutParams = paramsTextView
        textView.gravity = Gravity.CENTER_VERTICAL
        val margin = pxFromDp(12f).toInt()
        textView.setPadding(margin, 0, margin, 0)
        return textView
    }

    private fun getRadiusBackgroundShape(radius: Float): GradientDrawable {
        val radiusBackground = GradientDrawable()
        radiusBackground.cornerRadius = radius
        return radiusBackground
    }

    /**
     * Handle clipping of the rounded container
     */
    override fun onDraw(canvas: Canvas) {
        rect.let { rect = RectF(canvas.clipBounds) }
        // Clipping rounded corner
        val r: Float = canvas.height.toFloat() / 2
        clipPath.addRoundRect(rect, r, r, Path.Direction.CW)
        canvas.clipPath(clipPath)
        super.onDraw(canvas)
    }

    private fun pxFromDp(dp: Float): Float {
        return dp * resources.displayMetrics.density
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        newHeight = measuredHeight
        setStepperSize(viewStepper)
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

}
