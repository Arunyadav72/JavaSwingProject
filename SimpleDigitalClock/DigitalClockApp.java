import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;

//Class to represent the digital clock
class Clock{
    private JPanel clockPanel = new JPanel();
    private JLabel[] clockLabels = new JLabel[7]; //Panel to hold the clock display
    private int fontSize[] = {200, 150, 200, 150, 200, 20, 20}; //Labels for each part of the clock
    private final String[] labelText = {"00", ":", "00", ":", "00", "", ""}; //Font sizes for different parts of the clock

    private final int clockMilliSecond = 1000; //Update interval for the clock
    private final Color blackColor = new Color(0, 0, 0);
    private final Color whiteColor = new Color(255, 255, 255);

    //Constructor to initialize the clock
    public Clock(){
        initializePanel(); //Initialize clock panel
        startClock(); //Start the clock
    }

    //Method to initialize the clock panel and labels
    private void initializePanel(){
        clockPanel.setLayout(new GridBagLayout());
        clockPanel.setBackground(blackColor);

        //Create and add labels to the clock panel
        for(int i=0; i<clockLabels.length; i++){
            clockLabels[i] = createLabel(labelText[i], fontSize[i], blackColor, whiteColor);

            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.anchor = GridBagConstraints.CENTER;
            gridBagConstraints.insets = new Insets(4, 4, 4, 4);
            gridBagConstraints.gridx = i;
            gridBagConstraints.gridy = 0;
            if(i == 6){
                gridBagConstraints.gridx = 0;
                gridBagConstraints.gridy = 1;
            }
            clockPanel.add(clockLabels[i], gridBagConstraints);//add label to clockPanel.
        }
    }

    //Method to create a label with given text, font size, background, and foreground color
    private JLabel createLabel(String text, int fontSize, Color background, Color foreground){
        JLabel label = new JLabel(text, JLabel.CENTER);
        label.setBackground(background);
        label.setForeground(foreground);
        label.setFont(new Font("Arial", Font.BOLD, fontSize));
        return label;
    }

    //Method to update clock labels with current time and date
    private void updateClockLabels(int hours, int minutes, int seconds, int date, String month, int year) {
        String amPm = (hours >= 12) ? "PM" : "AM"; //Get AM/PM
        hours = (hours > 12) ? hours - 12 : (hours == 00) ? 12 : hours; //Convert 24-hour format to 12-hour format

        //Update labels with current time and date
        clockLabels[0].setText(String.format("%02d", hours));
        clockLabels[2].setText(String.format("%02d", minutes));
        clockLabels[4].setText(String.format("%02d", seconds));
        clockLabels[5].setText(amPm);
        clockLabels[6].setText(month + " " + date + "," + year);
    }

    //Method to start the clock
    private void startClock(){
        // Start a separate thread for continuous time update
        Thread timeUpdateThread = new Thread(() -> {
            while (true) {
                LocalDateTime dateTime = LocalDateTime.now();
                String month  = dateTime.getMonth().toString().substring(0, 3);
                int date = dateTime.getDayOfMonth();
                int year = dateTime.getYear();
                int hours = dateTime.getHour();
                int minutes = dateTime.getMinute();
                int seconds = dateTime.getSecond();

                //Update clock labels with current time and date
                updateClockLabels(hours, minutes, seconds, date, month, year);

                try {
                    Thread.sleep(clockMilliSecond); //Pause for 1 second (1000 milliseconds)
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        timeUpdateThread.start(); //Start the thread
    }

    // Method to get clock Labels.
    public JLabel[] getClockLabels(){
        return clockLabels;
    }

    //Method to get clock Panel.
    public JPanel getClockPanel(){
        return clockPanel;
    }
}

class LightMode implements ItemListener {
    JPanel lightModePanel = new JPanel(); //Panel to hold the light mode checkbox
    private JCheckBox lightModeCheckBox; //Checkbox to toggle light mode
    private Clock clock; //Reference to the clock

    private Color blackColor = new Color(0, 0, 0);
    private Color whiteColor = new Color(255, 255, 255);

    //Constructor to initialize light mode
    public LightMode(Clock clock){
        this.clock = clock;
        lightModePanel.setBackground(blackColor);
        lightModePanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        lightModeCheckBox = createCheckBox("Light-Mode", blackColor, whiteColor); //Create light mode checkbox
        lightModePanel.add(lightModeCheckBox); //Add checkbox to light mode panel
        lightModeCheckBox.addItemListener(this); //Add item listener to checkbox
    }

    //Method to create a checkbox with given text, background, and foreground color
    private JCheckBox createCheckBox(String checkBoxName, Color background, Color foreground){
        JCheckBox checkBox = new JCheckBox(checkBoxName);
        setColors(checkBox, background, foreground);
        return checkBox;
    }

    //Method to apply styling (colors) to components
    private void setColors(JComponent component, Color background, Color foreground){
        component.setBackground(background);
        component.setForeground(foreground);
    }

    //Method to apply Light Mode.
    private void applyLightMode(Color background, Color foreground){
        //Apply colors to light mode panel and checkbox
        setColors(this.lightModePanel, background, null);
        setColors(this.lightModeCheckBox, background, foreground);

        //apply color to clockPanel.
        setColors(clock.getClockPanel(), background, null);
        
        //apply color to clockLabels.
        for(JLabel labels : clock.getClockLabels()){
            setColors(labels, background, foreground);
        }
    }

    // Listener method for checkbox state change
    @Override
    public void itemStateChanged(ItemEvent e) {
        if(lightModeCheckBox.isSelected()){
            applyLightMode(whiteColor, blackColor); //Apply light mode
        }
        else{
            applyLightMode(blackColor, whiteColor); //Apply dark mode
        }
    }

    //Method to get lightMode Panel.
    public JPanel getLightModePanel(){
        return lightModePanel;
    }
}

//Main class for the digital clock application
public class DigitalClockApp{
    //Method to create and show the GUI
    private static void createAndShowGui(JFrame frame){
        frame.setTitle("Digital Clock"); //Set title of the frame
        frame.setSize(1050, 350); //Set size for frame
        frame.setLocationRelativeTo(null); //Set center the frame on the screen
        frame.setLayout(new BorderLayout()); //Set layout for frame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Set close operation for frame
    }

    public static void main(String args[]){
        JFrame frame = new JFrame();
        SwingUtilities.invokeLater(new Runnable(){
            public void run(){
                createAndShowGui(frame); //Invoked Create and show the GUI method

                Clock clock = new Clock(); //Create a new Clock instance
                frame.add(clock.getClockPanel(), BorderLayout.CENTER); //Add clock panel to the frame

                LightMode lightMode = new LightMode(clock); //Create a new LightMode instance
                frame.add(lightMode.getLightModePanel(), BorderLayout.EAST); //Add light mode panel to the frame

                frame.setVisible(true); //Set frame visibility to true
            }
        });
    }
}