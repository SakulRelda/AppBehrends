package styleviews;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.lukasadler.test1.R;

import java.util.List;

import logical.RepairHistory;

/**
 * Created by LukasAdler on 26.02.17.
 */
public class MaintenanceAdapter extends ArrayAdapter{

    private Context context;
    private int resource;
    public Activity activity;
    private List list;
    public MaintenanceAdapter(Context context, List list, Activity activity){

        super(context,0);
        this.list = list;
        this.activity = activity;
    }

    public MaintenanceAdapter(Context context, int resource, Activity activity) {
        super(context, resource);
        this.activity = activity;
    }

    @NonNull
    @Override
    public View getView(int pos, View convertView, final ViewGroup parent){
        if(convertView==null){
            convertView = activity.getLayoutInflater().inflate(R.layout.maintenance_list_item, null);
        }
        TextView txtView = (TextView) convertView.findViewById(R.id.listItemMaintenanceTitle);
        final RepairHistory m = (RepairHistory) getItem(pos);
        txtView.setText(m.getD_repairDate().toString());
        return convertView;
    }

}
