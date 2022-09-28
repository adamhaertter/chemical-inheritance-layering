public class Gateway {
    protected boolean deleted = false;

    public void delete() {
        try {
            // TODO code to delete from database
        } catch (Exception e) {
            // throw error about failing to delete
        }
        // does not hit unless sql delete is successful
        this.deleted = true;
    }

    private boolean verifyExistence() {
        return !deleted;
    }
}
