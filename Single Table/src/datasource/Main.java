package datasource;

import datasource.ChemicalDataGateway;
import datasource.CompoundDataGateway;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
    }

    public static ArrayList<CompoundDataGateway> getAllCompounds() {

    }

    //Retrieves all metals that are dissolved by a certain acid ID
    public static ArrayList<ChemicalDataGateway> getMetalsDissolvedBy(int acidID) {

    }

    //Retrieves all nonmetals that are dissolved by a certain acid ID
    public static ArrayList<ChemicalDataGateway> getNonMetalsDissolvedBy(int acidID) {

    }

    //Retrieves Compounds by an ElementID
    public static ArrayList<CompoundDataGateway> getCompoundsByElementID(int elementID) {

    }

    //Retrieves Elements in Compound
    public static void getCompoundElements() {

    }
}