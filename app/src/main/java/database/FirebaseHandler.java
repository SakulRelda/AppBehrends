package database;

import android.util.Log;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import logical.Machine;
import logical.RepairHistory;

/**
 * Created by LukasAdler on 10.02.17.
 */
public class FirebaseHandler {

    private static FirebaseHandler instance = new FirebaseHandler();
    final ArrayList<Machine> machines = new ArrayList<>();
    final ArrayList<RepairHistory> histories = new ArrayList<>();

    /**
     * Standard Constructor
     * (Private becouse of the Singelton-Pattern)
     */
    private FirebaseHandler(){

    }

    /**
     * Get Instance for the Singelton Pattern
     * @return Object of the FirebaseHandler
     */
    public static FirebaseHandler getInstance(){
        return instance;
    }


    /**
     * Checks if the Mail and the Password works for Check Log In
     * @param email - Mail Adress
     * @param pwd - Password
     * @return boolean
     */
    public boolean checkLogIn(String email, String pwd){
        if(email!=null&&pwd!=null){
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email,pwd);
            AuthResult res = FirebaseAuth.getInstance().signInWithEmailAndPassword(email,pwd).getResult();

            return true;
        }else{
            return false;
        }
    }

    /**
     * Gets the Logged In User
     * @return FirebaseUser
     */
    public FirebaseUser getFirebaseUser(){
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public boolean checkLoggedIn(){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser().isAnonymous()||firebaseAuth.getCurrentUser()==null) {
            return false;
        }
        return true;
    }

    /**
     * Saves an Object to the Database
     * @param objName -> Could be (Machine, Config, RepairHistory, RepairImage)
     * @param o -> represents Object which should be saved at the Database
     */
    public void saveObject(String objName, Object o){
        DatabaseReference refDatabase = FirebaseDatabase.getInstance().getReference();
        refDatabase.child(objName).push().setValue(o);
    }

    public void saveMachine(Machine machine){
        DatabaseReference refDatabase = FirebaseDatabase.getInstance().getReference();
        String keyID  = refDatabase.child("Machine").push().getKey();
        machine.setI_ID(keyID);
        refDatabase.child("Machine").child(keyID).setValue(machine);
    }

    public void saveMaintenance(RepairHistory histo){
        DatabaseReference refDatabase = FirebaseDatabase.getInstance().getReference();
        String keyID = refDatabase.child("Maintenance").push().getKey();
        histo.setS_ID(keyID);
        refDatabase.child("Maintenance").child(keyID).setValue(histo);
    }


    /**
     * Logs Out the User
     * @return true -> User LogOut / false -> User allready logged in
     */
    public boolean logOutUser(){
            FirebaseAuth.getInstance().signOut();
        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            return false;
        }else{
            return true;
        }
    }

    /**
     * Delete an Object from the Database
     * @return bool true if it was successfull
     */
    public boolean deleteMachine(Machine machine){
        try{
            DatabaseReference refDatabase = FirebaseDatabase.getInstance().getReference();
            String key = machine.getI_ID();
            refDatabase.child("Machine").child(key).removeValue();
            return true;
        }catch(Exception ex){
            return false;
        }
    }

    /**
     * Delete an Object from the Database
     * @param history
     * @return bool true if it was successfull
     */
    public boolean deleteMaintenance(RepairHistory history){
        try{
            DatabaseReference refDatabase = FirebaseDatabase.getInstance().getReference();
            String key = history.getS_ID();
            refDatabase.child("Maintenance").child(key).removeValue();
            return true;
        }catch (Exception ex){
            return false;
        }
    }

    /**
     *
     * @param machineID
     * @return
     */
    public ArrayList<RepairHistory> readMaintenance(final String machineID){
        DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        ChildEventListener childEventListener = mDatabaseRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                histories.clear();
                if(dataSnapshot.getKey().equals("Maintenance")){
                    for(DataSnapshot snap: dataSnapshot.getChildren()){
                        String key = snap.getKey();
                        RepairHistory history = snap.getValue(RepairHistory.class);
                        if(history.getS_machineID().equals(machineID)){
                            histories.add(history);
                        }

                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return histories;
    }

    /**
     * Read Machines from the Database
     * @return ArrayList with Machines
     */
    public ArrayList<Machine> readMachines(){
        DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        ChildEventListener childEventListener = mDatabaseRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                machines.clear();
                if (dataSnapshot.getKey().equals("Machine")) {
                    for (DataSnapshot snap : dataSnapshot.getChildren()) {
                        String key = snap.getKey();
                        Machine m = snap.getValue(Machine.class);
                        machines.add(m);
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        Log.d("FirebaseHandler","Done");
        return machines;
    }




}
