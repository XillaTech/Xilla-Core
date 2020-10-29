package net.xilla.core.library.manager;

import lombok.Getter;

import java.util.HashMap;

public class XillaManager extends Manager<Manager> {

    @Getter
    private static XillaManager instance = new XillaManager();

    public XillaManager() {
        super("Xilla_Manager");
    }

    @Override
    protected void load() {

    }

    @Override
    protected void objectAdded(Manager obj) {

    }

    @Override
    protected void objectRemoved(Manager obj) {

    }
}
