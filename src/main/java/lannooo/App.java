package lannooo;

import lannooo.comsumer.ConsumerProxy;
import lannooo.comsumer.MarkdownConsumer;
import lannooo.view.MyNote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;

public class App {
    private static final Logger logger = LoggerFactory.getLogger(App.class);
    public static void main(String[] args) {
        ConsumerProxy.getInstance()
                .registerConsumer(new MarkdownConsumer());
        logger.info("\"user.dir\": " + System.getProperty("user.dir"));
        SwingUtilities.invokeLater(() -> new MyNote());
    }
}
