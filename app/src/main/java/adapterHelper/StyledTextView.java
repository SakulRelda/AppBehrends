package adapterHelper;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

import com.example.lukasadler.test1.R;

/**
 * Created by LukasAdler on 14.02.17.
 */

public class StyledTextView  extends TextView{


    public StyledTextView(Context ctx){
        super(ctx);
        //this.getLayoutParams().height = 70;
        this.setTypeface(null, Typeface.BOLD_ITALIC);
        this.setGravity(Gravity.CENTER_VERTICAL);
        this.setTextAppearance(R.style.CategoryStyle);
    }


    public StyledTextView(Context ctx, AttributeSet attr){
        super(ctx,attr);
    }

    public StyledTextView(Context ctx, AttributeSet attr, int defStyle){
        super(ctx,attr,defStyle);
    }

}
