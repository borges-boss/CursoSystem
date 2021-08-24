/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cursosystem;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

/**
 *
 * @author borge
 */
public class FXMLClienteController implements Initializable {

   
    int count = 0;

    @FXML
    private VBox pnItems = null;

    @FXML
    private RadioButton rdnome, rddata, rdcpf;

    @FXML
    private TextField txtpesq;

    @FXML
    private Label lblcount;

    @FXML
    private ProgressIndicator statusRefresh;
    
    @FXML
    private ImageView imgSearch;

    DownloadData dw = new DownloadData();

    private void add() throws IOException {
        pnItems.getChildren().clear();//SULUTION 
        

      
    }

    @FXML
    private void test() {
        Parent root;

        try {
            root = FXMLLoader.load(getClass().getResource("FXMLDocuments/popAgenda.fxml"));

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
            stage.setResizable(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //NOVO CLIENTE
    @FXML
    private void newClient() {
        Pane newClient = new Pane();
        Label title = new Label();
        TextField txtnome = new TextField();
        MaskTextField txtcpf = new MaskTextField();
        MaskTextField txttel = new MaskTextField();
        DatePicker data = new DatePicker();
        Button btncad = new Button();
        Stage s = new Stage();
        RadioButton rdopc = new RadioButton();
        Label warning = new Label();
        //TextField
        txtnome.setLayoutX(29);
        txtnome.setLayoutY(83);
        txtnome.setId("txtnome");
        txtnome.setPromptText("nome...");

        txtcpf.setLayoutX(198);
        txtcpf.setLayoutY(83);
        txtcpf.setId("txtcpf");
        txtcpf.setPromptText("cpf...");
        txtcpf.setMask("NNNNNNNNNNN");

        txttel.setLayoutX(29);
        txttel.setLayoutY(131);
        txttel.setId("txttel");
        txttel.setPromptText("telefone...");
        txttel.setMask("NNNNNNNNNN");
        //DataPicker
        data.setLayoutX(198);
        data.setLayoutY(131);
        data.setPrefHeight(27);
        data.setPrefWidth(151);
        data.setId("data");
        data.setStyle("-fx-background-color: transparent");

        //Label
        title.setText("Nova Cliente");
        title.setLayoutX(29);
        title.setLayoutY(21);
        title.setPrefHeight(35);
        title.setPrefWidth(140);
        title.setFont(Font.font("System", 24));
        title.setStyle("-fx-text-fill: #e7e5e5");
        warning.setText("");
        warning.setLayoutX(169);
        warning.setLayoutY(30);
        warning.setStyle("-fx-text-fill:red");

        //Button
        btncad.setText("Cadastrar");
        btncad.setPrefHeight(35);
        btncad.setPrefWidth(97);
        btncad.setLayoutX(142);
        btncad.setLayoutY(190);
        btncad.getStylesheets().add("css/style.css");

        btncad.setOnAction(event -> {
            Thread thread = new Thread(new Runnable() {

                @Override
                public void run() {
                    Runnable updater = new Runnable() {

                        @Override
                        public void run() {
                            String cpf = txtcpf.getText(), tel = txttel.getText(), nome = null;
                            LocalDate date = null;
                            //VERIFICACAO DE CAMPOS 

                            if (txtnome.getText().equals("")) {

                                warning.setText("*Obrigatorio");
                                txtnome.setStyle("-fx-border-color:red");
                                nome = null;
                            } else {

                                nome = txtnome.getText();
                                txtnome.setStyle("-fx-border-color:#2A73FF");
                            }
                            if (cpf.equals("")) {
                                warning.setStyle("-fx-text-fill:red");
                                warning.setText("*Obrigatorio");
                                txtcpf.setStyle("-fx-border-color:red");
                                cpf = null;

                            } else if (cpf.length() < 11) {
                                warning.setStyle("-fx-text-fill:red");
                                warning.setText("*Formato de CPF invalido");
                                txtcpf.setStyle("-fx-border-color:red");
                                cpf = null;

                            } else {

                                cpf = txtcpf.getText();
                                System.out.println(cpf);
                                txtcpf.setStyle("-fx-border-color:#2A73FF");
                            }

                            if (tel.equals("")) {
                                warning.setStyle("-fx-text-fill:red");
                                warning.setText("*Obrigatorio");
                                txttel.setStyle("-fx-border-color:red");
                                tel = null;
                            } else if (tel.length() < 10) {

                                warning.setText("*Formato de telefone invalido");
                                txttel.setStyle("-fx-border-color:red");
                                tel = null;
                            } else {

                                tel = txttel.getText();

                                txttel.setStyle("-fx-border-color:#2A73FF");
                            }

                            if (data.getValue() == null) {
                                warning.setText("*Obrigatorio");
                                warning.setStyle("-fx-text-fill:red");
                                data.setStyle("-fx-border-color:red;-fx-background-color: transparent");

                            } else {
                                warning.setText("");
                                data.setStyle("-fx-border-color:#2A73FF;-fx-background-color:transparent");
                                date = data.getValue();
                            }

                            if (nome != null && cpf != null && tel != null && data != null) {

                                Http http = new Http();
                                if (http.insertData(nome, cpf, tel, date.toString()) == true) {
                                    cadastroLocal(nome, cpf, tel, date.toString());

                                    s.close();
                                    pnItems.getChildren().clear();
                                    try {
                                        pupulateVbox();
                                        DownloadData dw = new DownloadData();
                                        dw.downloadAgendaData();
                                        dw.downloadClientsData();
                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    warning.setText("Algo deu errado");

                                }

                            }
                        }
                    };

                    // UI update is run on the Application thread
                    Platform.runLater(updater);

                }

            });
            thread.start();

        });

        //Pane
        newClient.setPrefHeight(260);
        newClient.setPrefWidth(397);
        newClient.setStyle("-fx-background-color:#02030A");
        newClient.getStylesheets().addAll("/css/cadastroPop.css");
        // AutoCompleteTextField auto=new AutoCompleteTextField();
        //List a=new ArrayList();//teste
        //a.add("teste");
        //a.add("LOL");
        //auto.getEntries().addAll(a);
        newClient.getChildren().add(txtnome);
        newClient.getChildren().add(txtcpf);
        newClient.getChildren().add(txttel);
        newClient.getChildren().add(title);
        newClient.getChildren().add(data);
        newClient.getChildren().add(btncad);
        newClient.getChildren().add(warning);

        Scene sc = new Scene(newClient);
        s.setScene(sc);
        s.setResizable(false);
        s.show();

    }

    //BUTTON-ATUALIZAR
    @FXML
    private void refresh() {
        statusRefresh.setVisible(true);
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                dw.downloadAgendaData();
                dw.downloadClientsData();
                return null;
            }
        };

        task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent arg0) {

                statusRefresh.setVisible(false);

            }
        });

