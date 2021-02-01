package com.example.curvechartdemo

import android.R
import android.content.Context
import android.util.Log
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import kotlinx.android.synthetic.main.marker_view.view.*


class CustomMarkerView (context: Context?, layoutResource: Int) : MarkerView(context, layoutResource) {

    // callbacks everytime the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    override fun refreshContent(e: Entry, highlight: Highlight?) {
        Log.i("Yo", "How's it going?")
        tvContent.text = "" + e.y + "h"; // set the entry-value as the display text
        super.refreshContent(e, highlight)
    }

    fun getXOffset(xpos: Float): Int {
        // this will center the marker-view horizontally
        return -(width / 2)
    }

    fun getYOffset(ypos: Float): Int {
        // this will cause the marker-view to be above the selected value
        return -height
    }

    init {


    }
}