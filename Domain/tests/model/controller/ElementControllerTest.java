package model.controller;

import model.Element;
import model.ElementMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class ElementControllerTest
{
    @Test
    public void canGetExistingElement()
    {
        // put the object I'm getting into the database
        ElementMapper mapper = new ElementMapper("Oxygen", 8, 15.99);

        // Create an ElementController for the element
        ElementController controller = new ElementController("Oxygen");

        // Make sure everything is in the controller
        Element controllerResult = controller.getMyElement();
        assertEquals("Oxygen", controllerResult.getName());
        assertEquals(8, controllerResult.getAtomicNumber());
        assertEquals(15.999, controllerResult.getAtomicWeight(), 0.001);

        // Make sure it got all the way to the database
        ElementMapper afterMapper = new ElementMapper("Oxygen");
        Element mapperResult = afterMapper.getMyElement();
        assertEquals("Oxygen", mapperResult.getName());
        assertEquals(8, mapperResult.getAtomicNumber());
        assertEquals(15.999, mapperResult.getAtomicWeight(), 0.001);
    }
}
