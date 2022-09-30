package com.example.svgtojsonmapping

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.util.TypedValue
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.svgtojsonmapping.dataModel.SVGModel
import com.example.svgtojsonmapping.databinding.ActivityMainBinding
import java.io.IOException
import java.io.InputStream
import java.nio.charset.StandardCharsets
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class MainActivity : AppCompatActivity() {

    private lateinit var mainBinding: ActivityMainBinding

    private val workerThread: ExecutorService = Executors.newCachedThreadPool()
    private val workerHandler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        mainBinding.readSvg.setOnClickListener {

            workerThread.execute {
                val sVGModel = SVGModel.fromJson(loadJSONFromAsset()!!)

                if (sVGModel != null) {

                    Log.d("myTag", "${sVGModel.svg!!}")

                    val rectColorStyle = sVGModel.svg.rect?.style?.replace("fill:", "")

                    val svgText = sVGModel.svg.text?.text

                    val svgTextColorStyle = sVGModel.svg.text?.style?.substringAfterLast("fill:")
                        ?.substringBeforeLast(";")

                    val svgTextSize = sVGModel.svg.text?.style?.substringAfterLast("font-size:")
                        ?.substringBeforeLast("px;")

                    val svgTextFontName =
                        sVGModel.svg.text?.style?.substringAfterLast("font-family:")
                            ?.substringBeforeLast(",")

                    val svgTextX = sVGModel.svg.text?.transform?.replace("translate", "")
                        ?.substringAfterLast("(")?.substringBeforeLast(" ")

                    val svgTexty = sVGModel.svg.text?.transform?.replace("translate", "")
                        ?.substringAfterLast(" ")?.substringBeforeLast(")")

                    val svgPath = sVGModel.svg.path?.get(1)?.d

                    Log.d("myTextStyle", "$svgPath")

                    val pathList2 = listOf(PathData(svgPath, Color.parseColor("#ffffff")))

                    workerHandler.post {
                        rectColorStyle?.let {
                            mainBinding.mainRoot.setBackgroundColor(Color.parseColor(it))
                        }

                        svgText?.let {
                            val newText = TextView(this@MainActivity)
                            newText.text = it

                            if (svgTextColorStyle != null) {
                                newText.setTextColor(Color.parseColor(svgTextColorStyle))
                            }

                            if (svgTextSize != null) {
                                newText.setTextSize(TypedValue.COMPLEX_UNIT_PX, 151F)
                            }

                            if (svgTextFontName != null) {
                                val typeface =
                                    Typeface.createFromAsset(assets, "$svgTextFontName.ttf")
                                newText.typeface = typeface
                            }

                            mainBinding.mainRoot.addView(newText)

                            workerHandler.postDelayed({

                                if (svgTexty != null) {
                                    newText.y = 480f - newText.height
                                }

                                if (svgTextX != null) {
                                    newText.x = 90f
                                }

                            }, 500)

                            val newImageView = ImageView(this@MainActivity)

                            newImageView.setImageDrawable(
                                VectorDrawableCreator.getVectorDrawable(
                                    this,
                                    1000,
                                    1000,
                                    1000f,
                                    1000f,
                                    pathList2
                                )
                            )

                            mainBinding.mainRoot.addView(newImageView)
                        }
                    }

                } else {
                    Log.d("myTag", "Svg is  null")
                }
            }

        }

    }

    //*******************This method return the Json String *********************//
    private fun loadJSONFromAsset(): String? {
        val json: String = try {
            val `is`: InputStream = this.assets.open("1.json")
            val size = `is`.available()
            val buffer = ByteArray(size)
            `is`.read(buffer)
            `is`.close()
            String(buffer, StandardCharsets.UTF_8)
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }
        return json
    }

}