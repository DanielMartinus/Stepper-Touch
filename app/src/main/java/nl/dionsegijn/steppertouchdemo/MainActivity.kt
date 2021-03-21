package nl.dionsegijn.steppertouchdemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import nl.dionsegijn.steppertouch.OnStepCallback
import nl.dionsegijn.steppertouchdemo.recyclerview.RecyclerViewFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        stepperTouch.minValue = 0
        stepperTouch.maxValue = 5
        stepperTouch.addStepCallback(object : OnStepCallback {
            override fun onStep(value: Int, positive: Boolean) {
                Toast.makeText(applicationContext, value.toString(), Toast.LENGTH_SHORT).show()
            }
        })

        bottomStepperTouch.minValue = -10
        bottomStepperTouch.maxValue = 10
        bottomStepperTouch.sideTapEnabled = true
        bottomStepperTouch.addStepCallback(object : OnStepCallback {
            override fun onStep(value: Int, positive: Boolean) {
                // Hide and disable the negative or positive stepper when max or min is reached
                bottomStepperTouch.allowNegative(value > bottomStepperTouch.minValue)
                bottomStepperTouch.allowPositive(value < bottomStepperTouch.maxValue)
            }
        })

        supportFragmentManager.beginTransaction().add(R.id.fragmentContainer, RecyclerViewFragment()).commit()
    }

}
