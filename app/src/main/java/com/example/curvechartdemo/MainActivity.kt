package com.example.curvechartdemo

import android.graphics.Color
import android.graphics.Color.green
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.IMarker
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.LimitLine.LimitLabelPosition
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.Utils
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.reflect.typeOf


class MainActivity : AppCompatActivity() {

    fun createLineChart(entries: ArrayList<Entry>, sleepTarget: Float, chartView: LineChart){

        chartView.invalidate()

        // Graph formatting:

            // Create target line at "sleepTarget"
            val targetLine = LimitLine(sleepTarget, "Target: " + sleepTarget + " hours")
            targetLine.setLineColor(this.getResources().getColor(R.color.targetLineColor))
            targetLine.lineWidth = 4f
            targetLine.labelPosition = LimitLabelPosition.RIGHT_TOP
            targetLine.textSize = 10f

            // Get the three axes
            val xAxis = chartView.getXAxis()
            val yAxis = chartView.getAxisLeft()
            val rightAxis = chartView.getAxisRight()

            // add limit line
            yAxis.addLimitLine(targetLine)

            // Set x-axis to be at the bottom of the graph
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);


            yAxis.setSpaceTop(20.0f);       // Adds 20% of the screen as whitespace to the top of graph
            rightAxis.setSpaceTop(20.0f);   // Need to do it on both the right and left side of the graph in order for it to work
            yAxis.setAxisMinimum(0.0f);     // Start at zero
            rightAxis.setAxisMinimum(0.0f);
            xAxis.setAxisMinimum(0.0f);     // Start at zero
            xAxis.setAxisMaximum(8.0f);


            xAxis.setValueFormatter(IndexAxisValueFormatter(arrayOf("","S", "M", "T", "W", "T", "F", "S","")))  // Set days at bottom of graph
            xAxis.setTextSize(19.0f)                                                                            // Set X axis style
            xAxis.setTypeface(Typeface.DEFAULT_BOLD)


        // Creating and formatting the dataset:

            val lineDataSet = LineDataSet(entries, null)                  // Label on the legend for the dataset is currently null, can be a string
            lineDataSet.color = resources.getColor(R.color.startGradient)       // Color of the line
            lineDataSet.setDrawFilled(true)                                     // To fill areas below curve
    //        lineDataSet.setFillAlpha(255);         // To set the transparency, doesn't seem to work so did it in chart_gradient.xml instead as the first two hex digits in the colour code
            lineDataSet.setDrawCircles(true);      // To stop/start drawing circles at the points
            lineDataSet.setDrawCircleHole(true);
            lineDataSet.circleRadius = 5.0f
            lineDataSet.circleHoleRadius = 3.5f
            //lineDataSet.setColor(Color.WHITE);
            lineDataSet.setCircleColor(Color.WHITE)
            lineDataSet.setCircleHoleColor(resources.getColor(R.color.circleHole))    // I know this is deprecated, but I could not (and I tried very hard) get the color to come out correctly any other way
            lineDataSet.setDrawValues(false);                                       // To not display values on the graph (they are only displayed when selected, done with the CustomMarkerView)
            lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);                     // For Bezier/curved line


            // For getting the gradient fill below the bezier/curved line
            lineDataSet.setDrawFilled(true);
            if (Utils.getSDKInt() >= 18) {
                // fill drawable only supported on api level 18 and above
                val drawable = ContextCompat.getDrawable(this, R.drawable.chart_gradient);
                lineDataSet.setFillDrawable(drawable);
            }
            else {
                lineDataSet.setFillColor(Color.BLUE);
            }


        // Getting data displayed on the chart (setData()), as well as other formatting (selection of points displays values, animation)

            val data = LineData(lineDataSet)

            chartView.setTouchEnabled(true)                 // Needed for MarkerView, Lets you touch (and move? Not sure) the graph
            chartView.setPinchZoom(true);                   // Lets you zoom in and out on the graph

            // Lets you display a value on the graph when it is selected
            chartView.setDrawMarkers(true)
            val marker: IMarker = CustomMarkerView(this, R.layout.marker_view)
            chartView.setMarker(marker)

            chartView.getXAxis().setDrawGridLines(true)     // Draw Vertical Gridlines
            chartView.getAxisLeft().setDrawGridLines(false);    // Remove Horizontal Gridlines, Left Axis
            chartView.getAxisRight().setDrawGridLines(false);   // Remove Horizontal Gridlines, Right Axis
            chartView.getAxisRight().setEnabled(false);     // Remove the right axis
            chartView.getDescription().setText("")          // Remove the graph description
            chartView.getLegend().setEnabled(false)         // Include/exclude a legend
            chartView.animateXY(0,7000,Easing.EaseInOutSine)   // Animation, liked this one because it reminded me of a wave
                                                                                        // Feel free to try different ones
                                                                                        // Can change the time taken on X and Y axes, and also can change the animation style by
                                                                                        // changing Easing.EaseInOutSine

            chartView.setData(data)


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Hypothetical data read from Firebase would be entries 1 through 7 (entry 0 and 8 are just there to center the graph)

        val sleepData = ArrayList<Entry>()
        sleepData.add(Entry(0.0f, 6.0f))        // This point is here to center the days of the week like in the XD file
        sleepData.add(Entry(1.0f, 7.5f))        // Actual "data"
        sleepData.add(Entry(2.0f, 8.0f))
        sleepData.add(Entry(3.0f, 7.5f))
        sleepData.add(Entry(4.0f, 4.0f))
        sleepData.add(Entry(5.0f, 6.0f))
        sleepData.add(Entry(6.0f, 6.0f))
        sleepData.add(Entry(7.0f, 5.0f))
        sleepData.add(Entry(8.0f, 6.0f))        // This point is here to center the days of the week like in the XD file

        createLineChart(sleepData, 6f, chart)

    }

}