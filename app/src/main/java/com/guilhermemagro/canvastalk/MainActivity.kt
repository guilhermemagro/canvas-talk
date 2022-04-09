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
        ChartData(20, "R$ 20,00", "01/01/2022"),
        ChartData(25, "R$ 25,00", "02/01/2022"),
        ChartData(16, "R$ 16,00", "03/01/2022"),
        ChartData(30, "R$ 30,00", "04/01/2022"),
        ChartData(22, "R$ 22,00", "05/01/2022"),
    )

    private fun getLotsOfData() = listOf(
        ChartData(20, "R$ 20,00", "01/01/2022"),
        ChartData(25, "R$ 25,00", "02/01/2022"),
        ChartData(16, "R$ 16,00", "03/01/2022"),
        ChartData(30, "R$ 30,00", "04/01/2022"),
        ChartData(22, "R$ 22,00", "05/01/2022"),
        ChartData(10, "R$ 10,00", "06/01/2022"),
        ChartData(12, "R$ 12,00", "07/01/2022"),
        ChartData(16, "R$ 16,00", "08/01/2022"),
        ChartData(27, "R$ 27,00", "09/01/2022"),
        ChartData(21, "R$ 21,00", "10/01/2022"),
        ChartData(40, "R$ 40,00", "11/01/2022"),
        ChartData(32, "R$ 32,00", "12/01/2022"),
        ChartData(20, "R$ 20,00", "13/01/2022"),
        ChartData(17, "R$ 17,00", "14/01/2022"),
        ChartData(38, "R$ 38,00", "15/01/2022"),
        ChartData(41, "R$ 41,00", "16/01/2022"),
        ChartData(43, "R$ 43,00", "17/01/2022"),
        ChartData(37, "R$ 37,00", "18/01/2022"),
        ChartData(24, "R$ 24,00", "19/01/2022"),
        ChartData(15, "R$ 15,00", "20/01/2022"),
        ChartData(33, "R$ 33,00", "21/01/2022"),
        ChartData(47, "R$ 47,00", "22/01/2022"),
        ChartData(55, "R$ 55,00", "23/01/2022"),
        ChartData(53, "R$ 53,00", "24/01/2022"),
        ChartData(37, "R$ 37,00", "25/01/2022"),
        ChartData(35, "R$ 35,00", "26/01/2022"),
        ChartData(38, "R$ 38,00", "27/01/2022"),
        ChartData(28, "R$ 28,00", "28/01/2022"),
        ChartData(25, "R$ 25,00", "29/01/2022"),
        ChartData(22, "R$ 22,00", "30/01/2022"),
    )
}
