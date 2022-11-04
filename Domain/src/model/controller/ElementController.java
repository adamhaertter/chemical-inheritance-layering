package model.controller;

import exceptions.ElementNotFoundException;
import mappers.ClassElementMapper;
import mappers.ElementMapper;
import model.Element;

import java.util.ArrayList;
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
        try {
            ElementMapper mapper = new ClassElementMapper(myElement.getName());
            return mapper.getCompoundsContaining();
        } catch (ElementNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void delete(String name) throws ElementNotFoundException
    {
        ElementMapper mapper = new ClassElementMapper(name);
        mapper.delete();
    }

    public static Element[] getElementsBetween(int firstAtomicNumber,
                                               int lastAtomicNumber)
    {
        ArrayList elementList = ClassElementMapper.getElementsBetween(firstAtomicNumber, lastAtomicNumber);
        Element[] elementArray = new Element[elementList.size()];
        for(int i = 0; i < elementList.size(); i++) {
            elementArray[i] = (Element)elementList.get(i);
        }
        return elementArray;
    }

    public static Element[] getAllElements()
    {
        ArrayList elementList = ClassElementMapper.getAllElements();
        Element[] elementArray = new Element[elementList.size()];
        for(int i = 0; i < elementList.size(); i++) {
            elementArray[i] = (Element)elementList.get(i);
        }
        return elementArray;
    }

    public Element getMyElement()
    {
        return myElement;
    }
}
