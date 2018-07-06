package nl.dionsegijn.steppertouchdemo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import nl.dionsegijn.steppertouch.OnStepCallback
import nl.dionsegijn.steppertouch.StepperTouch


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super
                .onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val stepperTouch = findViewById(R.id.stepperTouch) as StepperTouch
        stepperTouch
                .stepper
                .setMin(0)
        stepperTouch
                .stepper
                .setMax(5)
        stepperTouch
                .stepper
                .addStepCallback(object : OnStepCallback {
                    override fun onStep(value: Int, positive: Boolean) {
                        Toast
                                .makeText(applicationContext, value.toString(), Toast.LENGTH_SHORT)
                                .show()
                    }
                })

        // options are set by xml only! :)
        val bottemStepperTouch = findViewById(R.id.bottomStepperTouch) as StepperTouch
    }

    // called from xml
    @Suppress("unused")
    fun onStep(newValue: Int, positive: Boolean) {
        Toast
                .makeText(this, "$newValue", Toast.LENGTH_SHORT)
                .show()
    }

}
