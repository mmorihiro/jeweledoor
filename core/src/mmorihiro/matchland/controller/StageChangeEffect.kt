package mmorihiro.matchland.controller

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.actions.Actions.delay
import com.badlogic.gdx.scenes.scene2d.actions.Actions.scaleTo
import com.badlogic.gdx.scenes.scene2d.ui.Image
import ktx.actors.plus
import ktx.actors.then
import ktx.assets.asset
import mmorihiro.matchland.view.View


class StageChangeEffect {
    fun addEffect(view: View) {
        (0..9).forEach { xIndex ->
            (0..15).forEach { yIndex ->
                val tile =
                        Image(asset<Texture>("white.png")).apply {
                            x = xIndex * width
                            y = yIndex * height
                            setScale(0f)
                            setOrigin(width / 2, height / 2)
                        }

                tile + (delay((16 - xIndex - yIndex) * 0.02f)
                        then scaleTo(1f, 1f, 0.5f)
                        then delay(0.2f + (xIndex + yIndex) * 0.02f * 2)
                        then scaleTo(0f, 0f, 0.5f)
                        then Actions.run { tile.remove() })
                view + tile
            }
        }
    }
}