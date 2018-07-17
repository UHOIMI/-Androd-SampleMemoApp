package com.example.g015c1140.samplememoapp

import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.EditText
import android.widget.Toast
import org.json.JSONArray
import org.json.JSONObject



class EditActivity : AppCompatActivity() {

    lateinit var mTitleEdit: EditText
    lateinit var mDetailEdit: EditText

    var position: Int = 0
    lateinit var pref: SharedPreferences
    lateinit var titleJsonArray: JSONArray
    lateinit var detailJsonArray: JSONArray

    var mMemo = JSONObject()
    var mEditFlg = -1
    lateinit var mNewOrEdit: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        mTitleEdit = findViewById(R.id.titleTextEdit)
        mDetailEdit = findViewById(R.id.detailTextEdit)

        mEditFlg = intent.getIntExtra("EditFlg",-1)
        when(mEditFlg){
            -1 ->{}
            else -> {
                mNewOrEdit = intent.getStringExtra("Memo")
                println("testExtra:  ${ mNewOrEdit }")
                mMemo = JSONObject(mNewOrEdit)
                mTitleEdit.setText(mMemo.getString("title"))
                mDetailEdit.setText(mMemo.getString("memo"))
            }
        }
    }

    fun saveButtonTapped(view: View){

        when {
            mTitleEdit.text.toString().isNullOrEmpty() -> {
                Toast.makeText(this, "タイトルは必須項目です", Toast.LENGTH_SHORT).show()
                mTitleEdit.error = "何か入力してください"
            }
            else -> {
                when{
                    mDetailEdit.text.toString().isNullOrEmpty() -> {
                        mDetailEdit.setText("")
                    }
                }

                when(mEditFlg){
                    -1 -> PostTask().execute("Insert",mTitleEdit.text.toString(),mDetailEdit.text.toString())
                    else -> PostTask().execute("Update",mMemo.getString("id"),mTitleEdit.text.toString(),mDetailEdit.text.toString())
                }
                Toast.makeText(this,"保存しました",Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}