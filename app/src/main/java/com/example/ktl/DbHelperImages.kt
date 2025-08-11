package com.example.ktl

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteDatabase.CursorFactory
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64

class DbHelperImages(val context: Context,val factory: SQLiteDatabase.CursorFactory?):
    SQLiteOpenHelper(context,"images",factory,1){


    override fun onCreate(db: SQLiteDatabase?) {
        val query = "CREATE TABLE images (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "image64 TEXT, idtrain INTEGER,  user INTEGER)"
        db!!.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS images")
        onCreate(db)
    }

    fun addImage(image64:String, idtrain : Int, user : Int){

        val db = this.writableDatabase

        val query ="INSERT INTO images (image64, idtrain, user )VALUES ('$image64','$idtrain','$user')"
        db!!.execSQL(query)

        db.close()
       /* val values = ContentValues()
        values.put("image64", image64)
        values.put("idtrain", idtrain)
        values.put("user", user)


        val db = this.writableDatabase
        db.insert("images",null,values)
        db.close()*/
    }

    fun isExist(idtrain:Int,user:Int):Boolean{
        val db = this.readableDatabase

        /*
        val cursor:Cursor = db.rawQuery("EXISTS(SELECT * FROM images WHERE user = '$user' " +
                "AND idtrain =  '$idtrain' ) ",null)

        cursor.moveToFirst()
        val preresult:Int =cursor.getInt(0)

        val result:Boolean
        if(preresult==1){
            result=true
        }else{
            result=false
        }

        db.close()
        return  result
        */

        val result = db.rawQuery("SELECT * FROM images WHERE user = '$user' AND idtrain ='$idtrain'",null)
        return result.moveToFirst()
    }

    fun getImage(idtrain:Int,user:Int):String{
        val db = this.readableDatabase

        val cursor: Cursor =db.rawQuery("SELECT * FROM images WHERE user = '$user'" +
                "AND idtrain =  '$idtrain'  ",null)

        if (cursor.moveToFirst()) {

            cursor.moveToFirst()


            val dd = cursor.getColumnIndex("image64")
            val image64: String = cursor.getString(dd)

            cursor.close()
            db.close()

            val result = image64
            return result
        }else {

            return ""
        }

        //val decodedString: ByteArray = Base64.decode(image64,Base64.DEFAULT)

       // val resultBitMap:Bitmap =BitmapFactory.decodeByteArray(decodedString,0,
            //decodedString.size)




    }

    fun changeImage(idImage:Int , uriImage:String, idtrain: Int,userId:Int){
        val db = this.readableDatabase

        if(isExist(idtrain,userId)) {
           /* db.rawQuery(
                "UPDATE images SET image64 = '$uriImage' WHERE id = '$idImage' ", null
            )*/

            val query = "UPDATE images SET image64 = '$uriImage' WHERE id = '$idImage' "
           db!!.execSQL(query)

            println("ЗАМЕНИЛ ФОТО !!! ID image = $idImage uri = $uriImage")
        }else{
            addImage(uriImage,idtrain,userId)
            println("НЕ СМОГ ЗАМЕНИТЬ. ДОБАВИЛ НОВОЕ ФОТО !!!")
        }
    }

    fun getImageId(idtrain: Int,user: Int):Int{

        if(isExist(idtrain,user)) {
            val db = this.readableDatabase
            val cursor: Cursor = db.rawQuery(
                "SELECT * FROM images WHERE user = '$user' " +
                        "AND idtrain =  '$idtrain'  ", null
            )
            cursor.moveToFirst()

            val idid = cursor.getColumnIndex("id")


            return cursor.getInt(idid)
        }else{
            return 0
        }
    }

    fun deleteImage(idtrain: Int,user: Int) {
        val db = this.writableDatabase

        if (isExist(idtrain, user)) {
            db!!.execSQL("DELETE FROM images WHERE user = '$user' AND idtrain =  '$idtrain' ")
        }
    }

}