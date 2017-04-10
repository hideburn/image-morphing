package imagemorph;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class MorphMain extends Application {
    
    public static int XMESH = 10, YMESH = 10;
    
    @Override
    public void start(Stage stage) throws Exception {   
        Parent root = FXMLLoader.load(getClass().getResource("FXMLMorph.fxml"));
        Scene scene = new Scene(root); 
        stage.setScene(scene);
        stage.getIcons().add(new Image("assets/img/appIcon.png") {});
        stage.setResizable(false);
        stage.setTitle("Image Morphing");
        stage.show();  
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
