import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

public class RecognitionWindow extends JPanel implements ActionListener {
    private int xImg, yImg, wImg, hImg;
    private ArrayList<Rectangle2D> rect = new ArrayList<>();
    private ArrayList<String> definition = new ArrayList<>();
    private int objectCounter = 0;
    private int redIndex = 30;
    private int redDiff = 1;
    private double zoom = 1;
    private int objectCutIndex = 0;
    private String outputImagePath = "./numberRecognition/records/outputs/.out";
    private boolean longDescription = true;

    BufferedImage image = getImageFromTxt();
    BufferedImage image1Copy = getImageFromTxt();
    BufferedImage image3 = getImageFromTxt();
    BufferedImage image4 = getImageFromTxt();
    BufferedImage image2 = getImageFromTxt();
    JButton medianFilt = new JButton("Median filter");
    JButton binary = new JButton("Binarization");
    JButton borderFilter = new JButton("Edge detection filter");
    JButton findObjects = new JButton("Find objects");
    JButton cutObjects = new JButton("Select objects");
    JButton scaleImage = new JButton("Scope");
    JButton goooButton = new JButton("Get result");
    JFileChooser chooser;
    BufferedImage imageForRecursion = image2;

    ArrayList<BufferedImage> imageOfNumber = new ArrayList<>();

    public RecognitionWindow() {
        imageOfNumber = getImageIdeal(10);

        setLayout(null);
        setSize(2000, 2000);
        chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File("."));

        medianFilt.setFocusable(false);
        medianFilt.setBounds(50, 550, 200, 30);
        medianFilt.setBackground(Color.GREEN);
        medianFilt.addActionListener(this);
        add(medianFilt);

        binary.setFocusable(false);
        binary.setBounds(50, 590, 200, 30);
        binary.setBackground(Color.GREEN);
        binary.addActionListener(this);
        add(binary);

        borderFilter.setFocusable(false);
        borderFilter.setBounds(50, 630, 200, 30);
        borderFilter.setBackground(Color.GREEN);
        borderFilter.addActionListener(this);
        add(borderFilter);

        findObjects.setFocusable(false);
        findObjects.setBounds(300, 550, 200, 30);
        findObjects.setBackground(Color.GREEN);
        findObjects.addActionListener(this);
        add(findObjects);

        cutObjects.setFocusable(false);
        cutObjects.setBounds(300, 590, 200, 30);
        cutObjects.setBackground(Color.GREEN);
        cutObjects.addActionListener(this);
        add(cutObjects);

