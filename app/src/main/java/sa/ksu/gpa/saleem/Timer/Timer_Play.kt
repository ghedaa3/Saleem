package sa.ksu.gpa.saleem.Timer

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_timer__play.*
import sa.ksu.gpa.saleem.R


class Timer_Play : AppCompatActivity(), View.OnClickListener {

    enum class timerstate{
        Stopped, Paused, Running
    }

    lateinit var prepare: String
    lateinit var active:String
    lateinit var rest: String
    lateinit var round: String
    lateinit var restRound: String
    lateinit var repeat: String
    lateinit var timerPlay: Button
    lateinit var timerPause: Button
    lateinit var timerBack: ImageView
    lateinit var timerRefresh: ImageView
    lateinit var timerTime: TextView
    lateinit var timerCurrentTime: TextView
    private var timerLengthSeconds: Long = 0
    private var timerState = timerstate.Stopped
    private var secondsRemaining: Long = 0
    private lateinit var timer: CountDownTimer
    var prepare1:Long = 0
    var active1:Long=0
    var rest1:Long=0
    var round1:Int=0
    var repeat1:Int=0
    var restRound1:Long=0
    var i:Int=0
    var j: Int=0
    var k:Int=0
    var rounList=ArrayList<Long>()
    var StringList=ArrayList<String>()



    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer__play)

        // user info
        prepare= intent.extras!!.getString("prepare").toString()
        active=intent.extras!!.getString("active").toString()
        rest=intent.extras!!.getString("rest").toString()
        round=intent.extras!!.getString("round").toString()
        restRound=intent.extras!!.getString("restRound").toString()
        repeat=intent.extras!!.getString("repeat").toString()
        // to int
         prepare1=prepare.toLong()
         active1=active.toLong()
         rest1=rest.toLong()
         round1=round.toInt()
         repeat1=repeat.toInt()
         restRound1=restRound.toLong()
         secondsRemaining=prepare1
        progress_countdown.progress = 0
        progress_countdown.setMax(prepare1.toInt())


        //looping through the list

        Log.d("playhere","repeat1= "+repeat1)
        Log.d("playhere","round1= "+round1)


        // find view by ids:
        timerPlay=findViewById(R.id.timer_play)
        timerPause=findViewById(R.id.timer_pause)
        timerTime=findViewById(R.id.timer_time)
        timerCurrentTime= findViewById(R.id.timer_current_time)
        timerBack= findViewById(R.id.back_button)
        timerRefresh= findViewById(R.id.refresh)
        timerPause.visibility = View.GONE

        addRoundLis()
        // Click Listener
        timerPlay.setOnClickListener(this)
        timerPause.setOnClickListener(this)
        timerBack.setOnClickListener(this)
        timerRefresh.setOnClickListener(this)


    }

    private fun addRoundLis() {
        i= round1
        j=repeat1


        while (i!=0){
          --  i
            j=repeat1
            while (j!=0){
               -- j
                rounList.add(active1)
                StringList.add("تحرك")
                rounList.add(rest1)
                StringList.add("الراحة")

            }
            rounList.add(restRound1)
            StringList.add("الراحة بين الدورات")

        }

     k=rounList.size
        Log.d("playhere","K= "+k)
        Log.d("rounList","rounList= "+rounList)
        Log.d("rounList","String= "+StringList)

    }

    override fun onPause() {
        super.onPause()
        if (timerState==timerstate.Running){
            timer.cancel()
            timerState=timerstate.Paused
            updateButtons()

        }

        else  if (timerState==timerstate.Paused){

        }

        PrefUtil.setPreviousTimerLengthSeconds(timerLengthSeconds, this)
        PrefUtil.setSecondsRemaining(secondsRemaining, this)
        PrefUtil.setTimerState(timerState, this)
    }


    override fun onClick(v: View?) {
        when (v) {
            timerPlay ->{

                timerState = timerstate.Running

                startTimer()
                updateButtons()




            }
            timerPause ->{
                timerState = timerstate.Paused
                timer.cancel()
                updateButtons()
            }

            timerBack->{
                onBackPressed()
            }
            timerRefresh->{

                if(timerState!=timerstate.Stopped){
                    timer.cancel()
                    timerCurrentTime.text="استعد"
                    secondsRemaining=prepare1

                    onTimerFinished()
                    updateButtons()
                    updateCountdownUI()
                }
            }
        }

    }

    private fun onTimerFinished() {
        timerState = timerstate.Stopped
        rounList.clear()
        StringList.clear()
        addRoundLis()
        progress_countdown.progress = 0
        progress_countdown.setMax(prepare1.toInt())

    }

    override fun onBackPressed() {
        super.onBackPressed()

    }

    private fun updateButtons() = when (timerState) {
        timerstate.Running ->{
            timerPlay.visibility = View.GONE
            timerPause.visibility = View.VISIBLE


        }
        timerstate.Stopped -> {
            timerPlay.visibility = View.VISIBLE
            timerPause.visibility = View.GONE

        }
        timerstate.Paused -> {
            timerPlay.visibility = View.VISIBLE
            timerPause.visibility = View.GONE
        }
    }

    @Synchronized  private  fun startTimer() {
        timerState = timerstate.Running
        timer = object : CountDownTimer(secondsRemaining * 1000, 1000) {


            override fun onTick(millisUntilFinished: Long) {
                secondsRemaining = millisUntilFinished / 1000
                updateCountdownUI()


                if (Math.round(millisUntilFinished.toFloat() / 1000.0f) == 5)
                    playSoundWork()

            }

            override fun onFinish(){
                playSound()
                progress_countdown.progress = 0

                if (rounList.size!=0){
                    secondsRemaining=rounList[0]
                    progress_countdown.setMax(rounList[0].toInt())
                    timerCurrentTime.text=StringList[0]
                    rounList.removeAt(0)
                    StringList.removeAt(0)
                    startTimer()
                    updateButtons()

                }
                else{
                    timerState=timerstate.Stopped
                    timerCurrentTime.text="تهانينا"
                    onTimerFinished()
                    progress_countdown.progress = 0
                    progress_countdown.setMax(prepare1.toInt())
                    updateButtons()

                }


            }

        }.start()
    }
    fun playSound() {
        val mp: MediaPlayer =
            MediaPlayer.create(baseContext, R.raw.cttut_buzz) //replace 'sound' by your    music/sound
        mp.start()
    }
    fun playSoundWork() {
        val mp: MediaPlayer =
            MediaPlayer.create(baseContext, R.raw.realworkout) //replace 'sound' by your    music/sound
        mp.start()
    }
    private fun updateCountdownUI() {
        val minutesUntilFinished = secondsRemaining / 60
        val secondsInMinuteUntilFinished = secondsRemaining - minutesUntilFinished * 60
        val secondsStr = secondsInMinuteUntilFinished.toString()
        timerTime.text = "$minutesUntilFinished:${if (secondsStr.length == 2) secondsStr else "0" + secondsStr}"
       progress_countdown.progress = ( secondsRemaining).toInt()
    }

}
