package djordjeh.architecture.mvp.util

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ItemTouchHelper
import android.view.View

abstract class SwipeToDeleteCallback protected constructor(private val icon: Drawable) :
        ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.START or ItemTouchHelper.END) {

    private val background: ColorDrawable = ColorDrawable(Color.RED)

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, viewHolder1: RecyclerView.ViewHolder) = false

    override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {

        val view = viewHolder.itemView

        val iconMargin = (view.height - icon.intrinsicHeight) / 2
        val iconTop = view.top + (view.height - icon.intrinsicHeight) / 2
        val iconBottom = iconTop + icon.intrinsicHeight

        // draw the red background
        when {
            dX > 0 -> { // Swiping to the right
                // Background
                background.setBounds(view.left, view.top, (view.left + dX).toInt(), view.bottom)

                // Icon
                val iconLeft = view.left + iconMargin
                val iconRight = iconLeft + icon.intrinsicWidth
                icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
            }
            dX < 0 -> { // Swiping to the left
                // Background
                background.setBounds((view.right + dX).toInt(), view.top, view.right, view.bottom)

                // Icon
                val iconLeft = view.right - iconMargin - icon.intrinsicWidth
                val iconRight = view.right - iconMargin
                icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
            }
            else -> // view is unSwiped
                background.setBounds(0, 0, 0, 0)
        }

        background.draw(c)
        icon.draw(c)
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }
}
