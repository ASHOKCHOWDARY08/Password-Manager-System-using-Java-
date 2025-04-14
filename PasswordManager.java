import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

public class PasswordManager {
    private final String FILE_NAME = "credentials.dat";
    private ArrayList<Credential> credentials = new ArrayList<>();
    private DefaultListModel<String> listModel = new DefaultListModel<>();

    public void showMainScreen() {
        JFrame frame = new JFrame("Password Manager");

        JList<String> list = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(list);

        JButton addButton = new JButton("Add");
        JButton deleteButton = new JButton("Delete");

        loadCredentials();

        addButton.addActionListener(e -> {
            JTextField website = new JTextField();
            JTextField username = new JTextField();
            JTextField password = new JTextField();

            Object[] message = {
                    "Website:", website,
                    "Username:", username,
                    "Password:", password
            };

            int option = JOptionPane.showConfirmDialog(frame, message, "Add Credential", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                String encrypted = EncryptionUtil.encrypt(password.getText());
                Credential cred = new Credential(website.getText(), username.getText(), encrypted);
                credentials.add(cred);
                listModel.addElement(cred.getWebsite() + " | " + cred.getUsername());
                saveCredentials();
            }
        });

        deleteButton.addActionListener(e -> {
            int selectedIndex = list.getSelectedIndex();
            if (selectedIndex != -1) {
                credentials.remove(selectedIndex);
                listModel.remove(selectedIndex);
                saveCredentials();
            }
        });

        list.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int i = list.locationToIndex(e.getPoint());
                    Credential cred = credentials.get(i);
                    String decrypted = EncryptionUtil.decrypt(cred.getPassword());
                    JOptionPane.showMessageDialog(frame,
                            "Website: " + cred.getWebsite() + "\nUsername: " + cred.getUsername() + "\nPassword: " + decrypted);
                }
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);

        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private void saveCredentials() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(credentials);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadCredentials() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            credentials = (ArrayList<Credential>) ois.readObject();
            for (Credential cred : credentials) {
                listModel.addElement(cred.getWebsite() + " | " + cred.getUsername());
            }
        } catch (Exception e) {
            // File may not exist yet, ignore
        }
    }
}
