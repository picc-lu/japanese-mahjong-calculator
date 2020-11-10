import util.ComboBoxUtil;

import javax.swing.*;
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
    private JComboBox<String> minKou28;
    private JComboBox<String> anKou28;
    private JComboBox<String> minKan28;
    private JComboBox<String> anKan28;
    private JComboBox<String> minKou19;
    private JComboBox<String> anKou19;
    private JComboBox<String> minKan19;
    private JComboBox<String> anKan19;
    private JButton resetKouKan;
    private JCheckBox tanKiKatachi;
    private JCheckBox jyanTou;
    private JCheckBox renPuuJyanTou;
    private JButton confirm;
    private JButton resetAll;
    private final TenSuuKeiSan tenSuuKeiSan;
    private final List<JComboBox<String>> koukanArray = Arrays.asList(minKou28, minKou19, anKou28, anKou19, minKan28
            , minKan19, anKan28, anKan19);

    public FuChart(TenSuuKeiSan tenSuuKeiSan) {
        this.tenSuuKeiSan = tenSuuKeiSan;
        setLook();
        initFrame();
        initToDefault();
        initListener();
        renPuuJyanTou.setToolTipText("若不承认连风雀头规则，则不要勾选。当雀头同时是场风和自风时，获得 4 符，不承认时获得 2 符。");
        confirm.setToolTipText("将更新符数");
    }

    private void setLook() {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException | UnsupportedLookAndFeelException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void initFrame() {
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

    private void initToDefault() {
        other.setSelected(true);
        menzen.setSelected(true);
        tsumo.setSelected(true);
        resetKouKan();
        Arrays.asList(tanKiKatachi, jyanTou, renPuuJyanTou).forEach(jCheckBox -> jCheckBox.setSelected(false));
    }

    private void initListener() {
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
        koukanArray.forEach(jComboBox -> jComboBox.addItemListener(e -> {
            final int kouKanNum = getKouKanNum();
            if (kouKanNum == KOU_KAN_MAX_NUM) {
                set0KouKanDisabled();
            } else if (kouKanNum > KOU_KAN_MAX_NUM) {
                JComboBox<?> target = (JComboBox<?>) e.getSource();
                target.setSelectedItem(String.valueOf(ComboBoxUtil.getSelected(target, Integer.class) - (kouKanNum - 4)));
            } else {
                setAllKouKanStatus(true);
            }
        }));
        resetKouKan.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (other.isSelected()) {
                    resetKouKan();
                }
            }
        });
        jyanTou.addItemListener(e -> renPuuJyanTou.setEnabled(jyanTou.isSelected()));
        resetAll.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (other.isSelected()) {
                    initToDefault();
                }
            }
        });
        confirm.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                tenSuuKeiSan.setFuChartWindowClosed(true);
                tenSuuKeiSan.getFu().setSelectedItem(String.valueOf(calculate()));
                jFrame.dispose();
            }
        });
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

    private void setOtherDisabled() {
        Arrays.asList(menzen, nonMenzen, tsumo, ron).forEach(jRadioButton -> jRadioButton.setEnabled(false));
        setAllKouKanStatus(false);
        Arrays.asList(tanKiKatachi, jyanTou, renPuuJyanTou).forEach(jCheckBox -> jCheckBox.setEnabled(false));
        resetKouKan.setEnabled(false);
        resetAll.setEnabled(false);
    }

    private int getKouKanNum() {
        return ComboBoxUtil.getSelected(minKou28, Integer.class) +
                ComboBoxUtil.getSelected(anKou28, Integer.class) +
                ComboBoxUtil.getSelected(minKan28, Integer.class) +
                ComboBoxUtil.getSelected(anKan28, Integer.class) +
                ComboBoxUtil.getSelected(minKou19, Integer.class) +
                ComboBoxUtil.getSelected(anKou19, Integer.class) +
                ComboBoxUtil.getSelected(minKan19, Integer.class) +
                ComboBoxUtil.getSelected(anKan19, Integer.class);
    }

    private void set0KouKanDisabled() {
        koukanArray.forEach(target -> target.setEnabled(ComboBoxUtil.getSelected(target, Integer.class) != 0));
    }

    private void resetKouKan() {
        koukanArray.forEach(jComboBox -> jComboBox.setSelectedItem("0"));
    }

    private void setAllKouKanStatus(boolean enabled) {
        koukanArray.forEach(jComboBox -> jComboBox.setEnabled(enabled));
    }

    private int calculate() {
        if (chiiToiTsu.isSelected()) {
            return 25;
        }
        int fu = 20;
        int kouKan = MINKOU_28 * ComboBoxUtil.getSelected(minKou28, Integer.class)
                + ANKOU_28 * ComboBoxUtil.getSelected(anKou28, Integer.class)
                + MINKAN_28 * ComboBoxUtil.getSelected(minKan28, Integer.class)
                + ANKAN_28 * ComboBoxUtil.getSelected(anKan28, Integer.class)
                + MINKOU_19 * ComboBoxUtil.getSelected(minKou19, Integer.class)
                + ANKOU_19 * ComboBoxUtil.getSelected(anKou19, Integer.class)
                + MINKAN_19 * ComboBoxUtil.getSelected(minKan19, Integer.class)
                + ANKAN_19 * ComboBoxUtil.getSelected(anKan19, Integer.class);

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
