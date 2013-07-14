package com.letterhero.android;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class SquareView extends TextView  {
  public SquareView(Context context) {
    super(context);
  }

  public SquareView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public SquareView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }

  public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    int size = Math.min(getMeasuredWidth(), getMeasuredHeight());
    setMeasuredDimension(size, size);
  }
}