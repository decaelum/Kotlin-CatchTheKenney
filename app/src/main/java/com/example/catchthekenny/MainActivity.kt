package com.example.catchthekenny

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.catchthekenny.databinding.ActivityMainBinding
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    var score : Int = 0
    var highScore : Int? = null
    var newHighScore : Boolean = false
    var imageArray = ArrayList<ImageView>()

    var runnable = Runnable{}
    var handler = Handler()

    lateinit var sharedPref : SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        sharedPref = getSharedPreferences("com.example.catchthekenny", MODE_PRIVATE)
        highScore = sharedPref.getInt("High Score", 0)

        binding.tVHighScore.text = "High Score : " + sharedPref.getInt("High Score" , 0)

        imageArray.add(binding.iVKenny1)
        imageArray.add(binding.iVKenny2)
        imageArray.add(binding.iVKenny3)
        imageArray.add(binding.iVKenny4)
        imageArray.add(binding.iVKenny5)
        imageArray.add(binding.iVKenny6)
        imageArray.add(binding.iVKenny7)
        imageArray.add(binding.iVKenny8)
        imageArray.add(binding.iVKenny9)

        hide()




        object : CountDownTimer(30500 , 1000){
            override fun onFinish() {
                binding.tVTimer.text = "Time : 0 "
                 newHighScore()

                handler.removeCallbacks(runnable)

                for (image in imageArray){
                    image.visibility = View.INVISIBLE
                }

                var alert = AlertDialog.Builder(this@MainActivity)
                alert.setTitle("Game Over")
                alert.setMessage("Restart The Game?")
                alert.setPositiveButton("Yes"){dialog, which ->
                    val intent = intent
                    finish()
                    startActivity(intent)
                }
                alert.setNegativeButton("No"){dialog , which ->
                    Toast.makeText(this@MainActivity,"Game Over",Toast.LENGTH_LONG).show()
                }
                alert.show()
            }

            override fun onTick(millisUntilFinished: Long) {
                binding.tVTimer.text = "Time : " + millisUntilFinished/1000
            }

        }.start()
    }
    fun increaseScore(view: View){
        score = score + 1
        binding.tVScore.text = "Score: $score"
    }
    fun newHighScore(){
        if(score > highScore!!){
            newHighScore = true
            highScore = score
        }else{
            newHighScore = false
        }
        sharedPref.edit().apply{ putInt("High Score", highScore!!) }.apply()
    }

    fun hide(){
        runnable = object : Runnable{
            override fun run() {
                for(image in imageArray){
                    image.visibility = View.INVISIBLE
                }

                var random = Random()
                var randomIndex = random.nextInt(9)
                imageArray[randomIndex].visibility = View.VISIBLE

                handler.postDelayed(runnable,500)
            }

        }
        handler.post(runnable)

    }


}