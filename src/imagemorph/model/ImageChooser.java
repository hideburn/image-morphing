package imagemorph.model;

import javafx.stage.FileChooser;

public final class ImageChooser {

    private FileChooser chooser;

    //formats separated with '-' ie. png-jpg
    private String formats;

    // only png by default
    public ImageChooser() {
        this.chooser = new FileChooser();
        this.chooser.getExtensionFilters()
                .add(new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG"));
    }

    public ImageChooser(String formats) {
        this.chooser = new FileChooser();
        setFormats(formats);
    }

    public FileChooser getImgChooser() {
        return chooser;
    }

    // todo: validate input
    public void setFormats(String formats) {
        this.chooser.getExtensionFilters().clear();
        String[] splits = formats.split("-");
        for (String format : splits) {
            this.chooser.getExtensionFilters()
                    .add(new FileChooser.ExtensionFilter(format.toUpperCase()
                            + " files (*." + format.toLowerCase()
                            + ")", "*." + format.toUpperCase()));
        }
        this.formats = formats;
    }

}
