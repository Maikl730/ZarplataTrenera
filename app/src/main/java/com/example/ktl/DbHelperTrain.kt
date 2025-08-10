package com.example.ktl

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DbHelperTrain(val context: Context,val factory: SQLiteDatabase.CursorFactory?):
    SQLiteOpenHelper(context,"trains",factory,1) {




    override fun onCreate(db: SQLiteDatabase?) {
        val query = "CREATE TABLE trains (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, data TEXT, time TEXT, description TEXT, col INTEGER, price INTEGER, user INTEGER)"
        db!!.execSQL(query)

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS trains")
        onCreate(db)
    }


    fun addItem(item: Item){
        val values = ContentValues()
        values.put("name", item.name)
        values.put("data", item.data)
        values.put("time", item.time)
        values.put("description", item.desc)
        values.put("col", item.col)
        values.put("price", item.price)
        values.put("user", item.user)





        val db = this.writableDatabase
        db.insert("trains",null,values)
        db.close()

    }


    @SuppressLint("Range")
    fun getItem(data1: String, data2: String, user: Int):ArrayList<Item> {
        val result = arrayListOf<Item>()
        val db =this.readableDatabase
        val cursor: Cursor =db.rawQuery("SELECT * FROM trains WHERE user = '$user' " +
                "AND data BETWEEN  '$data1' AND '$data2' ORDER BY data , time",null)

        if(cursor.moveToFirst()){

            val id:Int = cursor.getColumnIndex("id")
            val name:Int = cursor.getColumnIndex("name")
            val data:Int = cursor.getColumnIndex("data")
            val time:Int = cursor.getColumnIndex("time")
            val description:Int = cursor.getColumnIndex("description")
            val col:Int = cursor.getColumnIndex("col")
            val price:Int = cursor.getColumnIndex("price")
            val user:Int = cursor.getColumnIndex("user")

            do{
                val todo =Item(
                    cursor.getInt(id),
                    cursor.getString(name),
                    cursor.getString(data),
                    cursor.getString(time),
                    cursor.getString(description),
                    cursor.getInt(col),
                    cursor.getInt(price),
                    cursor.getInt(user)
                )
                result.add(todo)

            }while(cursor.moveToNext())

        }
        cursor.close()
        return result

    }

    fun deleteTrain(id:Int){
        val db = this.writableDatabase

        db!!.execSQL("DELETE FROM trains WHERE id = '$id'")

        db.close()
    }


    fun rewriteItem(item: Item, id:Int){
        val values = ContentValues()
        values.put("name", item.name)
        values.put("data", item.data)
        values.put("time", item.time)
        values.put("description", item.desc)
        values.put("col", item.col)
        values.put("price", item.price)
        values.put("user", item.user)





        val db = this.writableDatabase
        db!!.execSQL("UPDATE trains SET name ='${values.get("name")}'," +
                "data = '${values.get("data")}'," +
                "time = '${values.get("time")}'," +
                "description = '${values.get("description")}'," +
                "col = '${values.get("col")}'," +
                "price = '${values.get("price")}'," +
                "user = '${values.get("user")}'" +
                "  WHERE id =$id")

        db.close()

    }

    fun getSum(data1: String, data2: String, user: Int):Int
    {
        val db =this.readableDatabase
        val cursor: Cursor =db.rawQuery("SELECT SUM(price) FROM trains WHERE user = '$user' " +
                "AND data BETWEEN  '$data1' AND '$data2' ",null)

        cursor.moveToFirst()


        val result:Int = cursor.getInt(0)

        cursor.close()
        db.close()

        return result
    }

    fun getLastId():Int{
        val db = this.readableDatabase

        val cursor:Cursor = db.rawQuery("SELECT MAX(id) FROM trains  ", null)
        cursor.moveToFirst()

       // val idId = cursor.getColumnIndex("id")

        val lastId: Int = cursor.getInt(0)
        return  lastId
    }

}