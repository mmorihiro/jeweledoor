package mmorihiro.matchland.controller

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.actions.Actions.*
import ktx.actors.alpha
import ktx.actors.onClick
import ktx.actors.plus
import ktx.actors.then
import mmorihiro.matchland.model.ConfigModel
import mmorihiro.matchland.model.ItemType
import mmorihiro.matchland.view.ConnectEvent
import mmorihiro.matchland.view.MyImage
import mmorihiro.matchland.view.PuzzleView


class PuzzleController(onTurnEnd: (Int) -> Unit,
                       onPause: () -> Unit,
                       playerType: ItemType,
                       enemyType: ItemType) : Controller {
    override val view = PuzzleView(
            ::touchAction,
            ::onTouchDragged,
            { view ->
                onTouchUp(view, { event ->
                    iconReaction(view, event.connectedItems, true)
                    iconReaction(view, event.enemy, false)
                    addNewItems(event)
                    view + (delay(1f) then Actions.run {
                        onTurnEnd(view.tiles.map {
                            it.filter { it.color == ConfigModel.config.itemType.color }.size
                        }.sum())
                    })
                })
            }, playerType, enemyType).apply {
        this + backGround
        this + itemLayer
        tiles.forEach { it.forEach { itemLayer + it } }
        items.forEach { it.forEach { itemLayer + it } }
        this + pauseButton
        pauseButton.onClick { _, _ -> onPause() }
    }

    private fun addNewItems(event: ConnectEvent): Unit = view.run {
        val last = event.connectedItems.last()
        val item = items[last.second][last.first]
        items.forEach { row ->
            row.filter { it.color != Color.WHITE }
                    .forEach { existingItem ->
                        val action = moveTo(item.x, item.y, 0.2f) then
                                Actions.run {
                                    existingItem.remove()
                                }
                        existingItem.color = Color.WHITE
                        existingItem + action
                        val point = coordinateToPoint(
                                existingItem.x.toInt(), existingItem.y.toInt())
                        view.items[point.second][point.first] = newItem(existingItem)
                    }
        }
        resetBubbles()
        enemyAction(event)
    }

    private fun enemyAction(event: ConnectEvent) {
        val center = event.enemyPoint.let {
            view.items[it.second][it.first]
        }
        event.enemy.forEach {
            val item = view.items[it.second][it.first]
            val action = moveTo(center.x, center.y, 0.2f) then
                    Actions.run {
                        item.remove()
                    }
            item + action
            view.items[it.second][it.first] = newItem(item)
        }
    }

    private fun newItem(existingItem: MyImage): MyImage =
            view.loadItem().apply {
                x = existingItem.x
                y = existingItem.y
                alpha = 0f
                this + fadeIn(0.15f)
                view.itemLayer + this
            }
}