package com.example.weatherapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout

class ZipCode : AppCompatActivity() {

    //Reentering zip code components:
    lateinit var LLzipEntering: LinearLayout
    lateinit var edZipCode: EditText
    lateinit var btnZipCode: Button
    lateinit var LLerror: LinearLayout
    lateinit var btnRetry: Button

    var zip=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_zip_code)


        LLzipEntering=findViewById(R.id.LLzipEntering)
        edZipCode=findViewById(R.id.edZipCode)
        btnZipCode=findViewById(R.id.btnZipCode)

        LLerror=findViewById(R.id.LLerror)
        btnRetry=findViewById(R.id.btnRetry)

        btnZipCode.setOnClickListener(){
            zipCode()
        }

        btnRetry.setOnClickListener() {
            intent= Intent(this,MainActivity::class.java)
            startActivity(intent)
        }


    }//end onCteare()

    fun zipCode(){
        zip= edZipCode.text.toString()
        if (zip.isNotEmpty()){
            sendZip()
        }else {
            LLzipEntering.visibility= View.GONE
            LLerror.visibility=View.VISIBLE
        }
    }//end zipCode()

    fun sendZip(){
        val intent= Intent(this,MainActivity::class.java)
        intent.putExtra("zip",zip)
        startActivity(intent)
    }


}