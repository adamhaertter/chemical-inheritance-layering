package model;

import exceptions.ElementNotFoundException;
import mappers.ClassElementMapper;
import mappers.CompoundMapper;

import java.util.ArrayList;

public class Compound extends Chemical {

    public final CompoundMapper mapper;
    private ArrayList<Element> madeOf = new ArrayList<>();

    public Compound(CompoundMapper mapper, String name) {
        super(name);
        this.mapper = mapper;
        for(String element : mapper.getElementsInCompound()) {
            try {
                madeOf.add((new ClassElementMapper(element).getMyElement()));
            } catch (ElementNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public ArrayList<Element> getMadeOf() {
        return madeOf;
    }

    public void addElement(String element) throws ElementNotFoundException {
        madeOf.add((new ClassElementMapper(element).getMyElement()));
        mapper.addElement(element);
    }
}
