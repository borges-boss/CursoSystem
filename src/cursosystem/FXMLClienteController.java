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

    ToggleGroup tg = new ToggleGroup();
    final static Map<String, Integer> map = new HashMap<>();
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

    private HBox addItems(String nome, String cpf, String tel, String data) {

        HBox itembody = new HBox();
        Label lblnome = new Label();
        Label lblcpf = new Label();
        Label lbltel = new Label();
        Label lbldata = new Label();
        RadioButton ropc = new RadioButton();
        ImageView img = new ImageView();
        ImageView imgageButtonEdit = new ImageView();
        ImageView imgageButtonDel = new ImageView();
        TextField txtnome = new TextField();
        TextField txtcpf = new TextField();
        TextField txttel = new TextField();
        TextField txtdata = new TextField();
        Button edit = new Button();
        Button delete = new Button();
        RadioButton rdopc = new RadioButton();
        final String oldcpf = cpf;
        //HBox
        itembody.setStyle("-fx-background-color:#02030A");
        itembody.setStyle("-fx-background-radius:5");
        itembody.setStyle("-fx-background-insets:0");
        itembody.setPrefHeight(53);
        itembody.setPrefWidth(695);
        itembody.setSpacing(35);
        itembody.setAlignment(Pos.CENTER_LEFT);
        itembody.setVisible(true);
        itembody.setOnMouseEntered(event -> {
            itembody.setStyle("-fx-background-color : #0A0E3F");
        });
        itembody.setOnMouseExited(event -> {
            itembody.setStyle("-fx-background-color : #02030A");
        });
        //TextFields
        //nome 
        txtnome.setPrefHeight(18);
        txtnome.setPrefWidth(124);
        txtnome.setText(nome);
        txtnome.setEditable(false);
        //cpf
        txtcpf.setPrefHeight(18);
        txtcpf.setPrefWidth(133);
        txtcpf.setText(cpf.replaceAll("([0-9]{3})([0-9]{3})([0-9]{3})([0-9]{2})", "$1.$2.$3-$4"));
        txtcpf.setEditable(false);
        //tel
        txttel.setPrefHeight(18);
        txttel.setPrefWidth(117);
        txttel.setText(tel.replaceFirst("(\\d{2})(\\d{4})(\\d+)", "($1) $2-$3"));
        txttel.setEditable(false);

        //data
        txtdata.setPrefHeight(18);
        txtdata.setPrefWidth(107);
        txtdata.setText(data);
        txtdata.setEditable(false);

        //ImageView
        javafx.scene.image.Image i = new javafx.scene.image.Image(getClass().getResource("icongps.png").toExternalForm());
        img.setFitHeight(31);
        img.setFitWidth(25);
        img.setPreserveRatio(true);
        img.setSmooth(true);
        img.setImage(i);

        //delete-button-image
        javafx.scene.image.Image imagedelete = new javafx.scene.image.Image(getClass().getResource("delete-1-icon.png").toExternalForm());
        imgageButtonDel.setImage(imagedelete);
        imgageButtonDel.setFitHeight(20);
        imgageButtonDel.setFitWidth(20);
        imgageButtonDel.setSmooth(true);
        imgageButtonDel.setPreserveRatio(true);
        //Button

        //delete 
        delete.setText("");
        delete.setGraphic(imgageButtonDel);

        //Button
        delete.setOnMouseClicked(event -> {//EVENTO TEM A FUNÇAO EXCLUIR OS DADOS DE CLIENTE E A HBOX
            int idCliente = map.get(cpf);//RECUÈRANDO ID DO CLIENTE NA HASHMAP
            Http http = new Http();
            int resp = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja fazer esta exclusao ?\n Aviso:Ao exluir este cliente voce tambem excluira todos os agendamentos relacionados a ele(a)", "Aviso", JOptionPane.YES_NO_OPTION);

            if (resp == JOptionPane.YES_OPTION) {
                //E necessario excluir os dados da agenda para evitar o erro na foreign key
                if (http.deleteData("agenda", String.valueOf(idCliente)) == true && http.deleteData("clientes", String.valueOf(idCliente)) == true) {
                    Thread thread = new Thread(new Runnable() {

                        @Override
                        public void run() {
                            Runnable updater = new Runnable() {

                                @Override
                                public void run() {
                                    try {
                                        OpenConnection opa = new OpenConnection();
                                        Connection con = opa.getConnetion();
                                        String sql = null;
                                        sql = "DELETE FROM agenda_curso WHERE FK_ID_CLIENTE=?";
                                        PreparedStatement stm2 = con.prepareStatement(sql);
                                        stm2.setInt(1, idCliente);
                                        stm2.execute();
                                        sql = "DELETE FROM cliente WHERE id_cliente=?";
                                        PreparedStatement stm1 = con.prepareStatement(sql);
                                        stm1.setInt(1, idCliente);
                                        stm1.execute();
                                        pnItems.getChildren().remove(itembody);
                                        con.close();

                                    } catch (SQLException e) {
                                    }
                                }
                            };

                            // UI update is run on the Application thread
                            Platform.runLater(updater);
                            try {
                                Thread.sleep(50000);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(FXMLClienteController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }

                    });
                    thread.start();

                }
            } else {

            }
        });

        //RadioButton
        rdopc.setToggleGroup(tg);
        rdopc.getStylesheets().add("css/radio.css");
        rdopc.setOnAction(event -> {
            sessionUser s = new sessionUser();
            s.setIdClienteAgenda(map.get(cpf));//AO CLICAR O ID DO CLIENTE DEVE SER PASSADO AO AGENDADMENTO
            s.setNomeAgenda(txtnome.getText());

        });
        //TextBox events
        int wherValor = map.get(cpf);//Recuperando o id do cliente na HashMap
        textBoxEvent(txtnome, "nome", wherValor);
        textBoxEvent(txtcpf, "cpf", wherValor);
        textBoxEvent(txttel, "tel", wherValor);
        textBoxEvent(txtdata, "datacompra", wherValor);

        itembody.getChildren().add(img);
        itembody.getChildren().add(txtnome);
        itembody.getChildren().add(txtcpf);
        itembody.getChildren().add(txttel);
        itembody.getChildren().add(txtdata);
        itembody.getChildren().add(rdopc);
        itembody.getChildren().add(delete);

        return itembody;
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

    private TextField textBoxEvent(TextField txt, String campo, int whereValor) {
        Http http = new Http();
        txt.setOnMouseClicked(event -> {

            txt.setEditable(true);
            txt.setStyle("-fx-background-color:white;-fx-text-fill:black");
            txt.setOnKeyPressed(e -> {
                if (e.getCode() == KeyCode.ENTER) {
                    txt.setStyle("-fx-background-color:transparent;-fx-text-fill:#b7c3d7");
                    txt.setEditable(false);
                    String newValor = txt.getText();//Novo valor
                    String newValor2 = null;
                    //ATUALIZAR
                    if (http.updateData("clientes", campo, newValor, String.valueOf(whereValor)) == true) {//SERVER UPDATE
                        try {
                            OpenConnection opa = new OpenConnection();
                            Connection con = opa.getConnetion();
                            String sql = null;
                            PreparedStatement st = null;

                            if (campo.equals("nome")) {
                                sql = "UPDATE cliente SET nome_cliente=? WHERE id_cliente=?";
                                newValor2 = newValor;
                            } else if (campo.equals("cpf")) {
                                sql = "UPDATE cliente SET cpf_cliente=? WHERE id_cliente=?";
                                newValor2 = newValor.replaceAll("[^0-9]", "").trim();
                            } else if (campo.equals("tel")) {
                                sql = "UPDATE cliente SET telefone_cliente=? WHERE id_cliente=?";
                                newValor2 = newValor.replaceAll("[^0-9]", "").trim();
                            } else if (campo.equals("data")) {
                                sql = "UPDATE cliente SET data_compra= ? WHERE id_cliente=? ";
                                SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
                                newValor2 = newValor;
                            }
                            st = con.prepareStatement(sql);

                            st.setString(1, newValor2);
                            st.setInt(2, whereValor);
                            st.execute();
                            st.close();
                            con.close();

                        } catch (SQLException ex) {
                        }

                    } else {
                        JOptionPane.showMessageDialog(null, "Algo deu errado");
                    }
                }//if-fim
            });
        });

        return txt;
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
                map.put(cpf, id_cliente);//ADICIONANDO O ID DO CLIENTE NA HASHMAP
                pnItems.getChildren().add(addItems(nome, cpf, tel, datacompra));
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
        String sql = null;
         OpenConnection opa = new OpenConnection();
          Connection con = opa.getConnetion();
        try {
           
            ResultSet rs = null;
            if (rdnome.isSelected()) {
                sql = "SELECT nome_cliente,cpf_cliente,telefone_cliente,data_compra FROM cliente WHERE nome_cliente  LIKE ?";
            } else if (rddata.isSelected()) {
                sql = "SELECT nome_cliente,cpf_cliente,telefone_cliente,data_compra FROM cliente WHERE data_compra  LIKE ?";
            } else if (rdcpf.isSelected()) {
                sql = "SELECT nome_cliente,cpf_cliente,telefone_cliente,data_compra FROM cliente WHERE cpf_cliente  LIKE ?";
            } else {
                sql = "SELECT nome_cliente,cpf_cliente,telefone_cliente,data_compra FROM cliente WHERE nome_cliente  LIKE ?";
            }

            PreparedStatement stm = con.prepareStatement(sql);
            stm.setString(1, palavra + "%");
            rs = stm.executeQuery();
            pnItems.getChildren().clear();//Removendo itens da VBox 
            while (rs.next()) {
                nome = rs.getString("nome_cliente");
                cpf = rs.getString("cpf_cliente");
                tel = rs.getString("telefone_cliente");
                datacompra = rs.getString("data_compra");

                pnItems.getChildren().add(addItems(nome, cpf, tel, datacompra));
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
