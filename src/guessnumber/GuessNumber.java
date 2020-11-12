package guessnumber;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    private JTextArea txaResult;
    private JScrollPane scroll;

    public GuessNumber() {
        initUI();
    }

    private void initUI() {
        //locale = new Locale("en");
        locale = Locale.getDefault();
        bundle = ResourceBundle.getBundle("guessnumber.strings", locale);

        setTitle(bundle.getString("window_title"));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(350, 450);

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

        txaResult = new JTextArea();
        scroll = new JScrollPane (txaResult);
        scroll.setMinimumSize(new Dimension(250, 350));
        scroll.setPreferredSize(new Dimension(250, 350));
        //txaResult.setMaximumSize(new Dimension(250, 350));
        pane.add(scroll);

        class MyActionListener implements ActionListener {
            public void actionPerformed(ActionEvent e) {
                Object source = e.getSource();
                if (source.equals(btnOk)) {
                    int attempt = txfAttempt.getValue();
                    txaResult.append(String.format("%d\t", attempt));
                    if (attempt < secret) {
                        txaResult.append(bundle.getString("too_low_message") + '\n');
                    } else if (attempt > secret) {
                        txaResult.append(bundle.getString("too_high_message") + '\n');
                    } else {
                        txaResult.append(bundle.getString("guessed_message") + "\n\n");
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
        txaResult.append(bundle.getString("new_number") + '\n');
        return r;
    }

    public static void main(String[] args) {
        GuessNumber ex = new GuessNumber();
        ex.setVisible(true);
    }
}
