package imagemorph.model;

import imagemorph.config.Consts;
import java.awt.Image;

public class Animation {
    
    // frames per image animation
    private int frames;
    private Image[] images;
    // frames per second (animation speed)
    private long fps;

    public Animation(int frames, Image[] images) {
        this.frames = frames;
        this.images = images;
        this.fps = Consts.DEFAULT_ANIMATION_FPS;
    }

    public int getFrames() {
        return frames;
    }

    public Image[] getImages() {
        return images;
    }

    public void setFrames(int frames) {
        this.frames = frames;
    }

    public void setImages(Image[] images) {
        this.images = images;
    }

    public long getFps() {
        return fps;
    }

    public void setFps(long speed) {
        this.fps = speed;
    }
    
}
