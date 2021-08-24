
package cursosystem;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;


public class FXMLPopUpAgendaController implements Initializable {

    @FXML
    private TableColumn<AgendaData, String> name;

    @FXML
    private TableColumn<AgendaData, String> curso;

    @FXML
    private TableColumn<AgendaData, String> data;

    @FXML
    private TableColumn<AgendaData, String> hora;
    
    @FXML
    private TableView<AgendaData> tableagenda;
    
    
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
                
    name.setCellValueFactory(new PropertyValueFactory<>("Nome"));
    curso.setCellValueFactory(new PropertyValueFactory<>("Curso"));
    data.setCellValueFactory(new PropertyValueFactory<>("Data"));
    hora.setCellValueFactory(new PropertyValueFactory<>("Hora"));
    
    

//      ObservableList<AgendaData> data= FXCollections.observableArrayList(new AgendaData("Maria","lol","10/05/2019","7"));
     // tableagenda.setItems(data);
    }
    
}
