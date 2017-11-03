package lannooo.view;

import lannooo.comsumer.Consumer;
import lannooo.comsumer.ConsumerProxy;
import lannooo.comsumer.MarkdownConsumer;
import lannooo.data.Record;
import lannooo.util.FileUtil;
import lannooo.util.StringUtil;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.dispatcher.SwingDispatchService;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.*;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

public class MyNote extends JFrame implements WindowListener, NativeKeyListener {
    private static final long serialVersionUID = -7078030311369039390L;

    private Logger logger = LoggerFactory.getLogger(MyNote.class);
    private JTextArea textArea;
    private JButton btnOk;
    private JLabel keyLabel;
    private JComboBox comboBox;
    private JTextField titleField;

    private Set<String> allTypes = new HashSet<>();

    private boolean isAltPressed = false;
    private boolean isShiftPressed = false;
    private boolean isCtrlPressed = false;
    private Consumer consumer = ConsumerProxy.getInstance();

    public MyNote(){
        setTitle("MyNote");
        setSize(600, 400);
        setMinimumSize(new Dimension(500, 400));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        loadInitData();
        initView();

        try {
            GlobalScreen.setEventDispatcher(new SwingDispatchService());
            GlobalScreen.registerNativeHook();
            GlobalScreen.addNativeKeyListener(this);
        } catch (NativeHookException e) {
            logger.error("Fail to register Native Hook", e);
        }

        registerSystemTray();

        this.setVisible(true);
    }

    public void loadInitData() {
        logger.info("Init data");

        Properties prop = new FileUtil().loadProperty("conf/note.properties");
        String typeStr = prop.getProperty("record.types", "");
        logger.info(typeStr);
        String[] types = typeStr.split(",");
        if(types != null && types.length > 0){
            this.allTypes.clear();
            Collections.addAll(this.allTypes, types);
        }
    }

    private void initView() {
        // 设置内容框
        textArea = new JTextArea();
        textArea.setEditable(true);
        textArea.setBackground(new Color(0xFF, 0xFF, 0xFF));
        textArea.setForeground(new Color(0x00,0x00,0x00));
        textArea.setText("");
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(375, 125));
        add(scrollPane, BorderLayout.CENTER);

        // 底边的两行:关键词和按钮
        JPanel footPanel = new JPanel();
        footPanel.setLayout(new GridLayout(2,1));
        //关键词label
        keyLabel = new JLabel();
        keyLabel.setText("");
        footPanel.add(keyLabel);
        //按钮
        btnOk = new JButton("保存");
        btnOk.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                saveData();
            }
        });
        btnOk.registerKeyboardAction(e -> saveData(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW);
        footPanel.add(btnOk);
        add(footPanel, BorderLayout.SOUTH);

        //抬头的一行，类别和标题
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        // 类别选项
        comboBox = new JComboBox();
        freshComboxItems();
        titlePanel.add(comboBox);
        // 标题输入栏
        titleField = new JTextField();
        titleField.setPreferredSize(new Dimension(375, 30));
        titlePanel.add(titleField);
        add(titlePanel, BorderLayout.NORTH);
    }

    public void freshComboxItems() {
        logger.info("fresh combox Items");
        comboBox.removeAllItems();
        for(String each: allTypes){
            comboBox.addItem(each);
        }
    }

    private void saveData() {
        String content = textArea.getText();
        String type = comboBox.getSelectedItem().toString();
        String title = titleField.getText();
        String keyStr = keyLabel.getText();
        //TODO: 中英文混合分词
        String[] keys = new String[0];
        Record record = new Record();
        record.setContent(content);
        record.setType(type);
        record.setTitle(title);
        record.setKeyWords(keys);
        logger.info(record.toString());
        consumer.accept(record);
        //清空并关闭窗口
        clearText();
        hideWindow();
    }

    private void clearText() {
        keyLabel.setText("");
        textArea.setText("");
        titleField.setText("");
    }

    private void hideWindow(){
        this.setVisible(false);
    }

    private TrayIcon trayIcon;
    private void registerSystemTray(){
        if(SystemTray.isSupported()){
            PopupMenu popupMenu = new PopupMenu();

            MenuItem exitItem = new MenuItem("Exit");
            exitItem.addActionListener(e -> exit());
            popupMenu.add(exitItem);

            MenuItem configItem = new MenuItem("Setting");
            configItem.addActionListener(e -> modifySetting());
            popupMenu.add(configItem);

            ImageIcon icon = new ImageIcon("img/edit.png");
            trayIcon = new TrayIcon(icon.getImage(),"Test system tray", popupMenu);
            trayIcon.addActionListener(e -> MyNote.this.setVisible(true));

            try{
                SystemTray.getSystemTray().add(trayIcon);
            } catch (AWTException e) {
                logger.warn("Fail to register system tray", e);
            }
        }
    }

    private void modifySetting() {
        SettingFrame settingFrame = new SettingFrame(this);
    }

    private void unregisterSystemTray(){
        if(SystemTray.isSupported()){
            if(this.trayIcon != null){
                SystemTray.getSystemTray().remove(trayIcon);
            }
        }
    }

    private void exit(){
        unregisterSystemTray();
        try {
            GlobalScreen.unregisterNativeHook();
        } catch (NativeHookException e) {
            logger.error("Fail to unregister Native Hook", e);
        }
        System.runFinalization();
        System.exit(0);
    }

    private void println(String msg){
        textArea.append(msg + "\n");
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent nativeKeyEvent) {
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent nativeKeyEvent) {
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent nativeKeyEvent) {
        logger.info("Key Released: "+NativeKeyEvent.getKeyText(nativeKeyEvent.getKeyCode()));
        isAltPressed = (nativeKeyEvent.getModifiers() & NativeKeyEvent.ALT_MASK) != 0;
        isCtrlPressed = (nativeKeyEvent.getModifiers() & NativeKeyEvent.CTRL_MASK) != 0;
        isShiftPressed = (nativeKeyEvent.getModifiers() & NativeKeyEvent.SHIFT_MASK) != 0;

        if(isShiftPressed && isCtrlPressed && nativeKeyEvent.getKeyCode() == NativeKeyEvent.VC_V){
            // show window
            this.setAlwaysOnTop(true);
            this.setVisible(true);
            this.requestFocusInWindow();

            String content = getStringFromClipBorad().trim();
            println(content);

            this.setAlwaysOnTop(false);
        }
    }

    private String getStringFromClipBorad() {
        Clipboard sysclip = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable clipTransfer = sysclip.getContents(null);
        if(clipTransfer != null){
            if(clipTransfer.isDataFlavorSupported(DataFlavor.stringFlavor)){
                try {
                    return (String) clipTransfer.getTransferData(DataFlavor.stringFlavor);
                } catch (UnsupportedFlavorException e) {
                    logger.error("error", e);
                } catch (IOException e) {
                    logger.error("error", e);
                }
            }
        }
        return "";
    }

    public void windowClosing(WindowEvent e) {
        hideWindow();
    }

    public void windowOpened(WindowEvent e) {
        //do nothing
    }

    public void windowClosed(WindowEvent e) {
        //do nothing
    }

    public void windowIconified(WindowEvent e) {
        //do nothing
    }

    public void windowDeiconified(WindowEvent e) {
        //do nothing
    }

    public void windowActivated(WindowEvent e) {
        //do nothing
    }

    public void windowDeactivated(WindowEvent e) {
        //do nothing
    }
}
