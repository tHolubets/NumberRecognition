import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

public class WebcamWindow extends Frame implements ActionListener{
    Webcam webcam;
    JFrame window;

    public WebcamWindow(Webcam webcam) throws HeadlessException {
        this.webcam = webcam;
    }

    public void startWebcam() {
        webcam = Webcam.getDefault();
        webcam.setViewSize(WebcamResolution.VGA.getSize());

        WebcamPanel panel = new WebcamPanel(webcam);
        panel.setFPSDisplayed(true);
        panel.setLayout(new BorderLayout());
        panel.setDisplayDebugInfo(true);
        panel.setImageSizeDisplayed(true);
        panel.setMirrored(true);

        Button backButoon = new Button("Back");
        backButoon.setFocusable(false);
        backButoon.setBackground(Color.ORANGE);
        backButoon.addActionListener(this);
        panel.add(backButoon, BorderLayout.PAGE_START);

        Button makeFrame = new Button("Record picture");
        makeFrame.setFocusable(false);
        makeFrame.setBackground(Color.ORANGE);
        makeFrame.addActionListener(this);
        panel.add(makeFrame, BorderLayout.PAGE_END);

        window = new JFrame("Webcam");
        window.add(panel);
        window.setResizable(true);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if(actionEvent.getActionCommand().equals("Record picture")) {
            BufferedImage image = webcam.getImage();
            RecognitionWindow panel = new RecognitionWindow();
            panel.setImage(image);
            JFrame window;
            window = new JFrame("Recognition");
            window.setSize(1500, 750);
            window.add(panel);
            window.setLocationRelativeTo(null);
            window.setResizable(false);
            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            window.setVisible(true);
        }
        if(actionEvent.getActionCommand().equals("Back")) {
            webcam.close();
            window.dispose();
        }
    }
}
