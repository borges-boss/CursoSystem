package cursosystem;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;

import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javax.swing.JOptionPane;

//NESTA CLASSE ESTAO CONTIDOS TODOS OS METODOS DA TELA DE CONSULTA :"ancConsulta"
public class FXMLConsultaController implements Initializable {

    ancLoader load = new ancLoader();

    @FXML
    private TableView<ConsultaData> tableconsulta;

    @FXML
    private TableColumn<ConsultaData, String> nome;

    @FXML
    private TableColumn<ConsultaData, String> cpf;

    @FXML
    private TableColumn<ConsultaData, String> telefone;

    @FXML
    private Label lblnome;

    @FXML
    private GridPane gridnome;

    @FXML
    private RadioButton rdnome, rdcpf, rdtel;

    @FXML
    private TextField txtpesq;

    //Mostrar tela de ediçao
    @FXML
    private void showEditScreen(ActionEvent event) throws IOException {

//load.anotherScreenLoader("popupConsulta.fxml");
        Label l = new Label();
        Label lbltel = new Label();
        Label alt = new Label();
        Label nomealt = new Label();
        Label title = new Label();
        Label oldname = new Label();
        Label oldcpf = new Label();
        Label oldtel = new Label();
        String altername = "";
        Label lblcpf = new Label();
        Stage stage = new Stage();
        VBox v = new VBox();
        AnchorPane a = new AnchorPane();
        GridPane grid = new GridPane();
        GridPane desc = new GridPane();
        TextField txtnome = new TextField();
        TextField txtcpf = new TextField();
        TextField txttel = new TextField();
        Button btnalte = new Button();
        Button btncancel = new Button();

        //getting informations from the table
        TablePosition pos = tableconsulta.getSelectionModel().getSelectedCells().get(0);
        int p = pos.getRow();
        ConsultaData ag = tableconsulta.getItems().get(p);
        TableColumn namet = nome;
        TableColumn cpft = cpf;
        TableColumn telt = telefone;
        //valores da tabela
        String nometable = (String) namet.getCellObservableValue(ag).getValue();
        String cpftable = (String) cpft.getCellObservableValue(ag).getValue();
        String teltable = (String) telt.getCellObservableValue(ag).getValue();

        //valores a serem modificados
        altername = nometable;
        txtnome.setText(nometable);
        txtcpf.setText(cpftable);
        txttel.setText(teltable);
        //valores originais
        oldname.setText(nometable);
        oldcpf.setText(cpftable);
        oldtel.setText(teltable);

//settin TextField configs
        txtnome.setAlignment(Pos.CENTER_LEFT);
        txtcpf.setAlignment(Pos.CENTER_LEFT);
        txttel.setAlignment(Pos.CENTER_LEFT);

        /*
*setting Button configs

         */
        //Button configs
        btnalte.setText("Concluir alteraçoes");
        //Event listener

        btnalte.setOnAction(new EventHandler<ActionEvent>() {//EVENTO CONTEM A LOGICA PARA DAR UPDATE NO DB LOCAL
            @Override
            public void handle(ActionEvent ex) {
                Http update = new Http();
                LocalOperations dw = new LocalOperations();
                boolean name = false, cpf = false, tel = false;

                if (txtnome.getText() != oldname.getText()) {
                    name = update.updateData("clientes", "nome", txtnome.getText().toString(), oldcpf.getText());
                    oldname.setText(txtnome.getText());
                    dw.updateClientsLocalData("nome", txtnome.getText(), oldcpf.getText());
                }
                if (txtcpf.getText() != oldcpf.getText()) {
                    cpf = update.updateData("clientes", "cpf", txtcpf.getText(), oldcpf.getText());
                    dw.updateClientsLocalData("cpf", txtcpf.getText(), oldcpf.getText());
                    oldcpf.setText(txtcpf.getText());

                }
                if (txttel.getText() != oldtel.getText()) {
                    tel = update.updateData("clientes", "tel", txttel.getText(), oldcpf.getText());
                    oldtel.setText(txttel.getText());
                    dw.updateClientsLocalData("tel", txttel.getText(), oldcpf.getText());

                }
                if (name == true || cpf == true || tel || true) {
                    Thread thread = new Thread(new Runnable() {

                        @Override
                        public void run() {
                            Runnable updater = new Runnable() {

                                @Override
                                public void run() {
                                    JOptionPane.showMessageDialog(null, "Alteraçao concluida");

                                }
                            };

                            // UI update is run on the Application thread
                            Platform.runLater(updater);

                        }

                    });
                    thread.start();
                } else {
                    JOptionPane.showMessageDialog(null, "Erro");
                }
            }

        });

        btncancel.setText("Cancelar");
        btncancel.setLayoutX(434);
        btncancel.setLayoutY(259);
        //Event listener
        btncancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent ex) {
                stage.close();

            }

        });


        /*
*setting Label configs

         */
