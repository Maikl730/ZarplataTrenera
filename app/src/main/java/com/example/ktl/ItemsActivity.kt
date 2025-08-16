package com.example.ktl

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
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
    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_items)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val sP1 = getSharedPreferences("UserId",MODE_PRIVATE)
        val editor = sP1.edit()
        editor.putInt("UserId",2).commit()
        editor.putBoolean("isAuth",true).commit()

        var data1:String
        var data2:String
        var buttonChange:Int = 0

        val dataButtonSet1:TextView = findViewById(R.id.button_data_sort1)
        val dataButtonSet2:TextView = findViewById(R.id.button_data_sort2)

        val dataWeekClicker:TextView = findViewById(R.id.data_week)
        val data2WeekClicker:TextView = findViewById(R.id.data_2week)
        val dataMonthClicker:TextView = findViewById(R.id.data_month)


        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)


        data1 = makeDataString(year,month-1,day)
        data2 = makeDataString(year,month,day)



        dataButtonSet1.setText("C " + day +" "+ monthFormat(month))

        dataButtonSet2.setText("По " + day +" "+ monthFormat(month+1))


        val sP = getSharedPreferences("UserId",MODE_PRIVATE)
        val id = sP.getInt("UserId",0)

        val db = DbHelperTrain(this,null)

        val itemsList: RecyclerView = findViewById(R.id.items_list)
        items = db.getItem(data1,data2,id)

        val sumShow: TextView = findViewById(R.id.main_sum)
        sumShow.text = db.getSum(data1,data2,id).toString() + " руб."


        dataWeekClicker.setOnClickListener {
            if (c.get(Calendar.DAY_OF_WEEK)==2){
                val newDay = c.get(Calendar.DAY_OF_MONTH)
                val newMonth = c.get(Calendar.MONTH)
                val newYear = c.get(Calendar.YEAR)


                dataWeekClicker.setTextColor(getColor(R.color.main))
                data2WeekClicker.setTextColor(getColor(R.color.black))
                dataMonthClicker.setTextColor(getColor(R.color.black))

                dataButtonSet1.setText("C " + newDay +" "+ monthFormat(newMonth+1))
                dataButtonSet2.setText("По " + day +" "+ monthFormat(month+1))

                data1 = makeDataString(year,newMonth,newDay)
                data2 = makeDataString(year,month,day)

                items = db.getItem(data1,data2,id)
                itemsList.adapter = ItemsAdapter(items,this)
                sumShow.text = db.getSum(data1,data2,id).toString() + " руб."
                itemsList.refreshDrawableState()


            }
            if(c.get(Calendar.DAY_OF_WEEK)==7){

                c.add(Calendar.DAY_OF_YEAR,-5)
                val newDay = c.get(Calendar.DAY_OF_MONTH)
                val newMonth = c.get(Calendar.MONTH)
                val newYear = c.get(Calendar.YEAR)
                c.add(Calendar.DAY_OF_YEAR,5)



                dataWeekClicker.setTextColor(getColor(R.color.main))
                data2WeekClicker.setTextColor(getColor(R.color.black))
                dataMonthClicker.setTextColor(getColor(R.color.black))

                dataButtonSet1.setText("C " + newDay + " " + monthFormat(newMonth+1))
                dataButtonSet2.setText("По " + day +" "+ monthFormat(month+1))

                data1 = makeDataString(year,newMonth,newDay)
                data2 = makeDataString(year,month,day)

                items = db.getItem(data1,data2,id)
                itemsList.adapter = ItemsAdapter(items,this)
                sumShow.text = db.getSum(data1,data2,id).toString() + " руб."
                itemsList.refreshDrawableState()

            }else{

                val dayForMinus = c.get(Calendar.DAY_OF_WEEK)-2

                c.add(Calendar.DAY_OF_YEAR,-dayForMinus)
                val newDay = c.get(Calendar.DAY_OF_MONTH)
                val newMonth = c.get(Calendar.MONTH)
                val newYear = c.get(Calendar.YEAR)
                c.add(Calendar.DAY_OF_YEAR,dayForMinus)


                dataWeekClicker.setTextColor(getColor(R.color.main))
                data2WeekClicker.setTextColor(getColor(R.color.black))
                dataMonthClicker.setTextColor(getColor(R.color.black))

                dataButtonSet1.setText("C " + newDay + " " + monthFormat(newMonth+1))
                dataButtonSet2.setText("По " + day +" "+ monthFormat(month+1))

                data1 = makeDataString(year,newMonth,newDay)
                data2 = makeDataString(year,month,day)

                items = db.getItem(data1,data2,id)
                itemsList.adapter = ItemsAdapter(items,this)
                sumShow.text = db.getSum(data1,data2,id).toString() + " руб."
                itemsList.refreshDrawableState()

            }


        }

        dataMonthClicker.setOnClickListener {
            if(day!=1){
                val newDay = 1
                val newMonth = c.get(Calendar.MONTH)
                val newYear = c.get(Calendar.YEAR)



                dataWeekClicker.setTextColor(getColor(R.color.black))
                data2WeekClicker.setTextColor(getColor(R.color.black))
                dataMonthClicker.setTextColor(getColor(R.color.main))


                dataButtonSet1.setText("C " + newDay + " " + monthFormat(newMonth+1))
                dataButtonSet2.setText("По " + day +" "+ monthFormat(month+1))

                data1 = makeDataString(year,newMonth,newDay)
                data2 = makeDataString(year,month,day)

                items = db.getItem(data1,data2,id)
                itemsList.adapter = ItemsAdapter(items,this)
                sumShow.text = db.getSum(data1,data2,id).toString() + " руб."
                itemsList.refreshDrawableState()

            }else{
                c.add(Calendar.MONTH,-1)
                val newDay = 1
                val newMonth = c.get(Calendar.MONTH)
                val newYear = c.get(Calendar.YEAR)
                c.add(Calendar.MONTH,1)

                c.add(Calendar.DAY_OF_YEAR,-1)
                val newDay2 = c.get(Calendar.DAY_OF_MONTH)
                c.add(Calendar.DAY_OF_YEAR,1)


                dataWeekClicker.setTextColor(getColor(R.color.black))
                data2WeekClicker.setTextColor(getColor(R.color.black))
                dataMonthClicker.setTextColor(getColor(R.color.main))

                dataButtonSet1.setText("C " + newDay + " " + monthFormat(newMonth+1))
                dataButtonSet2.setText("По " + newDay2 + " " + monthFormat(newMonth+1))

                data1 = makeDataString(year,newMonth,newDay)
                data1 = makeDataString(year,newMonth,newDay2)

                items = db.getItem(data1,data2,id)
                itemsList.adapter = ItemsAdapter(items,this)
                sumShow.text = db.getSum(data1,data2,id).toString() + " руб."
                itemsList.refreshDrawableState()

            }


        }

        data2WeekClicker.setOnClickListener {

            if (c.get(Calendar.DAY_OF_WEEK)==2){
                c.add(Calendar.DAY_OF_YEAR,-7)
                val newDay = c.get(Calendar.DAY_OF_MONTH)
                val newMonth = c.get(Calendar.MONTH)
                val newYear = c.get(Calendar.YEAR)
                c.add(Calendar.DAY_OF_YEAR,7)


                dataWeekClicker.setTextColor(getColor(R.color.black))
                data2WeekClicker.setTextColor(getColor(R.color.main))
                dataMonthClicker.setTextColor(getColor(R.color.black))

                dataButtonSet1.setText("С " + newDay + " " + monthFormat(newMonth+1))
                dataButtonSet2.setText("По " + day +" "+ monthFormat(month+1))

                data1 = makeDataString(year,newMonth,newDay)
                data2 = makeDataString(year,month,day)

                items = db.getItem(data1,data2,id)
                itemsList.adapter = ItemsAdapter(items,this)
                sumShow.text = db.getSum(data1,data2,id).toString() + " руб."
                itemsList.refreshDrawableState()


            }
            if(c.get(Calendar.DAY_OF_WEEK)==7){

                c.add(Calendar.DAY_OF_YEAR,-(5+7))
                val newDay = c.get(Calendar.DAY_OF_MONTH)
                val newMonth = c.get(Calendar.MONTH)
                val newYear = c.get(Calendar.YEAR)
                c.add(Calendar.DAY_OF_YEAR,5+7)


                dataWeekClicker.setTextColor(getColor(R.color.black))
                data2WeekClicker.setTextColor(getColor(R.color.main))
                dataMonthClicker.setTextColor(getColor(R.color.black))

                dataButtonSet1.setText("С " + newDay + " " + monthFormat(newMonth+1))
                dataButtonSet2.setText("По " + day +" "+ monthFormat(month+1))

                data1 = makeDataString(year,newMonth,newDay)
                data2 = makeDataString(year,month,day)

                items = db.getItem(data1,data2,id)
                itemsList.adapter = ItemsAdapter(items,this)
                sumShow.text = db.getSum(data1,data2,id).toString() + " руб."
                itemsList.refreshDrawableState()

            }else{

                val dayForMinus = c.get(Calendar.DAY_OF_WEEK)-2

                c.add(Calendar.DAY_OF_YEAR,-(dayForMinus+7))
                val newDay = c.get(Calendar.DAY_OF_MONTH)
                val newMonth = c.get(Calendar.MONTH)
                val newYear = c.get(Calendar.YEAR)
                c.add(Calendar.DAY_OF_YEAR,dayForMinus+7)


                dataWeekClicker.setTextColor(getColor(R.color.black))
                data2WeekClicker.setTextColor(getColor(R.color.main))
                dataMonthClicker.setTextColor(getColor(R.color.black))

                dataButtonSet1.setText("С " + newDay + " " + monthFormat(newMonth+1))
                dataButtonSet2.setText("По " + day +" "+ monthFormat(month+1))

                data1 = makeDataString(year,newMonth,newDay)
                data2 = makeDataString(year,month,day)

                items = db.getItem(data1,data2,id)
                itemsList.adapter = ItemsAdapter(items,this)
                sumShow.text = db.getSum(data1,data2,id).toString() + " руб."
                itemsList.refreshDrawableState()

            }
        }




        val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            // Display Selected date in textbox
            val month1=monthOfYear
            if(buttonChange==1)
            {
                dataButtonSet1.setText("С " + dayOfMonth + " " + monthFormat(month1+1))
                data1 = makeDataString(year,monthOfYear,dayOfMonth)
                println(data1)
                items = db.getItem(data1,data2,id)
                itemsList.adapter = ItemsAdapter(items,this)
                sumShow.text = db.getSum(data1,data2,id).toString() + " руб."
                itemsList.refreshDrawableState()
            }else {
                dataButtonSet2.setText("По " + dayOfMonth + " " + monthFormat(month1+1))
                data2 = makeDataString(year,monthOfYear,dayOfMonth)
                println(data2)
                items = db.getItem(data1,data2,id)
                itemsList.adapter = ItemsAdapter(items,this)
                sumShow.text = db.getSum(data1,data2,id).toString() + " руб."
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


        itemsList.layoutManager = LinearLayoutManager(this)
        itemsList.adapter = ItemsAdapter(items,this)
        itemsList.refreshDrawableState()
    }

    fun toAddAct(view: View){
        val intent = Intent(this,ItemAddActivity::class.java)
        startActivity(intent)
    }


    private fun monthFormat(month:Int):String{
        if(month==1){
            return "Января"
        }
        if(month==2){
            return "Февраля"
        }
        if(month==3){
            return "Марта"
        }
        if(month==4){
            return "Апреля"
        }
        if(month==5){
            return "Мая"
        }
        if(month==6){
            return "Июня"
        }
        if(month==7){
            return "Июля"
        }
        if(month==8){
            return "Августа"
        }
        if(month==9){
            return "Сентебря"
        }
        if(month==10){
            return "Октября"
        }
        if(month==11){
            return "Ноября"
        }
        else{
            return "Декабря"
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