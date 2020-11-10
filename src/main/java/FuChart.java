import javax.swing.*;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.List;

/**
 * FuChart
 *
 * @author PICC
 * @date 2018-10-17 13:52
 */
public class FuChart {
    private static final int TSUMO = 2;
    private static final int MENZEN_RON = 10;
    private static final int MINKOU_28 = 2;
    private static final int ANKOU_28 = 4;
    private static final int MINKAN_28 = 8;
    private static final int ANKAN_28 = 16;
    private static final int MINKOU_19 = 4;
    private static final int ANKOU_19 = 8;
    private static final int MINKAN_19 = 16;
    private static final int ANKAN_19 = 32;
    private static final int TANKI = 2;
    private static final int JYANTOU = 2;
    private static final int RENPUU_JYANTOU = 4;

    /**
     * 刻子、杠子总和最大值
     */
    private static final int KOU_KAN_MAX_NUM = 4;

    private JFrame jFrame;
    private JPanel main;
    private JRadioButton chiiToiTsu;
    private JRadioButton other;
    private JRadioButton menzen;
    private JRadioButton nonMenzen;
    private JRadioButton tsumo;
    private JRadioButton ron;
    private JComboBox<Integer> minKou28;
    private JComboBox<Integer> anKou28;
    private JComboBox<Integer> minKan28;
    private JComboBox<Integer> anKan28;
    private JComboBox<Integer> minKou19;
    private JComboBox<Integer> anKou19;
    private JComboBox<Integer> minKan19;
    private JComboBox<Integer> anKan19;
    private JButton resetKouKan;
    private JCheckBox tanKiKatachi;
    private JCheckBox jyanTou;
    private JCheckBox renPuuJyanTou;
    private JButton confirm;
    private JButton resetAll;
    private final TenSuuKeiSan tenSuuKeiSan;
    private final List<JComboBox<Integer>> koukanArray = Arrays.asList(minKou28, minKou19, anKou28, anKou19, minKan28
            , minKan19, anKan28, anKan19);

