package nl.dionsegijn.steppertouch

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.TextView
import kotlin.properties.Delegates

/**
 * Created by dionsegijn on 3/19/17.
 */
internal class StepperCounter : LinearLayout, Stepper {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    var viewStepCounter: TextView? = null
        get() {
            return findViewById(R.id.viewTextStepperCount) as TextView
        }

    var count: Int by Delegates.observable(0) { _, old, new ->
        updateView(new); notifyStepCallback(new, new > old)
    }

    var maxValue: Int = Integer.MAX_VALUE
    var minValue: Int = Integer.MIN_VALUE
    val callbacks: MutableList<OnStepCallback> = mutableListOf()

    init {
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            elevation = 4f
        }
    }

    override fun addStepCallback(callback: OnStepCallback) {
        callbacks.add(callback)
    }

    override fun removeStepCallback(callback: OnStepCallback) {
        callbacks.remove(callback)
    }

    override fun notifyStepCallback(value: Int, positive: Boolean) {
        callbacks.forEach { it.onStep(value, positive) }
    }

    override fun getValue(): Int {
        return count
    }

    override fun setMax(value: Int) {
        maxValue = value
    }

    override fun setMin(value: Int) {
        minValue = value
    }

    override fun setValue(value: Int) {
        count = value
        updateView(count)
    }

    override fun setStepperTextColor(color: Int) {
        viewStepCounter?.setTextColor(color)
    }

    internal fun add() {
        if (count != maxValue) count += 1
    }

    internal fun subtract() {
        if (count != minValue) count--
    }

    private fun updateView(value: Int) {
        viewStepCounter?.text = value.toString()
    }
}
