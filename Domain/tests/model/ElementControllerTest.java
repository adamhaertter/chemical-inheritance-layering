package model;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;


public class ElementControllerTest
{
    @AfterEach
    public void rollback()
    {
        // make the database roll back the changes this test made
    }

    @Test
    public void canGetExistingElement()
    {
        // put the object I'm getting into the database
        new ElementMapper("Oxygen", 8, 15.99);

        // Create an ElementController for the element
        ElementController controller = new ElementController("Oxygen");

        // Make sure everything is in the controller
        checkElementDetails(controller.getMyElement(), "Oxygen", 8, 15.99);
    }


    @Test
    public void exceptionOnMissingElement()
    {
        assertThrows(ElementNotFoundException.class, () ->
                new ElementMapper("NOTHING"));
    }

    @Test
    public void canCreateElement()
    {
        ElementController controller = new ElementController("Oxygen", 8, 15.9);

        // Make sure everything is in the controller
        checkElementDetails(controller.getMyElement(), "Oxygen", 8, 15.9);

    }

    @Test
    public void canUpdateAtomicNumber() throws ElementNotFoundException
    {
        // put the object I'm getting into the database
        new ElementMapper("Oxygen", 8, 15.999);

        // Create an ElementController for the element
        ElementController controller = new ElementController("Oxygen");

        // Do the poke that we are testing
        controller.setAtomicNumber(42);

        // That's enough to get it into the model layer
        checkElementDetails(controller.getMyElement(), "Oxygen", 42, 15.999);

        // Make sure it did not go all the way to the database
        checkElementDetails(new ElementMapper("Oxygen").getMyElement(),
                "Oxygen", 8, 15.999);
    }

    @Test
    public void canUpdateAtomicWeight() throws ElementNotFoundException
    {
        // put the object I'm getting into the database
        new ElementMapper("Oxygen", 8, 15.999);

        // Create an ElementController for the element
        ElementController controller = new ElementController("Oxygen");

        // Do the poke that we are testing
        controller.setAtomicMass(42.25);

        // That's enough to get it into the model layer
        checkElementDetails(controller.getMyElement(), "Oxygen", 8, 42.25);

        // Make sure it did not go all the way to the database
        checkElementDetails(new ElementMapper("Oxygen").getMyElement(),
                "Oxygen", 8, 15.999);
    }

    @Test
    public void canUpdateName()
    {
        // put the object I'm getting into the database
        new ElementMapper("Oxygen", 8, 15.99);

        // Create an ElementController for the element
        ElementController controller = new ElementController("Oxygen");

        // Do the poke that we are testing
        controller.setName("Yucky Oxygen");

        // That's enough to get it into the model layer
        checkElementDetails(controller.getMyElement(), "Yucky Oxygen", 8,
                15.999);

        // Make sure it did not go all the way to the database
        checkThatElementIsNotInDB("Yucky Oxygen");
    }

    @Test
    public void canUpdateAndPersistName() throws ElementNotFoundException
    {
        // put the object I'm getting into the database
        new ElementMapper("Oxygen", 8, 15.99);

        // Create an ElementController for the element
        ElementController controller = new ElementController("Oxygen");

        // Do the poke that we are testing
        controller.setName("Yucky Oxygen");
        controller.persist();

        // That's enough to get it into the model layer
        checkElementDetails(controller.getMyElement(), "Yucky Oxygen", 8,
                15.999);

        // Make sure it got all the way to the database
        checkThatElementIsNotInDB("Oxygen");
        checkElementDetails((new ElementMapper("Yucky Oxygen")).getMyElement(), "Yucky Oxygen", 8, 15.999);
    }

    @Test
    public void canPersistEverythingExceptName() throws ElementNotFoundException
    {
        // put the object I'm getting into the database
        new ElementMapper("Oxygen", 8, 15.99);

        // Create an ElementController for the element
        ElementController controller = new ElementController("Oxygen");

        // change everything but the name
        controller.setAtomicMass(42.25);
        controller.setAtomicNumber(42);

        controller.persist();

        checkElementDetails((new ElementMapper("Oxygen")).getMyElement(),
                "Oxygen", 42, 42.25);
    }

    @Test
    public void canDelete()
    {
        // put the object I'm deleting into the database
        new ElementMapper("Oxygen", 8, 15.99);
        ElementController.delete("Oxygen");

        checkThatElementIsNotInDB("Oxygen");
    }

    @Test
    public void canRetrieveARange()
    {
        fillDBWithSequentialRecords(15, 32);

        final int rangeStart = 20;
        final int rangeEnd = 26;
        final int expectedQuantity = rangeEnd - rangeStart + 1;
        Element[] resultElements =
                ElementController.getElementsBetween(rangeStart,
                rangeEnd);
        assertNotNull(resultElements);
        assertEquals(rangeEnd - rangeStart + 1, resultElements.length);
        for (int i = 0; i < expectedQuantity; i++)
        {
            assertTrue(isBetween(resultElements[i], rangeStart, rangeEnd));
            // if each one's name is not equal to the next one's name, we
            // probably didn't retrieve the same element a bunch of times
            assertNotEquals(resultElements[i].getName(),
                    resultElements[(i + 1) % expectedQuantity].getName());
        }
    }

    @Test
    public void canRetrieveAll()
    {
        fillDBWithSequentialRecords(42, 67);
        int numRecords = 67 - 42 + 1;
        Element[] resultElements = ElementController.getAllElements();
        assertNotNull(resultElements);
        assertEquals(numRecords, resultElements.length);
        for (int i = 0; i < numRecords; i++)
        {
            assertEquals(42 + i, resultElements[i].getAtomicNumber());
            // if each one's name is not equal to the next one's name, we
            // probably didn't retrieve the same element a bunch of times
            assertNotEquals(resultElements[i].getName(),
                    resultElements[(i + 1) % numRecords].getName());
        }
    }

