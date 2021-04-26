import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageInByteArray {
    private BufferedImage image;
    private int[] bytes;
    private int number;

    public ImageInByteArray(BufferedImage image, int a){
        number = a;
        this.image = image;
        bytes = new int[image.getHeight()*image.getWidth()];
        int width = image.getWidth();
        int height = image.getHeight();
        Color pixel;
        System.out.println(" ");
        for (int i = 0; i < image.getHeight(); i++) {
            for (int j = 0; j < image.getWidth(); j++) {
                bytes[i*width+j] = 0;
                pixel = new Color(image.getRGB(j, i));
                if(pixel.getRed()<255){
                    bytes[i*width+j] = 1;
                }
                System.out.print(bytes[i*width+j]);
            }
            System.out.println(" ");
        }
    }

    public BufferedImage getImage(){
        return image;
    }

    public int getNumber(){
        return number;
    }

    public int[] getBytes(){
        return bytes;
    }
}
