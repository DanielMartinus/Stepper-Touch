package nl.dionsegijn.steppertouch

import android.support.annotation.ColorInt

/**
 * Created by dionsegijn on 3/19/17.
 */
interface Stepper {
    fun addStepCallback(callback: OnStepCallback)
    fun removeStepCallback(callback: OnStepCallback)
    fun notifyStepCallback(value: Int, positive: Boolean)
    fun setMax(value: Int)
    fun setMin(value: Int)
    fun getValue(): Int
    fun setValue(value: Int)
    fun setStepperTextColor(@ColorInt color: Int)
}
