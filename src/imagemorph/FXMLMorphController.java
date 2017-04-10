package imagemorph;

import imagemorph.config.Consts;
import imagemorph.model.Animation;
import imagemorph.model.ImageChooser;
import imagemorph.model.ImageViewWrapper;
import imagemorph.processors.MorphingProcessor;
import imagemorph.utils.GifSequenceWriter;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import java.awt.image.BufferedImage;
import static java.lang.Thread.sleep;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class FXMLMorphController implements Initializable {

    @FXML
    private AnchorPane pane;

    @FXML
    private ImageView imageViewInitial;

    @FXML
    private ImageView imageViewTarget;

    @FXML
    private ImageView imageViewAnimation;

    @FXML
    private Label labelStatus;

    @FXML
    private TextField textFieldFrames;

    @FXML
    private TextField textFieldSpeed;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Slider slider;

    @FXML
    private Button buttonPlay;

    @FXML
    private Button buttonStartMorphing;

    @FXML
    private Button buttonMeshScreen;

    @FXML
    private MenuItem menuItemSaveAnimation;

    private boolean isAnimated = false;

    private Animation lastAnimation;

    private final ImageViewWrapper imgViewWrapperInitial = new ImageViewWrapper();
    private final ImageViewWrapper imgViewWrapperTarget = new ImageViewWrapper();
    private final ImageViewWrapper imgViewWrapperAnimation = new ImageViewWrapper();

    private void defineImageViewWrappers() {
        imgViewWrapperInitial.setImageView(imageViewInitial);
        imgViewWrapperTarget.setImageView(imageViewTarget);
    }

    private final ImageChooser imageChooser = new ImageChooser("png-jpg");

    @FXML
    private void handleInitialImgBtn(ActionEvent event) {
        imageChooser.getImgChooser().setTitle(Consts.IMG_CHOOSER_TITLE_INITIAL);
        File file = imageChooser.getImgChooser().showOpenDialog(pane.getScene().getWindow());
        defineImageViewWrappers();
        if (file != null) {
            try {
                BufferedImage img = ImageIO.read(file);
                if (imgViewWrapperTarget.isDefaultImg() || isImportValid((BufferedImage) imgViewWrapperTarget.getImage(), img)) {
                    imgViewWrapperInitial.setImage(img);
                    progressBar.setProgress(0);
                    setLabel(Consts.SUCCESS_LBL_COLOR, Consts.SUCCESS_LOADED_INITIAL_RESOURCE);
                }
            } catch (IOException ex) {
                Logger.getLogger(FXMLMorphController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @FXML
    private void handleTargetImgBtn(ActionEvent event) {
        imageChooser.getImgChooser().setTitle(Consts.IMG_CHOOSER_TITLE_TARGET);
        File file = imageChooser.getImgChooser().showOpenDialog(pane.getScene().getWindow());
        defineImageViewWrappers();
        if (file != null) {
            try {
                BufferedImage img = ImageIO.read(file);
                if (imgViewWrapperInitial.isDefaultImg() || isImportValid((BufferedImage) imgViewWrapperInitial.getImage(), img)) {
                    imgViewWrapperTarget.setImage(img);
                    progressBar.setProgress(0);
                    setLabel(Consts.SUCCESS_LBL_COLOR, Consts.SUCCESS_LOADED_TARGET_RESOURCE);
                }
            } catch (IOException ex) {
                Logger.getLogger(FXMLMorphController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @FXML
    private void handleInitialImgClearBtn(ActionEvent event) {
        defineImageViewWrappers();
        imgViewWrapperInitial.clear();
        setLabel(Consts.SUCCESS_LBL_COLOR, Consts.SUCCESS_UNLOADED_INITIAL_RESOURCE);
        progressBar.setProgress(0);
        setButtonsDisabled(true);
    }

    @FXML
    private void handleTargetImgClearBtn(ActionEvent event) {
        defineImageViewWrappers();
        imgViewWrapperTarget.clear();
        setLabel(Consts.SUCCESS_LBL_COLOR, Consts.SUCCESS_UNLOADED_TARGET_RESOURCE);
        progressBar.setProgress(0);
        setButtonsDisabled(true);
    }

    @FXML
    private void handlePlay(ActionEvent event) {
        isAnimated = false;
        animate(lastAnimation);
    }

    @FXML
    private void handleMeshScreen(ActionEvent event) {
        setButtonsDisabled(true);
        MeshController.showMeshScreen(imgViewWrapperInitial, imgViewWrapperTarget, buttonStartMorphing, buttonMeshScreen);

    }

    @FXML
    private void handleStartMorphing(ActionEvent event) {
        isAnimated = false;
        final int frames;
        try {
            frames = Integer.parseInt(textFieldFrames.getText());
        } catch (NumberFormatException ex) {
            setLabel(Consts.ERR_LBL_COLOR, Consts.ERR_INVALID_FRAMES);
            return;
        }

        // Morphing process
        System.out.println("Starting morphing process: (FPI) " + frames);
        final ArrayList<Image> images = new ArrayList<>(frames);

        final Image[] animation = new Image[frames];
        Task<Void> task = new Task<Void>() {
            @Override
            public Void call() throws InterruptedException {
                System.out.println("Morphing process started.");
                for (int frame = 0; frame < frames; frame++) {
                    float alpha = frame * 1 / (float) (frames - 1);
                    Image image = MorphingProcessor.intermediateMorph(imgViewWrapperInitial.getImage(), imgViewWrapperInitial.mesh, imgViewWrapperTarget.getImage(), imgViewWrapperTarget.mesh, alpha);
                    animation[frame] = image;
                    updateMessage("Morphing in progress: " + (frame + 1) + " out of " + frames + " frames...  ");
                    updateProgress(frame + 1, frames);
                }
                return null;
            }
        };

        labelStatus.setTextFill(Color.web(Consts.PROCESSING_LBL_COLOR));
        labelStatus.textProperty().bind(task.messageProperty());
        progressBar.progressProperty().bind(task.progressProperty());
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();

        imgViewWrapperAnimation.setImageView(imageViewAnimation);

        task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent t) {
                System.out.println("Morphing process done.");
                labelStatus.textProperty().unbind();
                progressBar.progressProperty().unbind();
                labelStatus.setTextFill(Color.web(Consts.SUCCESS_LBL_COLOR));
                labelStatus.setText("Morphing completed.");
                images.addAll(Arrays.asList(animation));

                lastAnimation = new Animation(frames, animation);
                animate(lastAnimation);
            }
        });
    }

    private void animate(final Animation animation) {
        buttonPlay.setDisable(true);
        final Task<Void> taskAnimate = new Task<Void>() {
            @Override
            public Void call() throws InterruptedException {
                System.out.println("Animation started.");
                long fps;
                int count = 1;

                try {
                    fps = Long.parseLong(textFieldSpeed.getText());
                    updateMessage(Consts.PROCESSING_ANIMATION.replace("#fps#", Long.toString(fps)));
                } catch (NumberFormatException ex) {
                    updateMessage(Consts.ERR_INVALID_FPS.replace("#fps#", Long.toString(Consts.DEFAULT_ANIMATION_FPS)));
                    fps = Consts.DEFAULT_ANIMATION_FPS;
                }

                for (Image image : animation.getImages()) {
                    imgViewWrapperAnimation.setImage(image);
                    updateProgress(count++, animation.getFrames());
                    sleep(1000 / fps);
                }

                animation.setFps(fps);
                return null;
            }
        };

        labelStatus.setTextFill(Color.web(Consts.PROCESSING_LBL_COLOR));
        labelStatus.textProperty().bind(taskAnimate.messageProperty());
        slider.setDisable(false);
        slider.valueProperty().bind(taskAnimate.progressProperty().multiply(100));
        Thread thread = new Thread(taskAnimate);
        thread.setDaemon(true);
        thread.start();

        taskAnimate.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent t) {
                System.out.println("Animation finished.");
                isAnimated = true;
                slider.valueProperty().unbind();
                labelStatus.textProperty().unbind();
                labelStatus.setTextFill(Color.web(Consts.SUCCESS_LBL_COLOR));
                labelStatus.setText(Consts.SUCCESS_ANIMATION_DONE);
                menuItemSaveAnimation.setDisable(false);
                buttonPlay.setDisable(false);
            }
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        imgViewWrapperAnimation.setImageView(imageViewAnimation);
        slider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> ov,
                    Number old_val, Number new_val) {
                if (isAnimated) {
                    int imageIndex = (int) Math.ceil(new_val.doubleValue() * lastAnimation.getFrames() / 100);
                    imageIndex = (imageIndex == 0) ? 1 : imageIndex;
                    imgViewWrapperAnimation.setImage(lastAnimation.getImages()[imageIndex - 1]);
                }
            }
        });
    }

    private boolean isImportValid(BufferedImage imgOld, BufferedImage imgNew) {
        if ((imgOld.getHeight() != imgNew.getHeight()) || (imgOld.getWidth() != imgNew.getWidth())) {
            setLabel(Consts.ERR_LBL_COLOR, Consts.ERR_IMAGES_SIZE);
            return false;
        }

        setButtonsDisabled(false);
        return true;
    }

    private void setButtonsDisabled(boolean disabled) {
        buttonStartMorphing.setDisable(disabled);
        buttonMeshScreen.setDisable(disabled);
    }

    private void setLabel(String color, String text) {
        labelStatus.setTextFill(Color.web(color));
        labelStatus.setText(text);
    }

    /* Menu bar handlers */
    @FXML
    private void handleSaveAnimation(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Morphing animation");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("GIF file (*.gif)", "*.GIF"));
        final File file = fileChooser.showSaveDialog(pane.getScene().getWindow());
        
        final Task<Void> taskSave = new Task<Void>() {
            @Override
            public Void call() throws InterruptedException {
                try (ImageOutputStream output = new FileImageOutputStream(file)) {
                    GifSequenceWriter writer = new GifSequenceWriter(output, ((BufferedImage) lastAnimation.getImages()[0]).getType(), (int) lastAnimation.getFps(), false);
                    System.out.println("Saving animation started.");
                    updateMessage("Saving animation...");
                    for (int i = 0; i < lastAnimation.getImages().length - 1; i++) {
                        updateProgress(i + 1, lastAnimation.getImages().length - 1);
                        BufferedImage nextImage = (BufferedImage) lastAnimation.getImages()[i];
                        writer.writeToSequence(nextImage);
                    }
                    writer.close();
                } catch (IOException ex) {
                    setLabel(Consts.ERR_LBL_COLOR, Consts.ERR_SAVE_ANIMATION);
                }
                return null;
            }
        };

        labelStatus.setTextFill(Color.web(Consts.PROCESSING_LBL_COLOR));
        labelStatus.textProperty().bind(taskSave.messageProperty());
        progressBar.progressProperty().bind(taskSave.progressProperty());
        Thread thread = new Thread(taskSave);
        thread.setDaemon(true);
        thread.start();
        
        taskSave.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent t) {
                System.out.println("Animation saved.");
                labelStatus.textProperty().unbind();
                progressBar.progressProperty().unbind();
                labelStatus.setTextFill(Color.web(Consts.SUCCESS_LBL_COLOR));
                labelStatus.setText(Consts.SUCCESS_ANIMATION_SAVED);
            }
        });
    }

    @FXML

    private void handleExit(ActionEvent event) {
        Stage stage = (Stage) pane.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleMaxPanelSize(ActionEvent event) {
        MeshController.limitEnabled = !MeshController.limitEnabled;
    }

    @FXML
    private void handleAbout(ActionEvent event) {
        try {
            ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("assets/img/appIcon.png"));
            BufferedImage fetImg = ImageIO.read(getClass().getClassLoader().getResourceAsStream("assets/img/fet.png"));
            
            // labels 
            JLabel fetImgLbl = new JLabel(new ImageIcon(fetImg));
            JLabel univLbl = new JLabel("UNIVERSITY OF TUZLA", JLabel.CENTER);
            univLbl.setFont(new Font("Calibri", Font.BOLD, 14));
            JLabel fetLbl = new JLabel("FACULTY OF ELECTRICAL ENGINEERING", JLabel.CENTER);
            fetLbl.setFont(new Font("Calibri", Font.BOLD, 14));
            JLabel countryLbl = new JLabel("TUZLA, BOSNIA AND HERZEGOVINA", JLabel.CENTER);
            countryLbl.setFont(new Font("Calibri", Font.PLAIN, 12));
            JLabel authorsPrefixLbl = new JLabel("@authors ", JLabel.CENTER);
            JLabel lineLbl = new JLabel("--------------------------------------------------------", JLabel.CENTER);
            authorsPrefixLbl.setFont(new Font("Calibri", Font.BOLD, 12));
            JLabel authorsLbl = new JLabel("Behrudin Bajric, Sabina Piric", JLabel.CENTER);
            authorsLbl.setFont(new Font("Calibri", Font.PLAIN, 12));
            JLabel dateLbl = new JLabel("January, 2017", JLabel.CENTER);
            dateLbl.setFont(new Font("Calibri", Font.PLAIN, 10));

            //panel
            JPanel panelFet = new JPanel();
            panelFet.setLayout(new FlowLayout(FlowLayout.CENTER, 100, 1));
            panelFet.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            panelFet.add(fetImgLbl);
            panelFet.add(univLbl);
            panelFet.add(fetLbl);
            panelFet.add(countryLbl);
            panelFet.add(lineLbl);
            panelFet.add(authorsPrefixLbl);
            panelFet.add(authorsLbl);
            panelFet.add(dateLbl);

            //frame
            JFrame frame = new JFrame("About");
            frame.setIconImage(icon.getImage());
            frame.add(panelFet);
            frame.setSize(380, 270);
            MeshController.centreFrame(frame);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setResizable(false);
            frame.setVisible(true);
        } catch (IOException ex) {
            Logger.getLogger(FXMLMorphController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
