package cursosystem;

import com.sun.javafx.scene.control.skin.DatePickerSkin;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.MonthDay;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import javafx.stage.WindowEvent;

public class FXMLDocumentController implements Initializable {
    
    //ATENÇAO A NOVA START SCREEN AGORA TEM UM NOVO CONTROLLER ControllerTeste.java POR ENQUANTO

    Parent root;

    @FXML
    private Button btncad, btnhome;

    @FXML
    private AnchorPane homePage, ancButtons;
    @FXML
    private Button btnconsulta;

    @FXML
    private Label test;
    @FXML
    private RadioButton rdnome;

    @FXML
    private RadioButton rdcpf;

    @FXML
    private RadioButton rdcurso;

    @FXML
    private Button btnalterar;

    @FXML
    private Button btncalllogin;
    
    @FXML 
    private AnchorPane ancTeste;

    ancLoader load = new ancLoader();

    //EVENTOS PARA CARREGAR OS AnchorPanes
    @FXML
    private void cad(ActionEvent event) throws IOException {

        load.ancLoader("FXMLDocuments/ancCadastro.fxml", homePage);

    }

    @FXML
    private void check(ActionEvent event) throws IOException {
        load.ancLoader("FXMLDocuments/ancConsulta.fxml", homePage);

    }

    @FXML
    private void showpopup(ActionEvent event) throws IOException {

        load.anotherScreenLoader("Agenda.fxml");

    }

    @FXML
    private void homeOpen() {
        load.ancLoader("FXMLDocuments/ancHome.fxml", homePage);

    }

    @FXML
    private void openAgenda() {
        try {
            load.ancLoader("FXMLDocuments/ancAgenda2.fxml", homePage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openAbout() {
        try {
           
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        sessionUser s = new sessionUser();

        ancButtons.getStylesheets().add("Css/btnhome.css");
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                Runnable updater = new Runnable() {

                    @Override
                    public void run() {
                       
                        //lblunidade.setText(s.getidUnidade());//APENAS UM TESTE
                      // ancTeste.getStylesheets().addAll("Css/teste.css");

                    }
                };

                // UI update is run on the Application thread
                Platform.runLater(updater);

            }

        });
        //thread.start();

    }

}
