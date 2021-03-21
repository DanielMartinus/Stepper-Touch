package nl.dionsegijn.steppertouchdemo.recyclerview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.xwray.groupie.GroupieAdapter
import kotlinx.android.synthetic.main.fragment_recycler_view.recyclerView
import nl.dionsegijn.steppertouchdemo.R
import nl.dionsegijn.steppertouchdemo.recyclerview.items.StepperTouchItem
import nl.dionsegijn.steppertouchdemo.recyclerview.items.TextItem

class RecyclerViewFragment : Fragment() {

    private val adapter = GroupieAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_recycler_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.adapter = this.adapter
        recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        adapter.addAll(listOf(
            TextItem("First item"),
            TextItem("Second item"),
            StepperTouchItem(),
            TextItem("Fourth item"),
            TextItem("Fifth item"),
            StepperTouchItem(),
            TextItem("Seventh item"),
            TextItem("Eight item"),
            TextItem("Ninth item"),
            TextItem("Tenth item"),
            TextItem("Eleven item"),
            TextItem("Twelve item"),
            StepperTouchItem(),
            TextItem("Thirteen item"),
            TextItem("Fourteen item"),
        ))
        adapter.notifyDataSetChanged()
    }
}
