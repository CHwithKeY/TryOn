package wale_tech.tryon;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

/**
 * Created by lenovo on 2017/3/6.
 */

public class ImageScaleView extends ImageView implements ViewTreeObserver.OnGlobalLayoutListener {

    //是否第一次查看该图片，用于实现默认自动将图片平移到控件中间并宽度适应屏幕
    private boolean once = true;
    //是否处于100%显示状态，用于在宽度适应屏幕与100%显示之间切换
    private boolean inFull = false;
    //图片的矩阵
    private Matrix mMatrx;
    //图片临时状态的矩阵
    private Matrix mTempMatrix = new Matrix();
    //两指按下的点
    private PointF mStartPoint = new PointF();
    //两点的中心点
    private PointF mMidPoint = new PointF();
    //两指按下的距离
    private float oldDis = 1f;
    //状态
    private int state = NONE;
    //默认状态
    private static final int NONE = 0;
    //拖拽状态
    private static final int DRAG = 1;
    //缩放状态
    private static final int ZOOM = 2;

    //缩放到宽度适应屏幕
    private static final int SCALE_FIT_WIDTH = 0;
    //缩放到100%
    private static final int SCALE_FULL = 1;

    private GestureDetector mGestureDetector;

    public ImageScaleView(Context context) {
        this(context, null);
    }

    public ImageScaleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageScaleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

//注意需要将ScaleType设置为ScaleType.MATRIX以支持通过变换使用的矩阵动态修改图片显示大小
        super.setScaleType(ScaleType.MATRIX);

        init();
    }

    private void init() {
        mMatrx = new Matrix();
        mGestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap(MotionEvent e) {
//进行双击时图片缩放状态切换
                if (inFull) {
                    scalePicture(SCALE_FIT_WIDTH);
                } else {
                    scalePicture(SCALE_FULL);
                }
                inFull = !inFull;
                return true;
            }
        });
    }

    //当view 被添加到window中，被绘制之前的回调。
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    //当view被从窗体中移除时调用。
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        getViewTreeObserver().removeGlobalOnLayoutListener(this);
    }

    @Override
    public void onGlobalLayout() {
        //第一次layout，使图片默认缩放到适应屏幕宽度
        if (once) {
            scalePicture(SCALE_FIT_WIDTH);
            once = false;
        }
    }

    private void scalePicture(int scaleType) {
        //在对矩阵进行操作前，需要先将矩阵归0，否则会受之前在矩阵中设置的值的影响
        mMatrx.reset();
        Drawable drawable = getDrawable();
        if (drawable == null) {
            return;
        }
        //图片本身的宽高
        int dw = drawable.getIntrinsicWidth();
        int dh = drawable.getIntrinsicHeight();


        //控件的宽高，也即控件所占据的屏幕大小
        int w = getWidth();
        int h = getHeight();


        float scale = 1.0f;

        //显示宽度适应屏幕
        if (scaleType == SCALE_FIT_WIDTH)
            scale = (w * 1.0f) / dw;
            //显示到100%
        else if (scaleType == SCALE_FULL)
            scale = 1.0f;


        //平移到控件（也即屏幕）中间
        mMatrx.postTranslate((w - dw) / 2, (h - dh) / 2);
        //缩放
        mMatrx.postScale(scale, scale, w / 2, h / 2);
        //注意每次setImageMatrix都是将之前设置的矩阵效应清除后再使用当前设置的矩阵
        setImageMatrix(mMatrx);
    }

    @Override
    public void setImageResource(@DrawableRes int resId) {
        super.setImageResource(resId);
        scalePicture(SCALE_FIT_WIDTH);

    }

    @Override
    public void setImageDrawable(@Nullable Drawable drawable) {
        super.setImageDrawable(drawable);
        scalePicture(SCALE_FIT_WIDTH);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                mTempMatrix.set(mMatrx);
                mStartPoint.set(event.getX(), event.getY());
                state = DRAG;
                break;
            case MotionEvent.ACTION_POINTER_DOWN://双指
                oldDis = getDistance(event);
                if (oldDis > 10f) {
//缓存双指按压开始时的矩阵
                    mTempMatrix.set(mMatrx);
                    getMidPoint(mMidPoint, event);
                    //只有双指才被认为是缩放状态
                    state = ZOOM;
                }
                break;
            case MotionEvent.ACTION_MOVE:

                if (state == ZOOM) {
                    float newDis = getDistance(event);
                    if (newDis > 10f) {
//恢复双指按压时缓存的矩阵
                        mMatrx.set(mTempMatrix);
                        float scale = newDis / oldDis;//缩放尺度计算为与缓存距离的比例

//以控件中心为缩放中心
                        mMatrx.postScale(scale, scale, getWidth() / 2,
                                getHeight() / 2);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                state = NONE;
                break;
            case MotionEvent.ACTION_POINTER_UP:
                state = NONE;
                break;
        }
        setImageMatrix(mMatrx);
        mGestureDetector.onTouchEvent(event);
        //需要return true以使得控件能在收到down事件后继续监听move和up事件
        return true;

    }

    /**
     * 计算两点距离
     *
     * @param event
     * @return
     */
    private float getDistance(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    /**
     * 计算中心点
     *
     * @param point
     * @param event
     */
    private void getMidPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }
}
