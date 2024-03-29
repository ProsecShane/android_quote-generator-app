package com.prosecshane.quoteapp.presentation.adapter

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.drawable.ColorDrawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.prosecshane.quoteapp.R

/**
 * A callback for ItemTouchHelper that allows swipe to delete functionality.
 */
abstract class SwipeToDeleteCallback(
    context: Context
) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
    /**
     * Dimensions and objects used to style.
     */
    private val deleteIcon = ContextCompat.getDrawable(context, R.drawable.delete)
    private val intrinsicWidth = deleteIcon!!.intrinsicWidth
    private val intrinsicHeight = deleteIcon!!.intrinsicHeight
    private val background = ColorDrawable()
    private val backgroundColor = Color.parseColor("#f44336")
    private val clearPaint = Paint().apply { xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR) }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder,
    ): Boolean {
        return false
    }

    /**
     * Draws or removes the red background that appears while swiping.
     */
    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val itemView = viewHolder.itemView
        val itemHeight = itemView.bottom - itemView.top
        val isCanceled = dX == 0f && !isCurrentlyActive

        if (isCanceled) {
            clearCanvas(c, itemView.right + dX, itemView.top.toFloat(), itemView.right.toFloat(), itemView.bottom.toFloat())
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            return
        }

        background.color = backgroundColor
        background.setBounds(itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom)
        background.draw(c)

        val deleteIconMargin = (itemHeight - intrinsicHeight) / 2
        if ((-dX.toInt()) >= intrinsicWidth + 2 * deleteIconMargin) {
            deleteIcon!!.setBounds(
                itemView.right - deleteIconMargin - intrinsicWidth,
                itemView.top + (itemHeight - intrinsicHeight) / 2,
                itemView.right - deleteIconMargin,
                itemView.top + (itemHeight - intrinsicHeight) / 2 + intrinsicHeight)
            deleteIcon.draw(c)
        } else {
            val minimizeRatio = (-dX.toDouble()) /
                    (intrinsicWidth + 2 * deleteIconMargin).toDouble()
            val deleteIconWidth = (intrinsicWidth.toDouble() * minimizeRatio).toInt()
            val deleteIconHeight = (deleteIconWidth.toDouble() *
                    intrinsicHeight.toDouble() / intrinsicWidth.toDouble()).toInt()
            val verticalMargin = (itemHeight - deleteIconHeight) / 2

            val deleteIconRight = itemView.right -
                    (deleteIconMargin.toDouble() * minimizeRatio).toInt()
            val deleteIconLeft = deleteIconRight - deleteIconWidth
            val deleteIconTop = itemView.top + verticalMargin
            val deleteIconBottom = deleteIconTop + deleteIconHeight

            deleteIcon!!.setBounds(
                deleteIconLeft,
                deleteIconTop,
                deleteIconRight,
                deleteIconBottom)
            deleteIcon.draw(c)
        }

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    /**
     * Removes everything drawn by this callback.
     */
    private fun clearCanvas(c: Canvas?, left: Float, top: Float, right: Float, bottom: Float) {
        c?.drawRect(left, top, right, bottom, clearPaint)
    }
}
