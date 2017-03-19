package nl.dionsegijn.steppertouch

/**
 * Created by dionsegijn on 3/19/17.
 */
interface OnStepCallback {
    fun onStep(value: Int, positive: Boolean)
}
