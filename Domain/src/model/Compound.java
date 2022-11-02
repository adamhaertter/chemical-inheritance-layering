package model;

import mappers.CompoundMapper;

import java.util.List;

public class Compound extends Chemical {

    public final CompoundMapper mapper;

    public Compound(CompoundMapper mapper, String name) {
        super(name);
        this.mapper = mapper;
    }

    public List<Element> getMadeOf() {
        return null;
    }
}
