package com.example.ktl

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.yandex.mobile.ads.banner.AdSize
import com.yandex.mobile.ads.banner.BannerAdView
import java.util.Calendar

class ItemActivity : AppCompatActivity() {


    private val PICK_IMAGE_REQUEST = 1
    private lateinit var selectedImageUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_item)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val banner: BannerAdView = findViewById(R.id.banner2)
        banner.setAdUnitId("demo-banner-yandex")
        banner.setAdSize(AdSize.stickySize(350))
        val adRequest = com.yandex.mobile.ads.common.AdRequest.Builder().build()
        banner.loadAd(adRequest)

        selectedImageUri ="".toUri()
        var isImage:Boolean = false
        val button_delete: Button = findViewById(R.id.button_delete_a)
        val button_red: Button = findViewById(R.id.button_red_a)
        //val imageButton: ImageButton = findViewById(R.id.photo_item)




        var namein: String? =intent.getStringExtra("itemName")
        var data: String? =intent.getStringExtra("itemData")
        var time: String? = intent.getStringExtra("itemTime")

        val sP = getSharedPreferences("UserId",MODE_PRIVATE)
        val userId = sP.getInt("UserId",0)

        val name: TextView = findViewById(R.id.item_list_title_name_a)
        val dataB: Button = findViewById(R.id.button_data_a)
        val timeB: Button = findViewById(R.id.button_time_a)
        val desc: TextView = findViewById(R.id.item_list_desc_a)
        val col: TextView = findViewById(R.id.item_list_text_count_a)
        val price: TextView = findViewById(R.id.item_list_text_price_a)

        val id:Int = intent.getIntExtra("itemId",0)

        System.out.println("!!!!!!!!! $id !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
        name.text = intent.getStringExtra("itemName")
        dataB.text = intent.getStringExtra("itemData")

        timeB.text = intent.getStringExtra("itemTime")
        desc.text = intent.getStringExtra("itemDesc")

        col.text = intent.getIntExtra("itemCol",0).toString()
        price.text = intent.getIntExtra("itemPrice",0).toString()

/*
        val db = DbHelperImages(this,null)


        //Получаю Uri картинки

        if(db.isExist(id,userId)) {
            var newImageUri: String = db.getImage(id, userId)
            imageButton.setImageURI(newImageUri.toUri())

            println("ADDED ID Image = ${db.getImageId(id,userId)}")
            println("IMAGE URI IS " + newImageUri + " !!!!!!!!!!!!!!!!!!")
            imageButton.scaleY= 2F
            imageButton.scaleY= 2f
            isImage = true
        }else{

            isImage = false
            println("NO PICTURES!!!")
        }

*/

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)


        val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            // Display Selected date in textbox
            val month1=monthOfYear
            dataB.setText("" + dayOfMonth + ". " + monthFormat(month1+1) + ". " + year)
            data = makeDataString(year,monthOfYear,dayOfMonth)
        }, year, month, day)

        dataB.setOnClickListener { dpd.show() }

        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minutes = c.get(Calendar.MINUTE)


        val timePickerDialog = TimePickerDialog( this, TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            // Обработка событий изменения времени

            timeB.setText(makeTimeString(hourOfDay,minute))

            time=makeTimeString(hourOfDay,minute)

        }, hour, minutes, true )

        timeB.setOnClickListener { timePickerDialog.show() }

        button_delete.setOnClickListener {
            val db = DbHelperTrain(this,null)
           // val dbImage = DbHelperImages(this,null)


            db.deleteTrain(id)
           // dbImage.deleteImage(id,userId)

            val intent = Intent(this,ItemsActivity::class.java)
            startActivity(intent)
        }

        button_red.setOnClickListener {


            if(name.text.toString().trim().length>10){
                Toast.makeText(this, "Слишком длинное название", Toast.LENGTH_SHORT).show()
            }else {
                if (name.text.toString().trim().length < 2) {
                    Toast.makeText(this, "Слишком короткое название", Toast.LENGTH_SHORT).show()
                } else {

                    if (
                        dataB.text.toString().trim() == "" ||
                        timeB.text.toString().trim() == "" ||
                        name.text.toString().trim() == "" ||
                        col.text.toString().trim() == "" ||
                        price.text.toString().trim() == ""
                    ) {
                        Toast.makeText(this, "Не все поля заполнены", Toast.LENGTH_SHORT).show()

                    } else {
                        namein = name.text.toString()
                        val datain: String = data.toString()
                        val timein: String = time.toString()
                        val descin: String = desc.text.toString()
                        val colin: Int = Integer.parseInt(col.text.toString())
                        val pricein: Int = Integer.parseInt(price.text.toString())
                        val userin: Int = userId

                        val item = Item(
                            id,
                            namein!!,
                            datain,
                            timein,
                            descin,
                            colin,
                            pricein,
                            userin
                        )

                        val db = DbHelperTrain(this, null)
                      //  val dbImages = DbHelperImages(this,null)

                        db.rewriteItem(item, id)

                       // if(isImage==true) {
                         //   changePhoto()
                            //dbImages.deleteImage(id,userId)
                        //}
                      //  changePhoto()


                        val intent = Intent(this, ItemsActivity::class.java)
                        startActivity(intent)
                    }
                }
            }
        }
    }



    private fun makeTimeString(hourOfDay: Int, minute: Int): String {
        val hour:String
        val minutee:String

        if(hourOfDay<10){
            hour = "0"+hourOfDay
        }else{
            hour = hourOfDay.toString()
        }

        if(minute<10){
            minutee="0"+minute
        }else{
            minutee=minute.toString()
        }

        return hour+":"+minutee
    }

    private fun monthFormat(month:Int):String{
        if(month==1){
            return "Янв"
        }
        if(month==2){
            return "Фев"
        }
        if(month==3){
            return "Март"
        }
        if(month==4){
            return "Апр"
        }
        if(month==5){
            return "Май"
        }
        if(month==6){
            return "Июнь"
        }
        if(month==7){
            return "Июль"
        }
        if(month==8){
            return "Авг"
        }
        if(month==9){
            return "Сент"
        }
        if(month==10){
            return "Окт"
        }
        if(month==11){
            return "Ноя"
        }
        else{
            return "Дек"
        }

    }
    private fun makeDataString(year: Int, monthOfYear: Int, dayOfMonth: Int): String {

        val day:String
        val monthbeg:Int = monthOfYear+1
        val month:String

        if(monthOfYear<10){
            month = "0"+monthbeg
        }else{
            month=monthbeg.toString()
        }

        if(dayOfMonth<10){
            day="0"+dayOfMonth
        }else{
            day = dayOfMonth.toString()
        }

        return year.toString()+"-"+month+"-"+day
    }

/*
    fun openGallery(view:View){

        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            selectedImageUri = data.data!!

            var openGalleryButton:ImageButton = findViewById(R.id.photo_item)
            openGalleryButton.setImageURI(selectedImageUri)
            



            println("!!!!!!!!!!!!!!!" + selectedImageUri + "!!!!!!!!!!!!!!!!!!!!!!!")


        }
    }

*/
    fun changePhoto(){
        if(selectedImageUri.toString()!="") {
            val sP = getSharedPreferences("UserId", MODE_PRIVATE)
            val userId = sP.getInt("UserId", 0)
            val id: Int = intent.getIntExtra("itemId", 0)

            val db = DbHelperImages(this, null)

            println("ОТДАЛ ФОТО НА ЗАМЕНУ!!!  ID train = $id, User id = $userId   ${selectedImageUri.toString()}")
                db.changeImage(db.getImageId(id, userId), selectedImageUri.toString(), id, userId)

        }
    }
}