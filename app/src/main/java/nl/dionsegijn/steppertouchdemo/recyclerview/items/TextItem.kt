package nl.dionsegijn.steppertouchdemo.recyclerview.items

import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import kotlinx.android.synthetic.main.item_text.view.textView
import nl.dionsegijn.steppertouchdemo.R

class TextItem(val text: String): Item() {

    override fun getLayout() = R.layout.item_text

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.containerView.textView.setText(text)
    }


}
