import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageToASCII {
    public static final String palette = "#8%?*:. ";
    public static final String imageLocation = "C:/Users/karnavski/IdeaProjects/ImageToASCII/src/Image.png";
    public static final int squeeze = 2;

    public static void main(String[] args) throws IOException {
        File imageFile = new File(imageLocation);
        BufferedImage image = ImageIO.read(imageFile);

        System.out.println(ConvertImageToASCII(image));
    }

    public static int[] parseRGB(int color) {
        String hex = Integer.toHexString(color);
        int[] rgbColor = new int[3];

        for (int i = 0; i < 3; i++) {
            String hexColor = hex.substring((i + 1) * 2, (i + 1) * 2 + 2);
            rgbColor[i] = Integer.decode("0x" + hexColor);
        }

        return rgbColor;
    }

    public static float getBrightness(int[] color) {
        float r = 0;
        for (int i = 0; i < 3; i++) {
            r += color[i];
        }
        r /= 255 * 3;
        return r;
    }

    public static float getBrightnessAt(BufferedImage image, int x, int y) {
        return getBrightness(parseRGB(image.getRGB(x, y)));
    }

    public static float[][] getBrightnessMap(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        float[][] map = new float[width / squeeze][height / squeeze];

        for (int x = 0; x < width / squeeze; x ++) {
            for (int y = 0; y < height / squeeze; y ++) {
                map[x][y] = getBrightnessAt(image, x * squeeze, y * squeeze);
            }
        }

        return map;
    }

    public static char getPaletteChar(float value) {
        return palette.charAt(Math.round((value * (palette.length() - 1))));
    }

    public static String BrightnessMapToASCII(float[][] map) {
        StringBuilder output = new StringBuilder();
        for (int x = 0; x < map[0].length; x++) {
            for (float[] floats : map) {
                output.append(getPaletteChar(floats[x]));
            }
            output.append("\n");
        }
        return output.toString();
    }

    public static String ConvertImageToASCII(BufferedImage image) {
        return BrightnessMapToASCII(getBrightnessMap(image));
    }
}
