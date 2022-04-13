package com.guilhermemagro.canvastalk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.AppCompatButton

class MainActivity : AppCompatActivity() {

    lateinit var myChart: MyChart
    lateinit var btnLittleData: AppCompatButton
    lateinit var btnLotsOfData: AppCompatButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bindViews()
        setListeners()
    }

    private fun bindViews() {
        myChart = findViewById(R.id.my_chart)
        btnLittleData = findViewById(R.id.btn_little_data)
        btnLotsOfData = findViewById(R.id.btn_lots_of_data)
    }

    private fun setListeners() {
        btnLittleData.setOnClickListener {
            myChart.chartData = getLittleData()
        }

        btnLotsOfData.setOnClickListener {
            myChart.chartData = getLotsOfData()
        }
    }

    private fun getLittleData() = listOf(
        ChartData("01/01/2022", "R$ 20,00", 20),
        ChartData("02/01/2022", "R$ 25,00", 25),
        ChartData("03/01/2022", "R$ 16,00", 16),
        ChartData("04/01/2022", "R$ 30,00", 30),
        ChartData("05/01/2022", "R$ 22,00", 22)
    )

    private fun getLotsOfData() = listOf(
        ChartData("01/01/2022", "R$ 20,00", 20),
        ChartData("02/01/2022", "R$ 25,00", 25),
        ChartData("03/01/2022", "R$ 16,00", 16),
        ChartData("04/01/2022", "R$ 30,00", 30),
        ChartData("05/01/2022", "R$ 22,00", 22),
        ChartData("06/01/2022", "R$ 10,00", 10),
        ChartData("07/01/2022", "R$ 12,00", 12),
        ChartData("08/01/2022", "R$ 16,00", 16),
        ChartData("09/01/2022", "R$ 27,00", 27),
        ChartData("10/01/2022", "R$ 21,00", 21),
        ChartData("11/01/2022", "R$ 40,00", 40),
        ChartData("12/01/2022", "R$ 32,00", 32),
        ChartData("13/01/2022", "R$ 20,00", 20),
        ChartData("14/01/2022", "R$ 17,00", 17),
        ChartData("15/01/2022", "R$ 5,00", 5),
        ChartData("16/01/2022", "R$ 41,00", 41),
        ChartData("17/01/2022", "R$ 43,00", 43),
        ChartData("18/01/2022", "R$ 37,00", 37),
        ChartData("19/01/2022", "R$ 24,00", 24),
        ChartData("20/01/2022", "R$ 15,00", 15),
        ChartData("21/01/2022", "R$ 33,00", 33),
        ChartData("22/01/2022", "R$ 47,00", 47),
        ChartData("23/01/2022", "R$ 55,00", 55),
        ChartData("24/01/2022", "R$ 53,00", 53),
        ChartData("25/01/2022", "R$ 37,00", 37),
        ChartData("26/01/2022", "R$ 35,00", 35),
        ChartData("27/01/2022", "R$ 38,00", 38),
        ChartData("28/01/2022", "R$ 28,00", 28),
        ChartData("29/01/2022", "R$ 25,00", 25),
        ChartData("30/01/2022", "R$ 22,00", 22)
    )
}
