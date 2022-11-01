package model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the CompoundController.  Note that we are not requiring update or
 * delete operations on the relationships between a compound and the elements
 * it is made of.
 */
public class CompoundControllerTest
{
    public static final String WATER = "Water";
    public static final int HYDROGEN_ATOMIC_MASS = 2;
    public static final double OXYGEN_ATOMIC_MASS = 15.9;
    private final String[] WATER_ELEMENT_NAMES={"Hydrogen","Hydrogen","Oxygen"};
    @AfterEach
    public void rollback()
    {
        // make the database roll back the changes this test made
    }

    @Test
    public void canGetExistingCompound() throws CompoundNotFoundException
    {
        // put the object I'm getting into the database
        CompoundMapper.createCompound("Water");

        CompoundController controller = new CompoundController("Water");

        // Make sure everything is in the controller
        assertEquals("Water", controller.getMyCompound().getName());
    }

    @Test
    public void canCreateCompound()
    {
        CompoundController.createCompound ("Water");

        assertEquals("Water", new CompoundController("Water").getMyCompound().getName());

    }
    @Test
    public void exceptionOnMissingCompound()
    {
        assertThrows(CompoundNotFoundException.class, () ->
                new CompoundMapper("NOTHING"));
    }

    @Test
    public void canUpdateName()
    {
        // put the object I'm getting into the database
        CompoundMapper.createCompound("Sulfuric Acid");

        // Create an ElementController for the element
        CompoundController controller = new CompoundController("Sulfuric Acid");

        // Do the poke that we are testing
        controller.setName("Sulfuric Base");

        // That's enough to get it into the model layer
        assertEquals("Sulfuric Base",controller.getMyCompound().getName());

        // Make sure it did not go all the way to the database
        checkThatCompoundIsNotInDB("Sulfuric Acid");
    }

    private void checkThatCompoundIsNotInDB(String name)
    {
        try
        {
            new CompoundMapper(name);
            fail("It appears " + name + " is in the DB when the tests think " +
                    "it shouldn't be");
        }
        catch (CompoundNotFoundException e)
        {
            // no worries - we are hoping to see this.
        }
    }

    @Test
    public void canDelete()
    {
        // put the object I'm deleting into the database
        CompoundMapper.createCompound("HCl");

        CompoundController.delete("HCl");

        checkThatCompoundIsNotInDB("HCl");
    }

    @Test
    public void canAddAndRetrieveOneElement() throws ElementNotFoundException
    {
        CompoundMapper.createCompound("Water");
        new ElementMapper("Hydrogen",1, 2);

        CompoundController controller = new CompoundController("Water");
        controller.addElement("Hydrogen");

        List<String> elements = controller.getElements();
        assertEquals(1, elements.size());
        assertEquals("Hydrogen", elements.get(0));
    }

    @Test
    public void canAddAndRetrieveMultipleElement()
            throws ElementNotFoundException
    {
        buildWater();

        CompoundController controller = new CompoundController(WATER);
        List<String> elements = controller.getElements();
        assertEquals(3, elements.size());

        checkElementListMatchesNames(elements, WATER_ELEMENT_NAMES);
    }

    private void buildWater() throws ElementNotFoundException
    {
        CompoundMapper.createCompound(WATER);
        new ElementMapper("Hydrogen",1, HYDROGEN_ATOMIC_MASS);
        new ElementMapper("Oxygen",8, OXYGEN_ATOMIC_MASS);

        CompoundController controller = new CompoundController("Water");

        for(String name:WATER_ELEMENT_NAMES)
        {
            controller.addElement(name);
        }
    }

    @Test
    public void exceptionOnAddNonexistingElement()
            throws ElementNotFoundException
    {
        CompoundMapper.createCompound("Water");

        CompoundController controller = new CompoundController("Water");
        assertThrows(ElementNotFoundException.class,
                () -> controller.addElement("Hydrogen"));
    }

    @Test
    public void changeNameOfRelatedElementStillRelated() throws ElementNotFoundException
    {
        // create the stuff we need
        CompoundMapper.createCompound("Water");
        new ElementMapper("Hydrogen",1, 2);

        // make the compound be made of the element
        CompoundController controller = new CompoundController("Water");
        controller.addElement("Hydrogen");

        // change the name of the element
        (new ElementController("Hydrogen")).setName("Bad Hydrogen");

        // Make sure we are related to the renamed element
        List<String> elements = controller.getElements();
        assertEquals(1, elements.size());
        assertEquals("Bad Hydrogen", elements.get(0));
    }

    @Test
    public void correctAtomicWeight() throws ElementNotFoundException
    {
        buildWater();

        CompoundController controller = new CompoundController(WATER);
        assertEquals(2*HYDROGEN_ATOMIC_MASS + OXYGEN_ATOMIC_MASS, controller.getAtomicMass());
    }
    private void checkElementListMatchesNames(List<String> actualElementNames, String[] expectedElementNames)
    {
        assertEquals(expectedElementNames.length, actualElementNames.size());
        boolean[] matched = new boolean[expectedElementNames.length];
        for(String name:actualElementNames)
        {
            int i=0;
            while ((i < expectedElementNames.length) &&
                    (matched[i] || !name.equals(expectedElementNames[i])))
            {
                i++;
            }
            if (i == expectedElementNames.length)
            {
                fail("Couldn't find a match for " + name);
            } else
            {
                matched[i] = true;
            }
        }
    }
}

