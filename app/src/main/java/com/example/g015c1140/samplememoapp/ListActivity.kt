package com.example.g015c1140.samplememoapp

import android.content.Intent
import android.content.SharedPreferences
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_list.*
import org.json.JSONArray
import org.json.JSONException
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL


class ListActivity : AppCompatActivity() {

    lateinit var mPref: SharedPreferences
    lateinit var mEditer: SharedPreferences.Editor
    lateinit var arrayAdapter: ArrayAdapter<String>

    var JsonArray = JSONArray()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
        setSupportActionBar(toolbar)

        var listView: ListView = findViewById(R.id.listView)
        arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1)

        listView.adapter = arrayAdapter

        listView.setOnItemClickListener {parent, view, position, id ->

            // 項目の TextView を取得
            val itemTextView : TextView = view.findViewById(android.R.id.text1)
            startActivity(Intent(this, DetailActivity::class.java).putExtra("DetailPosition", position).putExtra("MemoList", JsonArray.toString()))
            Toast.makeText(this, "tappedText：${itemTextView.text}   position:$position      " , Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        GetAllMemoTask().execute()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_list, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if (item.itemId == R.id.addButton){
            Toast.makeText(this, "新規作成", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, EditActivity::class.java))
            return true
        }
        return false
    }

    //非同期処理用クラス
    inner class GetAllMemoTask: AsyncTask<String, String, String>() {

        override fun doInBackground(vararg params: String?): String? {
            //ここでAPIを叩きます。バックグラウンドで処理する内容です。
            var connection: HttpURLConnection? = null
            var reader: BufferedReader? = null
           // val buffer: StringBuffer

            try {
                //param[0]にはAPIのURI(String)を入れます(後ほど)。
                //AsynkTask<...>の一つめがStringな理由はURIをStringで指定するからです。
                val url = URL(Setting().getAllMemoURL)
                connection = url.openConnection() as HttpURLConnection
                connection.connect()  //ここで指定したAPIを叩いてみてます。

                //ここから叩いたAPIから帰ってきたデータを使えるよう処理していきます。

                val br = BufferedReader(InputStreamReader(connection.inputStream))
                val sb = StringBuilder()
                for (line in br.readLines()) {
                    line.run { sb.append(line) }
                }

                val titleArrayList = ArrayList<String>()

                try {
                    JsonArray = JSONArray(sb.toString())
                    for (i in 0 until JsonArray.length()) {
                        println("array.getJSONObject(i):${JsonArray.getJSONObject(i)}")
                        titleArrayList.add(JsonArray.getJSONObject(i).getString("title"))
                    }
                } catch (e: JSONException) {
                    println("error")
                }
                br.close()
                return titleArrayList.toString()

                //ここから下は、接続エラーとかJSONのエラーとかで失敗した時にエラーを処理する為のものです。
            } catch (e: MalformedURLException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            //finallyで接続を切断してあげましょう。
            finally {
                connection?.disconnect()
                try {
                    reader?.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            //失敗した時はnullやエラーコードなどを返しましょう。
            return null
        }

        //返ってきたデータをビューに反映させる処理はonPostExecuteに書きます。これはメインスレッドです。
        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            if (result == null) return

            val titleList = JSONArray(result)

            println("titleList${titleList}")
            arrayAdapter.clear()
            for (i in 0 until titleList.length()) {
                arrayAdapter.insert(titleList[i] as String?, i)
            }
            arrayAdapter.notifyDataSetChanged()
        }
    }
}
