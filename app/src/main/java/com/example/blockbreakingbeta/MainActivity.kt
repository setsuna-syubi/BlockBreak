package com.example.blockbreakingbeta

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    val handler = Handler()
    lateinit var myView: MyView
    val ball = Ball()
    //タップした座標がボールにぶつかったかを表す変数
    var isTapedBall = false
    var count =0
    var timer = 60

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        myView = MyView(this)
        area.addView(myView)
        //追加：touchListenerを付けてタップを検出
        myView.setOnTouchListener{_, motionEvent ->
            //追加：タップの状況に合わせて処理を分岐
            when(motionEvent.action){
                //追加実装：タップしたとき ->if(まだタップがボールにぶつかっていないとき) ballのタップ判定メソッドを呼び出す処理を記述すること
                MotionEvent.ACTION_DOWN->if(!isTapedBall) isTapedBall = ball.checkTap(motionEvent.x,motionEvent.y)
                //追加実装：ドラッグしたときき ->if(まだタップがボールにぶつかっていないとき) ballのタップ判定メソッドを呼び出す処理を記述すること
                MotionEvent.ACTION_MOVE->if(!isTapedBall) isTapedBall = ball.checkTap(motionEvent.x,motionEvent.y)
                //追加：タップをやめたときタップ判定変数をfalseに0x
                MotionEvent.ACTION_UP -> isTapedBall = false
            }
            return@setOnTouchListener false
        }

        buttonStart.setOnClickListener {
            ball.gameStop()
            handler.removeCallbacks(runnable)
            //追加：ゲーム開始時にタップ判定変数をfalseに設定
            textView.visibility = View.INVISIBLE
            isTapedBall = false
            count = 0
            ball.gameStart()
            handler.post(runnable)
            textView.text="score ${count}"
        }
    }
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        ball.initData(area.width.toFloat(), area.height.toFloat())
    }

    //追加：反射時にタップ判定変数をfalseに変更
    val runnable = object:Runnable {
        override fun run() {

            when(ball.check()){
                ball.REF_BOTH -> {
                    ball.refX()
                    ball.refY()
                    isTapedBall = false
                    count++
                }
                ball.REF_X -> {
                    ball.refX()
                    isTapedBall = false
                    count++
                }
                ball.REF_Y -> {
                    ball.refY()
                    isTapedBall = false
                    count++
                }
                ball.GAME_OVER -> {
                    ball.gameStop()
                    isTapedBall = false
                    textView.text="score ${count}"
                    textView.visibility = View.VISIBLE
                }
            }

            ball.move()
            //変更：ボールのデータを全て渡す
            myView.ball = ball
            myView.invalidate()
            handler.postDelayed(this, 10L)
        }
    }
}