        goooButton.setFocusable(false);
        goooButton.setBounds(300, 630, 200, 30);
        goooButton.setBackground(Color.ORANGE);
        goooButton.addActionListener(this);
        add(goooButton);

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        if (image != null) {
            g2.drawImage(image, 20, 50, null);
            g2.drawImage(image2, 820, 50, 820 + 640, 50 + 480, xImg, yImg, wImg, hImg, null);
            System.out.println(xImg + " " + yImg + " " + wImg + " " + hImg);
        }
    }

    protected void paintComponent2(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 20, 50, null);
        g.drawImage(image2, 820, 50, null);
        medianFilt.setVisible(false);
        binary.setVisible(false);
        borderFilter.setVisible(false);
        findObjects.setVisible(false);
        cutObjects.setVisible(false);
        scaleImage.setVisible(false);
        goooButton.setVisible(false);
        medianFilt.setVisible(true);
        binary.setVisible(true);
        borderFilter.setVisible(true);
        findObjects.setVisible(true);
        cutObjects.setVisible(true);
        scaleImage.setVisible(true);
        goooButton.setVisible(true);
    }

    protected void paintComponent3(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 20, 50, null);
        g.drawImage(image2, 820, 50, null);
        g.drawImage(image3, 720, 230, null);
        medianFilt.setVisible(false);
        binary.setVisible(false);
        borderFilter.setVisible(false);
        findObjects.setVisible(false);
        cutObjects.setVisible(false);
        scaleImage.setVisible(false);
        goooButton.setVisible(false);
        medianFilt.setVisible(true);
        binary.setVisible(true);
        borderFilter.setVisible(true);
        findObjects.setVisible(true);
        cutObjects.setVisible(true);
        scaleImage.setVisible(true);
        goooButton.setVisible(true);
        for (int i = 0; i < rect.size(); i++) {
            g.drawRect((int) rect.get(i).getX(), (int) rect.get(i).getY(), (int) rect.get(i).getWidth(), (int) rect.get(i).getHeight());
            g.drawString(definition.get(i), (int) rect.get(i).getX(), (int) rect.get(i).getY() - 2);
        }
    }

    public void setImage(String path) {
        try {
            image = ImageIO.read(new File(path));
            image2 = copyImage(image);
            image3 = copyImage(image);
            image4 = copyImage(image2);
            image1Copy = image;
            zoom = 1;
            xImg = 0;
            yImg = 0;
            wImg = image.getWidth(RecognitionWindow.this);
            hImg = image.getHeight(RecognitionWindow.this);
            repaint();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setImage(BufferedImage imageForImport) {
        image = copyImage(imageForImport);
        image2 = copyImage(image);
        image3 = copyImage(image);
        image4 = copyImage(image2);
        image1Copy = image;
        zoom = 1;
        xImg = 0;
        yImg = 0;
        wImg = image.getWidth(RecognitionWindow.this);
        hImg = image.getHeight(RecognitionWindow.this);
        repaint();
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (actionEvent.getActionCommand().equals("Get result")) {
            image2 = medianFilter(image2);
            image2 = binarization(image2);
            image2 = borderFilters(image2);
            image2 = findAllObjects(image2);
            image2 = deleteSmallObjects(image2);
            paintComponent2(this.getGraphics());
            for (int i = 0; i < objectCounter; i++) {
                image3 = cutObject(image2, redIndex + i * redDiff);
            }
            paintComponent3(this.getGraphics());
        }
        if (actionEvent.getActionCommand().equals("Median filter")) {
            image2 = medianFilter(image2);
            paintComponent2(this.getGraphics());
        }
        if (actionEvent.getActionCommand().equals("Binarization")) {
            image2 = binarization(image2);
            paintComponent2(this.getGraphics());
        }
        if (actionEvent.getActionCommand().equals("Edge detection filter")) {
            image2 = borderFilters(image2);
            paintComponent2(this.getGraphics());
        }
        if (actionEvent.getActionCommand().equals("Find objects")) {
            image2 = findAllObjects(image2);
            paintComponent2(this.getGraphics());
        }
        if (actionEvent.getActionCommand().equals("Select objects")) {
            image3 = cutObject(image2, redIndex + objectCutIndex * redDiff);
            paintComponent3(this.getGraphics());
            objectCutIndex++;
            if (objectCutIndex >= objectCounter) {
                objectCutIndex = 0;
            }
        }
    }


    private BufferedImage getImageFromTxt() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("./numberRecognition/records/record1.txt"));
            int width = Integer.parseInt(bufferedReader.readLine());
            int height = Integer.parseInt(bufferedReader.readLine());
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int red = Integer.parseInt(bufferedReader.readLine());
                    int green = Integer.parseInt(bufferedReader.readLine());
                    int blue = Integer.parseInt(bufferedReader.readLine());
                    int rgb = red;
                    rgb = (rgb << 8) + green;
                    rgb = (rgb << 8) + blue;
                    image.setRGB(x, y, rgb);
                }
            }
            return image;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static GraphicsConfiguration getDefaultConfiguration() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        return gd.getDefaultConfiguration();
    }

    public BufferedImage medianFilter(BufferedImage image) {
        Color[] pixel = new Color[9];
        int[] R = new int[9];
        int[] B = new int[9];
        int[] G = new int[9];
        File output = new File("./records/output.png");
        BufferedImage img = copyImage(image);
        for (int i = 1; i < img.getWidth() - 1; i++)
            for (int j = 1; j < img.getHeight() - 1; j++) {
                pixel[0] = new Color(img.getRGB(i - 1, j - 1));
                pixel[1] = new Color(img.getRGB(i - 1, j));
                pixel[2] = new Color(img.getRGB(i - 1, j + 1));
                pixel[3] = new Color(img.getRGB(i, j + 1));
                pixel[4] = new Color(img.getRGB(i + 1, j + 1));
                pixel[5] = new Color(img.getRGB(i + 1, j));
                pixel[6] = new Color(img.getRGB(i + 1, j - 1));
                pixel[7] = new Color(img.getRGB(i, j - 1));
                pixel[8] = new Color(img.getRGB(i, j));
                for (int k = 0; k < 9; k++) {
                    R[k] = pixel[k].getRed();
                    B[k] = pixel[k].getBlue();
                    G[k] = pixel[k].getGreen();
                }
                Arrays.sort(R);
                Arrays.sort(B);
                Arrays.sort(G);
                img.setRGB(i, j, new Color(R[4], G[4], B[4]).getRGB());
            }
        try {
            ImageIO.write(img, "png", output);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return img;
    }

    public BufferedImage binarization(BufferedImage image) {
        Color pixel;
        BufferedImage img = copyImage(image);
        for (int i = 0; i < img.getWidth(); i++)
            for (int j = 0; j < img.getHeight(); j++) {
                pixel = new Color(img.getRGB(i, j));
                int index = pixel.getRed() + pixel.getGreen() + pixel.getBlue();
                index /= 3;
                if (index <= 108) {
                    img.setRGB(i, j, new Color(0, 0, 0).getRGB());
                } else {
                    img.setRGB(i, j, new Color(255, 255, 255).getRGB());
                }
            }
        return img;
    }

    public BufferedImage cutObject(BufferedImage image, int colorIndex) {
        Color pixel;
        BufferedImage img = copyImage(image);
        int right = 640;
        int left = 0;
        int up = 480;
        int down = 0;
        for (int i = 0; i < img.getWidth(); i++) {
            for (int j = 0; j < img.getHeight(); j++) {
                pixel = new Color(img.getRGB(i, j));
                int index = pixel.getRed();
                if (index == colorIndex) {
                    if (j < up) {
                        up = j;
                    }
                    if (j > down) {
                        down = j;
                    }
                    if (i < right) {
                        right = i;
                    }
                    if (i > left) {
                        left = i;
                    }
                }
            }
        }
        System.out.println(right);
        System.out.println(left);
        System.out.println(up);
        System.out.println(down);
        if ((right == 640 || left == 0) || (up == 480 || down == 0)) {
            return null;
        }
        if (left > 640 || down > 480) {
            return null;
        }
        Rectangle2D rectNew = new Rectangle2D.Double(right + 20, up + 50, (left - right + 1), (down - up + 1));
        rect.add(rectNew);
        image3 = copyImage(image);
        image3 = image3.getSubimage(right, up, (left - right + 1), (down - up + 1));
        image3 = resizeImage(image3, 1);
        try {
            ImageIO.write(image3, "png", new File(outputImagePath + ((colorIndex - 30) / redDiff) + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        NeuralNetwork neuralNetwork = new NeuralNetwork();
        neuralNetwork.readWeights();
        double[] mas = neuralNetwork.determineNumber(image3);
        String textImageDefinition = ((int)(mas[0]+0.5)) + "";
        if(longDescription && (int)(mas[0]+0.5)<999) {
            textImageDefinition += " [" + ((int) (mas[1] * 1000) / 10.0) + "]";
        }
        if((int)(mas[0]+0.5)==999){
            textImageDefinition = "?";
        }
        definition.add(textImageDefinition);
        return image3;
    }

    public BufferedImage findAllObjects(BufferedImage image) {
        objectCounter = 0;
        imageForRecursion = copyImage(image);
        int redIndexLocal = redIndex;
        for (int i = 0; i < imageForRecursion.getWidth(); i++)
            for (int j = 0; j < imageForRecursion.getHeight(); j++) {
                Color pixel = new Color(imageForRecursion.getRGB(i, j));
                int index = pixel.getRed();
                if (index == 0) {
                    objectCounter++;
                    imageForRecursion.setRGB(i, j, new Color(redIndexLocal, 0, 0).getRGB());
                    System.out.println(i + " " + j);
                    findEndOfObject(i, j, redIndexLocal);
                    redIndexLocal = redIndexLocal + redDiff;
                    if (redIndexLocal > 230) {
                        redIndexLocal = redIndex;
                    }
                }
            }
        return imageForRecursion;
    }

    public BufferedImage deleteSmallObjects(BufferedImage image) {
        BufferedImage img = copyImage(image);
        int redIndex = this.redIndex;
        for (int k = 0; k < objectCounter; k++) {
            int firstI = 0;
            int firstJ = 0;
            int pixelCounter = 0;
            for (int i = 0; i < img.getWidth(); i++) {
                for (int j = 0; j < img.getHeight(); j++) {
                    Color pixel = new Color(img.getRGB(i, j));
                    int index = pixel.getRed();
                    if (index == redIndex) {
                        pixelCounter++;
                        if (firstI == 0) {
                            firstI = i;
                            firstJ = j;
                        }
                    }
                }
            }
            if (pixelCounter < 100) {
                for (int i = 0; i < img.getWidth(); i++) {
                    for (int j = 0; j < img.getHeight(); j++) {
                        Color pixel = new Color(img.getRGB(i, j));
                        int index = pixel.getRed();
                        if (index == redIndex) {
                            System.out.println("\n\n\n" + i + " " + j);
                            img.setRGB(i, j, new Color(255, 255, 255).getRGB());
                        }
                    }
                }
            }
            redIndex = redIndex + redDiff;
            if (redIndex > 230) {
                redIndex = this.redIndex;
            }
        }
        return img;
    }

    public BufferedImage borderFilters(BufferedImage image) {
        Color pixel;
        imageForRecursion = copyImage(image);
        int k = 0;
        for (int j = 0; j < imageForRecursion.getHeight(); j++) {
            pixel = new Color(imageForRecursion.getRGB(k, j));
            int index = pixel.getRed();
            if (index == 0) {
                imageForRecursion.setRGB(k, j, new Color(255, 255, 255).getRGB());
                System.out.println(k + " " + j);
                findEndOfBorder(k, j);
            }
        }
        k = imageForRecursion.getWidth() - 1;
        for (int j = 0; j < imageForRecursion.getHeight(); j++) {
            pixel = new Color(imageForRecursion.getRGB(k, j));
            int index = pixel.getRed();
            if (index == 0) {
                imageForRecursion.setRGB(k, j, new Color(255, 255, 255).getRGB());
                System.out.println(k + " " + j);
                findEndOfBorder(k, j);
            }
        }
        int l = 0;
        for (int i = 0; i < imageForRecursion.getWidth(); i++) {
            pixel = new Color(imageForRecursion.getRGB(i, l));
            int index = pixel.getRed();
            if (index == 0) {
                imageForRecursion.setRGB(i, l, new Color(255, 255, 255).getRGB());
                System.out.println(i + " " + l);
                findEndOfBorder(i, l);
            }
        }
        l = imageForRecursion.getHeight() - 1;
        for (int i = 0; i < imageForRecursion.getWidth(); i++) {
            pixel = new Color(imageForRecursion.getRGB(i, l));
            int index = pixel.getRed();
            if (index == 0) {
                imageForRecursion.setRGB(i, l, new Color(255, 255, 255).getRGB());
                System.out.println(i + " " + l);
                findEndOfBorder(i, l);
            }
        }
        return imageForRecursion;
    }

    public void findEndOfObject(int i, int j, int redIndex) {
        LinkedList<Integer> queue = new LinkedList<>();
        queue.offer(i * 1000 + j);
        while (queue.size() > 0) {
            int element = queue.poll();
            for (int k = (element / 1000 - 1); k <= (element / 1000 + 1); k++) {
                for (int l = (element % 1000 - 1); l <= (element % 1000 + 1); l++) {
                    try {
                        Color pixel = new Color(imageForRecursion.getRGB(k, l));
                        int index = pixel.getRed();
                        if (index == 0) {
                            imageForRecursion.setRGB(k, l, new Color(redIndex, 0, 0).getRGB());
                            queue.add(k * 1000 + l);
                        }
                    } catch (ArrayIndexOutOfBoundsException ex) {
                        continue;
                    }
                }
            }
        }
        return;
    }

    public void findEndOfBorder(int i, int j) {
        LinkedList<Integer> queue = new LinkedList<>();
        queue.offer(i * 1000 + j);
        while (queue.size() > 0) {
            int element = queue.poll();
            for (int k = (element / 1000 - 1); k <= (element / 1000 + 1); k++) {
                for (int l = (element % 1000 - 1); l <= (element % 1000 + 1); l++) {
                    try {
                        Color pixel = new Color(imageForRecursion.getRGB(k, l));
                        int index = pixel.getRed();
                        if (index == 0) {
                            imageForRecursion.setRGB(k, l, new Color(255, 255, 255).getRGB());
                            queue.add(k * 1000 + l);
                        }
                    } catch (ArrayIndexOutOfBoundsException ex) {
                        continue;
                    }
                }
            }
        }
        return;
    }

    public static BufferedImage copyImage(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(bi.getRaster().createCompatibleWritableRaster());
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }

    private static BufferedImage resizeImage(BufferedImage originalImage, int type) {
        BufferedImage resizedImage = new BufferedImage(40, 60, type);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, 40, 60, null);
        g.dispose();

        return resizedImage;
    }

    private ArrayList<BufferedImage> getImageIdeal(int i){
        ArrayList<BufferedImage> images= new ArrayList<>();
        BufferedImage image = null;
        for (int j = 0; j < i; j++) {
            try {
                String s = "./records/idealExamples/"+j+".png";
                image = ImageIO.read(new File("./records/idealExamples/"+j+".png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            images.add(image);
        }
        return images;
    }
}
