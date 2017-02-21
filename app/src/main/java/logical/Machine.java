package logical;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by LukasAdler on 08.02.17.
 */

public class Machine implements Serializable {

    private String i_uID; //User ID
    private String i_ID; //ID of the Machine
    private String s_Name; //Name of the Machine
    private String s_MachineType; //Name of the Machine Type
    private String s_MachineLocation;

    /**
     * Constructor to create a Machine
     */
    public Machine(){

    }

    public Machine(String i_uID, String i_ID, String s_Name, String s_MachineType, String s_MachineLocation) {
        this.i_uID = i_uID;
        this.i_ID = i_ID;
        this.s_Name = s_Name;
        this.s_MachineType = s_MachineType;
        this.s_MachineLocation = s_MachineLocation;
    }

    public String getI_uID() {
        return i_uID;
    }

    public void setI_uID(String i_uID) {
        this.i_uID = i_uID;
    }

    public String getI_ID() {
        return i_ID;
    }

    public void setI_ID(String i_ID) {
        this.i_ID = i_ID;
    }

    public String getS_Name() {
        return s_Name;
    }

    public void setS_Name(String s_Name) {
        this.s_Name = s_Name;
    }

    public String getS_Machinentyp() {
        return s_MachineType;
    }

    public void setS_Machinentyp(String s_MachineType) {
        this.s_MachineType = s_MachineType;
    }

    public String getS_MachineLocation() {
        return s_MachineLocation;
    }

    public void setS_MachineLocation(String s_MachineLocation) {
        this.s_MachineLocation = s_MachineLocation;
    }
}
