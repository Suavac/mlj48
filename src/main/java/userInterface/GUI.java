package userInterface;

import javax.swing.*;
import java.io.File;

/**
 * Created by 12100888 on 21/11/2016.
 */
public class GUI {

    JFrame gui;

    public GUI(){
        //1. Create the frame.
        JFrame frame = new JFrame("FrameDemo");

        //2. Optional: What happens when the frame closes?
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //3. Create components and put them in the frame.
        //...create emptyLabel...
        //frame.getContentPane().add(new String("sd"), BorderLayout.CENTER);
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(new JFrame()) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            // load from file
        }
        //4. Size the frame.
        frame.pack();

        //5. Show it.
        frame.setVisible(true);
        this.gui = frame;
    }

}
