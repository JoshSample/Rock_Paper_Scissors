package com.example.app6

import android.content.Context
import android.graphics.Canvas
import android.graphics.Point
import android.graphics.Rect
import android.opengl.Visibility
import android.os.Handler
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.TranslateAnimation
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.view.*
import java.util.*

class MyView : View {
    private var lion : ImageView? = null
    private var cobra : ImageView? = null
    private var rabbit : ImageView? = null
    private var lion_touched = false
    private var cobra_touched = false
    private var rabbit_touched = false
    private var lionCoords : Rect = Rect(0,0,0,0)
    private var cobraCoords : Rect = Rect(0,0,0,0)
    private var rabbitCoords : Rect = Rect(0,0,0,0)
    private var playerCoord = Rect(0, 0, 0, 0)
    private var opponentCoord = Rect(0, 0, 0, 0)
    private var startLionPoint : Point = Point(0,0)
    private var offsetLion : Point = Point(0,0)
    private var startCobraPoint : Point = Point(0,0)
    private var offsetCobra : Point = Point(0,0)
    private var startRabbitPoint : Point = Point(0,0)
    private var offsetRabbit : Point = Point(0,0)
    private var origtouchedLion = Rect(0,0,0,0)
    private var origtouchedCobra = Rect(0,0,0,0)
    private var origtouchedRabbit = Rect(0,0,0,0)
    private var backgroundCoord = Rect(0,0,0,0)
    private var start = true
    private var start2 = true
    private var images = arrayOf<Int>(R.drawable.lion0, R.drawable.lion1, R.drawable.lion2, R.drawable.lion3)
    private var opponents = arrayOf<Int>(R.drawable.lion0, R.drawable.cobra, R.drawable.rabbit)
    private var opponent : ImageView? = null
    private var results : TextView? = null

