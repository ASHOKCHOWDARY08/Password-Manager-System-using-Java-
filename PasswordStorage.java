import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PasswordStorage {
    private static final String FILE_NAME = "passwords.txt";

    public static void saveEntry(PasswordEntry entry) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME, true))) {
            writer.println(entry.getName() + ":" + entry.getPassword());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<PasswordEntry> loadEntries() {
        List<PasswordEntry> entries = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":", 2);
                if (parts.length == 2) {
                    entries.add(new PasswordEntry(parts[0], parts[1]));
                }
            }
        } catch (IOException e) {
          
        }
        return entries;
    }

    public static void saveAll(List<PasswordEntry> entries) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (PasswordEntry entry : entries) {
                writer.println(entry.getName() + ":" + entry.getPassword());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
