package djordjeh.architecture.mvp.ui.tasks;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

public abstract class SwipeToDeleteCallback extends ItemTouchHelper.SimpleCallback {

    private final ColorDrawable background;
    private final Drawable icon;

    SwipeToDeleteCallback(Drawable icon) {
        super(0, ItemTouchHelper.START | ItemTouchHelper.END);
        this.icon = icon;
        this.background = new ColorDrawable(Color.RED);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
        return false;
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

        final View view = viewHolder.itemView;

        int iconMargin = (view.getHeight() - icon.getIntrinsicHeight()) / 2;
        int iconTop = view.getTop() + (view.getHeight() - icon.getIntrinsicHeight()) / 2;
        int iconBottom = iconTop + icon.getIntrinsicHeight();

        // draw the red background
        if (dX > 0) { // Swiping to the right
            // Background
            background.setBounds(view.getLeft(), view.getTop(), (int) (view.getLeft() + dX), view.getBottom());

            // Icon
            int iconLeft = view.getLeft() + iconMargin;
            int iconRight = iconLeft+ icon.getIntrinsicWidth();
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
        } else if (dX < 0) { // Swiping to the left
            // Background
            background.setBounds((int) (view.getRight() + dX), view.getTop(), view.getRight(), view.getBottom());

            // Icon
            int iconLeft = view.getRight() - iconMargin - icon.getIntrinsicWidth();
            int iconRight = view.getRight() - iconMargin;
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
        } else { // view is unSwiped
            background.setBounds(0, 0, 0, 0);
        }

        background.draw(c);
        icon.draw(c);
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }
}
