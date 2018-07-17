package com.example.g015c1140.samplememoapp

class Setting {

    val ipAdd = "192.168.11.4"
    val userId = "2"

    val getAllMemoURL = "http://$ipAdd:3000/api/v1/selectmemo/all?userid=$userId"

    val insetMemoURL = "http://$ipAdd:3000/api/v1/insertmemo/"

    val deleteMemoURL = "http://$ipAdd:3000/api/v1/deletememo/"

    val updateMemoURL = "http://$ipAdd:3000/api/v1/updatememo/"
}