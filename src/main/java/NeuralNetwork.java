import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;

public class NeuralNetwork {
    private int numberDiffElements = 10;
    private int picHeight = 60;
    private int picWidth = 40;
    private double weigthStart = 0.0004;
    private double alpha = 1;
    private double newN = 0.01;
    private ArrayList<ImageInByteArray> imagesInByteArrays = new ArrayList();
    private ArrayList<ArrayList<Double>> weights = new ArrayList<>();
    private ArrayList<Double> neuralElements = new ArrayList<>();
    private ArrayList<Double> outs = new ArrayList<>();
    private ArrayList<Double> errors = new ArrayList<>();
    private String weightFileName = "weights.txt";

    public NeuralNetwork() {
        initialization();
    }

    public void setDataForLearning(ArrayList<ImageInByteArray> imageInByteArrays, int numberOfIterations) {
        this.imagesInByteArrays = imageInByteArrays;
        learn(numberOfIterations);
        writeWeights();
    }


    public void initialization() {
        int weigthsNumber = picHeight * picWidth;
        for (int k = 0; k < numberDiffElements; k++) {
            ArrayList<Double> weigthsOne = new ArrayList<>();
            for (int i = 0; i < weigthsNumber; i++) {
                weigthsOne.add(weigthStart);
            }
            weights.add(weigthsOne);
        }
        for (int i = 0; i < numberDiffElements; i++) {
            neuralElements.add(0.0);
            outs.add(0.0);
            errors.add(0.0);
        }
    }

    public void learn(int numberOfIterations) {
        int index=0;
        for (int i = 0; i < numberOfIterations; i++) {
            for (int k = 0; k < numberDiffElements; k++) {
                for (int j = 0; j < 10; j++) {
                    index++;
                    learnOneCycle(k);
                    System.out.println(index + ".HE \tout \terror (number = " + k+")");
                    for (int m = 0; m < numberDiffElements; m++) {
                        System.out.println(neuralElements.get(m) + " ___ " + outs.get(m) + " ___ " + errors.get(m));
                    }
                }
            }
        }
    }

    private void learnOneCycle(int numberOfExample) {
        for (int i = 0; i < numberDiffElements; i++) {
            neuralElements.set(i, 0.0);
        }
        int[] imageBytes = imagesInByteArrays.get(numberOfExample).getBytes();
        for (int i = 0; i < imageBytes.length; i++) {
            for (int j = 0; j < numberDiffElements; j++) {
                neuralElements.set(j, neuralElements.get(j) + (imageBytes[i] * weights.get(j).get(i)));
            }
        }
        for (int i = 0; i < numberDiffElements; i++) {
            double out = 1 / (1 + Math.exp((-alpha) * neuralElements.get(i)));
            outs.set(i, out);
            double error = -outs.get(i);
            if (i == imagesInByteArrays.get(numberOfExample).getNumber()) {
                error = 1 - outs.get(i);
            }
            errors.set(i, error);
        }
        for (int i = 0; i < numberDiffElements; i++) {
            double deltaWeight = errors.get(i) * outs.get(i) * newN;
            for (int j = 0; j < weights.get(0).size(); j++) {
                if (deltaWeight > 0 && imageBytes[j] > 0) {
                    weights.get(i).set(j, weights.get(i).get(j) + deltaWeight*10);
                } else {
                    weights.get(i).set(j, weights.get(i).get(j) + deltaWeight);
                }
            }
        }
    }

    public void writeWeights() {
        PrintWriter out = null;
        try {
            out = new PrintWriter(new FileOutputStream(weightFileName));
            for (int i = 0; i < weights.size(); i++) {
                for (int j = 0; j < weights.get(0).size(); j++) {
                    out.println(weights.get(i).get(j));
                }
            }
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void readWeights() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(weightFileName));
            int weigthsNumber = picHeight * picWidth;
            for (int k = 0; k < numberDiffElements; k++) {
                ArrayList<Double> weigthsOne = new ArrayList<>();
                for (int i = 0; i < weigthsNumber; i++) {
                    weigthsOne.add(Double.parseDouble(bufferedReader.readLine()));
                }
                weights.set(k, weigthsOne);
            }
            bufferedReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public double[] determineNumber(BufferedImage bufferedImage){
        int[] imageBytes = new int[bufferedImage.getHeight()*bufferedImage.getWidth()];
        int width = bufferedImage.getWidth();
        Color pixel;
        for (int i = 0; i < bufferedImage.getHeight(); i++) {
            for (int j = 0; j < bufferedImage.getWidth(); j++) {
                imageBytes[i*width+j] = 0;
                pixel = new Color(bufferedImage.getRGB(j, i));
                if(pixel.getRed()<255){
                    imageBytes[i*width+j] = 1;
                }
            }
        }
        for (int i = 0; i < numberDiffElements; i++) {
            neuralElements.set(i, 0.0);
        }
        for (int i = 0; i < imageBytes.length; i++) {
            for (int j = 0; j < numberDiffElements; j++) {
                neuralElements.set(j, neuralElements.get(j) + (imageBytes[i] * weights.get(j).get(i)));
            }
        }
        for (int i = 0; i < numberDiffElements; i++) {
            double out = 1 / (1 + Math.exp((-alpha) * neuralElements.get(i)));
            outs.set(i, out);
        }
        int maxNumber = 999;
        double maxProb = 0;
        for (int i = 0; i < numberDiffElements; i++) {
            if(outs.get(i)>maxProb){
                maxProb = outs.get(i);
                maxNumber = i;
            }
        }
        double[] mas = {maxNumber, maxProb};
        if(maxProb<0.3){
            mas[0] = 999;
        }
        return mas;
    }
}

