package adapterHelper;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lukasadler.test1.R;
import com.google.firebase.database.Query;

import logical.Machine;

/**
 * Created by LukasAdler on 16.02.17.
 * Extends from Firebase
 */

public class MachineAdapter extends OwnFirebaseListAdapter<Machine>{


    // The mUsername for this client. We use this to indicate which messages originated from this user
    private String mUsername;
    private Activity activity;

    public MachineAdapter(Query ref, Activity activity, int layout, String mUsername) {
        super(ref, Machine.class, layout, activity);
        this.activity = activity;
        this.mUsername = mUsername;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Machine m = (Machine) getItem(position);
        TextView txtView = (TextView) convertView.findViewById(R.id.listItemTitle);
        txtView.setText(m.getS_Name());
        return convertView;
    }

    @Override
    protected void populateView(View v, final Machine model) {
        TextView txtView = (TextView) v.findViewById(R.id.listItemTitle);
        txtView.setText(model.getS_Name());
    }
}
