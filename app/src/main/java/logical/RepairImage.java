package logical;

/**
 * Created by LukasAdler on 08.02.17.
 */

public class RepairImage {

    private byte[] b_blob;
    private int i_ID;


    public RepairImage() {
    }

    public RepairImage(byte[] b_blob, int i_ID) {
        this.b_blob = b_blob;
        this.i_ID = i_ID;
    }

    public byte[] getB_blob() {
        return b_blob;
    }

    public void setB_blob(byte[] b_blob) {
        this.b_blob = b_blob;
    }

    public int getI_ID() {
        return i_ID;
    }

    public void setI_ID(int i_ID) {
        this.i_ID = i_ID;
    }
}
