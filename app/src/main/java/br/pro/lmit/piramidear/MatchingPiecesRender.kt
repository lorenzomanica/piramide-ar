package br.pro.lmit.piramidear

import android.opengl.GLES20
import org.artoolkitx.arx.arxj.ARController
import org.artoolkitx.arx.arxj.Trackable
import org.artoolkitx.arx.arxj.rendering.ARRenderer
import org.artoolkitx.arx.arxj.rendering.shader_impl.Cube
import org.artoolkitx.arx.arxj.rendering.shader_impl.SimpleFragmentShader
import org.artoolkitx.arx.arxj.rendering.shader_impl.SimpleShaderProgram
import org.artoolkitx.arx.arxj.rendering.shader_impl.SimpleVertexShader
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10


class MatchingPiecesRender(

    private val model: MatchingPiecesViewModel

) : ARRenderer() {


    private var shaderProgram: SimpleShaderProgram? = null

    //TODO: I think we should add the trackable class to the library (arxj)

    private val trackables = arrayOf(
        Trackable("a", 40.0f),
        Trackable("b", 40.0f),
        Trackable("c", 40.0f),
        Trackable("d", 40.0f),
        Trackable("f", 40.0f),
        Trackable("g", 40.0f)
    )
    private val trackableUIDs = IntArray(trackables.size)

    private lateinit var topHint: Cube
    private lateinit var leftHint: Cube
    private lateinit var bottomHint: Cube
    private lateinit var rightHint: Cube
    private lateinit var piece: Cube
    private lateinit var piece2: Cube
    private lateinit var piece3: Cube

    /**
     * Markers can be configured here.
     */
    override fun configureARScene(): Boolean {
        var i = 0
        for (trackable in trackables) {
            trackableUIDs[i] =
                ARController.getInstance()
                    .addTrackable("single;patterns/" + trackable.name + ".patt;" + trackable.width)
            if (trackableUIDs[i] < 0) return false
            i++
        }
        return true
    }

    //Shader calls should be within a GL thread. GL threads are onSurfaceChanged(), onSurfaceCreated() or onDrawFrame()
    //As the cube instantiates the shader during setShaderProgram call we need to create the cube here.
    override fun onSurfaceCreated(unused: GL10?, config: EGLConfig?) {
        this.shaderProgram = SimpleShaderProgram(SimpleVertexShader(), SimpleFragmentShader())

        topHint = Cube(10.0f, 0.0f, 40.0f, -5.0f)
        leftHint = Cube(10.0f, -40.0f, 0.0f, -5.0f)
        bottomHint = Cube(10.0f, 0.0f, -40.0f, -5.0f)
        rightHint = Cube(10.0f, 40.0f, 0.0f, -5.0f)
        piece = Cube(80.0f, 0.0f, 0.0f, 40.0f)
        piece2 = Cube(80.0f, -80.0f, 0.0f, 120.0f)
        piece3 = Cube(80.0f, -160.0f, 0.0f, 200.0f)

        topHint.setShaderProgram(shaderProgram)
        leftHint.setShaderProgram(shaderProgram)
        bottomHint.setShaderProgram(shaderProgram)
        rightHint.setShaderProgram(shaderProgram)
        piece.setShaderProgram(shaderProgram)
        piece2.setShaderProgram(shaderProgram)
        piece3.setShaderProgram(shaderProgram)

        super.onSurfaceCreated(unused, config)
    }

    /**
     * Override the draw function from ARRenderer.
     */
    override fun draw() {
        super.draw()

        GLES20.glEnable(GLES20.GL_CULL_FACE)
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)
        GLES20.glFrontFace(GLES20.GL_CCW)

        model.clearVisibility()

        // Look for trackables, and draw on each found one.
        for (trackableUID in trackableUIDs) {
            // If the trackable is visible, apply its transformation, and render a cube
            val modelViewMatrix = FloatArray(16)
            if (ARController.getInstance().queryTrackableVisibilityAndTransformation(trackableUID, modelViewMatrix)) {
                val projectionMatrix = ARController.getInstance().getProjectionMatrix(10.0f, 10000.0f)

                when (trackableUID) {
                    0 -> model.pieceVisible[0] = true
                    1 -> model.pieceVisible[1] = true
                    2 -> model.pieceVisible[2] = true
                    3 -> model.pieceVisible[3] = true
                    4 -> model.pieceVisible[4] = true
                    5 -> model.pieceVisible[5] = true
                    6 -> model.pieceVisible[6] = true
                }

                if (model.pieceVisible[0] && model.pieceVisible[1] && model.pieceVisible[2]) {
                    model.pieceVisible[6] = true
                }
                if (model.pieceVisible[1] && model.pieceVisible[3] && model.pieceVisible[4]) {
                    model.pieceVisible[7] = true
                }
                if (model.pieceVisible[2] && model.pieceVisible[4] && model.pieceVisible[5]) {
                    model.pieceVisible[8] = true
                }
                if (model.pieceVisible[6] && model.pieceVisible[7] && model.pieceVisible[8]) {
                    model.pieceVisible[9] = true
                }

                /* HERE STARTS DRAWINGS */

                if (model.showHints) {
                    model.pieceHints.get(trackableUID)?.let {
                        it.forEach { hint ->
                            if (hint === MatchingPiecesViewModel.Hint.BOTTOM)
                                bottomHint.draw(projectionMatrix, modelViewMatrix)

                            if (hint === MatchingPiecesViewModel.Hint.LEFT)
                                leftHint.draw(projectionMatrix, modelViewMatrix)

                            if (hint === MatchingPiecesViewModel.Hint.RIGHT)
                                rightHint.draw(projectionMatrix, modelViewMatrix)

                            if (hint === MatchingPiecesViewModel.Hint.TOP)
                                topHint.draw(projectionMatrix, modelViewMatrix)
                        }

                    }
                } else {
                    when (trackableUID) {
                        0 -> piece.draw(projectionMatrix, modelViewMatrix)
                        1 -> if (model.pieceVisible[1]) piece.draw(projectionMatrix, modelViewMatrix)
                        2 -> if (model.pieceVisible[2]) piece.draw(projectionMatrix, modelViewMatrix)
                        3 -> if (model.pieceVisible[3]) piece.draw(projectionMatrix, modelViewMatrix)
                        4 -> if (model.pieceVisible[4]) piece.draw(projectionMatrix, modelViewMatrix)
                        5 -> if (model.pieceVisible[5]) piece.draw(projectionMatrix, modelViewMatrix)
                    }

                    when (trackableUID) {
                        2 -> if (model.pieceVisible[6]) piece2.draw(projectionMatrix, modelViewMatrix)
                        4 -> if (model.pieceVisible[7]) piece2.draw(projectionMatrix, modelViewMatrix)
                        5 -> if (model.pieceVisible[8]) piece2.draw(projectionMatrix, modelViewMatrix)
                    }

                    when (trackableUID) {
                        5 -> if (model.pieceVisible[9]) piece3.draw(projectionMatrix, modelViewMatrix)
                    }

                }

            }
        }

    }


}


