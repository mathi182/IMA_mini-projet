package ca.ulaval.ima.mp.alarmedeluxe.domain.types;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

public class DividerItemDecoration extends RecyclerView.ItemDecoration {

    private static final int[] ATTRIBUTES = { android.R.attr.listDivider };
    private Drawable divider;
    private int orientation;

    public DividerItemDecoration(Context context, int orientation) {
        final TypedArray typedArray = context.obtainStyledAttributes(ATTRIBUTES);
        divider = typedArray.getDrawable(0);
        typedArray.recycle();
        setOrientation(orientation);
    }

    public void setOrientation(int orientation) {
        if (orientation != LinearLayout.HORIZONTAL && orientation != LinearLayout.VERTICAL) {
            throw new IllegalArgumentException("invalid orientation");
        }
        this.orientation = orientation;
    }

    @Override
    public void onDrawOver(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        int leftPadding = parent.getPaddingLeft();
        int rightPadding = parent.getWidth() - parent.getPaddingRight();
        int topPadding = parent.getPaddingTop();
        int bottomPadding = parent.getHeight() - parent.getPaddingBottom();

        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            if (orientation == LinearLayout.VERTICAL) {
                topPadding = child.getBottom() + params.bottomMargin;
                bottomPadding = topPadding + divider.getIntrinsicHeight();
            } else {
                leftPadding = child.getRight() + params.rightMargin;
                rightPadding = leftPadding + divider.getIntrinsicHeight();
            }

            divider.setBounds(leftPadding, topPadding, rightPadding, bottomPadding);
            divider.draw(canvas);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (orientation == LinearLayout.VERTICAL) {
            outRect.set(0, 0, 0, divider.getIntrinsicHeight());
        } else {
            outRect.set(0, 0, divider.getIntrinsicWidth(), 0);
        }
    }
}
