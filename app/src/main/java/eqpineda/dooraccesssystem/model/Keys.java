package eqpineda.dooraccesssystem.model;

/**
 * Created by eqpineda on 4/10/15.
 */
public class Keys {
    int keyid;
    String authstring;
    String insertedon;
    boolean isdeleted;

    public Keys() {}

    public Keys(String authstring, String insertedon) {
        this.authstring = authstring;
        this.insertedon = insertedon;
        this.isdeleted = false;
    }

    public Keys(int keyid, String authstring, String insertedon) {
        this.keyid = keyid;
        this.authstring = authstring;
        this.insertedon = insertedon;
        this.isdeleted = false;
    }

    // Setters
    public void setKeyId(int keyid) {
        this.keyid = keyid;
    }

    public void setAuthstring(String authstring) {
        this.authstring = authstring;
    }

    public void setInsertedon(String insertedon) {
        this.insertedon = insertedon;
    }

    public void setIsdeleted(boolean isdeleted) {
        this.isdeleted = isdeleted;
    }

    // Getters
    public int getKeyid() {
        return this.keyid;
    }

    public String getAuthstring() {
        return this.authstring;
    }

    public boolean getIsdeleted() {
        return this.isdeleted;
    }
}
