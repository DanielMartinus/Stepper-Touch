package nl.dionsegijn.steppertouch

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.ShapeDrawable
import androidx.dynamicanimation.animation.SpringAnimation
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.stepper_touch.view.*
import kotlin.properties.Delegates

/**
 * Created by dionsegijn on 3/19/17.
 */
class StepperTouchNew : ConstraintLayout {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) { handleAttrs(attrs) }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) { handleAttrs(attrs) }

    // Drawing properties
    private val clippingBounds: RectF = RectF()
    private val clipPath = Path()

    // Animation properties
    private val stiffness: Float = 200f
    private val damping: Float = 0.6f
    private var startX: Float = 0f


    // Indication if tapping positive and negative sides is allowed
    var sideTapEnabled: Boolean = false
    private var allowDragging = false
    private var isTapped: Boolean = false

    var maxValue: Int by Delegates.observable(Integer.MAX_VALUE) { _, _, _ ->
        updateSideControls()
    }
    var minValue: Int by Delegates.observable(Integer.MIN_VALUE) { _, _, _ ->
        updateSideControls()
    }
    private val callbacks: MutableList<OnStepCallback> = mutableListOf()
    var count: Int by Delegates.observable(0) { _, old, new ->
        viewCounterText.text = new.toString()
        updateSideControls()
        notifyStepCallback(new, new > old)
    }

    // Style properties
    private var stepperBackground = R.color.stepper_background
    private var stepperActionColor = R.color.stepper_actions
    private var stepperActionColorDisabled = R.color.stepper_actions_disabled
    private var stepperTextColor = R.color.stepper_text
    private var stepperButtonColor = R.color.stepper_button
    private var stepperTextSize = 20
    private var allowNegative = true
    private var allowPositive = true

    init {
        LayoutInflater.from(context).inflate(R.layout.stepper_touch, this, true)
        clipChildren = true

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            viewCounter.elevation = 4f
        }
        setWillNotDraw(false)
    }

    private fun handleAttrs(attrs: AttributeSet) {
        val styles: TypedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.StepperTouch, 0, 0)

        try {
            stepperBackground = styles.getResourceId(R.styleable.StepperTouch_stepperBackgroundColor, R.color.stepper_background)
            stepperActionColor = styles.getResourceId(R.styleable.StepperTouch_stepperActionsColor, R.color.stepper_actions)
            stepperActionColorDisabled = styles.getResourceId(R.styleable.StepperTouch_stepperActionsDisabledColor, R.color.stepper_actions_disabled)
            stepperTextColor = styles.getResourceId(R.styleable.StepperTouch_stepperTextColor, R.color.stepper_text)
            stepperButtonColor = styles.getResourceId(R.styleable.StepperTouch_stepperButtonColor, R.color.stepper_button)
            stepperTextSize = styles.getDimensionPixelSize(R.styleable.StepperTouch_stepperTextSize, stepperTextSize)
            allowNegative = styles.getBoolean(R.styleable.StepperTouch_stepperAllowNegative, true)
            allowPositive = styles.getBoolean(R.styleable.StepperTouch_stepperAllowPositive, true)
        } finally {
            styles.recycle()
        }

        updateStyling()
    }

    private fun updateStyling() {
        viewBackground.setBackgroundColor(ContextCompat.getColor(context, stepperBackground))
        viewCounterText.setTextColor(ContextCompat.getColor(context, stepperTextColor))
        viewCounterText.textSize = stepperTextSize.toFloat()
        updateSideControls()

        viewCounter.background?.apply {
            if(this is GradientDrawable) setColor(ContextCompat.getColor(context, stepperButtonColor))
        }
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {

                if(event.isInBounds(viewCounter)) {
                    startX = event.x
                    allowDragging = true
                } else if(sideTapEnabled) {
                    isTapped = true
                    viewCounter.x = event.x - viewCounter.width * 0.5f
                }

                parent.requestDisallowInterceptTouchEvent(true)
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                if (allowDragging) {
                    viewCounter.translationX = event.x - startX
                }
                return true
            }
            MotionEvent.ACTION_UP -> {
                allowDragging = false

                when {
                    viewCounter.translationX > viewCounter.width * 0.5 && allowPositive -> add()
                    viewCounter.translationX < -(viewCounter.width * 0.5) && allowNegative -> subtract()
                }

                if (viewCounter.translationX != 0f) {
                    val animX = SpringAnimation(viewCounter, SpringAnimation.TRANSLATION_X, 0f)
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

    /**
     * Allow interact with negative section, if you disallow, the negative section will hide,
     * and it's not working
     * @param [allow] true if allow to use negative, false to disallow
     * */
    fun allowNegative(allow: Boolean) {
        allowNegative = allow
        updateSideControls()
    }

    /**
     * Allow interact with positive section, if you disallow, the positive section will hide,
     * and it's not working
     * @param [allow] true if allow to use positive, false to disallow
     * */
    fun allowPositive(allow: Boolean) {
        allowPositive = allow
        updateSideControls()
    }

    /**
     * Update visibility of the negative and positive views
     */
    private fun updateSideControls() {
        textViewNegative.setVisibility(allowNegative)
        textViewPositive.setVisibility(allowPositive)

        textViewNegative.setTextColor(
            ContextCompat.getColor(context,
                if (count == minValue) stepperActionColorDisabled else stepperActionColor)
        )
        textViewPositive.setTextColor(
            ContextCompat.getColor(context,
                if (count == maxValue) stepperActionColorDisabled else stepperActionColor)
        )
    }

    fun setTextSize(pixels: Float) {
        viewCounterText.textSize = pixels
    }

    fun addStepCallback(callback: OnStepCallback) {
        callbacks.add(callback)
    }

    fun removeStepCallback(callback: OnStepCallback) {
        callbacks.remove(callback)
    }

    private fun notifyStepCallback(value: Int, positive: Boolean) {
        callbacks.forEach { it.onStep(value, positive) }
    }

    fun setValue(value: Int) {
        count = value
    }

    private fun add() {
        if (count != maxValue) count += 1
    }

    private fun subtract() {
        if (count != minValue) count--
    }

    override fun onDraw(canvas: Canvas) {
        clippingBounds.set(canvas.clipBounds)
        val r: Float = height.toFloat() / 2
        clipPath.addRoundRect(clippingBounds, r, r, Path.Direction.CW)
        canvas.clipPath(clipPath)
        super.onDraw(canvas)
    }

    private fun MotionEvent.isInBounds(view: View): Boolean {
        val rect = Rect()
        view.getHitRect(rect)
        return rect.contains(x.toInt(), y.toInt())
    }

    private fun View.setVisibility(isVisible: Boolean) {
        visibility = if(isVisible) View.VISIBLE else View.INVISIBLE
    }
}
