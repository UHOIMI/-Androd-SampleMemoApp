package com.example.g015c1140.samplememoapp

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_detail.*
import org.json.JSONArray
import org.json.JSONObject

class DetailActivity : AppCompatActivity() {

    lateinit var mTitleText: TextView
    lateinit var mDetailText: TextView

    var mPosition: Int = 0
    lateinit var pref: SharedPreferences
    lateinit var titleJsonArray: JSONArray
    lateinit var detailJsonArray: JSONArray

    var mMemo = JSONObject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        mTitleText = findViewById(R.id.titleTextView)
        mDetailText = findViewById(R.id.detailTextView)
        mPosition = intent.extras.getInt("DetailPosition")

        mMemo = JSONArray(intent.extras.getString("MemoList")).getJSONObject(mPosition)
        var titleText = mMemo.getString("title")
        var detailText = mMemo.getString("memo")

        mTitleText.text = titleText
        mDetailText.text = detailText
    }

    fun onDeleteButtonTapped(view: View){
        PostTask().execute("Delete",mMemo.getString("id"))

        Toast.makeText(this, "削除しました", Toast.LENGTH_SHORT).show()
        finish()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_detail, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if (item.itemId == R.id.editButton){
            startActivity(Intent(this, EditActivity::class.java).putExtra("Memo", mMemo.toString()).putExtra("EditFlg", 1))
            finish()
            return true
        }
        return false
    }
}
