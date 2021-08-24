
package cursosystem;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;


public class CursoSystem extends Application {
    private double xOffset=0;
    private double yOffset=0;
    @Override
    public void start(Stage stage) throws Exception {
        
        Parent root = FXMLLoader.load(getClass().getResource("ancLoginScreen.fxml"));
        
        Scene scene = new Scene(root);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.setTitle("CursoSystem");
        stage.setResizable(false);
         
        root.setOnMousePressed(event -> {
        xOffset=event.getSceneX();
        yOffset=event.getSceneY();
            
        });
         root.setOnMouseDragged(ev->{
        stage.setX(ev.getScreenX() - xOffset);
        stage.setY(ev.getScreenY() - yOffset);
        });
        
         stage.addEventHandler(WindowEvent.WINDOW_HIDDEN, new  EventHandler<WindowEvent>()
         {
        @Override
        public void handle(WindowEvent window)
        {
         System.err.print("teste");
        }
    });
        stage.show();
      
        
        
    }

   
    public static void main(String[] args) {
        launch(args);
    }

    void start(Scene scene) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
