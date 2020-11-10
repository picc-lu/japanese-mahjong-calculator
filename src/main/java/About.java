import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * About
 *
 * @author PICC
 * @date 2018-10-13 08:53
 */
public class About {
    private JPanel main;
    private JButton copy;
    private JLabel versionStr;
    private JLabel ver;
    private JLabel talk;
    private JTextArea site;

    About() {
        setLook();
        init();
        copy.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                Transferable trans = new StringSelection(site.getText());
                // 把文本内容设置到系统剪贴板
                clipboard.setContents(trans, null);
            }
        });
        copy.setToolTipText("本来做的是一键打开浏览器，但是 360 报可疑程序了，就做成这样了😭");
        site.setCursor(new Cursor(Cursor.TEXT_CURSOR));
    }

    private void setLook() {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException | UnsupportedLookAndFeelException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void init() {
        JFrame jFrame = new JFrame("About");

        JPanel rootPane = main;
        jFrame.setContentPane(rootPane);
        jFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        jFrame.setResizable(false);

        jFrame.pack();
        jFrame.setTitle("关于");
        jFrame.setLocationRelativeTo(rootPane);
        jFrame.setVisible(true);
    }
}
