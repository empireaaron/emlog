import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Hello world!
 */
public class App {
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        for (int i = 0; i < 1; i++) {
            System.out.println("Hello World!" + i);
            logger.info("测试用例--" + i);
        }
    }
}
