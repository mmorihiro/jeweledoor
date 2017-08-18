package mmorihiro.larger_circle.view

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Stage
import mmorihiro.larger_circle.controller.BarController
import mmorihiro.larger_circle.controller.PuzzleController


class StageView : View() {
    private var currentViews: List<View> = createView()

    private fun createView(): List<View> {
        val barController = BarController({
            currentViews = createView()
        })
        val barView = barController.view
        val puzzleView =
                PuzzleController(barController::percentEffect).view
        Gdx.input.inputProcessor = puzzleView
        return listOf(puzzleView, barView)
    }

    override fun draw() {
        currentViews.forEach(Stage::draw)
    }

    override fun act(delta: Float) {
        currentViews.forEach { it.act(delta) }
    }
}