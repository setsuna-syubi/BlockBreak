package com.example.blockbreakingbeta

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import kotlin.random.Random

class Block{
    var LUcorner = 550f
    var RUcorner = 450f
    var LDcorner = 350f
    var RDcorner = 350f

}
class Ball {
    val NOT_REF = 0
    val REF_X = 1
    val REF_Y = 2
    val REF_BOTH = 3
    val GAME_OVER = 4

    //追加：タップ不可な範囲．上から2/3はタップしても無意味
    val topMargin = 600f

    val tapWidth = 135f
    val tapHeigth = 90f

    var mulX = 0f
    var mulY = 0f
    var pointX = 0f
    var pointY = 0f
    var vecX = 10f
    var vecY = 10f
    var xMax = 0f
    var yMax = 0f

    fun initData(xSize: Float, ySize: Float){
        mulX = xSize / 900f
        mulY = ySize / 900f
        pointX = xSize / 2f
        pointY = ySize / 2f
        xMax = xSize
        yMax = ySize
    }

    fun gameStart(){
        val temp = (0..5).random().toFloat()
        vecX = if(Random.nextBoolean()) (10f + temp) * mulX else (10f + temp) * mulX * -1f
        vecY = if(Random.nextBoolean()) (15f - temp) * mulY else (15f - temp) * mulY * -1f
    }

    fun check():Int{
        val tempX = pointX + vecX
        val tempY = pointY + vecY
        val gameOverFlag = yMax < tempY
        val xRefFlag = xMax < tempX || tempX < 0
        val yRefFlag = tempY < 0

        return when{
            gameOverFlag -> GAME_OVER
            xRefFlag && yRefFlag -> REF_BOTH
            xRefFlag && !yRefFlag -> REF_X
            !xRefFlag && yRefFlag -> REF_Y
            else -> NOT_REF
        }
    }

    // 追加：タップした部分がボールに当たったか判定
    fun checkTap(tapX:Float, tapY:Float):Boolean {
        //追加実装：タップしたときのy座標が0~タップ不可な範囲×縦の倍率内の場合，falseを返す処理を実装すること．early returnを使う
        if (tapY <= topMargin * mulY) {
            return false
        }

        val tapXRange = (tapWidth * mulX) /2
        val tapYRange = (tapHeigth * mulY) / 2
        val tapX0 = tapX - tapXRange
        val tapX1 = tapX + tapXRange
        val tapY0 = tapY - tapYRange
        val tapY1 = tapY + tapYRange


        //追加実装：タップした座標を中心に，倍率を掛けた当たり判定の幅と高さの座標を上下左右それぞれ算出する処理を実装すること
        if (tapX0<pointX && tapX1>pointX && tapY0<pointY && tapY1>pointY) {
            refY()
            return true
            //追加実装：範囲内であればrefY()を実行し，trueを返す．そうでなければfalseを返す処理を実装すること
        } else {
            return false
        }
    }


    //追加：タップ可能な範囲をRect型で返却するメソッド
    fun getTapArea():Rect{
        //追加実装：右が0，上がタップ不可な範囲×縦の倍率，左がxの最大値，下がyの最大値であるRect型を返す処理を実装
        return Rect(0, (topMargin*mulY).toInt(), xMax.toInt(), yMax.toInt() )
    }

    fun refX(){
        vecX *= -1f
    }

    fun refY(){
        vecY *= -1f
    }

    fun move(){
        pointX += vecX
        pointY += vecY
    }

    fun gameStop(){
        pointX = xMax / 2f
        pointY = yMax / 2f
        vecX = 0f
        vecY = 0f
    }
    //ブロックに当たった時に反転させる
    
}



class MyView(ctx: Context) : View(ctx) {
    val paint = Paint()
    var ball = Ball()
    var block = Block()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //追加：タップ可能な範囲を明るい灰色で描画する
        paint.color = Color.LTGRAY
        canvas.drawRect(ball.getTapArea(), paint)
        //変更：ボールを黒で描画する
        paint.color = Color.BLACK
        canvas.drawCircle(ball.pointX, ball.pointY, (30*ball.mulX), paint)
        //追加：スタートでブロックを赤で追加
        paint.color = Color.rgb(200,0,0)
        canvas.drawRect(block.LUcorner,block.RUcorner,block.LDcorner,block.RDcorner,paint)
    }
}