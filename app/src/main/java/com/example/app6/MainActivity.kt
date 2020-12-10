package com.example.app6

import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {
    // data fields for starting positions of the images
    private var origtouchedLion = Rect(0,0,0,0)
    private var origtouchedCobra = Rect(0,0,0,0)
    private var origtouchedRabbit = Rect(0,0,0,0)
    private var backgroundCoord = Rect(0,0,0,0)
    private var playerCoord = Rect(0, 0, 0, 0)
    private var opponentCoord = Rect(0, 0, 0, 0)


    // instance of main activity
    companion object {
        private var instance : MainActivity? = null
        public fun getInstance() : MainActivity {
            return instance!!
        }
    }

    // setters and getters for starting position of respective images
    fun setOrigTouchedLion(origtouchedLion : Rect) {
        this.origtouchedLion = origtouchedLion
    }

    fun getOrigTouchedLion() : Rect {
        return origtouchedLion
    }

    fun setOrigTouchedCobra(origtouchedCobra : Rect) {
        this.origtouchedCobra = origtouchedCobra
    }

    fun getOrigTouchedCobra() : Rect {
        return origtouchedCobra
    }

    fun setOrigTouchedRabbit(origtouchedRabbit : Rect) {
        this.origtouchedRabbit = origtouchedRabbit
    }

    fun getOrigTouchedRabbit() : Rect {
        return origtouchedRabbit
    }

    fun setBackgroundCoord(backgroundCoord : Rect) {
        this.backgroundCoord = backgroundCoord
    }

    fun getBackgroundCoord() : Rect {
        return backgroundCoord
    }

    fun setPlayerCoord(playerCoord : Rect) {
        this.playerCoord = playerCoord
    }

    fun getPlayerCoord() : Rect {
        return playerCoord
    }

    fun setOpponentCoord(opponentCoord : Rect) {
        this.opponentCoord = opponentCoord
    }

    fun getOpponentCoord() : Rect {
        return opponentCoord
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        instance = this

        player.visibility = View.INVISIBLE
        phoneText.visibility = View.INVISIBLE
        phoneScore.visibility = View.INVISIBLE
        playerText.visibility = View.INVISIBLE
        playerScore.visibility = View.INVISIBLE
        // start title zoom
        var myZoom = AnimationUtils.loadAnimation(MainActivity.getInstance(), R.anim.zoom_in)
        var zoomHandler = ZoomHandler()
        myZoom.setAnimationListener(zoomHandler)
        titleImage.startAnimation(myZoom)
    }

    /*********************ZoomHandler**********************************/
    inner class ZoomHandler : Animation.AnimationListener {
        override fun onAnimationRepeat(animation: Animation?) {

        }
        override fun onAnimationEnd(animation: Animation?) {
            // fade away title after zooming
            var myFade = AnimationUtils.loadAnimation(MainActivity.getInstance(), R.anim.fade_out)
            var fadeHandler = FadeOutHandler()
            myFade.setAnimationListener(fadeHandler)
            titleImage.startAnimation(myFade)
        }
        override fun onAnimationStart(animation: Animation?) {
            // set starting positions of lion, cobra, rabbit, and background
            setOrigTouchedLion(Rect(lion.left,lion.top,lion.right, lion.bottom))
            setOrigTouchedCobra(Rect(cobra.left,cobra.top,cobra.right, cobra.bottom))
            setOrigTouchedRabbit(Rect(rabbit.left,rabbit.top,rabbit.right, rabbit.bottom))
            setBackgroundCoord(Rect(frame.left, frame.top, frame.right, frame.bottom))
            setPlayerCoord(Rect(player.left, player.top, player.right, player.bottom))
            setOpponentCoord(Rect(opponent.left, opponent.top, opponent.right, opponent.bottom))
        }
    }

    /*********************FadeOutHandler**********************************/
    inner class FadeOutHandler : Animation.AnimationListener {
        override fun onAnimationRepeat(animation: Animation?) {

        }
        override fun onAnimationEnd(animation: Animation?) {
            var myFadeIn = AnimationUtils.loadAnimation(MainActivity.getInstance(), R.anim.fade_in)
            var fadeHandler = FadeInHandler()
            myFadeIn.setAnimationListener(fadeHandler)
            phoneScore.startAnimation(myFadeIn)
            phoneText.startAnimation(myFadeIn)
            playerScore.startAnimation(myFadeIn)
            playerText.startAnimation(myFadeIn)
        }
        override fun onAnimationStart(animation: Animation?) {

        }
    }

    /**************************FadeInHandler**********************************************/
    inner class FadeInHandler : Animation.AnimationListener {
        override fun onAnimationRepeat(animation: Animation?) {

        }
        override fun onAnimationEnd(animation: Animation?) {
            phoneText.visibility = View.VISIBLE
            phoneScore.visibility = View.VISIBLE
            playerText.visibility = View.VISIBLE
            playerScore.visibility = View.VISIBLE
        }
        override fun onAnimationStart(animation: Animation?) {

        }
    }



}


