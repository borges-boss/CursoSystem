package cursosystem;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javax.swing.JOptionPane;

public class FXMLStartScreenController implements Initializable {

    ancLoader loader = new ancLoader();
    @FXML
    private Button btnhome, btncliente, btnagenda;

    @FXML
    private Pane pnlHome;

    @FXML
    private AnchorPane anchorMain;

    @FXML
    private VBox mainVbox;

    @FXML
    private Label lblusername;
    Pane pane = null;
    AnchorPane anchor = null;
    AnchorPane anchorConfig = anchor;

    @FXML
    private void openHome() {

        try {
            pane = FXMLLoader.load(getClass().getResource("FXMLDocuments/pnlHome.fxml"));

            System.out.print("TESTE");
            pnlHome.getChildren().setAll(pane);
        } catch (IOException ex) {
            Logger.getLogger(FXMLStartScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    private void openCon() {

        try {

            pane = FXMLLoader.load(getClass().getResource("FXMLDocuments/pnlConsulta.fxml"));
            System.out.print("TESTE");
            pnlHome.getChildren().setAll(pane);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void openAgenda() {
        try {

            pane = FXMLLoader.load(getClass().getResource("FXMLDocuments/pnlAgenda2.fxml"));
            System.out.print("TESTE");
            pnlHome.getChildren().setAll(pane);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void openConfig() {
        try {
            Duration d = Duration.millis(1500);
            TranslateTransition tt = new TranslateTransition(d, anchor);
            tt.setFromX(1060);
            tt.setFromY(0);
            tt.setToX(266.4);
            System.out.print("TESTE");

            tt.play();
            anchor.setVisible(true);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void sair() throws Exception {
        int resp = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja sair ?");
        Parent root;

        try {
            if (resp == JOptionPane.YES_OPTION) {
                CursoSystem cs = new CursoSystem();
                OpenConnection op = new OpenConnection();
                Stage s = (Stage) anchorMain.getScene().getWindow();
                op.closeAll();
                s.close();
                
                root = FXMLLoader.load(getClass().getResource("ancLoginScreen.fxml"));

                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.setScene(scene);
                cs.start(stage);

               
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

        try {
            pane = FXMLLoader.load(getClass().getResource("FXMLDocuments/pnlHome.fxml"));
            pnlHome.getChildren().setAll(pane);

            //anchorConfig
            anchor = FXMLLoader.load(getClass().getResource("FXMLDocuments/ancConfig.fxml"));
            // anchor.setLayoutX(1060);
            anchor.setLayoutY(0);
            anchor.setVisible(false);
            anchorMain.getChildren().add(anchor);

            //user-name
            sessionUser s = new sessionUser();
            lblusername.setText(s.getNameUser());

        } catch (IOException ex) {
            Logger.getLogger(FXMLStartScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
