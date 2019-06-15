package br.pro.lmit.piramidear

import android.os.Bundle
import android.os.Handler
import android.widget.FrameLayout
import android.widget.TextView
import androidx.databinding.Observable
import androidx.databinding.ObservableField
import org.artoolkitx.arx.arxj.ARActivity
import org.artoolkitx.arx.arxj.rendering.ARRenderer


class MatchingPiecesActivity : ARActivity() {

    private lateinit var viewModel: MatchingPiecesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_layout)

        viewModel = MatchingPiecesViewModel()

        val label = this.findViewById<TextView>(R.id.label1)

        viewModel.labelText.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                label.text = (sender as ObservableField<String>).get()
            }

        })

        window.decorView.setOnTouchListener(object : HorizontalSwipeListener(96) {
            override fun onSwipeRight() {
                viewModel.showHints(true)
                viewModel.updateLabelText()
                Handler().postDelayed({
                    viewModel.showHints(false)
                    viewModel.updateLabelText()
                }, 10000)
            }

            override fun onSwipeLeft() {
                viewModel.checkSolution(true)
                viewModel.updateLabelText()
                Handler().postDelayed({
                    viewModel.checkSolution(false)
                    viewModel.updateLabelText()
                }, 3000)
            }
        })

    }

    override fun supplyFrameLayout(): FrameLayout {
        return this.findViewById(R.id.main_framelayout) as FrameLayout
    }

    override fun supplyRenderer(): ARRenderer {
        return MatchingPiecesRender(viewModel)
    }


}
