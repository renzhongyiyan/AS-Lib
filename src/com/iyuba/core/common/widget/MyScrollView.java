package com.iyuba.core.common.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class MyScrollView extends ScrollView  {

	public MyScrollView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	
	 
    public MyScrollView(Context context, AttributeSet attrs)  
    {  
        super(context, attrs);  
          
    }  
      
    public MyScrollView(Context context, AttributeSet attrs, int defStyle)  
    {  
        super(context, attrs, defStyle);  
    }  
      
	
	
	 public boolean onTouchEvent(MotionEvent event)
	    {  
	        super.onTouchEvent(event);  
	        return false;
	
	    }
	
	
	
	
	
	
}