//Nome
        l.setPrefSize(50, 50);
        l.setAlignment(Pos.CENTER);
        l.setTextAlignment(TextAlignment.LEFT);
        l.setFont(Font.font("Arial", 12));
        l.setStyle("-fx-text-fill: white");
        l.setText("Nome");

//CPF
        lblcpf.setPrefSize(50, 50);
        lblcpf.setAlignment(Pos.CENTER);
        lblcpf.setTextAlignment(TextAlignment.LEFT);
        lblcpf.setFont(Font.font("Arial", 12));
        lblcpf.setStyle("-fx-text-fill: white");

        lblcpf.setText("CPF");

//Telefone 
        lbltel.setPrefSize(50, 50);
        lbltel.setAlignment(Pos.CENTER_LEFT);
        lbltel.setTextAlignment(TextAlignment.LEFT);
        lbltel.setFont(Font.font("Arial", 12));
        lbltel.setStyle("-fx-text-fill: white");
        lbltel.setText("Telefone");

//Oldname
        oldname.setPrefSize(100, 50);
        oldname.setAlignment(Pos.CENTER);
        oldname.setTextAlignment(TextAlignment.CENTER);
        oldname.setFont(Font.font("Arial", 12));
        oldname.setStyle("-fx-text-fill: white");

//Oldcpf
        oldcpf.setPrefSize(100, 50);
        oldcpf.setAlignment(Pos.CENTER);
        oldcpf.setTextAlignment(TextAlignment.CENTER);
        oldcpf.setFont(Font.font("Arial", 12));
        oldcpf.setStyle("-fx-text-fill: white");

//Oldtel
        oldtel.setPrefSize(100, 50);
        oldtel.setAlignment(Pos.CENTER);
        oldtel.setTextAlignment(TextAlignment.CENTER);
        oldtel.setFont(Font.font("Arial", 12));
        oldtel.setStyle("-fx-text-fill: white");

        //Geral
        alt.setStyle("-fx-text-fill: white");
        alt.setFont(Font.font("Arial", 14));
        alt.setText("Voce esta alterando:");

        nomealt.setStyle("-fx-text-fill: white");
        nomealt.setFont(Font.font("Impact", 14));
        nomealt.setText(altername);

        title.setText("Alteraçao");
        title.setFont(Font.font("Arial", 18));
        title.setStyle("-fx-text-fill: white");
        title.setLayoutX(192);
        title.setLayoutY(14);

        /*
*setting configs and adding items to Grid

         */
//Grid Position
        grid.setLayoutX(120);
        grid.setLayoutY(103);
        grid.setGridLinesVisible(false);

//Grid items
        grid.setAlignment(Pos.CENTER);
        grid.prefWidth(322);
        grid.prefHeight(181);
        grid.add(l, 0, 0);
        grid.add(txtnome, 1, 0);
        grid.add(lblcpf, 0, 1);
        grid.add(txtcpf, 1, 1);
        grid.add(lbltel, 0, 2);
        grid.add(txttel, 1, 2);
        grid.add(btnalte, 1, 3);
        grid.add(oldname, 2, 0);
        grid.add(oldcpf, 2, 1);
        grid.add(oldtel, 2, 2);
        stage.setTitle("PopUp");

        desc.add(alt, 0, 0);
        desc.add(nomealt, 1, 0);
        desc.setLayoutX(14);
        desc.setLayoutY(58);

