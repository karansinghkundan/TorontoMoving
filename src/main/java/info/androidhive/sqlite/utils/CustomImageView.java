package info.androidhive.sqlite.utils;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.ImageView;

public class CustomImageView extends android.support.v7.widget.AppCompatImageView {

    public static float radius = 118.0f;

    public CustomImageView(Context context) {
        super(context);
    }

    public CustomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Path clipPath = new Path();
        RectF rect = new RectF(0, 0, this.getWidth(), this.getHeight());


            DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        float rectWidth = (float) Math.round(300 * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));


        float radius = Math.min((float)getMeasuredWidth() / 2f, (float)getMeasuredHeight() / 2f) + 1;
        clipPath.addCircle((float)getMeasuredWidth() / 2f, (float)getMeasuredHeight() / 2f, radius, Path.Direction.CCW);

        canvas.clipPath(clipPath);
        super.onDraw(canvas);
    }
}