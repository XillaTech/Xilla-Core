package net.xilla.core.library.manager;

import lombok.Getter;

public class XillaManager extends Manager<String, Manager> {

    @Getter
    private static XillaManager instance = new XillaManager();

    public XillaManager() {
        super("Xilla_Manager");
    }

}
