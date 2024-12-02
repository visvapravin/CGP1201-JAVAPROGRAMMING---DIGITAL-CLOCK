import java.awt.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.*;

public class DigitalClock extends JFrame {
    private JLabel timeLabel;
    private JButton toggleThemeButton;
    private JButton toggleFormatButton;
    private JButton setTimeButton;
    private JComboBox<String> countrySelector;
    private boolean isDarkMode = true;
    private boolean is24HourFormat = false;
    private LocalTime customTime = null; // For user-set time
    private ZoneId selectedZone = ZoneId.systemDefault(); // Default timezone

    public DigitalClock() {
        setTitle("Creative Digital Clock");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Time Label
        timeLabel = new JLabel("", SwingConstants.CENTER);
        timeLabel.setFont(new Font("Orbitron", Font.BOLD, 36));
        add(timeLabel, BorderLayout.CENTER);

        // Panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 2));

        // Theme Toggle Button
        toggleThemeButton = new JButton("Switch to Light Mode");
        toggleThemeButton.addActionListener(e -> toggleTheme());
        buttonPanel.add(toggleThemeButton);

        // Format Toggle Button
        toggleFormatButton = new JButton("Switch to 24-Hour Format");
        toggleFormatButton.addActionListener(e -> toggleTimeFormat());
        buttonPanel.add(toggleFormatButton);

        // Set Time Button
        setTimeButton = new JButton("Set Custom Time");
        setTimeButton.addActionListener(e -> setCustomTime());
        buttonPanel.add(setTimeButton);

        // Country Selector for World Clock
        String[] countries = {"Local Time", "USA (New York)", "UK (London)", "India (Mumbai)", "Australia (Sydney)", "Japan (Tokyo)"};
        countrySelector = new JComboBox<>(countries);
        countrySelector.addActionListener(e -> changeCountryTime());
        buttonPanel.add(countrySelector);

        add(buttonPanel, BorderLayout.SOUTH);

        // Set Dark Theme as Default
        applyDarkTheme();

        // Timer to Update Clock
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateTime();
            }
        }, 0, 1000);
    }

    private void updateTime() {
        LocalTime now = (customTime == null) ? LocalTime.now(selectedZone) : customTime;
        DateTimeFormatter formatter = is24HourFormat
                ? DateTimeFormatter.ofPattern("HH:mm:ss")
                : DateTimeFormatter.ofPattern("hh:mm:ss a");
        timeLabel.setText(now.format(formatter));

        // Increment custom time if set
        if (customTime != null) {
            customTime = customTime.plusSeconds(1);
        }
    }

    private void toggleTheme() {
        isDarkMode = !isDarkMode;
        if (isDarkMode) {
            applyDarkTheme();
            toggleThemeButton.setText("Switch to Light Mode");
        } else {
            applyLightTheme();
            toggleThemeButton.setText("Switch to Dark Mode");
        }
    }

    private void toggleTimeFormat() {
        is24HourFormat = !is24HourFormat;
        if (is24HourFormat) {
            toggleFormatButton.setText("Switch to 12-Hour Format");
        } else {
            toggleFormatButton.setText("Switch to 24-Hour Format");
        }
        updateTime(); // Update time immediately after switching format
    }

    private void setCustomTime() {
        String input = JOptionPane.showInputDialog(this, "Enter custom time (HH:mm:ss):", "Set Custom Time", JOptionPane.PLAIN_MESSAGE);
        if (input != null) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                customTime = LocalTime.parse(input, formatter);
                JOptionPane.showMessageDialog(this, "Custom time set to: " + customTime, "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Invalid time format. Please use HH:mm:ss.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void changeCountryTime() {
        String selectedCountry = (String) countrySelector.getSelectedItem();
        switch (selectedCountry) {
            case "Local Time":
                selectedZone = ZoneId.systemDefault();
                break;
            case "USA (New York)":
                selectedZone = ZoneId.of("America/New_York");
                break;
            case "UK (London)":
                selectedZone = ZoneId.of("Europe/London");
                break;
            case "India (Mumbai)":
                selectedZone = ZoneId.of("Asia/Kolkata");
                break;
            case "Australia (Sydney)":
                selectedZone = ZoneId.of("Australia/Sydney");
                break;
            case "Japan (Tokyo)":
                selectedZone = ZoneId.of("Asia/Tokyo");
                break;
        }
        customTime = null; // Reset custom time when changing country
        JOptionPane.showMessageDialog(this, "Time zone changed to: " + selectedCountry, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void applyDarkTheme() {
        getContentPane().setBackground(Color.BLACK);
        timeLabel.setForeground(Color.WHITE);
        toggleThemeButton.setBackground(Color.DARK_GRAY);
        toggleThemeButton.setForeground(Color.WHITE);
        toggleFormatButton.setBackground(Color.DARK_GRAY);
        toggleFormatButton.setForeground(Color.WHITE);
        setTimeButton.setBackground(Color.DARK_GRAY);
        setTimeButton.setForeground(Color.WHITE);
        countrySelector.setBackground(Color.DARK_GRAY);
        countrySelector.setForeground(Color.WHITE);
    }

    private void applyLightTheme() {
        getContentPane().setBackground(Color.WHITE);
        timeLabel.setForeground(Color.BLACK);
        toggleThemeButton.setBackground(Color.LIGHT_GRAY);
        toggleThemeButton.setForeground(Color.BLACK);
        toggleFormatButton.setBackground(Color.LIGHT_GRAY);
        toggleFormatButton.setForeground(Color.BLACK);
        setTimeButton.setBackground(Color.LIGHT_GRAY);
        setTimeButton.setForeground(Color.BLACK);
        countrySelector.setBackground(Color.LIGHT_GRAY);
        countrySelector.setForeground(Color.BLACK);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DigitalClock clock = new DigitalClock();
            clock.setVisible(true);
        });
    }
}
