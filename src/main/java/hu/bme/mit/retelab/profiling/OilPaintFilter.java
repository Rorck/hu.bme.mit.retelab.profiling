package hu.bme.mit.retelab.profiling;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.IOException;

public class OilPaintFilter {
    private int radius;

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        if (radius < 1) {
            this.radius = 1;
        } else {
            this.radius = radius;
        }
    }

    public OilPaintFilter(int radius) {
        this.setRadius(radius);
    }

    public BufferedImage apply(BufferedImage image) throws IOException {
        BufferedImage original = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        Graphics2D g = original.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();

        WritableRaster raster = image.getRaster();
        DataBufferByte dataBuffer = (DataBufferByte) raster.getDataBuffer();
        byte[] data = dataBuffer.getData();

        WritableRaster originalRaster = original.getRaster();
        DataBufferByte originalDataBuffer = (DataBufferByte) originalRaster.getDataBuffer();
        byte[] originalData = originalDataBuffer.getData();
    	
//        byte[] data = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
//        byte[] originalData = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        int width = image.getWidth();
        int height = image.getHeight();
        int pixelLength = 3;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int[] intensityCount = new int[256];
                int[] redSum = new int[256];
                int[] greenSum = new int[256];
                int[] blueSum = new int[256];

                for (int row = -radius; row <= radius; row++) {
                    int offsetY = y + row;
                    if (offsetY >= 0 && offsetY < height) {
                        for (int col = -radius; col <= radius; col++) {
                            int offsetX = x + col;
                            if (offsetX >= 0 && offsetX < width) {
                                int index = (offsetY * width + offsetX) * pixelLength;
                                int blue = data[index] & 0xff;
                                int green = data[index + 1] & 0xff;
                                int red = data[index + 2] & 0xff;

                                int intensity = (red + green + blue) / 3;
                                intensityCount[intensity]++;
                                redSum[intensity] += red;
                                greenSum[intensity] += green;
                                blueSum[intensity] += blue;
                            }
                        }
                    }
                }

                int maxCount = 0;
                int maxIntensity = 0;
                for (int i = 0; i < 256; i++) {
                    if (intensityCount[i] > maxCount) {
                        maxCount = intensityCount[i];
                        maxIntensity = i;
                    }
                }

                int index = (y * width + x) * pixelLength;
                originalData[index] = (byte) (blueSum[maxIntensity] / maxCount);
                originalData[index + 1] = (byte) (greenSum[maxIntensity] / maxCount);
                originalData[index + 2] = (byte) (redSum[maxIntensity] / maxCount);
            }
        }
        
        Raster rasterOriginal = original.getRaster();
        WritableRaster rasterImage = image.getRaster();
        rasterImage.setDataElements(0, 0, rasterOriginal);

        return image;
    }
}
