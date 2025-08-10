package com.example.ktl

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class AuthActivity : AppCompatActivity() {
    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_auth)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val userLogin: EditText = findViewById(R.id.user_login_auth)
        val userPass: EditText = findViewById(R.id.user_pass_auth)
        val button: Button = findViewById(R.id.button_auth)



        button.setOnClickListener {
            val login = userLogin.text.toString().trim()
            val pass = userPass.text.toString().trim()

            if(login == "" || pass =="")
                Toast.makeText(this,"Не все поля заполнены", Toast.LENGTH_SHORT).show()
            else{

                val db = DbHelper(this,null)
               val isAuth =  db.getUser(login,pass)

                if(isAuth){
                    Toast.makeText(this,"Пользователь $login авторизован", Toast.LENGTH_SHORT).show()

                    userLogin.text.clear()
                    userPass.text.clear()

                    val sP = getSharedPreferences("UserId",MODE_PRIVATE)
                    val editor = sP.edit()
                    editor.putInt("UserId",db.getUserId(login,pass)).commit()
                    editor.putBoolean("isAuth",true).commit()



                    val intent = Intent(this,ItemsActivity::class.java)
                            startActivity(intent)
                }else{
                    Toast.makeText(this,"Пользователь $login НЕ авторизован", Toast.LENGTH_SHORT).show()
                }

            }


            }


        val linkToReg: TextView = findViewById(R.id.link_to_reg)

        linkToReg.setOnClickListener{
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)

        }
    }
}