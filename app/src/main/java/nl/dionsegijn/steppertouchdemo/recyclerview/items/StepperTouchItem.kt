package nl.dionsegijn.steppertouchdemo.recyclerview.items

import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import kotlinx.android.synthetic.main.item_text.view.textView
import nl.dionsegijn.steppertouchdemo.R

class StepperTouchItem(): Item() {

    override fun getLayout() = R.layout.item_stepper_touch

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {}


}
