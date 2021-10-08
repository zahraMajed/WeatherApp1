package com.example.weatherapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.coroutines.*
import org.json.JSONObject
import java.lang.Exception
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    //my variables
    var city ="10001"

    //my xml components
    lateinit var CL_main:ConstraintLayout
    lateinit var tvCountryCity:TextView
    lateinit var tvDataTime:TextView
    lateinit var tvStatus:TextView
    lateinit var tvDegree:TextView
    lateinit var tvDegreeLow:TextView
    lateinit var tvDegreeHighe:TextView

    lateinit var tvSunriceTime:TextView
    lateinit var tvSunseTime:TextView
    lateinit var tvWindTime:TextView
    lateinit var tvPressTime:TextView
    lateinit var tvHumidityTime:TextView
    lateinit var tvRefresh:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        tvCountryCity=findViewById(R.id.tvCountryCity)
        tvDataTime=findViewById(R.id.tvDataTime)
        tvStatus=findViewById(R.id.tvStatus)
        tvDegree=findViewById(R.id.tvDegree)
        tvDegreeLow=findViewById(R.id.tvDegreeLow)
        tvDegreeHighe=findViewById(R.id.tvDegreeHighe)

        tvSunriceTime=findViewById(R.id.tvSunriceTime)
        tvSunseTime=findViewById(R.id.tvSunseTime)
        tvWindTime=findViewById(R.id.tvWindTime)
        tvPressTime=findViewById(R.id.tvPressTime)
        tvHumidityTime=findViewById(R.id.tvHumidityTime)
        tvRefresh=findViewById(R.id.tvRefresh)


        requestAPI()
        tvCountryCity.setOnClickListener(){
            val intent= Intent(this,ZipCode::class.java)
            startActivity(intent)
        }

    }//end onCeate()


    //
    fun requestAPI(){

        CoroutineScope(Dispatchers.IO).launch {
            var data = async {
                fetchData()
            }.await()

            if (data.isNotEmpty()){
                displayResult(data)
            }

        }
    }//end requestAPI()

    fun fetchData():String{
        var extra1=intent.extras
        if (extra1 != null) {
            var extra2=extra1.getString("zip")
            if (extra2 != null) {
                city = extra2
            }
        }
        var response=""
        try {
            response= URL("http://api.openweathermap.org/data/2.5/weather?zip=$city,us&units=metric&appid=28afddda30bbb236f328226b4a55e07c")
                .readText(Charsets.UTF_8)
        }catch (e:Exception){
            println("Error $e")
        }
        return response
    }//end fetchData()

    suspend fun displayResult(data: String){
        withContext(Dispatchers.Main){
            val obj=JSONObject(data)
            val lastUpdate=obj.getLong("dt")
            val lastUpdateText = "Updated at: " + SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(Date(lastUpdate*1000))

            var weatherStatus= obj.getJSONArray("weather").getJSONObject(0).getString("description")
            //var weatherStatus= obj.getJSONObject("weather").getString("description")

            var weatherTemp=obj.getJSONObject("main")
            var degreeTemp=weatherTemp.getString("temp")
            var degreeLow=weatherTemp.getString("temp_min")
            var degreeHigh=weatherTemp.getString("temp_max")
            var pressure=weatherTemp.getString("pressure")
            var humidity=weatherTemp.getString("humidity")

            var windSpeed=obj.getJSONObject("wind").getString("speed")

            var generalInfo=obj.getJSONObject("sys")
            var country=generalInfo.getString("country")
            var city=obj.getString("name")
            var sunRiseT=generalInfo.getLong("sunrise")
            var sunSetT=generalInfo.getLong("sunset")

            //we have timezone as well

            tvCountryCity.text="$city, $country".uppercase()
            tvDataTime.text=lastUpdateText

            tvStatus.text=weatherStatus
            tvDegree.text="${degreeTemp.substring(0,degreeTemp.indexOf('.'))} °C"
            tvDegreeLow.text="Low: ${degreeLow.substring(0,degreeLow.indexOf('.'))} °C"
            tvDegreeHighe.text="High: ${degreeHigh.substring(0,degreeHigh.indexOf('.'))} °C"

            tvPressTime.text= pressure.toString()
            tvHumidityTime.text=humidity.toString()
            tvWindTime.text=windSpeed.toString()

            tvSunriceTime.text= SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunRiseT*1000))
            tvSunseTime.text= SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunSetT*1000))

        }
    }//end


}//end class()