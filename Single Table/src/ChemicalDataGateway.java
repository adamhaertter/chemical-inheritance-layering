import java.sql.*;

public class ChemicalDataGateway extends Gateway {
    long id = 0;
    String name = "";
    int atomicNumber = 0;
    double atomicMass = 0.0;
    int baseSolute = 0;
    int acidSolute = 0;
    String[] dissolved;
    long dissolvedBy = 0;
    protected boolean deleted = false;

    public ChemicalDataGateway(long identification) throws SQLException {
        id = identification;
    }

    public ChemicalDataGateway(String n, int number, double mass, int bSolute,
                               int aSolute, String[] diss, long dissBy) {
        name = n;
        atomicNumber = number;
        atomicMass = mass;
        baseSolute = bSolute;
        acidSolute = aSolute;
        dissolved = diss;
        dissolvedBy = dissBy;
    }

    //Getters & Setters ----------------------------------------------------------------------
    public long getID() {
        verifyExistence();
        return id;
    }

    public String getName() {
        verifyExistence();
        return name;
    }

    public void setName(String name) {
        verifyExistence();
        this.name = name;
        persist();
    }

    public int getAtomicNumber() {
        verifyExistence();
        return atomicNumber;
    }

    public void setAtomicNumber(int atomicNumber) {
        verifyExistence();
        this.atomicNumber = atomicNumber;
        persist();
    }

    public double getAtomicMass() {
        verifyExistence();
        return atomicMass;
    }

    public void setAtomicMass(double atomicMass) {
        verifyExistence();
        this.atomicMass = atomicMass;
        persist();
    }

    public int getBaseSolute() {
        verifyExistence();
        return baseSolute;
    }

    public void setBaseSolute(int baseSolute) {
        verifyExistence();
        this.baseSolute = baseSolute;
        persist();
    }

    public int getAcidSolute() {
        verifyExistence();
        return acidSolute;
    }

    public void setAcidSolute(int acidSolute) {
        verifyExistence();
        this.acidSolute = acidSolute;
        persist();
    }

    public String[] getDissolved() {
        verifyExistence();
        return dissolved;
    }

    public void setDissolved(String[] dissolved) {
        verifyExistence();
        this.dissolved = dissolved;
        persist();
    }

    public long getDissolvedBy() {
        verifyExistence();
        return dissolvedBy;
    }

    public void setDissolvedBy(long dissolvedBy) {
        verifyExistence();
        this.dissolvedBy = dissolvedBy;
        persist();
    }

    public boolean persist() {
        verifyExistence();
        try {
        } catch(Error e) {

        }

    }

    public void delete() {
        try {
            // delete code from DB
        } catch (Exception e) {
            //throw error about delete failure
        }
        this.deleted = true;
    }

    private void verifyExistence() {
        if (deleted)
            try {
                throw new Exception("This item has been deleted.");
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
    }
}