//Anchor pane
        a.setStyle("-fx-background-color:black");
        a.getChildren().add(grid);
        a.getChildren().add(desc);
        a.getChildren().add(title);
        a.getChildren().add(btncancel);
        Scene scene = new Scene(a, 500, 306);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent e) {
                stage.close();
            }

        });

    }

    //O EVENTO ABAIXO TEM A FUNÇAO DE ATUALIZAR A TABELA FAZENDO O DOWNLOAD DOS DADOS NOVAMENTE
    //ATUALIZAR TABELA
    @FXML
    private void updateTable() {
        DownloadData dw = new DownloadData();
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                Runnable updater = new Runnable() {

                    @Override
                    public void run() {
                        dw.downloadClientsData();
                        dw.downloadAgendaData();
                        dw.downloadCursosData();
                        
                        configTable();

                    }
                };

                // UI update is run on the Application thread
                Platform.runLater(updater);
            }

        });
        thread.start();

    }

    //DELETE
    @FXML
    private void deleteData(ActionEvent event) {//NESTE EVENTO ESTAO CONTIDOS OS METODOS PARA EXCLUSAO DE DADOS DA TABELA
        LocalOperations op = new LocalOperations();
        Http h = new Http();
        TablePosition pos = tableconsulta.getSelectionModel().getSelectedCells().get(0);
        int p = pos.getRow();
        ConsultaData ag = tableconsulta.getItems().get(p);
        TableColumn namet = nome;
        TableColumn cpft = cpf;
        TableColumn telt = telefone;
        //valores da tabela
        String nometable = (String) namet.getCellObservableValue(ag).getValue();
        String cpftable = (String) cpft.getCellObservableValue(ag).getValue();
        String teltable = (String) telt.getCellObservableValue(ag).getValue();

        System.out.println(nometable + cpftable + teltable);
        if (nometable != null && cpftable != null && teltable != null) {

            Thread thread = new Thread(new Runnable() {

                @Override
                public void run() {
                    Runnable updater = new Runnable() {

                        @Override
                        public void run() {
                            int resp = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja fazer esta exclusao ?\n Obs:Ao exluir este cliente voce tambem excluira todos os agendamentos relacionados a ele(a)", "Warning", JOptionPane.YES_NO_OPTION);
                            if (resp == JOptionPane.YES_OPTION) {
                                int id =4;//CLIENTE ID
                                h.deleteData("agenda", String.valueOf(id));//EXCLUINDO DE AGENDA SERVER
                                //op.deleteAgendaLocalData(id);//EXCLUINDO DE AGENDA LOCAL
                                        
                                boolean r = h.deleteData("clientes", String.valueOf(id));//EXCLUINDO DE CLIENTES SERVER
                                op.deleteClientsLocalData(id);//EXCLUINDO DE CLIENTES LOCAL

                                if (r == true) {
                                    JOptionPane.showMessageDialog(null, "Excluido com sucesso!");
                                    DownloadData dw = new DownloadData();
                                    tableconsulta.refresh();
                                }
                                if (r == false) {
                                    JOptionPane.showMessageDialog(null, "Excluido com sucesso!", "Aviso", JOptionPane.OK_OPTION);
                                }

                            }
                            if (resp == JOptionPane.NO_OPTION) {

                            }
                        }
                    };

                    // UI update is run on the Application thread
                    Platform.runLater(updater);

                }

            });
            thread.start();
        }

    }

    @FXML
    private void getCliente(MouseEvent e) {

    }

    //PESQUISA NO BD LOCAL
    @FXML
    private void buscarDados() {
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                Runnable updater = new Runnable() {

                    @Override
                    public void run() {

                        LocalOperations p = new LocalOperations();
                        String filtro = null, palavra = null;
                        nome.setCellValueFactory(new PropertyValueFactory<>("Nome"));
                        cpf.setCellValueFactory(new PropertyValueFactory<>("Cpf"));
                        telefone.setCellValueFactory(new PropertyValueFactory<>("tel"));

                        if (rdnome.isSelected()) {
                            filtro = "SELECT nome_cliente,cpf_cliente,telefone_cliente FROM cliente WHERE nome_cliente  LIKE ?";
                            palavra = txtpesq.getText();
                        } else if (rdcpf.isSelected()) {
                            filtro = "SELECT nome_cliente,cpf_cliente,telefone_cliente FROM cliente WHERE cpf_cliente  LIKE ?";
                            palavra = txtpesq.getText();
                        } else if (rdtel.isSelected()) {
                            filtro = "SELECT nome_cliente,cpf_cliente,telefone_cliente FROM cliente WHERE telefone_cliente  LIKE ?";
                            palavra = txtpesq.getText();
                        } else {
                            filtro = "SELECT nome_cliente,cpf_cliente,telefone_cliente FROM cliente WHERE nome_cliente  LIKE ?";
                            palavra = txtpesq.getText();
                        }

                        //tableconsulta.setItems(p.search(filtro, palavra));
                        tableconsulta.refresh();

                    }
                };

                // UI update is run on the Application thread
                Platform.runLater(updater);

            }

        });
        thread.start();

    }
    //AGENDAR UM CURSO
    @FXML
    private void openAgendamento(ActionEvent event) {
 
        AnchorPane a = new AnchorPane();
        Stage stage = new Stage();
        Label indica = new Label();
        Label lblnome = new Label();
        Label lbldata = new Label();
        Label lblhora = new Label();
        Label lbltitle = new Label();
        Label lblcurso = new Label();
        ComboBox cb = new ComboBox();
        GridPane gp = new GridPane();
        Button btndone = new Button();
        DatePicker dp = new DatePicker();
        TextField txthora = new TextField();

        LocalOperations op = new LocalOperations();
        Http h = new Http();

        String nomeagenda = null;

        //OBTENDO INFORMAÇOES DA TABELA
        TablePosition pos = tableconsulta.getSelectionModel().getSelectedCells().get(0);
        int p = pos.getRow();
        ConsultaData ag = tableconsulta.getItems().get(p);

        TableColumn namet = nome;
        TableColumn cpft = cpf;
        TableColumn telt = telefone;
        //valores da tabela
        String nometable = (String) namet.getCellObservableValue(ag).getValue();
        String cpftable = (String) cpft.getCellObservableValue(ag).getValue();
        String teltable = (String) telt.getCellObservableValue(ag).getValue();

        /**
         * @param args the command line arguments 
         * 
         * //btndone-event
         * O EVENTO DE btndone DEVE CHAMAR OS METODOS NECESSARIOS PARA FAZER A INSERÇAO NA TABELA agenda_cursos 
         * DO BD LOCAL E DO BD REMOTO.
         * 
         * //getIdCliente()
         * OS PARAMETROS DO METODO getIdCliente FUNCIONAM COMO UMA "Primary key" TEMPORARIA APENAS PARA ENCONTRAR 
         * O ID DO CLIENTE OU COD DO CURSO.
         * 
         * //insertAgendaData()
         * O METODO insertAgendaData() TEM A FUNÇAO DE ENVIAR OS DADOS DO AGENDAMENTO PARA O BANCO DE DADOS REMOTO
         * E RETORNAR O A PRIMARY KEY DO DADO INSERIDO NO SERVIDOR(id_agenda) QUE SERA USADO NA INSERÇAO DE DADOS LOCAL.
         *
         *  //updateAgendaCadastro()
         * O METODO updateAgendaCadastro() TEM A FUNÇAO DE ATUALIZAR OS DADOS DA AGENDA NO BD LOCAL
         * 
         */
        btndone.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent ex) {

                String hora = txthora.getText();
                LocalDate data = dp.getValue();
                String curso = cb.getSelectionModel().getSelectedItem().toString();//COMBOBOX
                int cursoSend = 4;
                int idCliente = 8;//OBTEM O ID PARA SER ENVIADO AO SERVER
                System.out.println(curso);
                System.out.println(cursoSend);
                String id = h.insertAgendaData(idCliente, data.toString(), hora, String.valueOf(cursoSend));//ID DO AGENDAMENTO
                boolean retorno=false;
                
                if(id!=null && retorno==true){
                JOptionPane.showMessageDialog(null, "Agendamento concluido");
                stage.close();
                
                }
                else{
                JOptionPane.showMessageDialog(null, "Algo deu errado");
                
                }

            }

        });

        nomeagenda = nometable;

        //FRONT-END STUFF
        //Generic Labels
        indica.setStyle("-fx-text-fill: white");
        indica.setFont(Font.font("Arial", 14));
        indica.setText("Agendar um curso para:");
        indica.setStyle("-fx-text-fill: white");
        //name label
        lblnome.setStyle("-fx-text-fill: white");
        lblnome.setFont(Font.font("Impact", 14));
        lblnome.setText(nomeagenda);
        //date label
        lbldata.setText("Data");
        lbldata.setFont(Font.font("Arial", 14));
        lbldata.setStyle("-fx-text-fill: white");
        lbldata.setLayoutX(135);
        lbldata.setLayoutY(153);
        //hora label
        lblhora.setText("Horario");
        lblhora.setFont(Font.font("Arial", 14));
        lblhora.setStyle("-fx-text-fill: white");
        lblhora.setLayoutX(134);
        lblhora.setLayoutY(107);
        //curso label
        lblcurso.setText("Curso");
        lblcurso.setFont(Font.font("Arial", 14));
        lblcurso.setStyle("-fx-text-fill: white");
        lblcurso.setLayoutX(131);
        lblcurso.setLayoutY(196);
        //title
        lbltitle.setText("Agendamento");
        lbltitle.setPrefHeight(57);
        lbltitle.setPrefWidth(163);
        lbltitle.setLayoutX(168);
        lbltitle.setLayoutY(0);
        lbltitle.setStyle("-fx-text-fill: white");
        lbltitle.setFont(Font.font("Arial", 24));

        //DatePicker
        dp.setPrefHeight(25);
        dp.setPrefWidth(149);
        dp.setLayoutX(188);
        dp.setLayoutY(148);

        //TextField
        txthora.setLayoutX(186);
        txthora.setLayoutY(103);
        txthora.setPromptText("00:00");
        txthora.setPrefHeight(25);
        txthora.setPrefWidth(149);

        //ComboBox
        cb.setLayoutX(187);
        cb.setLayoutY(191);
        cb.setPrefWidth(149);
        cb.setPrefHeight(25);
        cb.setPromptText("Cursos");
