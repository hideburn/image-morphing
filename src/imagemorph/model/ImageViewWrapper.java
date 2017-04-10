package imagemorph.model;

import imagemorph.MorphMain;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.ImageView;
import javax.imageio.ImageIO;

public class ImageViewWrapper {

    private ImageView imageView;
    private boolean isDefaultImg;
    private final Image defaultImg;
    private Image currentImg;
    
    // Mesh
    public Mesh mesh;
    public int width, height;
    public int mwidth, mheight;
    
    public ImageViewWrapper() {
        this.defaultImg = getDefaultImg();
        this.isDefaultImg = true;
    }

    public void setImage(Image image) {
        this.currentImg = image;
        this.imageView.setImage(SwingFXUtils.toFXImage((BufferedImage) image, null));
        this.isDefaultImg = false;

        this.width = ((BufferedImage) image).getWidth();
        this.height = ((BufferedImage) image).getHeight();
        
        setDefaultMesh();
    }
    
    public void setDefaultMesh(){
        this.mesh = new Mesh(MorphMain.XMESH, MorphMain.YMESH, width, height);
        this.mwidth = MorphMain.XMESH;
        this.mheight = MorphMain.YMESH;
    }

    public Image getImage() {
        return currentImg; // SwingFXUtils.fromFXImage(imageView.getImage(), null);
    }

    public void clear() {
        this.imageView.setImage(SwingFXUtils.toFXImage((BufferedImage) defaultImg, null));
        this.isDefaultImg = true;
    }

    public boolean isDefaultImg() {
        return isDefaultImg;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    private Image getDefaultImg() {
        try {
            InputStream in = getClass().getClassLoader().getResourceAsStream("assets/img/default.png");
            return ImageIO.read(in);
        } catch (IOException ex) {
            Logger.getLogger(ImageViewWrapper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
