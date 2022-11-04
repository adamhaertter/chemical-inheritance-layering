package model.controller;

import exceptions.ElementNotFoundException;
import mappers.ClassElementMapper;
import mappers.ElementMapper;
import model.Element;

import java.util.List;

public class ElementController
{
    private Element myElement;

    public ElementController(String name) throws ElementNotFoundException {
        ElementMapper mapper = new ClassElementMapper(name);
        myElement = mapper.getMyElement();
    }

    public ElementController(String name, int atomicNumber, double atomicMass)
    {
        ElementMapper mapper = new ClassElementMapper(name, atomicNumber, atomicMass);
        myElement = mapper.getMyElement();
    }

    public void setAtomicNumber(int newAtomicNumber)
    {
        myElement.setAtomicNumber(newAtomicNumber);
    }

    public void setAtomicMass(double newAtomicMass)
    {
        myElement.setAtomicMass(newAtomicMass);
    }

    public void setName(String newName)
    {
        myElement.setName(newName);
    }

    public void persist()
    {
        myElement.persist();
    }

    public List<String> getCompoundsContaining() {
        return null;
    }

    public static void delete(String name) throws ElementNotFoundException
    {
        ElementMapper mapper = new ClassElementMapper(name);
        mapper.delete();
    }

    public static Element[] getElementsBetween(int firstAtomicNumber,
                                               int lastAtomicNumber)
    {
        return null;
    }

    public static Element[] getAllElements()
    {
        return null;
    }

    public Element getMyElement()
    {
        return myElement;
    }
}
