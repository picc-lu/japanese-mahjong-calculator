import javax.swing.*;

/**
 * QuickCalc
 *
 * @author PICC
 * @date 2018-11-01 17:40
 */
public class QuickCalc {
    private JFrame jFrame;
    private JPanel main;
    private JTextField textField1;
    private JTextField textField2;

    private TenSuuKeiSan tenSuuKeiSan;

    public QuickCalc(TenSuuKeiSan tenSuuKeiSan) {
        this.tenSuuKeiSan = tenSuuKeiSan;
        setLook();
        init();
    }

    private void init() {
        jFrame = new JFrame("quickCalc");
        tenSuuKeiSan.setQuickCalcJFrame(jFrame);

        JPanel rootPane = main;
        jFrame.setContentPane(rootPane);
        jFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        jFrame.setResizable(false);
        jFrame.pack();
        jFrame.setTitle("快速计算 (beta)");

        JFrame tenSuuJFrame = tenSuuKeiSan.getjFrame();
        jFrame.setLocation(tenSuuJFrame.getX() - jFrame.getWidth() - 20, tenSuuJFrame.getY());
        jFrame.setVisible(true);
    }

    private void setLook() {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException | UnsupportedLookAndFeelException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
