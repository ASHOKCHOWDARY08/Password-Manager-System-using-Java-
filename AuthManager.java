import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.security.MessageDigest;

public class AuthManager {
    private final String MASTER_PASSWORD_HASH = "e3afed0047b08059d0fada10f400c1e5"; 

    public void showLoginScreen() {
        JFrame frame = new JFrame("Login");
        JLabel label = new JLabel("Enter Master Password:");
        JPasswordField passwordField = new JPasswordField(15);
        JButton loginButton = new JButton("Login");

        loginButton.addActionListener(e -> {
            String input = new String(passwordField.getPassword());
            if (hash(input).equals(MASTER_PASSWORD_HASH)) {
                frame.dispose();
                new PasswordManager().showMainScreen();
            } else {
                JOptionPane.showMessageDialog(frame, "Wrong Password");
            }
        });

        JPanel panel = new JPanel();
        panel.add(label);
        panel.add(passwordField);
        panel.add(loginButton);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(panel, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
    }

    private String hash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(input.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            return null;
        }
    }
}
