import net.xilla.core.library.web.WebServer;

import java.io.IOException;

public class TestWebServer {

    public static void main(String[] args) {
        try {
            WebServer webServer = new WebServer(8080, 10);
            //webServer.setVerbose(true);
            webServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
