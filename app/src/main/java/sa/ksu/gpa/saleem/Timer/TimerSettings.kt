package sa.ksu.gpa.saleem.Timer

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_timer_settings.*
import sa.ksu.gpa.saleem.R

class TimerSettings : AppCompatActivity() {
    private lateinit var prepare: EditText
    private lateinit var active: EditText
    private lateinit var rest: EditText
    private lateinit var round: EditText
    private lateinit var restRound: EditText
    private lateinit var repeat: EditText
    private lateinit var startButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer_settings)
        prepare=findViewById(R.id.timer_prepaer)
        active= findViewById(R.id.timer_active)
        rest=findViewById(R.id.timer_rest)
        round=findViewById(R.id.timer_round)
        restRound=findViewById(R.id.timer_rest_round)
        repeat=findViewById(R.id.timer_repeat)
        startButton=findViewById(R.id.timer_start_timer_button)

        timer_back_button.setOnClickListener{
            onBackPressed()
        }

        //clicklistener
        startButton.setOnClickListener {

           var intent= Intent(this,Timer_Play::class.java)
             // numbers
            if (prepare.length()!=0)
            intent.putExtra("prepare",prepare.text.toString())
            else
                intent.putExtra("prepare","5")

            if (active.length()!=0)
            intent.putExtra("active",active.text.toString())
            else
                intent.putExtra("active","40")

            if (rest.length()!=0)
            intent.putExtra("rest",rest.text.toString())
            else
                intent.putExtra("rest","20")

            if (repeat.length()!=0)
            intent.putExtra("repeat",repeat.text.toString())
            else
                intent.putExtra("repeat","1")

            if (round.length()!=0)
            intent.putExtra("round",round.text.toString())
            else
                intent.putExtra("round","1")

            if (restRound.length()!=0)
            intent.putExtra("restRound",restRound.text.toString())
            else
                intent.putExtra("restRound","60")

            startActivity(intent)


        }
    }
}
