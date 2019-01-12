package nl.dionsegijn.steppertouchdemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import nl.dionsegijn.steppertouch.OnStepCallback


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        stepperTouch.stepper.setMin(0)
        stepperTouch.stepper.setMax(5)
        stepperTouch.stepper.addStepCallback(object : OnStepCallback {
            override fun onStep(value: Int, positive: Boolean) {
                Toast.makeText(applicationContext, value.toString(), Toast.LENGTH_SHORT).show()
            }
        })

        bottomStepperTouch.stepper.setMin(-10)
        bottomStepperTouch.stepper.setMax(10)
        bottomStepperTouch.enableSideTap(true)
        bottomStepperTouch.stepper.addStepCallback(object : OnStepCallback {
            override fun onStep(value: Int, positive: Boolean) {
                // Hide and disable the negative or positive stepper when max or min is reached
                bottomStepperTouch.allowNegative(value > bottomStepperTouch.stepper.getMin())
                bottomStepperTouch.allowPositive(value < bottomStepperTouch.stepper.getMax())
            }
        })
    }

}
