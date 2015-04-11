package eqpineda.dooraccesssystem.model;

/**
 * Created by eqpineda on 4/10/15.
 */
public class Keys {
    int keyid;
    String authstring;
    String description;
    boolean isdeleted;
    String insertedon;

    public Keys() {}

    public Keys(String authstring, String description) {
        this.authstring = authstring;
        this.description = description;
        this.isdeleted = false;
    }

    // Setters
    public void setKeyid(int keyid) {
        this.keyid = keyid;
    }

    public void setAuthstring(String authstring) {
        this.authstring = authstring;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setIsdeleted(boolean isdeleted) {
        this.isdeleted = isdeleted;
    }

    public void setInsertedon(String insertedon) {
        this.insertedon = insertedon;
    }

    // Getters
    public int getKeyid() {
        return this.keyid;
    }

    public String getAuthstring() {
        return this.authstring;
    }

    public String getDescription() {
        return this.description;
    }

    public boolean getIsdeleted() {
        return this.isdeleted;
    }

    public String getInsertedon() {
        return this.insertedon;
    }
}
