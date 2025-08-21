package com.example.ktl

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.yandex.mobile.ads.banner.AdSize
import com.yandex.mobile.ads.banner.BannerAdView
import com.yandex.mobile.ads.interstitial.InterstitialAd
import com.yandex.mobile.ads.interstitial.InterstitialAdEventListener
import java.io.ByteArrayOutputStream
import java.util.Calendar


class ItemAddActivity : AppCompatActivity() {


  //  private val PICK_IMAGE_REQUEST = 1
   // private lateinit var selectedImageUri: Uri
  //  private val READ_MEDIA_IMAGE_PERMISSION_CODE = 101


    var data:String = ""
    var time:String = ""
    var name:String = "-"
    //var image64:String = ""

/*
    fun openGallery(view:View){

        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            selectedImageUri = data.data!!

            var openGalleryButton:ImageButton = findViewById(R.id.photo)
            openGalleryButton.setImageURI(selectedImageUri)
            openGalleryButton.scaleY = 2F
            openGalleryButton.scaleX = 2f



            println("!!!!!!!!!!!!!!!" + selectedImageUri + "!!!!!!!!!!!!!!!!!!!!!!!")

          // image64=toBase64FromUri()

        }
    }

   fun toBase64FromUri():String{
        val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, selectedImageUri)

        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == READ_MEDIA_IMAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted.
                Toast.makeText(this, "ok", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "not ok", Toast.LENGTH_SHORT).show()
                // Permission denied by the user.
            }
        }
    }
*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_item_add)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }







        val banner:BannerAdView = findViewById(R.id.banner)
        banner.setAdUnitId("R-M-16765762-2")
        banner.setAdSize(AdSize.stickySize(350))
        val adRequest = com.yandex.mobile.ads.common.AdRequest.Builder().build()
        banner.loadAd(adRequest)



/*
        // Check if the permission is already granted
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_MEDIA_IMAGES
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is already granted.
            Toast.makeText(this, "already ok", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "prompting", Toast.LENGTH_SHORT).show()
            // Permission is not granted.
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                READ_MEDIA_IMAGE_PERMISSION_CODE
            )
        }



        selectedImageUri = "".toUri()
        */

        val addButton: Button = findViewById(R.id.button_add)
        val dataButton: Button = findViewById(R.id.button_add_data)
        val timeButton: Button = findViewById(R.id.button_add_time)
        
        

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        data= makeDataString(year,month,day)

        dataButton.setText(makeDataString(year,month,day))

        val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            // Display Selected date in textbox
            val month1=monthOfYear
            dataButton.setText("" + dayOfMonth + ". " + monthFormat(month1+1) + ". " + year)
            data = makeDataString(year,monthOfYear,dayOfMonth)
        }, year, month, day)

        dataButton.setOnClickListener { dpd.show() }
        
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minutes = c.get(Calendar.MINUTE)
        time= makeTimeString(hour,minutes)

        timeButton.setText(makeTimeString(hour,minutes))

        val timePickerDialog = TimePickerDialog( this, TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            // Обработка событий изменения времени 
            
            timeButton.setText(makeTimeString(hourOfDay,minute))
            
            time=makeTimeString(hourOfDay,minute)
            
            }, hour, minutes, true )  
            
        timeButton.setOnClickListener { timePickerDialog.show() }

        addButton.setOnClickListener {

            if(
                findViewById<EditText>(R.id.item_add_name).text.toString().trim()==""||
                findViewById<EditText>(R.id.item_add_price).text.toString().trim()==""||
                findViewById<EditText>(R.id.item_add_count).text.toString().trim()==""
                )
            {
             Toast.makeText(this,"Не все поля заполнены",Toast.LENGTH_SHORT).show()
            }else{

             //   val lastid =  intent.getIntExtra("lastid",0)
            val db = DbHelperTrain(this,null)
               // val dbImage = DbHelperImages(this,null)
            val sP = getSharedPreferences("UserId",MODE_PRIVATE)

            name = findViewById<EditText>(R.id.item_add_name).text.toString()
            val desc:String = findViewById<EditText>(R.id.item_add_desc).text.toString()
            val price:Int = Integer.parseInt(findViewById<EditText>(R.id.item_add_price).text.toString())
            val col:Int = Integer.parseInt(findViewById<EditText>(R.id.item_add_count).text.toString())
            val user:Int = sP.getInt("UserId",0)






            val item = Item(0,
                name,
                data,
                time,
                desc,
                col,
                price,
                user)



            db.addItem(item)

/*
                if(selectedImageUri!=null){
                    if (selectedImageUri.toString()!="") {
                        dbImage.addImage(selectedImageUri.toString(), db.getLastId(), user)
                         println("LAST ID IS " + db.getLastId() + " !!!!!!!!!!!!!!!!!!")
                    }
                }
*/

            val intent = Intent(this,ItemsActivity::class.java)
            startActivity(intent)}
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

}