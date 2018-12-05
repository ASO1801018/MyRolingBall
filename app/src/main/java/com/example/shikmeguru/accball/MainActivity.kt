package com.example.shikmeguru.accball

import android.content.Context
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.graphics.Paint
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.SurfaceHolder
import kotlinx.android.synthetic.main.activity_main.*
import org.w3c.dom.Text

class MainActivity : AppCompatActivity(),SensorEventListener, SurfaceHolder.Callback {

    private var surfaceWidth:Int = 0
    private var surfaceHeight:Int = 0

    private var radius = 25f
    private var coef = 1000f

    private var ballX: Float = 0f
    private var ballY: Float = 0f

    private var goaltop: Float = 0f
    private var goalbottom: Float = 0f
    private var goalleft: Float = 0f
    private var goalright: Float = 0f
    private var goalcount:Int = 0

    private var walltop: Float = 0f
    private var wallbottom: Float = 0f
    private var wallleft: Float = 0f
    private var wallright: Float = 0f

    private var walltop2: Float = 0f
    private var wallbottom2: Float = 0f
    private var wallleft2: Float = 0f
    private var wallright2: Float = 0f

    private var walltop3: Float = 0f
    private var wallbottom3: Float = 0f
    private var wallleft3: Float = 0f
    private var wallright3: Float = 0f

    private var walltop4: Float = 0f
    private var wallbottom4: Float = 0f
    private var wallleft4: Float = 0f
    private var wallright4: Float = 0f

    private var life = true


    private var vx: Float = 0f
    private var vy: Float = 0f

    private var time: Long = 0L


