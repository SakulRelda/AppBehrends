package styleviews;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lukasadler.test1.R;

import java.util.List;

import logical.Machine;

/**
 * Created by LukasAdler on 16.02.17.
 */

public class MachineAdapter extends ArrayAdapter {


    public MachineAdapter(Context context, int resource, Activity act) {
        super(context, resource);

    }

    /**
     *
     * @param context - Context like the Main Activity or something else
     * @param list - List with Items for the Adapter
     */
    public MachineAdapter(Context context, List list){
        //0--> Kein vordefiniertes Layout
        super(context,0,list);
    }

    @NonNull
    @Override
    public View getView(int pos, View convertView, ViewGroup parent){
        if(convertView==null){
            return null;
        }
        TextView txtView = (TextView) convertView.findViewById(R.id.listItemTitle);
        txtView.setText("Testtext");
        ImageView imgViewEdit = (ImageView) convertView.findViewById(R.id.listItemEdit);
        ImageView imgViewDelete = (ImageView) convertView.findViewById(R.id.listItemDelete);
        imgViewEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return convertView;

    }


}
