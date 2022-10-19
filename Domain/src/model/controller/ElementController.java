package model.controller;

import model.Element;

public class ElementController
{
    public Element getMyElement()
    {
        return myElement;
    }

    private Element myElement;
    public ElementController(String name)
    {
    }

    public ElementController(String name, int atomicNumber, double atomicMass)
    {

    }
}