        task.setOnFailed(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent arg0) {

                statusRefresh.setVisible(false);
                JOptionPane.showMessageDialog(null, "Erro:Porfavor confira sua conexao com a internet");

            }
        });
        task.setOnCancelled(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent arg0) {

                statusRefresh.setVisible(false);

            }
        });
        statusRefresh.progressProperty().bind(task.progressProperty());
        Thread th = new Thread(task);
        th.start();
    }


    private void cadastroLocal(String nome, String cpf, String tel, String data) {
        try {
            OpenConnection p = new OpenConnection();
            Connection con = p.getConnetion();

            String sql = null;
            PreparedStatement st = null;
            sessionUser s = new sessionUser();
            //ATUALIZAÇAO DO CADASTRO

            sql = "INSERT INTO cliente(id_cliente, nome_cliente,cpf_cliente,telefone_cliente,data_compra) VALUES (?,?,?,?,?)";
            st = con.prepareStatement(sql);

            if (nome != null && cpf != null && tel != null) {
                st.setInt(1, s.getIdCliente());
                st.setString(2, nome);
                st.setString(3, cpf);
                st.setString(4, tel);
                st.setString(5, data);
                st.execute();
                st.close();
                con.close();
            }

        } catch (SQLException e) {
        }

    }

    
    //O METODO ABAIXO TEM A FUNÇAO DE POPULAR A VBox COM OS DADOS DO BD
    private void pupulateVbox() throws SQLException {
        OpenConnection opa = new OpenConnection();
        String nome = null, cpf = null, tel = null, datacompra = null;
        int id_cliente = 0;
        try {
            count = 0;
            Connection con = opa.getConnetion();
            ResultSet rs = null;
            String sql = "SELECT id_cliente,nome_cliente,cpf_cliente,telefone_cliente,data_compra FROM cliente";
            PreparedStatement stm = con.prepareStatement(sql);
            rs = stm.executeQuery();

            while (rs.next()) {
                id_cliente = rs.getInt("id_cliente");
                nome = rs.getString("nome_cliente");
                cpf = rs.getString("cpf_cliente");
                tel = rs.getString("telefone_cliente");
                datacompra = rs.getString("data_compra");
                pnItems.getChildren().add(new ClientData(id_cliente,nome,cpf,tel,datacompra,pnItems).addItems());
                count++;
            }
            lblcount.setText(String.valueOf(count));
            con.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
   //PESQUISA
    private void search(String palavra) {
        String nome = null, cpf = null, tel = null, datacompra = null;
        int id=0;
        String sql = null;
         OpenConnection opa = new OpenConnection();
          Connection con = opa.getConnetion();
        try {
           
            ResultSet rs = null;
            if (rdnome.isSelected()) {
                sql = "SELECT id_cliente,nome_cliente,cpf_cliente,telefone_cliente,data_compra FROM cliente WHERE nome_cliente  LIKE ?";
            } else if (rddata.isSelected()) {
                sql = "SELECT id_cliente,nome_cliente,cpf_cliente,telefone_cliente,data_compra FROM cliente WHERE data_compra  LIKE ?";
            } else if (rdcpf.isSelected()) {
                sql = "SELECT id_cliente,nome_cliente,cpf_cliente,telefone_cliente,data_compra FROM cliente WHERE cpf_cliente  LIKE ?";
            } else {
                sql = "SELECT id_cliente,nome_cliente,cpf_cliente,telefone_cliente,data_compra FROM cliente WHERE nome_cliente  LIKE ?";
            }

            PreparedStatement stm = con.prepareStatement(sql);
            stm.setString(1, palavra + "%");
            rs = stm.executeQuery();
            pnItems.getChildren().clear();//Removendo itens da VBox 
            while (rs.next()) {
                id=rs.getInt("id_cliente");
                nome = rs.getString("nome_cliente");
                cpf = rs.getString("cpf_cliente");
                tel = rs.getString("telefone_cliente");
                datacompra = rs.getString("data_compra");

                pnItems.getChildren().add(new ClientData(id,nome,cpf,tel,datacompra,pnItems).addItems());
            }
               con.close();       
        } catch (SQLException e) {
            e.printStackTrace();
        }
    

    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                Runnable updater = new Runnable() {

                    @Override
                    public void run() {
                        try {
                            Http http = new Http();
                            pupulateVbox();
                            txtpesq.setOnKeyPressed(e -> {
                                if (e.getCode() == KeyCode.ENTER) {
                                    search(txtpesq.getText());
                                }
                                
                            });
                            
                            imgSearch.setOnMouseClicked(se ->{
                            
                            search(txtpesq.getText());
                            
                            });

                        } catch (SQLException ex) {
                            Logger.getLogger(FXMLClienteController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                };

                // UI update is run on the Application thread
                Platform.runLater(updater);
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(FXMLClienteController.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

        });

        thread.start();

    }

}
