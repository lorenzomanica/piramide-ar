package br.pro.lmit.piramidear

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel

class MatchingPiecesViewModel : ViewModel() {


    enum class Hint {
        TOP, BOTTOM, LEFT, RIGHT
    }

    val pieceVisible = mutableListOf<Boolean>()

    val pieceHints = HashMap<Int, List<Hint>>(
        6
    )

    val pieceDistance = HashMap<Pair<Int, Int>, Float>()

    var showHints: Boolean = false
    var checking = false
    var solved = false


    val labelText: ObservableField<String> = ObservableField()

    init {
        repeat(10) {
            pieceVisible.add(it, false)
        }


        pieceHints[0] = listOf(Hint.BOTTOM)
        pieceHints[1] = listOf(Hint.BOTTOM, Hint.RIGHT, Hint.TOP)
        pieceHints[2] = listOf(Hint.BOTTOM, Hint.LEFT)
        pieceHints[3] = listOf(Hint.RIGHT, Hint.TOP)
        pieceHints[4] = listOf(Hint.LEFT, Hint.RIGHT, Hint.TOP)
        pieceHints[5] = listOf(Hint.LEFT)

        pieceDistance.put(Pair(0, 1), 8.9442f)
        pieceDistance.put(Pair(0, 1), 8.0f)

        updateLabelText()

    }

    fun showHints(value: Boolean) {
        showHints = value
    }

    fun clearVisibility() {
        pieceVisible.fill(false)
    }

    fun updateLabelText() {
        if (checking) {
            if (solved) labelText.set("Parabéns! Você conseguiu.")
            else labelText.set("Falta organizar corretamente os blocos")
        } else if (showHints) labelText.set("Conexões válidas")
        else labelText.set("Construa uma pirâmide de blocos")
    }

    fun checkSolution(value: Boolean) {
        checking = value
        solved = !pieceVisible.any { false }


    }

}
