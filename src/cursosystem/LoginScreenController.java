package cursosystem;

import java.io.IOException;
import java.net.ConnectException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import static javafx.geometry.Pos.TOP_CENTER;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.json.JSONArray;
import org.json.JSONObject;

/*
Controller da ancUniScreen.fxml



 */
public class LoginScreenController implements Initializable {

    private String login = null, senha = null;
    boolean reto;
    String resposta = null;
    ancLoader loader = new ancLoader();
    Http httpLogin = new Http();
    sessionUser session = new sessionUser();
    Task loading;
    @FXML
    Button btnlogin;

    @FXML
    private TextField txtlogin;

    @FXML
    private TextField txtpass;

    @FXML
    private ComboBox cmbunidade;

    @FXML
    private Label lblalert;

    @FXML
    private AnchorPane ancUni;

    @FXML
    private Button btnlogar;

    @FXML
    private ProgressBar pro;

    @FXML
    private Label lblstatusmessage;

    @FXML
    private ProgressIndicator prlogin;

    @FXML
    private void handleLogin(ActionEvent event) throws IOException {
        checkFields();

    }

    private void validate() {
        Service<Void> service = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        //Background work          

                        final CountDownLatch latch = new CountDownLatch(1);

                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    //FX Stuff done here

                                } finally {
                                    latch.countDown();
                                }
                            }
                        });
                        latch.await();
                        //Keep with the background work
                        return null;
                    }
                };
            }
        };
        service.start();

    }

    @FXML
    private void closeWindow() {
        Stage stage = (Stage) ancUni.getScene().getWindow();
        stage.close();

    }

    private void checkFields() {
        reto = false;
CountDownLatch latch=new CountDownLatch(2);
        login = txtlogin.getText();
        senha = txtpass.getText();
        if (login.equals("") && senha.equals("")) {

            lblalert.setText("Login ou senha incorretos");

        } else {
            Thread thread = new Thread(new Runnable() {

                @Override
                public void run() {
                    Runnable updater = new Runnable() {

                        @Override
                        public void run() {
                           
                            resposta = httpLogin.validateLogin(login, senha);//VALIDAÇAO  
                           
                            if (resposta.equals("")) {
                            }
                            if (resposta.equals("Erro")) {
                                lblalert.setText("Nao foi possivel conectar");
                               
                            } else if (Integer.parseInt(resposta) == 0) {
                                
                                lblalert.setText("Login ou senha incorretos ");
                            } else if (Integer.parseInt(resposta) > 0) {
                               
                                session.setidUnidade(resposta);
                                session.setNameUser(txtlogin.getText());//NOME DE USUARIO
                                Task<Void> taskLoad = new Task<Void>() {
                                    Http httpSelectData = new Http();
                                    DownloadData dw = new DownloadData();

                                    @Override
                                    protected Void call() throws Exception {
                                        int i = 0;
                                        int leng = 100;//APENAS PARA DEFINIR O LIMITE DA REPETIÇAO
                                            dw.downloadClientsData();//NOVO CAMPO DO TIPO DATA EM CLIENTES 
                                            dw.downloadAgendaData();
                                            dw.downloadCursosData();
                                            
                                        

                                        for (i = 0; i < leng; i++) {
                                            updateProgress(i, leng);
                                        }

                                        return null;
                                    }

                                };

                                //TASK PROPERTYS
                                taskLoad.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                                    @Override
                                    public void handle(WorkerStateEvent arg0) {

                                        loader.anotherScreenLoader("FXMLDocuments/StartScreen.fxml");//TELA INICIAL
                                        Stage stage = (Stage) ancUni.getScene().getWindow();
                                        stage.close();

                                    }
                                });
                                taskLoad.setOnFailed(new EventHandler<WorkerStateEvent>() {
                                    @Override
                                    public void handle(WorkerStateEvent arg0) {
                                        System.out.println("Algo deu errado");

                                    }
                                });
                                new Thread(taskLoad).start();

                                try {
                                    ProgressBar pro = new ProgressBar();

                                    AnchorPane pane = FXMLLoader.load(getClass().getResource("FXMLDocuments/LoadingScreen.fxml"));

                                    ancUni.getChildren().setAll(pane);
                                    // pro.setStyle("-fx-accent: #1e90ff; -fx-background-color:transparent");
                                    pro.setLayoutX(0);
                                    pro.setLayoutY(480);
                                    pro.setPrefHeight(7);
                                    pro.prefWidthProperty().bind(pane.widthProperty().subtract(4));
                                    pro.progressProperty().bind(taskLoad.progressProperty());

                                    pane.getChildren().add(pro);

                                } catch (IOException ex) {
                                    Logger.getLogger(LoginScreenController.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }

                        }//else-if
                    };

                    // UI update is run on the Application thread
                    Platform.runLater(updater);

                }

            }
            );
            thread.setDaemon(true);
            thread.start();
        }//else-fim

    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

    }

}
