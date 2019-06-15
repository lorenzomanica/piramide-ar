package br.pro.lmit.piramidear

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val label = findViewById<TextView>(R.id.label1)
        label.text = Html.fromHtml(getString(R.string.instructions))

        window.decorView.setOnTouchListener(
            object : HorizontalSwipeListener(96) {
                override fun onSwipeRight() {
                    Toast.makeText(this@MainActivity, "Swipe Left", Toast.LENGTH_SHORT).show()
                }

                override fun onSwipeLeft() {
                    Toast.makeText(this@MainActivity, "Swipe Right", Toast.LENGTH_SHORT).show()
                }

            })

        val startButton = findViewById<Button>(R.id.button)
        startButton.setOnClickListener {
            startActivity(Intent(this, MatchingPiecesActivity::class.java))
        }

    }
}