    @Test
    public void canGetPeriod()
    {
        int[] periodStartPoint = {1, 3, 11, 19, 37, 55, 87};
        for (int period = 0; period < periodStartPoint.length; period++)
        {
            checkPeriodForAtomicNumber(periodStartPoint[period], period +1);
            if (period + 1 < periodStartPoint.length)
            {
                checkPeriodForAtomicNumber(periodStartPoint[period + 1] - 1,
                        period + 1);
            }
        }
    }

    @Test
    public void canGetAllCompoundsContainingElement()
            throws ElementNotFoundException
    {
        new ElementMapper("Hydrogen", 1, 2.1);
        new ElementMapper("Oxygen", 8, 15.99);
        new ElementMapper("Sodium", 11, 22.990);

        CompoundMapper.createCompound("Water");
        CompoundController waterController = new CompoundController("Water");
        waterController.addElement("Hydrogen");
        waterController.addElement("Hydrogen");
        waterController.addElement("Oxygen");

        CompoundMapper.createCompound("Sodium Hydroxide");
        CompoundController h2SController = new CompoundController("Hydrogen " +
                "Sulfide");
        h2SController.addElement("Hydrogen");
        h2SController.addElement("Oxygen");
        h2SController.addElement("Sodium");

        // Now check that we can retrieve the compounds made of an element
        //Simple case: only one compound
        ElementController sodiumController = new ElementController("Sodium");
        List<String> compoundNames = sodiumController.getCompoundsContaining();
        assertEquals(1, compoundNames.size());
        assertTrue(compoundNames.contains("Sodium Hydroxide"));

        // Case: More than one compound
        ElementController oxygenController = new ElementController("Oxygen");
        compoundNames = oxygenController.getCompoundsContaining();
        assertEquals(2, compoundNames.size());
        assertTrue(compoundNames.contains("Water"));
        assertTrue(compoundNames.contains("Sodium Hydroxide"));

        // Case: Names shouldn't be duplicated
        ElementController hydrogenController = new ElementController(
                "Hydrogen");
        compoundNames = hydrogenController.getCompoundsContaining();
        assertEquals(2, compoundNames.size());
        assertTrue(compoundNames.contains("Water"));
        assertTrue(compoundNames.contains("Sodium Hydroxide"));
    }

    private void fillDBWithSequentialRecords(int firstAtomicNumber,
                                             int lastAtomicNumber)
    {
        int quantity = lastAtomicNumber - firstAtomicNumber + 1;
        ElementForTest[] testData = new ElementForTest[quantity];
        for (int i = 0; i < quantity; i++)
        {
            testData[i] =
                    new ElementForTest("E" + firstAtomicNumber + i,
                            firstAtomicNumber + i, firstAtomicNumber + 0.1 + i, 0);
        }
        loadDB(testData);
    }

    private static void checkPeriodForAtomicNumber(int atomicNumber,
                                                   int expectedPeriod)
    {
        ElementMapper mapper = new ElementMapper("Name" + atomicNumber,
                atomicNumber, 42.2);
        assertEquals(expectedPeriod, mapper.getMyElement().getPeriod());
    }

    private void checkElementDetails(Element element, String name,
                                     int atomicNumber,
                                     double atomicMass)
    {
        assertEquals(name, element.getName());
        assertEquals(atomicNumber, element.getAtomicNumber());
        assertEquals(atomicMass, element.getAtomicMass(), 0.001);
    }

    private void checkThatElementIsNotInDB(String name)
    {
        try
        {
            new ElementMapper(name);
            fail("It appears " + name + " is in the DB when the tests think " +
                    "it shouldn't be");
        }
        catch (ElementNotFoundException e)
        {
            // no worries - we are hoping to see this.
        }
    }

    private boolean isBetween(@NotNull Element resultElement, int first,
                              int last)
    {
        return (resultElement.getAtomicNumber() >= first) && (
                resultElement.getAtomicNumber() <= last);
    }

    private void loadDB(ElementForTest @NotNull [] elements)
    {
        for (ElementForTest e : elements)
        {
            new ElementMapper(e.name, e.atomicNumber, e.atomicMass);
        }
    }

    private static class ElementForTest
    {
        final int period;
        final String name;
        int atomicNumber;
        double atomicMass;

        public ElementForTest(String name, int atomicNumber,
                              double atomicMass, int period)
        {
            this.name = name;
            this.atomicNumber = atomicNumber;
            this.atomicMass = atomicMass;
            this.period = period;
        }

        @Override
        public boolean equals(Object o)
        {
            if (this == o)
            {
                return true;
            }
            if (o == null || getClass() != o.getClass())
            {
                return false;
            }

            ElementForTest that = (ElementForTest) o;

            if (atomicNumber != that.atomicNumber)
            {
                return false;
            }
            if (Double.compare(that.atomicMass, atomicMass) != 0)
            {
                return false;
            }
            return Objects.equals(name, that.name);
        }

        @Override
        public int hashCode()
        {
            int result;
            long temp;
            result = name != null ? name.hashCode() : 0;
            result = 31 * result + atomicNumber;
            temp = Double.doubleToLongBits(atomicMass);
            result = 31 * result + (int) (temp ^ (temp >>> 32));
            return result;
        }
    }
}
