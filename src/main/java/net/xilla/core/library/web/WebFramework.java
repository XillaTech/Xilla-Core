package net.xilla.core.library.web;

import net.xilla.core.library.manager.Manager;
import net.xilla.core.library.web.framework.WebPage;

public class WebFramework extends Manager<String, WebPage> {

    public WebFramework(String name) {
        super(name);
    }

    public WebFramework(String name, String folder) {
        super(name, folder);
    }

    public WebFramework(String name, String folder, Class<WebPage> clazz) {
        super(name, folder, clazz);
    }

}
