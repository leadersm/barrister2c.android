package com.lsm.barrister2c.ui.widget;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class GridViewForList extends GridView {

    public GridViewForList(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
