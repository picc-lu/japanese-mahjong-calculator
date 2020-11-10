import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;
import java.util.Random;

/**
 * TenSuuKeiSan
 *
 * @author PICC
 * @date 2018-09-27 11:29
 */
public class TenSuuKeiSan {
    private final JFrame jFrame = new JFrame("main");
    private JFrame fuChartJFrame = null;
    private JFrame quickCalcJFrame = null;
    private JPanel main;
    private JComboBox<Integer> fu;
    private JComboBox<Integer> fan;
    private JButton calculateBtn;
    private JSpinner bonba;
    private JTextField oyaron;
    private JTextField koron;
    private JTextField oyatsumo;
    private JTextField kotsumo;
    private JButton clearBonba;
    private JButton redoBonba;
    private JLabel about;
    private JLabel fuCalculate;
    private JButton quickCalcBtn;
    private int savedBonba = 0;
    private boolean isFuChartWindowClosed = true;
    private boolean isQuickCalcWindowClosed = true;
    private final String[] emoji = new String[]{"_(:з)∠)_", "o(=•ェ•=)m", "o(*￣▽￣*)ブ", "φ(゜▽゜*)♪", "（╯‵□′）╯︵┴─┴",
            "┴─┴︵╰（‵□′╰）", "( ￣▽￣)o╭╯☆(￣#)з￣)"};

    public static final int YAKUMAN_FAN = 13;
    public static final int MAX_BONBA = 999;