    public FuChart(TenSuuKeiSan tenSuuKeiSan) {
        this.tenSuuKeiSan = tenSuuKeiSan;
        setLook();
        init();
        initToDefault();
        jFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                tenSuuKeiSan.setFuChartWindowClosed(true);
            }
        });
        other.addItemListener(e -> {
            if (other.isSelected()) {
                setOtherEnabled();
            } else {
                setOtherDisabled();
            }
        });
        resetKouKan.addMouseListener(new MouseAdapter() {
            /**
             * {@inheritDoc}
             *
             * @param e
             */
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (other.isSelected()) {
                    resetKouKan();
                }
            }
        });
        jyanTou.addItemListener(e -> renPuuJyanTou.setEnabled(jyanTou.isSelected()));
        renPuuJyanTou.setToolTipText("若不承认连风雀头规则，则不要勾选。当雀头同时是场风和自风时，获得 4 符，不承认时获得 2 符。");
        ItemListener listener = e -> {
            final int kouKanNum = getKouKanNum();
            if (kouKanNum == KOU_KAN_MAX_NUM) {
                set0KouKanDisabled();
            } else if (kouKanNum > KOU_KAN_MAX_NUM) {
                JComboBox<Integer> target = (JComboBox<Integer>) e.getSource();
                target.setSelectedItem(Integer.parseInt(String.valueOf(target.getSelectedItem())) - (kouKanNum - 4) + "");
            } else {
                setAllKouKanStatus(true);
            }
        };
        koukanArray.forEach(jComboBox -> jComboBox.addItemListener(listener));
        resetAll.addMouseListener(new MouseAdapter() {
            /**
             * {@inheritDoc}
             *
             * @param e
             */
            @Override
            public void mouseClicked(MouseEvent e) {
                if (other.isSelected()) {
                    super.mouseClicked(e);
                    initToDefault();
                }
            }
        });
        confirm.addMouseListener(new MouseAdapter() {
            /**
             * {@inheritDoc}
             *
             * @param e
             */
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                tenSuuKeiSan.setFuChartWindowClosed(true);
                tenSuuKeiSan.getFu().setSelectedItem(calculate() + "");
                jFrame.dispose();
            }
        });
        confirm.setToolTipText("将更新符数");
    }

    public JRadioButton getChiiToiTsu() {
        return chiiToiTsu;
    }

    public JRadioButton getOther() {
        return other;
    }

    public JRadioButton getMenzen() {
        return menzen;
    }

    public JRadioButton getNonMenzen() {
        return nonMenzen;
    }

    public JRadioButton getTsumo() {
        return tsumo;
    }

    public JRadioButton getRon() {
        return ron;
    }

    public JComboBox<Integer> getMinKou28() {
        return minKou28;
    }

    public JComboBox<Integer> getAnKou28() {
        return anKou28;
    }

    public JComboBox<Integer> getMinKan28() {
        return minKan28;
    }

    public JComboBox<Integer> getAnKan28() {
        return anKan28;
    }

    public JComboBox<Integer> getMinKou19() {
        return minKou19;
    }

    public JComboBox<Integer> getAnKou19() {
        return anKou19;
    }

    public JComboBox<Integer> getMinKan19() {
        return minKan19;
    }

    public JComboBox<Integer> getAnKan19() {
        return anKan19;
    }

    public JCheckBox getTanKiKatachi() {
        return tanKiKatachi;
    }

    public JCheckBox getJyanTou() {
        return jyanTou;
    }

    public JCheckBox getRenPuuJyanTou() {
        return renPuuJyanTou;
    }

    public int getKouKanNum() {
        return Integer.parseInt((String) minKou28.getSelectedItem())
                + Integer.parseInt((String) anKou28.getSelectedItem())
                + Integer.parseInt((String) minKan28.getSelectedItem())
                + Integer.parseInt((String) anKan28.getSelectedItem())
                + Integer.parseInt((String) minKou19.getSelectedItem())
                + Integer.parseInt((String) anKou19.getSelectedItem())
                + Integer.parseInt((String) minKan19.getSelectedItem())
                + Integer.parseInt((String) anKan19.getSelectedItem());
    }

    public void set0KouKanDisabled() {
        koukanArray.forEach(target -> target.setEnabled(!"0".equals(target.getSelectedItem())));
    }

    private void setOtherEnabled() {
        Arrays.asList(menzen, nonMenzen, tsumo, ron).forEach(jRadioButton -> jRadioButton.setEnabled(true));

        if (getKouKanNum() >= KOU_KAN_MAX_NUM) {
            set0KouKanDisabled();
        } else {
            setAllKouKanStatus(true);
        }

        resetKouKan.setEnabled(true);
        resetAll.setEnabled(true);
        tanKiKatachi.setEnabled(true);
        jyanTou.setEnabled(true);
        if (jyanTou.isSelected()) {
            renPuuJyanTou.setEnabled(true);
        }
    }

    private void resetKouKan() {
        koukanArray.forEach(jComboBox -> jComboBox.setSelectedItem("0"));
    }

    private void setAllKouKanStatus(boolean enabled) {
        koukanArray.forEach(jComboBox -> jComboBox.setEnabled(enabled));
    }

    private void setOtherDisabled() {
        Arrays.asList(menzen, nonMenzen, tsumo, ron).forEach(jRadioButton -> jRadioButton.setEnabled(false));
        setAllKouKanStatus(false);
        Arrays.asList(tanKiKatachi, jyanTou, renPuuJyanTou).forEach(jCheckBox -> jCheckBox.setEnabled(false));
        resetKouKan.setEnabled(false);
        resetAll.setEnabled(false);
    }

    private void init() {
        jFrame = new JFrame("table");
        tenSuuKeiSan.setFuChartJFrame(jFrame);

        JPanel rootPane = main;
        jFrame.setContentPane(rootPane);
        jFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        jFrame.setResizable(false);
        jFrame.pack();
        jFrame.setTitle("符数计算");

        JFrame tenSuuJFrame = tenSuuKeiSan.getjFrame();
        jFrame.setLocation(tenSuuJFrame.getX() + tenSuuJFrame.getWidth() + 20, tenSuuJFrame.getY());
        jFrame.setVisible(true);
    }

    private void setLook() {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException | UnsupportedLookAndFeelException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void initToDefault() {
        other.setSelected(true);
        menzen.setSelected(true);
        tsumo.setSelected(true);
        resetKouKan();
        Arrays.asList(tanKiKatachi, jyanTou, renPuuJyanTou).forEach(jCheckBox -> jCheckBox.setSelected(false));
    }

    private int calculate() {
        if (chiiToiTsu.isSelected()) {
            return 25;
        }
        int fu = 20;
        int kouKan = MINKOU_28 * Integer.parseInt((String) minKou28.getSelectedItem())
                + ANKOU_28 * Integer.parseInt((String) anKou28.getSelectedItem())
                + MINKAN_28 * Integer.parseInt((String) minKan28.getSelectedItem())
                + ANKAN_28 * Integer.parseInt((String) anKan28.getSelectedItem())
                + MINKOU_19 * Integer.parseInt((String) minKou19.getSelectedItem())
                + ANKOU_19 * Integer.parseInt((String) anKou19.getSelectedItem())
                + MINKAN_19 * Integer.parseInt((String) minKan19.getSelectedItem())
                + ANKAN_19 * Integer.parseInt((String) anKan19.getSelectedItem());

        /*
         * 平和、门前清自摸和两个役同时出现时，固定 20 符，不再添加自摸 +2 符。其余情况统一为 30 符。
         * @version 0.1.2
         */
        if (isPinFu(kouKan)) {
            return menzen.isSelected() && tsumo.isSelected() ? 20 : 30;
        }

        fu += kouKan;

        if (tsumo.isSelected()) {
            fu += TSUMO;
        }

        if (menzen.isSelected() && ron.isSelected()) {
            fu += MENZEN_RON;
        }

        if (tanKiKatachi.isSelected()) {
            fu += TANKI;
        }

        if (jyanTou.isSelected()) {
            fu += renPuuJyanTou.isSelected() ? RENPUU_JYANTOU : JYANTOU;
        }

        fu = Math.min(fu, 110);

        fu = (int) Math.ceil(fu / 10.0) * 10;
        return fu;
    }

    /**
     * 是否符合平和型
     *
     * @param kouKan 刻子、杠子所加符数
     * @return true 则符合平和型，false 不符合。
     * @author PICC
     */
    private boolean isPinFu(final int kouKan) {
        return !tanKiKatachi.isSelected() && !jyanTou.isSelected() && kouKan == 0;
    }
}
