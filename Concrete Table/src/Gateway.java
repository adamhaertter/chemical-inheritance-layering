/**
 * General gateway class that has the methods needed in all gateways
 */

public class Gateway {
    protected boolean deleted = false;

    public void delete() {
        try {
            // delete code from DB
        } catch (Exception e) {
            //throw error about delete failure
        }
        this.deleted = true;
    }
}
