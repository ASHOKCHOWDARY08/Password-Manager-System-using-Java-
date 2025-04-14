public class PasswordEntry {
    private String name;
    private String password;

    public PasswordEntry(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return name + ":" + password;
    }

    public static PasswordEntry fromString(String line) {
        String[] parts = line.split(":", 2);
        if (parts.length == 2) {
            return new PasswordEntry(parts[0], parts[1]);
        }
        return null;
    }
}
