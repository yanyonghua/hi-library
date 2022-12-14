package org.devio.hi.library.util

import android.graphics.Color

/**
 * @Author yanyonghua
 * @Date 2022/11/18-22:25
 * @Des $.
 */
object ColorUtil {
    //根据百分比 计算出 start  --->end 之间的 中间色
    fun getCurrentColor(startColor: Int,endColor:Int,fraction:Float):Int{
        // color = a r g b
        //endColor(argb) - startColor(argb) =difference(argb)
        //startColor(argb) +difference(argb) *fraction =newColor(argb)
        val redStart= Color.red(startColor)
        val blueStart =Color.blue(startColor)
        val greenStart =Color.green(startColor)
        val alphaStart= Color.alpha(startColor)

        val redEnd= Color.red(endColor)
        val blueEnd =Color.blue(endColor)
        val greenEnd =Color.green(endColor)
        val alphaEnd= Color.alpha(endColor)

        val redDiff = redEnd - redStart
        val blueDiff = blueEnd - blueStart
        val greenDiff = greenEnd - greenStart
        val alphaDiff = alphaEnd - alphaStart

        val redCurrent = (redStart + redDiff * fraction).toInt()
        val blueCurrent = (blueStart + blueDiff * fraction).toInt()
        val greenCurrent = (greenStart + greenDiff * fraction).toInt()
        val alphaCurrent = (alphaStart + alphaDiff * fraction).toInt()
        return Color.argb(alphaCurrent,redCurrent,blueCurrent,greenCurrent)
    }
}