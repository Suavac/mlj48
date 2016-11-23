package userInterface;

import classifier.C45;
import classifier.Classifier;
import classifier.ClassifierType;
import decisionTree.Tree;
import driver.Driver;
import driver.PreprocessedData;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;


/**
 * Created by 12100888 on 21/11/2016.
 */
public class GUI {

    JFrame gui;

    //public List<String> discreteAttributes = new ArrayList<String>();
    //public String target;

    public GUI() throws Exception {


        Driver driver = new Driver();

        //1. Create the frame.
        JFrame frame = new JFrame("FrameDemo");

        //2. Optional: What happens when the frame closes?
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        //3. Create components and put them in the frame.
        //...create emptyLabel...
        //frame.getContentPane().add(new String("sd"), BorderLayout.CENTER);


        /**
         * Setup a GridBagLayout
         */

        //Set frame to use a grid bag layout
        frame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();


        /**
         * Create and initialize components
         */


        JButton fileChooserButton = new JButton("Open dataset...");

        JLabel selectedFile = new JLabel();
        selectedFile.setLabelFor(fileChooserButton);


        //JComboBox<String> targetChooser = new JComboBox<>();

        //JLabel targetChooserLabel = new JLabel("Target Attribute:");
        //targetChooserLabel.setLabelFor(targetChooser);

        JTextField trainingSplitPercentageTextField = new JTextField();
        trainingSplitPercentageTextField.setText(Double.toString(driver.getTrainingSplitPercentage()));

        JLabel trainingSplitPercentageLabel = new JLabel("Training Split Percentage:");
        trainingSplitPercentageLabel.setLabelFor(trainingSplitPercentageTextField);

        JTextArea textDisplay = new JTextArea(20,20);
        textDisplay.setEditable(false);
        JScrollPane display = new JScrollPane(textDisplay);

        JButton trainButton = new JButton("Train");
        JButton testButton = new JButton("Test");


        /**
         * Add action listeners
         */

        fileChooserButton.addActionListener(e -> {

            JFileChooser fileChooser = new JFileChooser();

            //Program currently only supports CSV files
            fileChooser.setAcceptAllFileFilterUsed(false);
            FileNameExtensionFilter csvFilter = new FileNameExtensionFilter("csv datasets (*.csv)", "csv");
            fileChooser.setFileFilter(csvFilter);

            int returnValue = fileChooser.showOpenDialog(new JFrame());
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                try {
                    selectedFile.setText(file.getName());

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Not a valid csv file.");
                    return;
                }


            }
        });

        /*//Add action listener that updates the value of target when
        //the user selects a different option from the dropdown
        targetChooser.addActionListener( e -> {
            JComboBox updated = (JComboBox)e.getSource();
            target = (String)updated.getSelectedItem();
        });
        */

        /*trainingSplitPercentageTextField.addActionListener( e -> {
            JTextField updated = (JTextField)e.getSource();
            driver.setTrainingSplitPercentage(updated.getText());

        });*/

        trainButton.addActionListener(e -> {
            try {

                //Check if the split percentage is valid

                int splitPercent = Integer.parseInt(trainingSplitPercentageTextField.getText());
                if ((splitPercent < 100) && (splitPercent > 1)) {
                    driver.setTrainingSplitPercentage((double)splitPercent/100.00);
                }
                else{
                    throw new Exception();
                }

            }
            catch(Exception ex){
                JOptionPane.showMessageDialog(null, "Please enter a value between 1 and 100 for the training/test split percentage.");
                return;
            }

            //Check if the 'number of times to run' is valid

            //TODO

        });

        testButton.addActionListener(e -> {
            double accuracy = driver.test();
            textDisplay.setText("Testing complete.\nAccuracy: "+accuracy);
        });


        /**
         * Display the components.
         *
         * Column 1:
         */

        //Row 1

        gbc.weightx = 1;
        gbc.weighty = 1;

        gbc.gridx = 0;
        gbc.gridy = 0;

        frame.add(fileChooserButton, gbc);

        //Row 2

        gbc.gridy++;

        frame.add(trainingSplitPercentageLabel, gbc);

        //Row 3

        gbc.gridy++;



        //Row 4

        gbc.gridy++;

        frame.add(trainButton, gbc);

        /**
         * Column 2:
         **/

        //Row 1

        gbc.gridx = 1;
        gbc.gridy = 0;

        frame.add(selectedFile, gbc);

        //Row 2

        gbc.gridx = 1;
        gbc.gridy = 1;

        //frame.add(trainingSplitPercentageLabel, gbc);

        //frame.add(targetChooser, gbc);

        //Row 3

        //gbc.gridy++;

        frame.add(trainingSplitPercentageTextField, gbc);

        //Row 4

        gbc.gridy++;

        frame.add(testButton, gbc);


        /**
         * Column 3:
         */

        //Row 1

        gbc.weightx = 10;
        gbc.weighty = 10;

        gbc.gridx++;
        gbc.gridy = 0;

        gbc.gridheight = 6;

        frame.add(display, gbc);

        //4. Size the frame.
        frame.pack();

        //5. Show it.
        frame.setVisible(true);
        this.gui = frame;
    }




}
