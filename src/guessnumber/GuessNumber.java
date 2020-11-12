package guessnumber;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.Locale;
import java.util.Random;
import java.util.ResourceBundle;

public class GuessNumber extends JFrame {

    //private final int MIN_NUMBER = 0;
    //private final int MAX_NUMBER = 100;
    private int secret;

    private Locale locale;
    private ResourceBundle bundle;
    private Container pane;
    private JPanel firstPanel, secondPanel;
    private IntTextField txfMin, txfMax, txfAttempt;
    private JButton btnOk;
    private JTextPane txpResult;
    private JScrollPane scroll;

    public GuessNumber() {
        initUI();
    }

    private void initUI() {
        //locale = new Locale("en");
        locale = Locale.getDefault();
        bundle = ResourceBundle.getBundle("guessnumber.strings", locale);
        String classpathStr = System.getProperty("java.class.path");
        System.out.println(classpathStr);

        URL imgURL = this.getClass().getResource("myappicon.png");
        System.out.println(imgURL);
        ImageIcon myAppIcon =  new ImageIcon(imgURL);
        setIconImage(myAppIcon.getImage());

        setTitle(bundle.getString("window_title"));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(350, 450);
        setLocationRelativeTo(null); // this method display the JFrame to center position of a screen

        pane = getContentPane();
        pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));

        firstPanel = new JPanel();
        firstPanel.add(new JLabel("Min"));
        txfMin = new IntTextField(0, 10);
        firstPanel.add(txfMin);
        firstPanel.add(new JLabel("Max"));
        txfMax = new IntTextField(100, 10);
        firstPanel.add(txfMax);
        pane.add(firstPanel);

        secondPanel = new JPanel();
        txfAttempt = new IntTextField(0, 10);
        secondPanel.add(txfAttempt);
        btnOk = new JButton(bundle.getString("button_label"));
        secondPanel.add(btnOk);
        pane.add(secondPanel);

        txpResult = new JTextPane();
        scroll = new JScrollPane (txpResult);
        scroll.setMinimumSize(new Dimension(250, 350));
        scroll.setPreferredSize(new Dimension(250, 350));
        pane.add(scroll);

        class MyActionListener implements ActionListener {
            public void actionPerformed(ActionEvent e) {
                Object source = e.getSource();
                if (source.equals(btnOk)) {
                    int attempt = txfAttempt.getValue();
                    appendToPane(txpResult, String.format("%d\t", attempt), Color.BLACK);
                    if (attempt < secret) {
                        appendToPane(txpResult,bundle.getString("too_low_message") + "\n", Color.BLUE);
                    } else if (attempt > secret) {
                        appendToPane(txpResult, bundle.getString("too_high_message") + "\n", Color.ORANGE);
                    } else {
                        appendToPane(txpResult, bundle.getString("guessed_message") + "\n\n", Color.GREEN);
                        // TODO: ask for new game
                        secret = generateRandomNumber(txfMin.getValue(), txfMax.getValue());
                    }
                }

                if (source.equals(txfMin) || source.equals(txfMax)) {
                    int min = txfMin.getValue();
                    int max = txfMax.getValue();
                    if (min < max) {
                        secret = generateRandomNumber(min, max);
                    }
                }
            }
        }

        btnOk.addActionListener(new MyActionListener());
        txfMin.addActionListener(new MyActionListener());
        txfMax.addActionListener(new MyActionListener());

        secret = generateRandomNumber(txfMin.getValue(), txfMax.getValue());

    }

    private int generateRandomNumber(int min, int max) {
        int spread = max - min;
        if(spread < 0) spread = 0;
        int r = new Random().nextInt(spread + 1) + min;
        appendToPane(txpResult, bundle.getString("new_number") + '\n', Color.MAGENTA);
        return r;
    }

    private void appendToPane(JTextPane tp, String msg, Color c)
    {
        StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c);

        aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Lucida Console");
        aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);

        int len = tp.getDocument().getLength();
        tp.setCaretPosition(len);
        tp.setCharacterAttributes(aset, false);
        tp.replaceSelection(msg);
    }
    public static void main(String[] args) {
        GuessNumber ex = new GuessNumber();
        ex.setVisible(true);
    }
}
