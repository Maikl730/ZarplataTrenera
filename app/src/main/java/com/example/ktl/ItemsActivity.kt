package com.example.ktl

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.Calendar

class ItemsActivity : AppCompatActivity() {
    lateinit var items: ArrayList<Item>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_items)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        var data1:String
        var data2:String
        var buttonChange:Int = 0

        val dataButtonSet1:Button = findViewById(R.id.button_data_sort1)
        val dataButtonSet2:Button = findViewById(R.id.button_data_sort2)


        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)


        data1 = makeDataString(year,month-1,day)
        data2 = makeDataString(year,month,day)


        dataButtonSet1.setText(makeDataString(year,month-1,day))
        dataButtonSet2.setText(makeDataString(year,month,day))

        val sP = getSharedPreferences("UserId",MODE_PRIVATE)
        val id = sP.getInt("UserId",0)
        val isAuth =sP.getBoolean("isAuth",true)

        val db = DbHelperTrain(this,null)

        val itemsList: RecyclerView = findViewById(R.id.items_list)
        items = db.getItem(data1,data2,id)

        val sumShow: TextView = findViewById(R.id.main_sum)
        sumShow.text = "Итого: " + db.getSum(data1,data2,id).toString() + " руб."







        val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            // Display Selected date in textbox
            val month1=monthOfYear
            if(buttonChange==1)
            {
                dataButtonSet1.setText("" + dayOfMonth + ". " + monthFormat(month1+1) + ". " + year)
                data1 = makeDataString(year,monthOfYear,dayOfMonth)
                println(data1)
                items = db.getItem(data1,data2,id)
                itemsList.adapter = ItemsAdapter(items,this)
                sumShow.text = "Итого: " + db.getSum(data1,data2,id).toString() + " руб."
                itemsList.refreshDrawableState()
            }else {
                dataButtonSet2.setText("" + dayOfMonth + ". " + monthFormat(month1+1) + ". " + year)
                data2 = makeDataString(year,monthOfYear,dayOfMonth)
                println(data2)
                items = db.getItem(data1,data2,id)
                itemsList.adapter = ItemsAdapter(items,this)
                sumShow.text = "Итого: " + db.getSum(data1,data2,id).toString() + " руб."
                itemsList.refreshDrawableState()

            }
        }, year, month, day)

        dataButtonSet1.setOnClickListener {
            dpd.show()
            buttonChange=1
        }

        dataButtonSet2.setOnClickListener {
            dpd.show()
            buttonChange=2
        }

//github string helloo testtttt !!!!

        itemsList.layoutManager = LinearLayoutManager(this)
        itemsList.adapter = ItemsAdapter(items,this)
        itemsList.refreshDrawableState()
    }

    fun toAddAct(view: View){
        val intent = Intent(this,ItemAddActivity::class.java)
           // intent.putExtra("lastid",items[items.lastIndex].id)
           // println("!!!!!!!!!!!!!!!!!!!!!" + items[items.lastIndex].id + "!!!!!!!!!!!!!!!!!!!!!!!!!!!!")

        startActivity(intent)

    }

    fun exitFromUser(view: View){

        val sP = getSharedPreferences("UserId",MODE_PRIVATE)
        val editor = sP.edit()
        editor.putBoolean("isAuth",false).commit()

        val intent = Intent(this,AuthActivity::class.java)
        startActivity(intent)
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