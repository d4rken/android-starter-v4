package eu.darken.androidstarter.common.lists

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.Rect
import android.graphics.drawable.Drawable
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import eu.darken.androidstarter.R
import eu.darken.androidstarter.common.UnitConverter
import eu.darken.androidstarter.common.debug.logging.log
import eu.darken.androidstarter.common.debug.logging.logTag
import eu.darken.androidstarter.common.getColorForAttr
import kotlin.math.absoluteValue


@Suppress("UnnecessaryVariable")
class ItemSwipeTool(vararg actions: SwipeAction) {

    init {
        require(actions.isNotEmpty()) {
            "SwipeTool without actions doesn't make sense."
        }
        require(actions.map { it.direction }.toSet().size == actions.size) {
            "Duplicate direction actions are not allowed."
        }
    }

    private val touchCallback = object : ItemTouchHelper.SimpleCallback(
        0,
        actions.map { it.direction }
            .fold(initial = 0, operation = { acc: Int, dir: SwipeAction.Direction -> acc.or(dir.value) })
    ) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, directionValue: Int) {
            val action = actions.single { it.direction.value == directionValue }
            log(TAG) { "onSwiped(): $action" }
            action.callback(viewHolder, action.direction)
        }

        override fun onChildDraw(
            canvas: Canvas,
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            dX: Float,
            dY: Float,
            actionState: Int,
            isCurrentlyActive: Boolean
        ) {
            super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

            val iv = viewHolder.itemView
            val context = recyclerView.context

            val curDir: SwipeAction.Direction? = when {
                dX > 0 -> SwipeAction.Direction.RIGHT
                dX < 0 -> SwipeAction.Direction.LEFT
                else -> null
            }

            val background = actions.find { it.direction == curDir }?.background
            when (curDir) {
                SwipeAction.Direction.RIGHT -> {
                    background?.setBounds(iv.left, iv.top, iv.left + dX.toInt(), iv.bottom)

                }
                SwipeAction.Direction.LEFT -> {
                    background?.setBounds(iv.right + dX.toInt(), iv.top, iv.right, iv.bottom)
                }
                else -> background?.setBounds(0, 0, 0, 0)
            }
            background?.draw(canvas)

            val defaultPadding = UnitConverter.dpToPx(recyclerView.context, 16f)
            val textPaint = Paint()
            textPaint.color = context.getColorForAttr(R.attr.colorOnError)
            textPaint.textSize = UnitConverter.spToPx(context, 18f)

            val actionItem = actions.find { it.direction == curDir }

            when (curDir) {
                SwipeAction.Direction.RIGHT -> {
                    val icon = actionItem?.icon
                    if (icon != null) {
                        val iconTop = iv.top + iv.height / 2 - icon.intrinsicHeight / 2
                        val iconStart = defaultPadding
                        val iconEnd = iconStart + icon.intrinsicWidth
                        val iconBottom = iconTop + icon.intrinsicHeight
                        if (dX > iconEnd + defaultPadding) {
                            icon.bounds = Rect(iconStart, iconTop, iconEnd, iconBottom)
                        } else {
                            icon.bounds = Rect(0, 0, 0, 0)
                        }
                    }

                    val label = actionItem?.label
                    if (label != null) {
                        val clipBounds = Rect()
                        canvas.getClipBounds(clipBounds)
                        textPaint.getTextBounds(label, 0, label.length, clipBounds)

                        val textTop = iv.top + iv.height / 2 + (clipBounds.height() / 2)
                        var textStart = defaultPadding
                        if (icon != null) textStart += icon.intrinsicWidth + defaultPadding
                        val textEnd = textStart + clipBounds.width()
                        if (dX > textEnd) {
                            canvas.drawText(label, textStart.toFloat(), textTop.toFloat(), textPaint)
                        }
                    }
                    actions.filter { it.icon != icon }.forEach {
                        it.icon?.setBounds(0, 0, 0, 0)
                    }
                }
                SwipeAction.Direction.LEFT -> {
                    val icon = actions.find { it.direction == curDir }?.icon
                    if (icon != null) {
                        val iconTop = iv.top + iv.height / 2 - icon.intrinsicHeight / 2
                        val iconStart = iv.width - defaultPadding - icon.intrinsicWidth
                        val iconEnd = iconStart + icon.intrinsicWidth
                        val iconBottom = iconTop + icon.intrinsicHeight
                        if (iv.width - dX.absoluteValue < iconStart - defaultPadding) {
                            icon.bounds = Rect(iconStart, iconTop, iconEnd, iconBottom)
                        } else {
                            icon.bounds = Rect(0, 0, 0, 0)
                        }
                    }
                    val label = actionItem?.label
                    if (label != null) {
                        val clipBounds = Rect()
                        canvas.getClipBounds(clipBounds)
                        textPaint.getTextBounds(label, 0, label.length, clipBounds)

                        val textTop = iv.top + iv.height / 2 + (clipBounds.height() / 2)
                        var textStart = iv.width - defaultPadding - clipBounds.width()
                        if (icon != null) textStart = textStart - icon.intrinsicWidth - defaultPadding
                        val textEnd = iv.width - textStart
                        if (dX.absoluteValue > textEnd) {
                            canvas.drawText(label, textStart.toFloat(), textTop.toFloat(), textPaint)
                        }
                    }
                    actions.filter { it.icon != icon }.forEach {
                        it.icon?.setBounds(0, 0, 0, 0)
                    }
                }
                else -> { // NONE
                    actions.forEach {
                        it.icon?.setBounds(0, 0, 0, 0)
                    }
                }
            }
            actions.forEach {
                it.icon?.setColorFilter(context.getColorForAttr(R.attr.colorOnError), PorterDuff.Mode.SRC_IN)
                it.icon?.draw(canvas)
            }
        }
    }
    private val touchHelper by lazy { ItemTouchHelper(touchCallback) }

    fun attach(recyclerView: RecyclerView) {
        touchHelper.attachToRecyclerView(recyclerView)
    }

    data class SwipeAction(
        val direction: Direction,
        val callback: (RecyclerView.ViewHolder, Direction) -> Unit,
        val icon: Drawable?,
        val label: String?,
        val background: Drawable?
    ) {
        enum class Direction(val value: Int) {
            LEFT(ItemTouchHelper.LEFT),
            RIGHT(ItemTouchHelper.RIGHT),
            START(ItemTouchHelper.START),
            END(ItemTouchHelper.END),
        }
    }

    companion object {
        internal val TAG = logTag("ItemSwipeTool")
    }

}