//                Log.d(
//                    "[PROJ_MATRIX]", "\n" +
//                            "[${projectionMatrix[0]} ${projectionMatrix[1]} ${projectionMatrix[2]} ${projectionMatrix[3] }" +
//                            " ${projectionMatrix[4]} ${projectionMatrix[5]} ${projectionMatrix[6]} ${projectionMatrix[7]} " +
//                            " ${projectionMatrix[8]} ${projectionMatrix[9]} ${projectionMatrix[10]} ${projectionMatrix[11]}" +
//                            " ${projectionMatrix[12]} ${projectionMatrix[13]} ${projectionMatrix[14]} ${projectionMatrix[15]}]"
//                )
//                Log.d(
//                    "[MODEL_MATRIX]", "\n" +
//                            "[${modelViewMatrix[0]} ${modelViewMatrix[1]} ${modelViewMatrix[2]} ${modelViewMatrix[3]} " +
//                            " ${modelViewMatrix[4]} ${modelViewMatrix[5]} ${modelViewMatrix[6]} ${modelViewMatrix[7]} " +
//                            " ${modelViewMatrix[8]} ${modelViewMatrix[9]} ${modelViewMatrix[10]} ${modelViewMatrix[11]}" +
//                            " ${modelViewMatrix[12]} ${modelViewMatrix[13]} ${modelViewMatrix[14]} ${modelViewMatrix[15]}]"
//                )
