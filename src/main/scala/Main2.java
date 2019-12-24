import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;

public class Main2 extends Application {
    private Stage primaryStage;
    private Pane mainPane;
    static Main main = new Main();

//    @FXML
//    private RangeSlider rangeSlider;


    public static void main(String[] args){
//        main.searchByKey(main.df2(),key.getText());
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        AnchorPane root =  FXMLLoader.load(getClass().getResource("Gui.fxml"));
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }

    private void showMainView() throws IOException{
        FXMLLoader loader = new FXMLLoader();
//        loader.setLocation(new U);
        mainPane = loader.load();
        Scene scene = new Scene (mainPane);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}