//        cb.getItems().addAll(op.fillCursoComboBox());//ADICIONNADO ITEMS
        cb.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent ex) {

            }
        });

        //Button
        btndone.setText("Concluir");
        btndone.setLayoutX(175);
        btndone.setLayoutY(271);
        btndone.setPrefHeight(34);
        btndone.setPrefWidth(149);

        //GridPane
        gp.setLayoutX(25);
        gp.setLayoutY(65);
        gp.add(indica, 0, 0);
        gp.add(lblnome, 1, 0);

        //Anchor pane
        a.setStyle("-fx-background-color:black");
        a.getChildren().add(gp);
        a.getChildren().add(lbltitle);
        a.getChildren().add(lbldata);
        a.getChildren().add(lblhora);
        a.getChildren().add(txthora);
        a.getChildren().add(btndone);
        a.getChildren().add(lblcurso);
        a.getChildren().add(cb);
        a.getChildren().add(dp);
        Scene scene = new Scene(a, 468, 319);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();

    }

    public void configTable() {
        LocalOperations p = new LocalOperations();
        //setting the columns
        nome.setCellValueFactory(new PropertyValueFactory<>("Nome"));
        cpf.setCellValueFactory(new PropertyValueFactory<>("Cpf"));
        telefone.setCellValueFactory(new PropertyValueFactory<>("tel"));
        //config the selection mode
        //tableconsulta.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        // tableconsulta.getSelectionModel().setCellSelectionEnabled(true);
//        tableconsulta.setItems(p.pupulateTableClients());
        tableconsulta.refresh();

    }

    //ESTE METODO TEM A FINALIDADE APENAS DE TESTE
    private ObservableList getInitialTableData() {
        List list = new ArrayList();

        list.add(new ConsultaData("Hello", "World", "!"));
        list.add(new ConsultaData("Maria", "0000000-00", "34124935"));
        ObservableList<ConsultaData> data = FXCollections.observableArrayList(list);

        //tableagenda.refresh();
        return data;
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                Runnable updater = new Runnable() {

                    @Override
                    public void run() {

                        configTable();

                    }
                };

                // UI update is run on the Application thread
                Platform.runLater(updater);

            }

        });
        thread.start();

    }

}
