package com.example.getsunset

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


    }

    fun GetSunset(view: View) {
        var city = etCityName.text.toString()
        val url = "https://weather-ydn-yql.media.yahoo.com/forecastrss?location="+city+",ca&format=json"
        MyAsyncTask().execute(url)
    }

    inner class MyAsyncTask : AsyncTask<String, String, String>() {

        override fun onPreExecute() {
            //Before task started
        }
        override fun doInBackground(vararg params: String?): String {
            //TODO HTTP call.
            try{
                val url = URL(params[0])

                val urlConnect = url.openConnection() as HttpURLConnection
                urlConnect.connectTimeout = 7000

                var inString = ConvertStreamToString(urlConnect.inputStream)
                // Cannot access to UI
                publishProgress(inString)

            }catch (ex : Exception){}

            return " "
        }

        override fun onProgressUpdate(vararg values: String?) {
            try {
                var json = JSONObject(values[0])
                val currentObservation = json.getJSONObject("current_observation")
                val astronomy = currentObservation.getJSONObject("astronomy")
                var sunset = astronomy.getString("sunset")

                tvSunsetTime.text ="Sunset Time is "+sunset

            }catch (ex :Exception){}
        }
        override fun onPostExecute(result: String?) {
            //after task done
        }
    }
    fun ConvertStreamToString(inputStream: InputStream):String{

        val bufferReader = BufferedReader(InputStreamReader(inputStream))
        var line:String
        var AllString:String = ""

        try {
            do {
                line = bufferReader.readLine()
                if (line != null){
                    AllString += line
                }
            }while (line != null)

        }catch (ex: Exception){}

        return AllString
    }


}
