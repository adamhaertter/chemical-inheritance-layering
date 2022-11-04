package model;

import mappers.CompoundMapper;

import java.util.ArrayList;
import java.util.List;

public class Compound extends Chemical {

    public final CompoundMapper mapper;
    private ArrayList<String> madeOf = new ArrayList<>();

    public Compound(CompoundMapper mapper, String name) {
        super(name);
        this.mapper = mapper;
    }

    public List<Element> getMadeOf() {
        return null;
    }

    public void addElement(String element) {
        madeOf.add(element);
        mapper.addElement(element);
    }
}
