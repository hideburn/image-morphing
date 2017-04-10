package imagemorph;

import imagemorph.config.Consts;
import imagemorph.model.ImageViewWrapper;
import imagemorph.processors.MeshProcessor;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import javafx.scene.control.Button;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

/* Swing */
public class MeshController {

    public static JFrame frame;
    public static boolean limitEnabled = false;
    
    public static void showMeshScreen(ImageViewWrapper imageViewWrapperInitial, ImageViewWrapper imageViewWrapperTarget, final Button startBtn, final Button meshBtn) {
        MeshProcessor meshViewInitial = new MeshProcessor(imageViewWrapperInitial);
        MeshProcessor meshViewTarget = new MeshProcessor(imageViewWrapperTarget);
        Dimension imageDim = new Dimension(imageViewWrapperInitial.width, imageViewWrapperInitial.height);
        Dimension borderDim = new Dimension(meshViewInitial.imgsBorderX, meshViewInitial.imgsBorderY);
        
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 2));
        panel.add(meshViewInitial);
        panel.add(meshViewTarget);
        
        frame = new JFrame("Mesh screen - Right mouse button resets the panel mesh") {
            @Override
            public void dispose() {
                meshBtn.setDisable(false);
                startBtn.setDisable(false);
                super.dispose();
            }
        };
        
        frame.setLayout(new BorderLayout());
        frame.add(panel);
        setFrameDimensions(imageDim, borderDim);
        centreFrame(frame);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ImageIcon icon = new ImageIcon(MeshController.class.getClassLoader().getResource("assets/img/appIcon.png"));
        frame.setIconImage(icon.getImage());
        frame.setVisible(true);
    }

    public static void centreFrame(JFrame frame) {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
        frame.setLocation(x, y);
    }
    
    private static void setFrameDimensions(Dimension imageDim, Dimension borderDim){
        int panelWidth = (limitEnabled && imageDim.width > Consts.MAX_PANEL_WIDTH) ? Consts.MAX_PANEL_WIDTH : imageDim.width;
        int panelHeight = (limitEnabled && imageDim.height > Consts.MAX_PANEL_HEIGHT) ? Consts.MAX_PANEL_HEIGHT : imageDim.height;

        frame.setSize(panelWidth * 2 + borderDim.width * 6, panelHeight + borderDim.height * 6);
    }

}
