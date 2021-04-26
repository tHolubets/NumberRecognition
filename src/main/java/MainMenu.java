import com.github.sarxos.webcam.Webcam;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;

public class MainMenu extends JPanel implements ActionListener {
    JButton numberRecognitionFromFileButton = new JButton("Recognition (image from file)");
    JButton numberRecognitionFromCamButton = new JButton("Recognition (image from webcam)");
    JButton learnNeuralNetworkButton = new JButton("Start neural network learning");
    JTextField learnCycleNumberField = new JTextField();
    JFileChooser chooser;

    ArrayList<ImageInByteArray> imagesIdealForLearnimg = getImageIdealForLearning(10);

    public MainMenu() {
        setLayout(null);
        setSize(1000, 1000);
        chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File("."));


        numberRecognitionFromFileButton.setFocusable(false);
        numberRecognitionFromFileButton.setBounds(25, 50, 500, 50);
        numberRecognitionFromFileButton.setBackground(Color.ORANGE);
        numberRecognitionFromFileButton.addActionListener(this);
        numberRecognitionFromFileButton.setEnabled(true);
        add(numberRecognitionFromFileButton);

        numberRecognitionFromCamButton.setFocusable(false);
        numberRecognitionFromCamButton.setBounds(25, 120, 500, 50);
        numberRecognitionFromCamButton.setBackground(Color.ORANGE);
        numberRecognitionFromCamButton.addActionListener(this);
        numberRecognitionFromCamButton.setEnabled(true);
        add(numberRecognitionFromCamButton);

        learnCycleNumberField.setBounds(140, 200, 300, 50);
        learnCycleNumberField.setBackground(Color.ORANGE);
        learnCycleNumberField.setEnabled(true);
        add(learnCycleNumberField);

        learnNeuralNetworkButton.setFocusable(false);
        learnNeuralNetworkButton.setBounds(140, 270, 300, 50);
        learnNeuralNetworkButton.setBackground(Color.ORANGE);
        learnNeuralNetworkButton.addActionListener(this);
        learnNeuralNetworkButton.setEnabled(true);
        add(learnNeuralNetworkButton);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (actionEvent.getActionCommand().equals("Recognition (image from file)")) {
            chooser.setCurrentDirectory(new File("./records/"));
            int result = chooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                RecognitionWindow panel = new RecognitionWindow();
                panel.setImage(chooser.getSelectedFile().getPath());
                panel.setImage(chooser.getSelectedFile().getPath());
                JFrame window;
                window = new JFrame("PicEditor");
                window.setSize(1500, 750);
                window.add(panel);
                window.setLocationRelativeTo(null);
                window.setResizable(false);
                window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                window.setVisible(true);
            }
        }

        if (actionEvent.getActionCommand().equals("Recognition (image from webcam)")) {
            Webcam webcam = Webcam.getDefault();
            WebcamWindow webcamWindow = new WebcamWindow(webcam);
            webcamWindow.startWebcam();
        }

        if (actionEvent.getActionCommand().equals("Start neural network learning")) {
            NeuralNetwork neuralNetwork = new NeuralNetwork();
            int numberOfIterations = 10;
            if(!learnCycleNumberField.getText().equals("")){
                numberOfIterations = Integer.parseInt(learnCycleNumberField.getText());
            }
            neuralNetwork.setDataForLearning(imagesIdealForLearnimg, numberOfIterations);
        }
    }

    private static ArrayList<ImageInByteArray> getImageIdealForLearning(int i){
        ArrayList<ImageInByteArray> images= new ArrayList<>();
        BufferedImage image = null;
        for (int j = 0; j < i; j++) {
            try {
                String s = "./records/idealExamples/"+j+".png";
                image = ImageIO.read(new File("./records/idealExamples/"+j+".png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            images.add(new ImageInByteArray(image, j));
        }
        return images;
    }
}