    //更新時
    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
        surfaceWidth = width
        surfaceHeight = height
        ballX = (width/2).toFloat()
        ballY = (height/2).toFloat()
    }
    //破棄した時
    override fun surfaceDestroyed(holder: SurfaceHolder?) {
        val sensorManager = getSystemService(Context.SENSOR_SERVICE)as SensorManager
        sensorManager.unregisterListener(this)

    }
    //作成時
    override fun surfaceCreated(holder: SurfaceHolder?) {

        val sensorManager = this.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val accSonsor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sensorManager.registerListener(this,accSonsor,SensorManager.SENSOR_DELAY_GAME)

    }

    //センサーの精度が変わった時に呼ばれるやつ
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {


    }
    //センサーが変わった時に呼ばれるやつ
    override fun onSensorChanged(event: SensorEvent?) {
        if(event == null){return}
        if(!life){
            TextView.text = "ざんねんでした"
            return}

        if(time == 0L) time = System.currentTimeMillis()
        if(event.sensor.type == Sensor.TYPE_ACCELEROMETER){
            val x = -event.values[0]
            val y =event.values[1]

            var t = (System.currentTimeMillis()-time).toFloat()
            time = System.currentTimeMillis()
            t /= 1000.0f


            val dx = vx*t+x*t*t
            val dy = vy*t+y*t*t
            ballX += dx*coef
            ballY += dy*coef
            vx += x*t
            vy += y*t

            if(ballX - radius <0 && vx <0){
                vx = -vx/1.5f
                ballX = radius
            }else if(ballX + radius > surfaceWidth && vx >0){
                vx = -vx/1.5f
                ballX = surfaceWidth - radius
            }
            if(ballY - radius <0 && vy <0){
                vy = -vy/1.5f
                ballY = radius
            }else if(ballY + radius > surfaceHeight && vy >0){
                vy = -vy/1.5f
                ballY = surfaceHeight - radius
            }

            if(ballY - radius < goaltop && ballY - radius > goalbottom){
                if(ballX - radius < goalright && ballX - radius > goalleft){
                    goalcount++
                            var goaltext = goalcount.toString()
                                goaltext += "回ゴールしたよ！"
                            TextView.text = goaltext
                    rand()
                }
            }
            if(ballY - radius < walltop && ballY - radius > wallbottom){
                if(ballX - radius < wallright && ballX - radius > wallleft) {
                    //vy = -vy/1.5f
                    //vx = -vx/1.5f
                    life = false
                }
            }
            if(ballY - radius < walltop2 && ballY - radius > wallbottom2){
                if(ballX - radius < wallright2 && ballX - radius > wallleft2) {
                   // vy = -vy/1.5f
                   // vx = -vx/1.5f

                    life = false
                }
            }
            if(ballY - radius < walltop3 && ballY - radius > wallbottom3){
                if(ballX - radius < wallright3 && ballX - radius > wallleft3) {
                   // vy = -vy/1.5f
                   // vx = -vx/1.5f

                    life = false
                }
            }
            if(ballY - radius < walltop4 && ballY - radius > wallbottom4){
                if(ballX - radius < wallright4 && ballX - radius > wallleft4) {
                   // vy = -vy/1.5f
                   // vx = -vx/1.5f

                    life = false
                }
            }

            drawCavas()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(R.layout.activity_main)
        val holder =  surfaceView.holder
        holder.addCallback(this)

        risettobutton.setOnClickListener{risetto()}

        rand()

    }

    private fun drawCavas(){
        val canvas = surfaceView.holder.lockCanvas()
        canvas.drawColor(Color.WHITE)
        canvas.drawCircle(ballX,ballY,radius, Paint().apply{
            color = Color.GRAY
        })

        canvas.drawRect(goalleft, goaltop, goalright, goalbottom,Paint().apply {
            color = Color.YELLOW
        })

        canvas.drawRect(wallleft, walltop, wallright, wallbottom,Paint().apply {
            color = Color.BLACK
        })

        canvas.drawRect(wallleft2, walltop2, wallright2, wallbottom2,Paint().apply {
            color = Color.BLACK
        })

        canvas.drawRect(wallleft3, walltop3, wallright3, wallbottom3,Paint().apply {
            color = Color.BLACK
        })

        canvas.drawRect(wallleft4, walltop4, wallright4, wallbottom4,Paint().apply {
            color = Color.BLACK
        })

        surfaceView.holder.unlockCanvasAndPost(canvas)
    }

    fun risetto(){
        ballX = (100).toFloat()
        ballY = (100).toFloat()
        TextView.text = "がんばれ！"
        life = true
        rand()
        goalcount=0
    }

    fun rand(){
        goalbottom = (Math.random()*500).toFloat()
        goalleft = (Math.random()*500).toFloat()
        goalright = (Math.random()*500).toFloat() +goalleft
        goaltop = (Math.random()*500).toFloat() + goalbottom

        wallbottom = (Math.random()*500).toFloat()
        wallleft = (Math.random()*500).toFloat()
        wallright = (Math.random()*500).toFloat() +wallleft
        walltop = (Math.random()*500).toFloat() + wallbottom

        wallbottom2 = (Math.random()*500).toFloat()
        wallleft2 = (Math.random()*500).toFloat()
        wallright2 = (Math.random()*500).toFloat() +wallleft2
        walltop2 = (Math.random()*500).toFloat() + wallbottom2

        wallbottom3 = (Math.random()*500).toFloat()
        wallleft3 = (Math.random()*500).toFloat()
        wallright3 = (Math.random()*500).toFloat() +wallleft3
        walltop3 = (Math.random()*500).toFloat() + wallbottom3

        wallbottom4 = (Math.random()*500).toFloat()
        wallleft4 = (Math.random()*500).toFloat()
        wallright4 = (Math.random()*500).toFloat() +wallleft4
        walltop4 = (Math.random()*500).toFloat() + wallbottom4

        if(goaltop<walltop && goalbottom>wallbottom) {
                rand()
        }else if(goalleft>wallleft && goalright<wallright){
                rand()
        }
        if(goaltop<walltop2 && goalbottom>wallbottom2) {
            rand()
        }else if(goalleft>wallleft2 && goalright<wallright2){
            rand()
        }
        if(goaltop<walltop3 && goalbottom>wallbottom3) {
            rand()
        }else if(goalleft>wallleft3 && goalright<wallright3){
            rand()
        }
        if(goaltop<walltop4 && goalbottom>wallbottom4) {
            rand()
        }else if(goalleft>wallleft4 && goalright<wallright4){
            rand()
        }


        if(ballY<walltop && ballY>wallbottom) {
            rand()
        }else if(ballX>wallleft && ballX<wallright){
            rand()
        }
        if(ballY<walltop2 && ballY>wallbottom2) {
            rand()
        }else if(ballX>wallleft2 && ballX<wallright2){
            rand()
        }
        if(ballY<walltop3 && ballY>wallbottom3) {
            rand()
        }else if(ballX>wallleft3 && ballX<wallright3){
            rand()
        }
        if(ballY<walltop4 && ballY>wallbottom4) {
            rand()
        }else if(ballX>wallleft4 && ballX<wallright4){
            rand()
        }
        }
    }
