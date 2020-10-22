import net.xilla.core.script.Script;

public class Test {

    public static void main(String[] args) {
        try {
            new Script("test_script.xs").run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
