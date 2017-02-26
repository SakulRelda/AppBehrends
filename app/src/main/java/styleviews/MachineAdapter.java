package styleviews;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lukasadler.test1.R;
import com.example.lukasadler.test1.SummaryActivity;

import java.util.List;

import database.FirebaseHandler;
import logical.Machine;

/**
 * Created by LukasAdler on 16.02.17.
 */

public class MachineAdapter extends ArrayAdapter {


    private Context context;
    private int resource;
    public Activity activity;
    private List list;

    public MachineAdapter(Context context, int resource, Activity act) {
        super(context, resource);
        this.context = context;
        this.resource = resource;
        this.activity = act;

    }

    /**
     * @param context - Context like the Main Activity or something else
     * @param list    - List with Items for the Adapter
     */
    public MachineAdapter(Context context, List list) {
        //0--> Kein vordefiniertes Layout
        super(context, 0, list);
        this.context = context;
        this.resource = 0;
        this.list = list;

    }

    @NonNull
    @Override
    public View getView(int pos, View convertView, final ViewGroup parent) {
        if (convertView == null) {
            convertView = activity.getLayoutInflater().inflate(R.layout.list_item, null);
        }
        TextView txtView = (TextView) convertView.findViewById(R.id.listItemTitle);
        final Machine m = (Machine) getItem(pos);
        txtView.setText(m.getS_Name());
        ImageView imgViewEdit = (ImageView) convertView.findViewById(R.id.listItemEdit);
        ImageView imgViewDelete = (ImageView) convertView.findViewById(R.id.listItemDelete);

        imgViewEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //EDIT LOGIC
            }
        });

        imgViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //DELETE LOGIC
                AlertDialog.Builder dia = new AlertDialog.Builder(v.getRootView().getContext());
                dia.setTitle(R.string.delete_machine);
                dia.setMessage(R.string.delete_machine_text);
                dia.setPositiveButton(R.string.ja_text, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        //DELETE PRESS YES
                        Toast.makeText(activity, m.getS_Name(), Toast.LENGTH_SHORT).show();
                        FirebaseHandler h = FirebaseHandler.getInstance();
                        h.deleteMachine(m);
                    }
                });
                dia.setNegativeButton(R.string.nein_text, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        //DELETE PRESS NO
                        Toast.makeText(activity, m.getS_Name(), Toast.LENGTH_SHORT).show();
                    }
                });
                dia.show();
            }
        });

        return convertView;

    }


}
