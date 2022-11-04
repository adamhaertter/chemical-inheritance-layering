package model.controller;

import exceptions.CompoundNotFoundException;
import exceptions.ElementNotFoundException;
import mappers.ClassCompoundMapper;
import mappers.CompoundMapper;
import model.Compound;
import model.Element;

import java.util.ArrayList;
import java.util.List;

public class CompoundController {
    
    private Compound myCompound;
    private ArrayList<Element> elements;
    
    public CompoundController(String name) throws CompoundNotFoundException {
        CompoundMapper mapper = new ClassCompoundMapper(name);
        myCompound = mapper.getMyCompound();
        elements = myCompound.getMadeOf();
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

    public void addElement(String element) throws ElementNotFoundException {
        myCompound.addElement(element);
    }

    public List<String> getElements() {
        elements = myCompound.getMadeOf();
        ArrayList<String> names = new ArrayList<String>();
        for(Element e : elements) {
            names.add(e.getName());
        }
        return names;
    }

    public double getAtomicMass() throws ElementNotFoundException {
        double mass = 0;
        for(Element elem : elements) {
            mass += elem.getAtomicMass();
        }
        return mass;
    }
}
