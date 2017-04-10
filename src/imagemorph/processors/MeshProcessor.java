package imagemorph.processors;

import imagemorph.MeshController;
import imagemorph.model.ImageViewWrapper;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JPanel;

/* Swing */
public class MeshProcessor extends JPanel implements MouseListener, MouseMotionListener  {

    private ImageViewWrapper imageViewWrapper;
    private int movingX, movingY;
    private int offsetx, offsety;
    
    public int imgsBorderX = 10;
    public int imgsBorderY = 10;
    
    @SuppressWarnings({"OverridableMethodCallInConstructor", "LeakingThisInConstructor"})
    public MeshProcessor(ImageViewWrapper imageViewWrapper){
        this.imageViewWrapper = imageViewWrapper;
        addMouseMotionListener(this);
        addMouseListener(this);
    }
    
    public void drawMesh(Graphics g, int offsetx, int offsety){
        this.offsetx = offsetx;
        this.offsety = offsety;
        g.setColor(new Color(0, 0, 255, 50));
        g.fillRect(offsetx, offsety, imageViewWrapper.width, imageViewWrapper.height);

        for(int i = 0; i < imageViewWrapper.mwidth; i++){
            for(int j = 0; j < imageViewWrapper.mheight; j++){
                Point p = imageViewWrapper.mesh.points[i][j];

                /* Draw connecting lines */
                g.setColor(Color.yellow);
                if(j != imageViewWrapper.mheight - 1){
                    Point next = imageViewWrapper.mesh.points[i][j+1];
                    g.drawLine(p.x + offsetx, p.y + offsety, next.x + offsetx, next.y + offsety);
                }
                if(i != imageViewWrapper.mwidth - 1){
                    Point next = imageViewWrapper.mesh.points[i+1][j];
                    g.drawLine(p.x + offsetx, p.y + offsety, next.x + offsetx, next.y + offsety);
                }
                if(j != imageViewWrapper.mheight - 1 && i != imageViewWrapper.mwidth -1){
                    Point next = imageViewWrapper.mesh.points[i+1][j+1];
                    g.drawLine(p.x + offsetx, p.y + offsety, next.x + offsetx, next.y + offsety);
                }

                /* Draw circles */
                int radius = 6;
                g.setColor(Color.black);
                g.fillOval(offsetx - radius + p.x, offsety - radius + p.y, 2 * radius, 2 * radius);

                radius = 3;
                g.setColor(Color.yellow);
                g.fillOval(offsetx - radius + p.x, offsety - radius + p.y, 2 * radius, 2 * radius);    
            }
        }
    }

    @Override
    public void update(Graphics g){
        g.drawImage(imageViewWrapper.getImage(), imgsBorderX, imgsBorderY, this);
        drawMesh(g, imgsBorderX, imgsBorderY);
    }
    
    @Override
    public void paint(Graphics g){
        update(g);
    }
    
    @Override
    public void paintComponent(Graphics g){
        paint(g);
    }
    
    @Override
    public void mouseClicked(MouseEvent e) { }

    @Override
    public void mousePressed(MouseEvent e) {
        if((e.getButton() == MouseEvent.BUTTON3)){
            imageViewWrapper.setDefaultMesh();
            MeshController.frame.repaint();
            return;
        }
        
        int x = e.getX() - offsetx;
        int y = e.getY() - offsety;

        for(int i = 0; i < imageViewWrapper.mwidth; i ++){
            for(int j = 0; j < imageViewWrapper.mheight; j++){
                Point p = imageViewWrapper.mesh.points[i][j];
                int radius = 6;
                if(p.distance(x, y) <= radius){
                    if(i != 0 && i != imageViewWrapper.mwidth-1 && j != 0 && j != imageViewWrapper.mheight -1){
                        movingX = i;
                        movingY = j;
                        return;
                    }
                }
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        movingX = movingY = -1;
    }

    @Override
    public void mouseEntered(MouseEvent e) { }

    @Override
    public void mouseExited(MouseEvent e) { }
    
    @Override
    public void mouseDragged(MouseEvent e) {
        int x = e.getX() - offsetx;
        int y = e.getY() - offsety;

        if(movingX != -1 && movingY != -1){
            imageViewWrapper.mesh.points[movingX][movingY].x = x;
            imageViewWrapper.mesh.points[movingX][movingY].y = y;
            MeshController.frame.repaint();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) { }
    
}
