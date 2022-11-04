package model.controller;

import exceptions.CompoundNotFoundException;
import mappers.ClassCompoundMapper;
import mappers.CompoundMapper;
import model.Compound;

import java.util.ArrayList;
import java.util.List;

public class CompoundController {
    
    private Compound myCompound;
    private ArrayList<String> elements = new ArrayList();
    
    public CompoundController(String name) throws CompoundNotFoundException {
        CompoundMapper mapper = new ClassCompoundMapper(name);
        myCompound = mapper.getMyCompound();
    }

    public static void createCompound(String name) {
        ClassCompoundMapper.createCompound(name);
    }

    public static void delete(String name) throws CompoundNotFoundException {
        CompoundMapper mapper = new ClassCompoundMapper(name);
        mapper.delete();
    }

    public Compound getMyCompound() {
        return myCompound;
    }

    public void setName(String name) {
        myCompound.setName(name);
    }

    public void addElement(String element) {
        elements.add(element);
        myCompound.addElement(element);
    }

    public List<String> getElements() {
        return null;
    }

    public double getAtomicMass() {
        return 0;
    }
}
