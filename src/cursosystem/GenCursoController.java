
package cursosystem;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;


public class GenCursoController implements Initializable {

   @FXML
   TableView<CursoConsulta> tablecurso;
   
   @FXML
   TableColumn<CursoConsulta,String> id;
   
   @FXML
   TableColumn<CursoConsulta,String> curso;
   
   @FXML
   TableColumn <CursoConsulta,String>descricao;
   
   
   public void configTable(){
   
        //setting the columns
        id.setCellValueFactory(new PropertyValueFactory<>("Id"));
        curso.setCellValueFactory(new PropertyValueFactory<>("curso"));
        descricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
    
        //config the selection mode
        tablecurso.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    tablecurso.getSelectionModel().setCellSelectionEnabled(true);
   
    tablecurso.setItems( getInitialTableData());
    
    
   
   
   }
    
     private ObservableList getInitialTableData() {
      List list=new ArrayList();
 
      
    
      list.add(new CursoConsulta("Hello","World","!"));
      list.add(new CursoConsulta("1","Curso 1","Este e um curso de teste"));
      ObservableList<CursoConsulta> data= FXCollections.observableArrayList(list);
      //tableagenda.refresh();
      return data;
}
    
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
       
        configTable();
        
        
    }
    
}
