package com.example.g015c1140.samplememoapp

import android.os.AsyncTask
import android.util.Log
import java.io.IOException
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL

class PostTask: AsyncTask<String, String, String>() {

    //insert
    override fun doInBackground(vararg params: String?): String? {
        //ここでAPIを叩きます。バックグラウンドで処理する内容です。
        var connection: HttpURLConnection? = null
        var result: String? = null

        try {
            //param[0]にはAPIのURI(String)を入れます(後ほど)。
            //AsynkTask<...>の一つめがStringな理由はURIをStringで指定するからです。
            val url = when(params[0]){
                "Insert" -> URL(Setting().insetMemoURL)
                "Update" -> URL(Setting().updateMemoURL)
                "Delete" -> URL(Setting().deleteMemoURL)
                else -> null
            }
            if (url == null){
                println("POSTTASK 引数異常URL：$params[0]")
                return  null
            }

            connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "POST"
            connection.instanceFollowRedirects = false
            connection.doOutput = true
            connection.connect()  //ここで指定したAPIを叩いてみてます。


            var out: OutputStream? = null
            try {

                out = connection.outputStream
                when(params[0]){
                    "Insert" -> out.write( "userid=${Setting().userId}&title=${params[1]}&memo=${params[2]}".toByteArray() )
                    "Update" -> out.write( "userid=${Setting().userId}&id=${params[1]}&title=${params[2]}&memo=${params[3]}".toByteArray() )
                    "Delete" -> out.write("userid=${Setting().userId}&id=${params[1]}".toByteArray())
                    else -> null
                }
                if (url == null){
                    println("POSTTASK 引数異常WRITE：$params[0]")
                    return  null
                }
                out.flush()
                Log.d("debug", "flush")
            } catch (e: IOException) {
                // POST送信エラー
                e.printStackTrace()
                result = "POST送信エラー"
            } finally {
                out?.close()
            }

            val status = connection.responseCode
            when(status){
                HttpURLConnection.HTTP_OK -> result = "HTTP-OK"
                else -> result="status=$status"
            }

        }catch (e: IOException) {
            e.printStackTrace()
        } finally {
            if (connection != null) {
                connection.disconnect()
            }
        }
        return result
        //finallyで接続を切断してあげましょう。
        //失敗した時はnullやエラーコードなどを返しましょう。
    }

    //返ってきたデータをビューに反映させる処理はonPostExecuteに書きます。これはメインスレッドです。
    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        if (result == null){
            println("リザルトNull")
            return
        }else{
            println("リザルトNullじゃねー　result：$result")
        }
    }
}