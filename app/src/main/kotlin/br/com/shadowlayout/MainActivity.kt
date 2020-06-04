package br.com.shadowlayout

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        simulateRequestBehavior()

        btnStart.setOnClickListener {
            simulateRequestBehavior()
        }
//
//        btnStop.setOnClickListener {
//            shadow.stopShadow()
//        }
    }

    @SuppressLint("SetTextI18n")
    private fun simulateRequestBehavior() {
        shadow.startShadow()
        GlobalScope.launch(Dispatchers.Main + SupervisorJob()) {
            delay(5000L)
            img.setImageResource(R.drawable.ic_android)
            txt1.text = "Nome: Sit"
            txt2.text = "Sobrenome: Lorem ipsum dolor sit amet"
            txt3.text = "Idade: 19"
            txt4.text = "E-mail: Lorem.ipsum@dolor.com.br"
            txt5.text =
                "Descrição: Lorem ipsum dolor sit amet, consectetur adipiscing elit. " +
                        "Donec et urna a lorem laoreet laoreet. " +
                        "Phasellus et metus sit amet eros maximus semper quis id augue. " +
                        "Curabitur in est tincidunt, mollis orci vel, elementum ligula. " +
                        "Fusce et odio semper, molestie ante a, fermentum neque. " +
                        "Cras tristique vestibulum condimentum."
            shadow.stopShadow()
        }
    }
}
