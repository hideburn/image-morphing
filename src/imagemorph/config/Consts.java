package imagemorph.config;

public class Consts {
    
    public static final String IMG_CHOOSER_TITLE_INITIAL = "Open Initial Resource Image";
    public static final String IMG_CHOOSER_TITLE_TARGET = "Open Target Resource Image";
    
    //public static final String ERR_LOAD_RESOURCES = "Load images to start morphing process."; //covered with disabling btn
    public static final String ERR_IMAGES_SIZE = "Images must be the same size.";
    public static final String ERR_INVALID_FRAMES = "Invalid or empty frames per image field.";
    public static final String ERR_INVALID_FPS = "Invalid FPS input. Animating with #fps# FPS...";
    public static final String ERR_NO_ANIMATION = "Start new morphing process.";
    public static final String ERR_SAVE_ANIMATION = "Animation save failed.";
    
    public static final String SUCCESS_LOADED_INITIAL_RESOURCE = "Initial image loaded.";
    public static final String SUCCESS_LOADED_TARGET_RESOURCE = "Target image loaded.";
    
    public static final String SUCCESS_UNLOADED_INITIAL_RESOURCE = "Initial image unloaded.";
    public static final String SUCCESS_UNLOADED_TARGET_RESOURCE = "Target image unloaded.";
    
    public static final String SUCCESS_ANIMATION_DONE = "Animation done.";
    public static final String SUCCESS_ANIMATION_SAVED = "Animation saved.";
    
    public static final String PROCESSING_ANIMATION = "Animating with #fps# FPS...";
    
    public static final String ERR_LBL_COLOR = "#ff0000";
    public static final String SUCCESS_LBL_COLOR = "#007f00";
    public static final String PROCESSING_LBL_COLOR = "#0000ff";
    
    public static final int MAX_PANEL_WIDTH = 500;
    public static final int MAX_PANEL_HEIGHT = 500;
    
    public static Long DEFAULT_ANIMATION_FPS = 5L;
}
