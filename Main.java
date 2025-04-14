import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {

    private static final String MASTER_PASSWORD = "ashok";
    private static List<PasswordEntry> entries = new ArrayList<>();
    private static DefaultTableModel tableModel;

    public static void main(String[] args) {
       
        String inputPassword = JOptionPane.showInputDialog(null, "Enter Master Password:", "Authentication", JOptionPane.PLAIN_MESSAGE);
        if (inputPassword == null || !inputPassword.equals(MASTER_PASSWORD)) {
            JOptionPane.showMessageDialog(null, "Access Denied");
            System.exit(0);
        }

        entries = PasswordStorage.loadEntries();

        
        JFrame frame = new JFrame("🔐 Password Manager");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

        tableModel = new DefaultTableModel(new Object[]{"Name", "Password"}, 0);
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        updateTable(entries);

        JPanel inputPanel = new JPanel(new GridLayout(3, 3));
        JTextField nameField = new JTextField();
        JTextField passwordField = new JTextField();
        JButton generateBtn = new JButton("Generate Password");
        JButton saveBtn = new JButton("Save Password");
        JButton deleteBtn = new JButton("Delete Selected");

        inputPanel.add(new JLabel("Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel(""));

        inputPanel.add(new JLabel("Password:"));
        inputPanel.add(passwordField);
        inputPanel.add(generateBtn);
        inputPanel.add(saveBtn);
        inputPanel.add(deleteBtn);

        // 🔍 Search Panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel searchLabel = new JLabel("🔍");
        JTextField searchField = new JTextField(15);
        JButton searchBtn = new JButton("Search");

        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchBtn);

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        topPanel.add(searchPanel, BorderLayout.NORTH);
        topPanel.add(inputPanel, BorderLayout.SOUTH);

        generateBtn.addActionListener(e -> {
            String generated = generatePassword(12);
            passwordField.setText(generated);
        });

        saveBtn.addActionListener(e -> {
            String name = nameField.getText();
            String password = passwordField.getText();
            if (!name.isEmpty() && !password.isEmpty()) {
                PasswordEntry entry = new PasswordEntry(name, password);
                entries.add(entry);
                PasswordStorage.saveEntry(entry);
                updateTable(entries);
                nameField.setText("");
                passwordField.setText("");
            }
        });

        deleteBtn.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                String name = (String) tableModel.getValueAt(selectedRow, 0);
                String password = (String) tableModel.getValueAt(selectedRow, 1);
                entries.removeIf(entry -> entry.getName().equals(name) && entry.getPassword().equals(password));
                PasswordStorage.saveAll(entries);
                updateTable(entries);
            } else {
                JOptionPane.showMessageDialog(null, "Please select a row to delete.");
            }
        });

        searchBtn.addActionListener(e -> {
            String query = searchField.getText().toLowerCase();
            List<PasswordEntry> filtered = new ArrayList<>();
            for (PasswordEntry entry : entries) {
                if (entry.getName().toLowerCase().contains(query)) {
                    filtered.add(entry);
                }
            }
            updateTable(filtered);
        });

        frame.setLayout(new BorderLayout());
        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private static String generatePassword(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*";
        Random rand = new Random();
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < length; i++) {
            password.append(chars.charAt(rand.nextInt(chars.length())));
        }
        return password.toString();
    }

    private static void updateTable(List<PasswordEntry> data) {
        tableModel.setRowCount(0); // Clear table
        for (PasswordEntry entry : data) {
            tableModel.addRow(new Object[]{entry.getName(), entry.getPassword()});
        }
    }
}
