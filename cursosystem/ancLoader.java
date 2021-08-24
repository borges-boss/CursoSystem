
package cursosystem;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class ancLoader {
    @FXML 
    private AnchorPane cadload;
    
     public void ancLoader(String anc,AnchorPane a){
        
        try{
          
     AnchorPane pane=FXMLLoader.load(getClass().getResource(anc));
     
    a.getChildren().setAll(pane);
     
    
        }catch(IOException e){
       
        
        }
    
    }
     
      public void anotherScreenLoader(String load){
        Parent root;
        
        try{
root = FXMLLoader.load(getClass().getResource(load));
        
        Scene scene = new Scene(root);
        Stage stage=new Stage();
        stage.setScene(scene);
        stage.setResizable(false);
        //stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
      
        }
        
        catch(IOException e){e.printStackTrace();}
    }
     
}
