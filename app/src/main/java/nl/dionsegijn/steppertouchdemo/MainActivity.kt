package nl.dionsegijn.steppertouchdemo

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import kotlinx.android.synthetic.main.view_credits.*
import nl.dionsegijn.steppertouch.OnStepCallback
import nl.dionsegijn.steppertouch.StepperTouch
import nl.dionsegijn.steppertouchdemo.util.HtmlFormatter


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val stepperTouch = findViewById(R.id.stepperTouch) as StepperTouch
        stepperTouch.stepper.setMin(0)
        stepperTouch.stepper.setMax(10)
        stepperTouch.stepper.addStepCallback(object : OnStepCallback {
            override fun onStep(value: Int, positive: Boolean) {
                Toast.makeText(applicationContext, value.toString(), Toast.LENGTH_SHORT).show()
            }
        })

        setCreditsAndSource()
    }

    fun setCreditsAndSource() {
        val htmlFormatter = HtmlFormatter()
        credits.text = htmlFormatter.stringToHtml(getString(R.string.credits))
        source.text = htmlFormatter.stringToHtml(getString(R.string.source))
        viewOpenGithub.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.github_repo))))
        }
    }
}