    private TenSuuKeiSan() {
        calculateBtn.addMouseListener(new MouseAdapter() {
            /**
             * {@inheritDoc}
             *
             * @param e
             */
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                final int fuAmount = Integer.parseInt((String) fu.getSelectedItem());
                int fanAmount;

                String fanStr = (String) fan.getSelectedItem();
                switch (fanStr) {
                    case "两倍役满":
                        fanAmount = 13 * 2;
                        break;
                    case "三倍役满":
                        fanAmount = 13 * 3;
                        break;
                    case "四倍役满":
                        fanAmount = 13 * 4;
                        break;
                    case "五倍役满":
                        fanAmount = 13 * 5;
                        break;
                    case "六倍役满":
                        fanAmount = 13 * 6;
                        break;
                    default:
                        fanAmount = Integer.parseInt(fanStr);
                }

                double a = fuAmount * Math.pow(2, fanAmount + 2);
                int bonBa = (int) bonba.getValue() * 100;

                switch (fanAmount) {
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                        if (fanAmount == 3 && fuAmount >= 70 || fanAmount == 4 && fuAmount >= 40) {
                            oyaron.setText(String.valueOf(12000 + bonBa * 3));
                            oyatsumo.setText("每人 " + (4000 + bonBa));
                            koron.setText(String.valueOf(8000 + bonBa * 3));
                            kotsumo.setText((2000 + bonBa) + " · " + (4000 + bonBa));
                        } else {
                            oyaron.setText(String.valueOf((int) Math.ceil(a * 6 / 100) * 100 + bonBa * 3));
                            oyatsumo.setText("每人 " + ((int) Math.ceil(a * 2 / 100) * 100 + bonBa));
                            koron.setText(String.valueOf((int) Math.ceil(a * 4 / 100) * 100 + bonBa * 3));
                            kotsumo.setText(
                                    ((int) Math.ceil(a / 100) * 100 + bonBa) + " · " + ((int) Math.ceil(a * 2 / 100) * 100 + bonBa));
                        }
                        break;
                    case 5:
                        oyaron.setText(String.valueOf(12000 + bonBa * 3));
                        oyatsumo.setText(("每人 ") + (4000 + bonBa));
                        koron.setText(String.valueOf(8000 + bonBa * 3));
                        kotsumo.setText((2000 + bonBa) + " · " + (4000 + bonBa));
                        break;
                    case 6:
                    case 7:
                        oyaron.setText(String.valueOf(18000 + bonBa * 3));
                        oyatsumo.setText("每人 " + (6000 + bonBa));
                        koron.setText(String.valueOf(12000 + bonBa * 3));
                        kotsumo.setText((3000 + bonBa) + " · " + (6000 + bonBa));
                        break;
                    case 8:
                    case 9:
                    case 10:
                        oyaron.setText(String.valueOf(24000 + bonBa * 3));
                        oyatsumo.setText("每人 " + (8000 + bonBa));
                        koron.setText(String.valueOf(16000 + bonBa * 3));
                        kotsumo.setText((4000 + bonBa) + " · " + (8000 + bonBa));
                        break;
                    case 11:
                    case 12:
                        oyaron.setText(String.valueOf(36000 + bonBa * 3));
                        oyatsumo.setText("每人 " + (12000 + bonBa));
                        koron.setText(String.valueOf(24000 + bonBa * 3));
                        kotsumo.setText((6000 + bonBa) + " · " + (12000 + bonBa));
                        break;
                    case 13:
                        oyaron.setText(String.valueOf(48000 + bonBa * 3));
                        oyatsumo.setText("每人 " + (16000 + bonBa));
                        koron.setText(String.valueOf(32000 + bonBa * 3));
                        kotsumo.setText((8000 + bonBa) + " · " + (16000 + bonBa));
                        break;
                    default:
                        if (fanAmount % YAKUMAN_FAN == 0) {
                            oyaron.setText(String.valueOf(48000 * (fanAmount / 13) + bonBa * 3));
                            oyatsumo.setText("每人 " + (16000 * (fanAmount / 13) + bonBa));
                            koron.setText(String.valueOf(32000 * (fanAmount / 13) + bonBa * 3));
                            kotsumo.setText((8000 * (fanAmount / 13) + bonBa) + " · " + (16000 * (fanAmount / 13) + bonBa));
                        } else {
                            oyaron.setText("0");
                            oyatsumo.setText("0");
                            koron.setText("0");
                            kotsumo.setText("0");
                        }
                }

                // 改变按钮 emoji
                calculateBtn.setText(emoji[new Random().nextInt(emoji.length)]);
            }
        });
        clearBonba.addMouseListener(new MouseAdapter() {
            /**
             * {@inheritDoc}
             *
             * @param e
             */
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int beforeBonba;
                if ((beforeBonba = (int) bonba.getValue()) != 0) {
                    bonba.setValue(0);
                    savedBonba = beforeBonba;
                }
            }
        });
        redoBonba.addMouseListener(new MouseAdapter() {
            /**
             * {@inheritDoc}
             *
             * @param e
             */
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                bonba.setValue(savedBonba);
            }
        });
        bonba.addChangeListener(e -> {
            int value = (int) bonba.getValue();
            if (value < 0) {
                bonba.setValue(0);
                value = 0;
            } else if (value > MAX_BONBA) {
                bonba.setValue(MAX_BONBA);
                value = MAX_BONBA;
            }
            savedBonba = value;
        });

        about.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                new About();
            }
        });
        fuCalculate.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                initFuChart();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                setUnderline(fuCalculate);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                fuCalculate.setText("符数计算");
            }
        });
        quickCalcBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                initQuickCalc();
            }
        });
        about.setCursor(new Cursor(Cursor.HAND_CURSOR));
        fuCalculate.setCursor(new Cursor(Cursor.HAND_CURSOR));
        quickCalcBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private static void init() {
        TenSuuKeiSan tenSuuKeiSan = new TenSuuKeiSan();
        JFrame jFrame = tenSuuKeiSan.jFrame;
        JPanel rootPane = tenSuuKeiSan.main;
        jFrame.setContentPane(rootPane);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jFrame.setResizable(false);
        jFrame.pack();
        jFrame.setTitle("日麻点数计算");
        jFrame.setIconImage(new ImageIcon(Objects.requireNonNull(TenSuuKeiSan.class.getClassLoader()
                .getResource("icon.png"))).getImage());
        jFrame.setLocationRelativeTo(rootPane);
        jFrame.setVisible(true);
    }

    private static void setLook() {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException | UnsupportedLookAndFeelException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void setUnderline(JLabel jLabel) {
        String text = jLabel.getText();
        jLabel.setText("<html><u>" + text + "</u></html>");
    }

    public static void main(String[] args) {
        setLook();
        init();
    }

    public JComboBox<Integer> getFu() {
        return fu;
    }

    public JFrame getjFrame() {
        return jFrame;
    }

    public void setFuChartJFrame(JFrame fuChartJFrame) {
        this.fuChartJFrame = fuChartJFrame;
    }

    public void setQuickCalcJFrame(JFrame quickCalcJFrame) {
        this.quickCalcJFrame = quickCalcJFrame;
    }

    public void setFuChartWindowClosed(boolean fuChartWindowClosed) {
        isFuChartWindowClosed = fuChartWindowClosed;
    }

    public void setQuickCalcWindowClosed(boolean quickCalcWindowClosed) {
        isQuickCalcWindowClosed = quickCalcWindowClosed;
    }

    private void initFuChart() {
        if (isFuChartWindowClosed) {
            FuChart fuChart = new FuChart(TenSuuKeiSan.this);
            isFuChartWindowClosed = false;
        } else {
            fuChartJFrame.setState(Frame.NORMAL);
        }
    }

    private void initQuickCalc() {
        if (isQuickCalcWindowClosed) {
            QuickCalc quickCalc = new QuickCalc(TenSuuKeiSan.this);
            isQuickCalcWindowClosed = false;
        } else {
            quickCalcJFrame.setState(Frame.NORMAL);
        }
    }
}
