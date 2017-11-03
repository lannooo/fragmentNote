package lannooo.view;

import lannooo.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Properties;

public class SettingFrame extends JFrame {
    private static final long serialVersionUID = -7078030311369039390L;
    private Logger logger = LoggerFactory.getLogger(SettingFrame.class);

    private MyNote callback;

    public SettingFrame(MyNote callback){
        this.callback = callback;
        setTitle("Settings");
        setSize(400, 300);
        setMinimumSize(new Dimension(300, 200));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        initView();
        this.setVisible(true);
    }

    private JTextArea textArea;
    private JTextField input;

    private void initView() {
        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setBackground(new Color(0xFF, 0xFF, 0xFF));
        textArea.setForeground(new Color(0x00,0x00,0x00));
        textArea.setText(getOldTypes());
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(300, 125));
        add(scrollPane, BorderLayout.CENTER);

        JPanel footPanel = new JPanel();
        footPanel.setLayout(new GridLayout(1,3));
        JLabel label = new JLabel("添加新类别");
        input = new JTextField();
        input.setPreferredSize(new Dimension(300,28));
        JButton btnOk = new JButton("添加");
        btnOk.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                addNewType();
                SettingFrame.this.dispose();
            }
        });
        footPanel.add(label);
        footPanel.add(input);
        footPanel.add(btnOk);
        add(footPanel, BorderLayout.SOUTH);
    }

    private void addNewType() {
        Properties prop = FileUtil.loadProperty("conf/note.properties");
        String typeStr = prop.getProperty("record.types", "");

        String newOne = input.getText().trim();
        typeStr = typeStr + (typeStr.length()==0 ? newOne : "," + newOne);

        prop.setProperty("record.types", typeStr);
        logger.info(prop.toString());

        FileUtil.setProperties("conf/note.properties", prop);
        this.callback.loadInitData();
        this.callback.freshComboxItems();
    }

    public String getOldTypes() {
        Properties prop = FileUtil.loadProperty("conf/note.properties");
        String typeStr = prop.getProperty("record.types", "");

        return "当前笔记类型有: \n" + typeStr;
    }
}
