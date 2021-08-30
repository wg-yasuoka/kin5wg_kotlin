package kin5.wg.sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

import android.widget.EditText
import android.widget.Button
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.serchBtn).setOnClickListener {
            /* 画面にて指定されたIDを取得する */
            val editText = findViewById<EditText>(R.id.editTextId)
            val id = editText?.text

            //非同期処理の開始。
            // httpリクエストは非同期にしないとダメらしい
            val thread = Thread() {
                //println("非同期処理の結果:${startGetRequest(id.toString())}")
                val str: String =startGetRequest(id.toString())
                try {
                    // 戻りは配列式のjsonarrayで取得
                    val jsonArray =  JSONArray(str)

                    //配列の1つ目（1つしかないけど）を取得
                    val jsonObject: JSONObject = jsonArray.getJSONObject(0)

                    //JSON内のname属性を取得
                    val name = jsonObject.getString("name")
                    findViewById<TextView>(R.id.textName).text = name
                    //println("name = $name")

                }catch(e: JSONException){
                    e.printStackTrace()
                }
            }.start()

        }
    }

    fun startGetRequest(id: String): String {
        // HttpURLConnectionの作成
        // アプリからはローカルサーバのアドレスは「10.0.2.2」になるらしい
        val url = URL("http://10.0.2.2:8080/user?id=$id")
        val connection = url.openConnection() as HttpURLConnection
        val br = BufferedReader(InputStreamReader(connection.inputStream))

        var line: String? = null
        val sb = StringBuilder()
        sb.append(br.readLines())
/* 取得結果が複数行ある場合はこっち
        for (line in br.readLines()) {
            line?.let { sb.append(line) }
        }
*/

        br.close()

        return sb.toString()
    }

}