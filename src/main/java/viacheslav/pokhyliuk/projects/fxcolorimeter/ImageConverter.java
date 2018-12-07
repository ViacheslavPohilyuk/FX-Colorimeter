package viacheslav.pokhyliuk.projects.fxcolorimeter;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import javax.imageio.stream.MemoryCacheImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

class ImageConverter {
    ImageConverter() {
        ImageIO.setUseCache(false);
    }

    byte[] toPNG(Image image) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int width = image.getWidth(null);
        int height = image.getHeight(null);
        BufferedImage bi = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = bi.createGraphics();
        g.setComposite(AlphaComposite.Src);
        g.drawImage(image, 0, 0, null);
        g.dispose();
        ImageOutputStream ios = new MemoryCacheImageOutputStream(out);
        try {
            if (!ImageIO.write(bi, "PNG", ios)) {
                throw new IOException("ImageIO.write failed");
            }
            ios.close();
        } catch (IOException ex) {
            throw new RuntimeException("saveImage: " + ex.getMessage());
        }
        return out.toByteArray();
    }
}
