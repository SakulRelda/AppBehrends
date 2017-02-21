package styleviews;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.lukasadler.test1.R;

/**
 * Created by LukasAdler on 14.02.17.
 * <item name="android:layout_width">match_parent</item>
 <item name="android:layout_height">@dimen/list_item_height</item>
 <item name="android:gravity">center_vertical</item>
 <item name="android:padding">16dp</item>
 <item name="android:textColor">@android:color/white</item>
 <item name="android:textStyle">bold</item>
 <item name="android:textAppearance">?android:textAppearanceMedium</item>
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