    companion object {
        private var instance : MyView? = null
        public fun getInstance() : MyView {
            return instance!!
        }
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        this.setWillNotDraw(false)
        instance = this
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        // this assures everything stays in place
        if (start) {
            start = false
            opponent?.visibility = INVISIBLE
        }
        else {
            lion?.setX(lionCoords.left.toFloat())
            lion?.setY(lionCoords.top.toFloat())
            cobra?.setX(cobraCoords.left.toFloat())
            cobra?.setY(cobraCoords.top.toFloat())
            rabbit?.setX(rabbitCoords.left.toFloat())
            rabbit?.setY(rabbitCoords.top.toFloat())
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        lion = MainActivity.getInstance().findViewById(R.id.lion)
        cobra = MainActivity.getInstance().findViewById(R.id.cobra)
        rabbit = MainActivity.getInstance().findViewById(R.id.rabbit)
        opponent = MainActivity.getInstance().findViewById(R.id.opponent)
        results = MainActivity.getInstance().findViewById(R.id.results)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        // this assures everything stays in place
        if (start2) {
            origtouchedLion = MainActivity.getInstance().getOrigTouchedLion()
            origtouchedCobra = MainActivity.getInstance().getOrigTouchedCobra()
            origtouchedRabbit = MainActivity.getInstance().getOrigTouchedRabbit()
            backgroundCoord = MainActivity.getInstance().getBackgroundCoord()
            playerCoord = MainActivity.getInstance().getPlayerCoord()
            opponentCoord = MainActivity.getInstance().getOpponentCoord()
            lionCoords = origtouchedLion
            cobraCoords = origtouchedCobra
            rabbitCoords = origtouchedRabbit
            start2 = false
        }
        var x = event.getX() //Returns a Float
        var y = event.getY()

        if (event.getAction() == MotionEvent.ACTION_DOWN ) {
            if ( (x > lion?.left!!) && (x < lion?.right!!) &&
                (y > lion?.top!!) && (y < lion?.bottom!!)) {
                lionCoords = Rect(lion?.left!!,lion?.top!!,lion?.right!!,lion?.bottom!!)
                startLionPoint.set(x.toInt(),y.toInt())
                offsetLion.set((x - lionCoords.left).toInt(), (y - lionCoords.top).toInt())
                lion_touched = true
                // while the lion is touched, change the lion images
                val handler = Handler()
                var i = 0
                handler.postDelayed(object : Runnable {
                    override fun run() {
                        lion!!.setImageResource(images.get(i))
                        handler.postDelayed(this, 2000)
                        i++
                        if (i > images.size-1)
                            i = 0
                        if (!lion_touched) {
                            handler.removeCallbacks(this)
                            lion!!.setImageResource(images.get(0))
                            lion!!.left = lionCoords.left
                            lion!!.right = lionCoords.right
                            lion!!.top = lionCoords.top
                            lion!!.bottom = lionCoords.bottom
                            invalidate()
                        }
                    }
                }, 2000)
            }
            else if ((x > cobra?.left!!) && (x < cobra?.right!!) &&
                (y > cobra?.top!!) && (y < cobra?.bottom!!)) {
                cobraCoords = Rect(cobra?.left!!,cobra?.top!!,cobra?.right!!,cobra?.bottom!!)
                startCobraPoint.set(x.toInt(),y.toInt())
                offsetCobra.set((x - cobraCoords.left).toInt(), (y - cobraCoords.top).toInt())
                cobra_touched = true
            }
            else if ((x > rabbit?.left!!) && (x < rabbit?.right!!) &&
                (y > rabbit?.top!!) && (y < rabbit?.bottom!!)) {
                rabbitCoords = Rect(rabbit?.left!!,rabbit?.top!!,rabbit?.right!!,rabbit?.bottom!!)
                startRabbitPoint.set(x.toInt(),y.toInt())
                offsetRabbit.set((x - rabbitCoords.left).toInt(), (y - rabbitCoords.top).toInt())
                rabbit_touched = true
            }
        }
        else if (event.getAction() ==  MotionEvent.ACTION_MOVE) {
            if (lion_touched) {
                lionCoords.left = x.toInt() - offsetLion.x
                lionCoords.top = y.toInt() - offsetLion.y
                lionCoords.right = (x + lion?.getWidth()!! - offsetLion.x).toInt()
                lionCoords.bottom = (y + lion?.getHeight()!! - offsetLion.y).toInt()
                this.invalidate()
            }
            else if (cobra_touched) {
                cobraCoords.left = x.toInt() - offsetCobra.x
                cobraCoords.top = y.toInt() - offsetCobra.y
                cobraCoords.right = (x + cobra?.getWidth()!! - offsetCobra.x).toInt()
                cobraCoords.bottom = (y + cobra?.getHeight()!! - offsetCobra.y).toInt()
                this.invalidate()
            }
            else if (rabbit_touched) {
                rabbitCoords.left = x.toInt() - offsetRabbit.x
                rabbitCoords.top = y.toInt() - offsetRabbit.y
                rabbitCoords.right = (x + rabbit?.getWidth()!! - offsetRabbit.x).toInt()
                rabbitCoords.bottom = (y + rabbit?.getHeight()!! - offsetRabbit.y).toInt()
                this.invalidate()
            }
        }
        else if (event.getAction() == MotionEvent.ACTION_UP) {
            if (lion_touched) {
                // check if image is in frame
                if (doOverlap(backgroundCoord, lionCoords)) {
                    lionCoords = playerCoord;
                    lion?.left = lionCoords.left
                    lion?.right = lionCoords.right
                    lion?.top = lionCoords.top
                    lion?.bottom = lionCoords.bottom
                    lion_touched = false
                    var rival = opponents.get((0..2).random())
                    opponent?.setImageResource(rival)
                    opponent?.visibility = VISIBLE
                    results?.visibility = VISIBLE
                    if (rival == R.drawable.lion0) {
                        opponent?.scaleX = -1.0f
                        results?.text = "Tie!"
                        var myFadeIn = AnimationUtils.loadAnimation(MyView.getInstance().context, R.anim.fade_in)
                        var fadeHandler = FadeInHandler()
                        myFadeIn.setAnimationListener(fadeHandler)
                        opponent?.startAnimation(myFadeIn)
                        results?.startAnimation(myFadeIn)
                        var myFadeOut = AnimationUtils.loadAnimation(MyView.getInstance().context, R.anim.fade_out)
                        var fadeOutHandler = FadeOutHandler()
                        myFadeOut.setAnimationListener(fadeOutHandler)
                        var lionFadeOut = AnimationUtils.loadAnimation(MyView.getInstance().context, R.anim.fade_out)
                        var lionHandler = FadeOutHandler()
                        lionFadeOut.setAnimationListener(lionHandler)
                        myFadeOut.fillAfter = false
                        lionFadeOut.fillAfter = false
                        lion?.startAnimation(lionFadeOut)
                        opponent?.startAnimation(myFadeOut)

                    }
                    if (rival == R.drawable.cobra) {
                        opponent?.scaleX = -1.0f
                        results?.text = "Cobra defeats Lion - You Lose!"
                        var myFadeIn = AnimationUtils.loadAnimation(MyView.getInstance().context, R.anim.fade_in)
                        var fadeHandler = FadeInHandler()
                        myFadeIn.setAnimationListener(fadeHandler)
                        opponent?.startAnimation(myFadeIn)
                        results?.startAnimation(myFadeIn)
                        var myFadeOut = AnimationUtils.loadAnimation(MyView.getInstance().context, R.anim.fade_out)
                        var fadeOutHandler = FadeOutHandler()
                        myFadeOut.setAnimationListener(fadeOutHandler)
                        var lionFadeOut = AnimationUtils.loadAnimation(MyView.getInstance().context, R.anim.fade_out)
                        var lionHandler = FadeOutHandler()
                        lionFadeOut.setAnimationListener(lionHandler)
                        lionFadeOut.fillAfter = false
                        lion?.startAnimation(lionFadeOut)
                        var deltaX = (playerCoord.left-opponentCoord.left).toFloat()
                        var deltaY = (playerCoord.top-opponentCoord.top).toFloat()
                        var moveAnim = TranslateAnimation(0f, deltaX,
                            0f, deltaY)
                        moveAnim.duration = 2000
                        moveAnim.fillAfter = true
                        var handler = MoveHandler()
                        moveAnim.setAnimationListener(handler)
                        opponent?.startAnimation(moveAnim)
                        var score = MainActivity.getInstance().findViewById<TextView>(R.id.phoneScore)
                        score.setText("" + (Integer.parseInt(score.text.toString()) + 1))
                    }
                    if (rival == R.drawable.rabbit) {
                        results?.text = "Lion defeats Rabbit - You Win!"
                        var score = MainActivity.getInstance().findViewById<TextView>(R.id.playerScore)
                        score.setText("" + (Integer.parseInt(score.text.toString()) + 1))
                        var myFadeIn = AnimationUtils.loadAnimation(MyView.getInstance().context, R.anim.fade_in)
                        var fadeHandler = FadeInHandler()
                        myFadeIn.setAnimationListener(fadeHandler)
                        opponent?.startAnimation(myFadeIn)
                        results?.startAnimation(myFadeIn)
                        var myFadeOut = AnimationUtils.loadAnimation(MyView.getInstance().context, R.anim.fade_out)
                        var fadeOutHandler = FadeOutHandler()
                        myFadeOut.setAnimationListener(fadeOutHandler)
                        var lionFadeOut = AnimationUtils.loadAnimation(MyView.getInstance().context, R.anim.fade_out)
                        var lionHandler = FadeOutHandler()
                        lionFadeOut.setAnimationListener(lionHandler)
                        lionFadeOut.fillAfter = false
                        var deltaX = (opponentCoord.left-playerCoord.left).toFloat()
                        var deltaY = (opponentCoord.top-playerCoord.top).toFloat()
                        var moveAnim = TranslateAnimation(0f, deltaX,
                            0f, deltaY)
                        moveAnim.duration = 2000
                        moveAnim.fillAfter = false
                        var handler = MoveHandler()
                        moveAnim.setAnimationListener(handler)
                        lion?.startAnimation(moveAnim)
                        opponent?.startAnimation(lionFadeOut)
                    }
                }
                // else set coords to original spot
                else {
                    lionCoords = origtouchedLion
                    lion?.left = lionCoords.left
                    lion?.right = lionCoords.right
                    lion?.top = lionCoords.top
                    lion?.bottom = lionCoords.bottom
                    lion_touched = false
                }
                this.invalidate()
            }
            else if (cobra_touched) {
                // check if image is in frame
                if (doOverlap(backgroundCoord, cobraCoords)) {
                    cobraCoords = playerCoord;
                    cobra?.left = cobraCoords.left
                    cobra?.right = cobraCoords.right
                    cobra?.top = cobraCoords.top
                    cobra?.bottom = cobraCoords.bottom
                    cobra_touched = false
                    var rival = opponents.get((0..2).random())
                    opponent?.setImageResource(rival)
                    opponent?.visibility = VISIBLE
                    results?.visibility = VISIBLE
                    if (rival == R.drawable.lion0) {
                        results?.text = "Cobra defeats Lion - You Win!"
                        var score = MainActivity.getInstance().findViewById<TextView>(R.id.playerScore)
                        score.setText("" + (Integer.parseInt(score.text.toString()) + 1))
                        var myFadeIn = AnimationUtils.loadAnimation(MyView.getInstance().context, R.anim.fade_in)
                        var fadeHandler = FadeInHandler()
                        myFadeIn.setAnimationListener(fadeHandler)
                        opponent?.startAnimation(myFadeIn)
                        results?.startAnimation(myFadeIn)
                        var myFadeOut = AnimationUtils.loadAnimation(MyView.getInstance().context, R.anim.fade_out)
                        var fadeOutHandler = FadeOutHandler()
                        myFadeOut.setAnimationListener(fadeOutHandler)
                        var cobraFadeOut = AnimationUtils.loadAnimation(MyView.getInstance().context, R.anim.fade_out)
                        var cobraHandler = FadeOutHandler()
                        cobraFadeOut.setAnimationListener(cobraHandler)
                        cobraFadeOut.fillAfter = false
                        var deltaX = (opponentCoord.left-playerCoord.left).toFloat()
                        var deltaY = (opponentCoord.top-playerCoord.top).toFloat()
                        var moveAnim = TranslateAnimation(0f, deltaX,
                            0f, deltaY)
                        moveAnim.duration = 2000
                        moveAnim.fillAfter = false
                        var handler = MoveHandler()
                        moveAnim.setAnimationListener(handler)
                        cobra?.startAnimation(moveAnim)
                        opponent?.startAnimation(cobraFadeOut)
                    }
                    if (rival == R.drawable.cobra) {
                        opponent?.scaleX = -1.0f
                        results?.text = "Tie!"
                        var myFadeIn = AnimationUtils.loadAnimation(MyView.getInstance().context, R.anim.fade_in)
                        var fadeHandler = FadeInHandler()
                        myFadeIn.setAnimationListener(fadeHandler)
                        opponent?.startAnimation(myFadeIn)
                        results?.startAnimation(myFadeIn)
                        var myFadeOut = AnimationUtils.loadAnimation(MyView.getInstance().context, R.anim.fade_out)
                        var fadeOutHandler = FadeOutHandler()
                        myFadeOut.setAnimationListener(fadeOutHandler)
                        var cobraFadeOut = AnimationUtils.loadAnimation(MyView.getInstance().context, R.anim.fade_out)
                        var cobraHandler = FadeOutHandler()
                        cobraFadeOut.setAnimationListener(cobraHandler)
                        myFadeOut.fillAfter = false
                        cobraFadeOut.fillAfter = false
                        cobra?.startAnimation(cobraFadeOut)
                        opponent?.startAnimation(myFadeOut)
                    }
                    if (rival == R.drawable.rabbit) {
                        opponent?.scaleX = -1.0f
                        results?.text = "Rabbit defeats Cobra - You Lose!"
                        var myFadeIn = AnimationUtils.loadAnimation(MyView.getInstance().context, R.anim.fade_in)
                        var fadeHandler = FadeInHandler()
                        myFadeIn.setAnimationListener(fadeHandler)
                        opponent?.startAnimation(myFadeIn)
                        results?.startAnimation(myFadeIn)
                        var myFadeOut = AnimationUtils.loadAnimation(MyView.getInstance().context, R.anim.fade_out)
                        var fadeOutHandler = FadeOutHandler()
                        myFadeOut.setAnimationListener(fadeOutHandler)
                        var cobraFadeOut = AnimationUtils.loadAnimation(MyView.getInstance().context, R.anim.fade_out)
                        var cobraHandler = FadeOutHandler()
                        cobraFadeOut.setAnimationListener(cobraHandler)
                        cobraFadeOut.fillAfter = false
                        cobra?.startAnimation(cobraFadeOut)
                        var deltaX = (playerCoord.left-opponentCoord.left).toFloat()
                        var deltaY = (playerCoord.top-opponentCoord.top).toFloat()
                        var moveAnim = TranslateAnimation(0f, deltaX,
                            0f, deltaY)
                        moveAnim.duration = 2000
                        moveAnim.fillAfter = true
                        var handler = MoveHandler()
                        moveAnim.setAnimationListener(handler)
                        opponent?.startAnimation(moveAnim)
                        var score = MainActivity.getInstance().findViewById<TextView>(R.id.phoneScore)
                        score.setText("" + (Integer.parseInt(score.text.toString()) + 1))
                    }
                }
                // else set coords to original spot
                else {
                    cobraCoords = origtouchedCobra
                    cobra?.left = cobraCoords.left
                    cobra?.right = cobraCoords.right
                    cobra?.top = cobraCoords.top
                    cobra?.bottom = cobraCoords.bottom
                    cobra_touched = false
                }
                this.invalidate()
            }
            else if (rabbit_touched) {
                // check if image is in frame
                if (doOverlap(backgroundCoord, rabbitCoords)) {
                    rabbitCoords = playerCoord;
                    rabbit?.left = rabbitCoords.left
                    rabbit?.right = rabbitCoords.right
                    rabbit?.top = rabbitCoords.top
                    rabbit?.bottom = rabbitCoords.bottom
                    rabbit?.scaleX = -1.0f
                    rabbit_touched = false
                    var rival = opponents.get((0..2).random())
                    opponent?.setImageResource(rival)
                    opponent?.visibility = VISIBLE
                    results?.visibility = VISIBLE
                    if (rival == R.drawable.lion0) {
                        opponent?.scaleX = -1.0f
                        results?.text = "Lion defeats Rabbit - You Lose!"
                        var myFadeIn = AnimationUtils.loadAnimation(MyView.getInstance().context, R.anim.fade_in)
                        var fadeHandler = FadeInHandler()
                        myFadeIn.setAnimationListener(fadeHandler)
                        opponent?.startAnimation(myFadeIn)
                        results?.startAnimation(myFadeIn)
                        var myFadeOut = AnimationUtils.loadAnimation(MyView.getInstance().context, R.anim.fade_out)
                        var fadeOutHandler = FadeOutHandler()
                        myFadeOut.setAnimationListener(fadeOutHandler)
                        var rabbitFadeOut = AnimationUtils.loadAnimation(MyView.getInstance().context, R.anim.fade_out)
                        var rabbitHandler = FadeOutHandler()
                        rabbitFadeOut.setAnimationListener(rabbitHandler)
                        rabbitFadeOut.fillAfter = false
                        rabbit?.startAnimation(rabbitFadeOut)
                        var deltaX = (playerCoord.left-opponentCoord.left).toFloat()
                        var deltaY = (playerCoord.top-opponentCoord.top).toFloat()
                        var moveAnim = TranslateAnimation(0f, deltaX,
                            0f, deltaY)
                        moveAnim.duration = 2000
                        moveAnim.fillAfter = true
                        var handler = MoveHandler()
                        moveAnim.setAnimationListener(handler)
                        opponent?.startAnimation(moveAnim)
                        var score = MainActivity.getInstance().findViewById<TextView>(R.id.phoneScore)
                        score.setText("" + (Integer.parseInt(score.text.toString()) + 1))
                    }
                    if (rival == R.drawable.cobra) {
                        opponent?.scaleX = -1.0f
                        results?.text = "Rabbit defeats Cobra - You Win!"
                        var score = MainActivity.getInstance().findViewById<TextView>(R.id.playerScore)
                        score.setText("" + (Integer.parseInt(score.text.toString()) + 1))
                        var myFadeIn = AnimationUtils.loadAnimation(MyView.getInstance().context, R.anim.fade_in)
                        var fadeHandler = FadeInHandler()
                        myFadeIn.setAnimationListener(fadeHandler)
                        opponent?.startAnimation(myFadeIn)
                        results?.startAnimation(myFadeIn)
                        var myFadeOut = AnimationUtils.loadAnimation(MyView.getInstance().context, R.anim.fade_out)
                        var fadeOutHandler = FadeOutHandler()
                        myFadeOut.setAnimationListener(fadeOutHandler)
                        var rabbitFadeOut = AnimationUtils.loadAnimation(MyView.getInstance().context, R.anim.fade_out)
                        var rabbitHandler = FadeOutHandler()
                        rabbitFadeOut.setAnimationListener(rabbitHandler)
                        rabbitFadeOut.fillAfter = false
                        var deltaX = (opponentCoord.left-playerCoord.left).toFloat()
                        var deltaY = (opponentCoord.top-playerCoord.top).toFloat()
                        var moveAnim = TranslateAnimation(0f, deltaX,
                            0f, deltaY)
                        moveAnim.duration = 2000
                        moveAnim.fillAfter = false
                        var handler = MoveHandler()
                        moveAnim.setAnimationListener(handler)
                        rabbit?.startAnimation(moveAnim)
                        opponent?.startAnimation(rabbitFadeOut)
                    }
                    if (rival == R.drawable.rabbit) {
                        results?.text = "Tie!"
                        var myFadeIn = AnimationUtils.loadAnimation(MyView.getInstance().context, R.anim.fade_in)
                        var fadeHandler = FadeInHandler()
                        myFadeIn.setAnimationListener(fadeHandler)
                        opponent?.startAnimation(myFadeIn)
                        results?.startAnimation(myFadeIn)
                        var myFadeOut = AnimationUtils.loadAnimation(MyView.getInstance().context, R.anim.fade_out)
                        var fadeOutHandler = FadeOutHandler()
                        myFadeOut.setAnimationListener(fadeOutHandler)
                        var rabbitFadeOut = AnimationUtils.loadAnimation(MyView.getInstance().context, R.anim.fade_out)
                        var rabbitHandler = FadeOutHandler()
                        rabbitFadeOut.setAnimationListener(rabbitHandler)
                        myFadeOut.fillAfter = false
                        rabbitFadeOut.fillAfter = false
                        rabbit?.startAnimation(rabbitFadeOut)
                        opponent?.startAnimation(myFadeOut)
                    }
                }
                // else set coords to original spot
                else {
                    rabbitCoords = origtouchedRabbit
                    rabbit?.left = rabbitCoords.left
                    rabbit?.right = rabbitCoords.right
                    rabbit?.top = rabbitCoords.top
                    rabbit?.bottom = rabbitCoords.bottom
                    if (rabbit?.scaleX == -1.0f)
                        rabbit?.scaleX = 1.0f
                    rabbit_touched = false
                }
                this.invalidate()
            }
        }

        return true
    }

    /*********************Move Handler**********************************/
    inner class MoveHandler : Animation.AnimationListener {
        override fun onAnimationRepeat(animation: Animation?) {

        }
        override fun onAnimationEnd(animation: Animation?) {

        }
        override fun onAnimationStart(animation: Animation?) {

        }
    }


    /**************************FadeInHandler**********************************************/
    inner class FadeInHandler : Animation.AnimationListener {
        override fun onAnimationRepeat(animation: Animation?) {

        }
        override fun onAnimationEnd(animation: Animation?) {

        }
        override fun onAnimationStart(animation: Animation?) {

        }
    }

    /*********************FadeOutHandler**********************************/
    inner class FadeOutHandler : Animation.AnimationListener {
        override fun onAnimationRepeat(animation: Animation?) {

        }
        override fun onAnimationEnd(animation: Animation?) {
            opponent?.visibility = INVISIBLE
            results?.visibility = INVISIBLE
            if (lionCoords == playerCoord) {
                lionCoords = origtouchedLion
                lion?.left = lionCoords.left
                lion?.right = lionCoords.right
                lion?.top = lionCoords.top
                lion?.bottom = lionCoords.bottom
                invalidate()
                var myFadeOut = AnimationUtils.loadAnimation(MyView.getInstance().context, R.anim.fade_out)
                var fadeOutHandler = FadeOutHandler()
                myFadeOut.setAnimationListener(fadeOutHandler)
                results?.startAnimation(myFadeOut)
            }
            if (cobraCoords == playerCoord) {
                cobraCoords = origtouchedCobra
                cobra?.left = cobraCoords.left
                cobra?.right = cobraCoords.right
                cobra?.top = cobraCoords.top
                cobra?.bottom = cobraCoords.bottom
                invalidate()
            }
            if (rabbitCoords == playerCoord) {
                rabbitCoords = origtouchedRabbit
                rabbit?.left = rabbitCoords.left
                rabbit?.right = rabbitCoords.right
                rabbit?.top = rabbitCoords.top
                rabbit?.bottom = rabbitCoords.bottom
                if (rabbit?.scaleX == -1.0f)
                    rabbit?.scaleX = 1.0f
                invalidate()
            }
        }
        override fun onAnimationStart(animation: Animation?) {

        }
    }

    // function to determine if rectangles overlap
    fun doOverlap(rect1 : Rect, rect2 : Rect) : Boolean {
        if (rect1.left > rect2.right || rect2.left > rect1.right)
            return false
        if (rect1.top > rect2.bottom || rect2.top > rect1.bottom)
            return false
        return true
    }

}