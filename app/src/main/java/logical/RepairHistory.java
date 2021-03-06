package logical;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by LukasAdler on 08.02.17.
 */

public class RepairHistory implements Serializable{


    private Date d_repairDate = new Date();
    private String s_ID;
    private String s_Description;
    private String s_machineID;
    private String s_author;
    private String s_shortDescr;
    private Bitmap image;



    public RepairHistory() {
    }

    public RepairHistory(Date d_repairDate, String s_ID, String s_Description, String s_machineID, String s_author, String s_shortDescr, Bitmap image) {
        this.d_repairDate = d_repairDate;
        this.s_ID = s_ID;
        this.s_Description = s_Description;
        this.s_machineID = s_machineID;
        this.s_author = s_author;
        this.s_shortDescr = s_shortDescr;
        this.image = image;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getS_ID() {
        return s_ID;
    }

    public void setS_ID(String s_ID) {
        this.s_ID = s_ID;
    }

    public String getS_machineID() {
        return s_machineID;
    }

    public void setS_machineID(String s_machineID) {
        this.s_machineID = s_machineID;
    }

    public Date getD_repairDate() {
        return d_repairDate;
    }

    public void setD_repairDate(Date d_repairDate) {
        this.d_repairDate = d_repairDate;
    }

    public String getS_Description() {
        return s_Description;
    }

    public void setS_Description(String s_Description) {
        this.s_Description = s_Description;
    }

    public String getS_author() {
        return s_author;
    }

    public void setS_author(String s_author) {
        this.s_author = s_author;
    }

    public String getS_shortDescr() {
        return s_shortDescr;
    }

    public void setS_shortDescr(String s_shortDescr) {
        this.s_shortDescr = s_shortDescr;
    }
}
