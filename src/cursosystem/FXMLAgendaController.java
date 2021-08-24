package cursosystem;

import static com.sun.javafx.scene.control.skin.Utils.getResource;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.application.Preloader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableRow;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.paint.Paint;
import javax.swing.JOptionPane;

public class FXMLAgendaController implements Initializable {

    /*
 NESTA CLASSE ESTAO CONTIDOS TODOS OS METODOS DE CONTROLE DO LAYOUT ancAgenda.fxml   
     */
    LocalOperations op = new LocalOperations();
    ancLoader ac = new ancLoader();
    Config configurar = new Config();
    String[] monthName = {"Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"};

    LocalDate date = null;
    Calendar cal = Calendar.getInstance();
    public static Map<String, Integer> mapAgendamento = new HashMap<>();

    int yearCalendar = cal.get(Calendar.YEAR), monthCalendar = cal.get(Calendar.MONTH), dayCalendar = cal.get(Calendar.DAY_OF_MONTH);

    @FXML
    private GridPane calendar;

    @FXML
    private Text lblmonthtop;

    @FXML
    private Text lblyear;

    @FXML
    private Label label29;

    @FXML
    private Label lblmonth;

    @FXML
    private Text lblmesano;

    @FXML
    private Text lbldayofmonth;

    @FXML
    private ImageView btnprev, btnnext;

    @FXML
    private VBox cursosVbox;

    ancLoader loader = new ancLoader();
    private String oldcurso = null, oldhora = null, oldnome = null;

    private int countRegistroCell = 0;

    private List<Integer> diasMarcados = new ArrayList();

    //Calendario
    @FXML
    private void openHistory() {
        loader.anotherScreenLoader("FXMLDocuments/popHistory.fxml");

    }

    @FXML
    private void nextMonth(ActionEvent event) {
        calendar.getChildren().removeAll(calendar.lookupAll(".label"));//SOLUÇAO
        calendar.getChildren().removeAll(calendar.lookupAll(".image-view"));
        monthCalendar++;
        populateCalendar();

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                Runnable updater = new Runnable() {

                    @Override
                    public void run() {
                        lblmonthtop.setText(monthName[monthCalendar]);

                    }
                };

                // UI update is run on the Application thread
                Platform.runLater(updater);

            }

        });
        thread.start();
    }

    @FXML
    private void prevMonth() {
        calendar.getChildren().removeAll(calendar.lookupAll(".label"));//SOLUÇAO
        calendar.getChildren().removeAll(calendar.lookupAll(".image-view"));
        monthCalendar--;
        populateCalendar();
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                Runnable updater = new Runnable() {

                    @Override
                    public void run() {
                        lblmonthtop.setText(monthName[monthCalendar]);

                    }
                };

                // UI update is run on the Application thread
                Platform.runLater(updater);

            }

        });
        thread.start();

    }

    @FXML
    private void mousePressedUp() {
        javafx.scene.image.Image image = new javafx.scene.image.Image(getClass().getResource("/images/arrow-up-01-512.png").toExternalForm());
        btnprev.setImage(image);

    }

    @FXML
    private void mouseReleasedUp() {
        javafx.scene.image.Image image = new javafx.scene.image.Image(getClass().getResource("/images/arrow-up-white.png").toExternalForm());
        btnprev.setImage(image);

    }

    @FXML
    private void mousePressedDown() {
        javafx.scene.image.Image imaged = new javafx.scene.image.Image(getClass().getResource("/images/arrow-down.png").toExternalForm());
        btnnext.setImage(imaged);

    }

    @FXML
    private void mouseReleasedDown() {
        javafx.scene.image.Image imaged = new javafx.scene.image.Image(getClass().getResource("/images/arrow-down-white.png").toExternalForm());
        btnnext.setImage(imaged);

    }

    private int[] calendarFill(int month) {
        int days[] = new int[30];
        OpenConnection op = new OpenConnection();
        try {
            ResultSet rs = null;
            String sql = "SELECT data,strftime('%d',data) FROM agenda_curso WHERE strftime('%m',data) in ( ? )";

            int i = 0;
            Connection con = op.getConnetion();
            PreparedStatement stm = con.prepareStatement(sql);

            //System.out.println("SQL:" + String.format("%02d", month));
            stm.setString(1, String.format("%02d", month));
            rs = stm.executeQuery();

            while (rs.next()) {
                days[i] = rs.getInt("strftime('%d',data)");
                i++;

            }

        } catch (SQLException e) {
        }

        return days;
    }

    public ObservableList infoCalendar(int day, int month) {
        List list = new ArrayList();
        OpenConnection op = new OpenConnection();

        try {
            ResultSet rs = null;
            String sql = "SELECT * FROM agenda_curso INNER JOIN cliente ON agenda_curso.FK_ID_CLIENTE=cliente.id_cliente INNER JOIN cursos ON agenda_curso.FK_COD_CURSO=cursos.cod_curso WHERE strftime('%d',data) in ( ? ) AND strftime('%m',data) in ( ? )";
            String nome = null, hora = null, curso = null;
            Connection con = op.getConnetion();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setString(1, String.format("%02d", day));
            stm.setString(2, String.format("%02d", month));
            rs = stm.executeQuery();

            while (rs.next()) {

                nome = rs.getString("nome_cliente");
                hora = rs.getString("hora");
                curso = rs.getString("nome_curso");

                mapAgendamento.put(nome + hora + curso, rs.getInt("id_agenda"));
                list.add(new ConsultaData(nome, hora, curso, ""));
                countRegistroCell++; //Utilizado para contar a quantidade de registros em cada celula do calendario

            }
        } catch (SQLException e) {
        }

        ObservableList<ConsultaData> data = FXCollections.observableArrayList(list);
        return data;
    }

    private void updateLocalAgendaData(String data, String hora, Integer id) {

        OpenConnection op = new OpenConnection();
        try {
            ResultSet rs = null;
            String sql = "UPDATE agenda_curso SET data= ?,hora=? WHERE id_agenda=?";

            Connection con = op.getConnetion();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setString(1, data);
            stm.setString(2, hora);
            stm.setInt(3, id);
            stm.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void deleteAgendamentoLocal(Integer id) {

        OpenConnection op = new OpenConnection();
        try {
            ResultSet rs = null;
            String sql = "DELETE FROM agenda_curso WHERE id_agenda=?";

            Connection con = op.getConnetion();
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setInt(1, id);
            stm.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /*
    //O METODO ABAIXO DEVE PREENCHER A CELULA DE VERMELHO COM OS DIAS ARMAZENADOS NO BD LOCAL
    private void configCells(int[] days) {
        /*
     *O CODIGO A SEGUIR TEM A FUNÇAO DE PREENCCHER AS CELULAS DO CALENDARIO COM OS DIAS MARCADOS NO DB.
     *PARA ADICIONAR O BOTAO NA POSIÇAO CORRESPONDENTE DO CALENDARIO E NECESSARIO
     *DIMINUIR O VALOR DO VETOR days[] PELO VALOR DA PRIMEIRA COLUNA DE CADA LINHA
     *
     */
 /*
        for (int i = 0; i < days.length; i++) {
            if (days[i] >= 1 && days[i] <= 7) {

                calendar.add(eventoDayCell(new Button(String.valueOf(days[i])), days[i], mesNum + 1), days[i] - 1, 0);
            }
            if (days[i] >= 8 && days[i] <= 14) {

                calendar.add(eventoDayCell(new Button(String.valueOf(days[i])), days[i], mesNum + 1), days[i] - 8, 1);

            }
            if (days[i] >= 15 && days[i] <= 21) {

                calendar.add(eventoDayCell(new Button(String.valueOf(days[i])), days[i], mesNum + 1), days[i] - 15, 2);

            }
            if (days[i] >= 22 && days[i] <= 28) {

                calendar.add(eventoDayCell(new Button(String.valueOf(days[i])), days[i], mesNum + 1), days[i] - 22, 3);

            }

            if (days[i] >= 29 && days[i] <= 31) {

                calendar.add(eventoDayCell(new Button(String.valueOf(days[i])), days[i], mesNum + 1), days[i] - 29, 4);

            }
        }
    }

     */
    //New Sreen 2.0
    private Label configCell(int content, Label label, int day, int month) {
        Calendar ca = Calendar.getInstance();
        LocalOperations lo = new LocalOperations();
        Map<String, Integer> mapCurso = lo.getMapCurso();
        ObservableList<String> cursos = lo.populateComboBoxCurso();
        ObservableList<String> horario = lo.populateComboBoxHorario();
        Http http = new Http();

        try {
            //Este metodo foi invocado com o objetivo de ativar o contador countRegistroCell
            //countRegistroCell deve armazenar temporariamente a quantidade de registros de cada celula do calendario
            //A quantidade de registros deve ser armazenada temporariamente com a intençao de adicionar a ImageView do metodo statusCell()
            infoCalendar(day, month);

            ca.set(yearCalendar, month - 1, day);
            String color = "";
            int sunday = ca.get(Calendar.DAY_OF_WEEK);

            if (sunday == 1) {
                color = "red";
            } else {
                color = "white";
            }

            //Size    
            label.setPrefWidth(99);
            label.setPrefHeight(93);

            label.setStyle("-fx-background-color:transparent;-fx-text-fill:" + color + ";");
            label.setFont(Font.font("Cambria", 12));
            label.setAlignment(Pos.CENTER);
            calendar.setHalignment(label, HPos.CENTER);
            label.setText(String.valueOf(content));

            label.setOnMouseClicked(event -> {

                LocalOperations op = new LocalOperations();
                AnchorPane a = new AnchorPane();
                Stage stage = new Stage();
                Text lblshow = new Text();
                Text txtdata = new Text();
                Label lblstatus = new Label();
                TableView<ConsultaData> tableView = new TableView();
                TableColumn<ConsultaData, String> nameAge = new TableColumn();
                TableColumn<ConsultaData, String> hora = new TableColumn();
                TableColumn<ConsultaData, String> curso = new TableColumn();
                Button remarcar = new Button();
                Button excluir = new Button();
                Button confirm = new Button();
                DatePicker newdata = new DatePicker();
                TextField txtnome = new TextField();
                TextField cmbcurso = new TextField();
                TextField txthora=new TextField();
                ComboBox cmbhora = new ComboBox();

                tableView.setOnMouseClicked(mc -> {
                    TablePosition tbp = tableView.getSelectionModel().getSelectedCells().get(0);
                    int row = tbp.getRow();
                    ConsultaData cd = tableView.getItems().get(row);
                    oldnome = (String) nameAge.getCellObservableValue(cd).getValue();
                    oldhora = (String) hora.getCellObservableValue(cd).getValue();
                    oldcurso = (String) curso.getCellObservableValue(cd).getValue();

                });

                //Buttons
                remarcar.setText("Remarcar");
                remarcar.setPrefHeight(48);
                remarcar.setPrefWidth(99);
                remarcar.getStylesheets().add("css/style.css");
                remarcar.setStyle("-fx-background-radius:20px;-fx-border-color:#00baff;-fx-border-radius:20px");
                remarcar.setLayoutX(422);
                remarcar.setLayoutY(65);

                //EVENTO REMARCAR
                remarcar.setOnAction(evnet -> {
                    TablePosition tbp = tableView.getSelectionModel().getSelectedCells().get(0);
                    int row = tbp.getRow();
                    ConsultaData cd = tableView.getItems().get(row);
                    int flag = 0, i = 0;
                    oldnome = (String) nameAge.getCellObservableValue(cd).getValue();
                    oldhora = (String) hora.getCellObservableValue(cd).getValue();
                    oldcurso = (String) curso.getCellObservableValue(cd).getValue();

                    txtnome.setText(oldnome);
                    cmbcurso.setText(oldcurso);
                    txthora.setText(oldhora);
                    confirm.setDisable(false);
                    cmbhora.setDisable(false);
                    cmbcurso.setDisable(false);
                    txtnome.setDisable(false);
                    txthora.setDisable(false);
                    newdata.setDisable(false);

                });

                excluir.setText("Excluir");
                excluir.setPrefHeight(48);
                excluir.setPrefWidth(99);
                excluir.getStylesheets().add("css/style.css");
                excluir.setStyle("-fx-background-radius:20px;-fx-border-color:red;-fx-border-radius:20px");
                excluir.setLayoutX(526);
                excluir.setLayoutY(65);

                //EVENTO EXCLUSAO
                //Evento abaixo deve excluir o agendamento selecionado
                excluir.setOnAction(p -> {
                    int opc = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja excluir este agendamento ?");

                    if (opc == JOptionPane.YES_OPTION) {

                        if (http.deleteData("delete_agendamento", String.valueOf(mapAgendamento.get(oldnome + oldhora + oldcurso)))) {
                            deleteAgendamentoLocal(mapAgendamento.get(oldnome + oldhora + oldcurso));
                            tableView.setItems(infoCalendar(day, month));
                        } else {
                            JOptionPane.showMessageDialog(null, "Algo deu errado, porfavor confira sua conexao com a internet");
                        }

                    }

                });

                confirm.setText("Confirmar");
                confirm.setLayoutX(471);
                confirm.setLayoutY(369);
                confirm.setStyle("-fx-border-color:#00baff;");
                confirm.getStylesheets().add("css/style.css");
                confirm.setDisable(true);
                confirm.setPrefHeight(48);
                confirm.setPrefWidth(99);

                //EVENTO DE CONFIRMAÇAO
                confirm.setOnAction(ev -> {
                    String nome = txtnome.getText();
                    String horaup = cmbhora.getSelectionModel().getSelectedItem().toString();

                    LocalDate local = newdata.getValue();//datapicker
                    String novadata = String.valueOf(local);
                    Integer idagenda = mapAgendamento.get(nome + oldhora + oldcurso);
                    //System.err.println("id obtido: " + idagenda);

                    //ATUALIZAÇAO NO AGENDAMENTO
                    if (http.updateData("agenda_curso", "hora", horaup, String.valueOf(idagenda)) == true && http.updateData("agenda_curso", "data", novadata, String.valueOf(idagenda)) == true) {
                        updateLocalAgendaData(novadata, horaup, idagenda);
                        confirm.setDisable(true);
                        cmbhora.setDisable(true);
                        txtnome.setDisable(true);
                        cmbcurso.setDisable(true);
                        newdata.setDisable(true);
                        txthora.setDisable(true);
                        lblstatus.setText("Remarcado para dia: " + novadata);
                        oldnome = null;
                        oldhora = null;
                        oldcurso = null;
                    } else {
                        JOptionPane.showMessageDialog(null, "Algo deu errado, por favor confira sua conexao com a internet ou tente mais tarde");

                    }

                });
                //DatePicker
                newdata.setId("data");
                newdata.getStylesheets().add("css/cadastroPop.css");
                newdata.setStyle("-fx-background-color:transparent;");
                newdata.setLayoutX(445);
                newdata.setLayoutY(127);
                newdata.setPrefWidth(150);
                newdata.setPrefHeight(27);
                newdata.setDisable(true);

                txtnome.setPromptText("nome");
                txtnome.setLayoutX(445);
                txtnome.setLayoutY(193);
                txtnome.setId("txtnome");
                txtnome.getStylesheets().add("css/cadastroPop.css");
                txtnome.setPrefWidth(150);
                txtnome.setEditable(false);
                txtnome.setDisable(true);

                lblstatus.setLayoutX(440);
                lblstatus.setLayoutY(346);
                lblstatus.setTextFill(Paint.valueOf("#2eff00"));

                //TextField curso
                cmbcurso.setPromptText("Curso");
                cmbcurso.setLayoutX(445);
                cmbcurso.setLayoutY(234);
                cmbcurso.setStyle("-fx-background-color:transparent;-fx-border-radius:20px;-fx-border-color:#2A73FF;-fx-text-fill:white;");
                cmbcurso.setPrefWidth(150);
                cmbcurso.setEditable(false);
                cmbcurso.setDisable(true);

                cmbhora.setPromptText("Novo Horario");
                cmbhora.setLayoutX(445);
                cmbhora.setLayoutY(304);
                cmbhora.setStyle("-fx-background-color:transparent;-fx-border-radius:20px;-fx-border-color:#2A73FF;-fx-text-fill:white;");
                cmbhora.setPrefWidth(150);
                cmbhora.setItems(horario);
                cmbhora.setDisable(true);
                
                txthora.setStyle("-fx-background-color:transparent;-fx-border-radius:20px;-fx-border-color:#2A73FF;-fx-text-fill:white;");
                txthora.setLayoutY(270);
                txthora.setLayoutX(445);
                txthora.setDisable(true);
                txthora.setEditable(false);
                //Labels
                //label data
                lblshow.setText("Detalhes do Agendamento");
                lblshow.setFont(Font.font("System", FontWeight.BOLD, 24));
                lblshow.setFill(Paint.valueOf("white"));
                lblshow.setSmooth(true);
                lblshow.setLayoutX(14);
                lblshow.setLayoutY(34);

                //label padrao
                txtdata.setText(String.valueOf(day) + "/" + String.valueOf(month) + "/" + String.valueOf(yearCalendar));
                txtdata.setFont(Font.font("System", FontWeight.BOLD, 18));
                txtdata.setFill(Paint.valueOf("white"));
                txtdata.setSmooth(true);
                txtdata.setLayoutX(526);
                txtdata.setLayoutY(32);

                //TableView
                tableView.setLayoutX(14);
                tableView.setLayoutY(65);
                tableView.setPrefHeight(352);
                tableView.setPrefWidth(395);
                tableView.setStyle("-fx-background-color:#05071F");
                tableView.getColumns().addAll(nameAge, hora, curso);

                //Table columns
                nameAge.setText("Nome");
                hora.setText("Hora");
                curso.setText("Curso");
                nameAge.setCellValueFactory(new PropertyValueFactory<ConsultaData, String>("Nome"));
                hora.setCellValueFactory(new PropertyValueFactory<>("Hora"));
                curso.setCellValueFactory(new PropertyValueFactory<>("Cpf"));
                nameAge.setPrefWidth(132);
                hora.setPrefWidth(130);
                curso.setPrefWidth(132);

                //Anchor pane
                a.setStyle("-fx-background-color:#02030A");
                a.getChildren().add(txtdata);
                a.getChildren().add(lblshow);
                a.getChildren().add(tableView);
                a.getChildren().add(remarcar);
                a.getChildren().add(excluir);
                a.getChildren().add(newdata);
                a.getChildren().add(txtnome);
                a.getChildren().add(cmbcurso);
                a.getChildren().add(cmbhora);
                a.getChildren().add(confirm);
                a.getChildren().add(lblstatus);
                a.getChildren().add(txthora);

                Scene scene = new Scene(a, 648, 448);
                scene.getStylesheets().add("css/TableStyle.css");
                stage.setResizable(false);
                stage.setScene(scene);

                stage.show();

                tableView.setItems(infoCalendar(day, month));
            });

        } catch (Exception e) {
        }

        return label;
    }

    private Label configPrevPostCells(int content, Label label, int day, int month) {
        Calendar ca = Calendar.getInstance();
        try {
            ca.set(yearCalendar, month, day);
            String color = "";
            int sunday = ca.get(Calendar.DAY_OF_WEEK);
            if (sunday == 1) {
                color = "red";
            } else {
                color = "white";
            }

            //Size    
            label.setPrefWidth(99);
            label.setPrefHeight(93);

            label.setStyle("-fx-background-color:#434343;-fx-text-fill:" + color + ";");
            label.setFont(Font.font("Cambria", FontWeight.BOLD, 12));
            label.setAlignment(Pos.CENTER);
            calendar.setHalignment(label, HPos.CENTER);
            label.setText(String.valueOf(content));

        } catch (Exception e) {
        }

        return label;
    }

    //Este metodo tem a funçao de adicionar uma imagem a cada celua do calendario indicando quantos registros estao presentes
    //cada celula.
    private ImageView statusCell() {
        javafx.scene.image.Image image = null;
        ImageView img = new ImageView();
        double limite = 4.0;
        try {
            double calc = countRegistroCell / limite * 100;

            if (calc > 10 && calc <= 25) {
                image = new javafx.scene.image.Image(getClass().getResource("/images/status-bar-good.png").toExternalForm());
            } else if (calc > 25 && calc <= 50) {
                image = new javafx.scene.image.Image(getClass().getResource("/images/status-bar-middle.png").toExternalForm());
            } else if (calc > 50 && calc <= 75) {
                image = new javafx.scene.image.Image(getClass().getResource("/images/status-bar-almost.png").toExternalForm());
            } else if (calc >= 100) {
                image = new javafx.scene.image.Image(getClass().getResource("/images/status-bar-full.png").toExternalForm());

            } else {
                image = null;

            }
            img.setImage(image);
            img.setVisible(true);
            img.setSmooth(true);
            img.setFitHeight(11);
            img.setFitWidth(43);
            calendar.setHalignment(img, HPos.CENTER);
            calendar.setValignment(img, VPos.BOTTOM);
            calendar.setMargin(img, new Insets(0, 0, 5, 0));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return img;
    }

    private int getMaxDaysOfTheMonth(int month) {
        int limite = 0;
        GregorianCalendar c = new GregorianCalendar();
        if (month == 2) {
            //Fevereiro
            limite = 28;
            //MESES COM 30 DIAS
        } else if (month == 2 && c.isLeapYear(yearCalendar)) {
            limite = 29;

        } else if (month == 4 || month == 6 || month == 9 || month == 11) {
            //Abril, Junho, Setembro, Novembro
            limite = 30;
            //MESES COM 31 DIAS
        } else {
            limite = 31;
        }
        return limite;
    }

    private int getFirstIndex() {
        int firstIndex = 0;
        int retorno = 0;
        Calendar c = Calendar.getInstance();
        c.set(yearCalendar, monthCalendar, 1);
        //Descobrindo qual dia da semana vai cair o dia primeiro de cada mes.
        int diaPrimeiroSemana = c.get(Calendar.DAY_OF_WEEK) - 1;//Indice do dia primeiro(1) no GridPane(matriz)
        int limiteDeDias = getMaxDaysOfTheMonth(monthCalendar);
        //numeros de indices restantes
        int indicesRestantes = diaPrimeiroSemana - 1;
        retorno = diaPrimeiroSemana;
        if (indicesRestantes < 0) {

        } else {
            firstIndex = limiteDeDias - indicesRestantes;
            int col = 0;

            while (col < diaPrimeiroSemana) {
                calendar.add(configPrevPostCells(firstIndex, new Label(), firstIndex, monthCalendar - 1), col, 0);
                firstIndex++;
                col++;
            }

        }

        return retorno;
    }
//Popula as celulas do calendario
    private void populateCalendar() {
        int firstindex = getFirstIndex();
        int count1 = 0;
        for (int row = 0; row < 5; row++) {

            for (int col = firstindex; col < 7; col++) {
                count1++;
                if (count1 <= getMaxDaysOfTheMonth(monthCalendar + 1)) {//+1 por que a ordem dos meses no vetor começa de 0
                    calendar.add(configCell(count1, new Label(), count1, monthCalendar + 1), col, row);
                    calendar.add(statusCell(), col, row);
                    countRegistroCell = 0;
                } else {
                    col = 7;
                    row = 5;

                }
            }
            firstindex = 0;//Resolvido
        }

    }

    //Tela de notificaçao lateral
    private Pane addNotification(String tituloData, List content) {

        Pane basePane = new Pane();
        Text title = new Text();
        Text dia = new Text();
        Text curso1 = new Text();
        Text curso2 = new Text();
        Text curso3 = new Text();
        Text curso4 = new Text();
        Button viewDet = new Button();
        ImageView imageButton = new ImageView();

        String[] curso = {"Horario Livre", "Horario Livre", "Horario Livre", "Horario Livre"};
        basePane.setPrefHeight(113);
        basePane.setPrefWidth(230);
        basePane.setStyle("-fx-background-color:#10165F");

        title.setText("Cursos:");
        title.setFill(Paint.valueOf("white"));
        title.setFont(Font.font("System", FontWeight.BOLD, 14));
        title.setLayoutX(14);
        title.setLayoutY(25);

        dia.setText(tituloData);
        dia.setLayoutX(65);
        dia.setLayoutY(25);
        dia.setFont(Font.font("System", FontWeight.BOLD, 14));
        dia.setFill(Paint.valueOf("white"));
        int limit = content.size();
        int i = 0;

        while (i < limit) {
            curso[i] = String.valueOf(content.get(i));
           
            i++;
        }

        curso1.setText("• " + curso[0]);
        curso1.setFill(Paint.valueOf("#c6c6c6"));
        curso1.setFont(Font.font("System", 12));
        curso1.setLayoutX(18);
        curso1.setLayoutY(47);

        curso2.setText("• " + curso[1]);
        curso2.setFill(Paint.valueOf("#c6c6c6"));
        curso2.setFont(Font.font("System", 12));
        curso2.setLayoutX(18);
        curso2.setLayoutY(64);

        curso3.setText("• " + curso[2]);
        curso3.setFill(Paint.valueOf("#c6c6c6"));
        curso3.setFont(Font.font("System", 12));
        curso3.setLayoutX(18);
        curso3.setLayoutY(81);

        curso4.setText("• " + curso[3]);
        curso4.setFill(Paint.valueOf("#c6c6c6"));
        curso4.setFont(Font.font("System", 12));
        curso4.setLayoutX(18);
        curso4.setLayoutY(99);
        //ImageView
        javafx.scene.image.Image image = new javafx.scene.image.Image(getClass().getResource("/images/Go-back-icon.png").toExternalForm());
        imageButton.setImage(image);
        imageButton.setFitHeight(18);
        imageButton.setFitWidth(24);
        imageButton.setSmooth(true);
        imageButton.setPreserveRatio(true);

        //Button
        viewDet.setText("");
        viewDet.setGraphic(imageButton);
        viewDet.setPrefHeight(27);
        viewDet.setPrefWidth(30);
        viewDet.setLayoutX(183);
        viewDet.setLayoutY(4);
        viewDet.setDisable(true);

        basePane.getChildren().add(title);
        basePane.getChildren().add(dia);
        basePane.getChildren().add(curso1);
        basePane.getChildren().add(curso2);
        basePane.getChildren().add(curso3);
        basePane.getChildren().add(curso4);
        basePane.getChildren().add(viewDet);

        return basePane;
    }

    //Spacing between two Panes 
    private HBox spacing() {
        HBox space = new HBox();
        space.setPrefHeight(35);
        return space;
    }

    private List getRegistros(int dia, int mes, int ano) {
        OpenConnection op = new OpenConnection();

        List result = new ArrayList();
        try {

            
            String sql = "SELECT * FROM agenda_curso INNER JOIN cliente ON agenda_curso.FK_ID_CLIENTE=cliente.id_cliente INNER JOIN cursos ON agenda_curso.FK_COD_CURSO=cursos.cod_curso WHERE strftime('%d',data) in ( ? ) AND strftime('%m',data) in ( ? ) order by hora,nome_cliente";
            Connection con = op.getConnetion();

            PreparedStatement stm = con.prepareStatement(sql);
            stm.setString(1, String.format("%02d", dia));
            stm.setString(2, String.format("%02d", mes));
            ResultSet rs = stm.executeQuery();
            int i = 0;

            while (rs.next()) {
               
                if (rs.getString("hora") != null && i < 4) {
                    result.add(rs.getString("hora") + "-" + rs.getString("nome_cliente"));
                } else if (rs.getString("hora") == null) {
                    result.add("Horario livre");
                   
                }
                i++;

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

//Este metodo tem a funçao de descobrir os 2 dias mais proximos do dia atual
//E realizar uma consulta no banco de dados com estes dias + o mes atual
//Os valores do ArrayList diasMarcados sao obtidos na consulta do metodo infoCalendar()
    private void populateNotificationVbox() {
        OpenConnection op = new OpenConnection();
        int i = 0;
        int d = 0;
        int diaAtual = dayCalendar;
        int menorDia = 0;
        int mesAtual = monthCalendar + 1;
        int anoAtual = yearCalendar;
        int quantDiasRestantesPara = 0;
        int menorQuantDiasRestantes = 0;
        String sql = null;
        ResultSet rs = null;

        Connection con = op.getConnetion();
        try {
            sql = "select distinct strftime('%d',data) from agenda_curso where strftime('%m',data) in ( ? ) order by strftime('%d',data)";
            PreparedStatement stm = con.prepareStatement(sql);

           
            stm.setString(1, String.valueOf(String.format("%02d", mesAtual)));
            rs = stm.executeQuery();

            while (rs.next()) {

                diasMarcados.add(Integer.parseInt(rs.getString("strftime('%d',data)")));
            }
            con.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        //O valor inicial de menorQuantDiasRestantes nao pode ser 0 ou igual ao menorQuantDiasRestantes anterior
        //caso o contrario o calculo daria errado
        while (i < 2) {
            //Definindo o valor inicial de menorQuantDiasRestantes
            while (menorQuantDiasRestantes == 0) {
                if (diasMarcados.get(d) > diaAtual) {
                    quantDiasRestantesPara = diasMarcados.get(d) - diaAtual;
                    menorQuantDiasRestantes = quantDiasRestantesPara;
                    
                    break;
                } else if (diasMarcados.get(d) == diaAtual) {
                    cursosVbox.getChildren().add(addNotification("Hoje", getRegistros(diaAtual, mesAtual, anoAtual)));
                    cursosVbox.getChildren().add(spacing());
                    diasMarcados.remove(d);
                }
                d++;
            }
            //Descobrindo os 2 dias mais proximos do dia atual    
            for (d = 0; d < diasMarcados.size(); d++) {
                //dayCalendar=dia atual
              
                if (diasMarcados.get(d) > diaAtual) {
                    quantDiasRestantesPara = diasMarcados.get(d) - diaAtual;

                    if (quantDiasRestantesPara <= menorQuantDiasRestantes) {
                        menorQuantDiasRestantes = quantDiasRestantesPara;
                        menorDia = diasMarcados.get(d);

                        cursosVbox.getChildren().add(addNotification(String.valueOf(menorDia) + "/" + String.valueOf(mesAtual) + "/" + String.valueOf(anoAtual), getRegistros(menorDia, mesAtual, anoAtual)));
                        cursosVbox.getChildren().add(spacing());
                        diasMarcados.remove(d);
                        break;
                    } else {
                       
                    }

                }

            }
            d = 0;
            i++;
            menorQuantDiasRestantes = 0;
        }
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

        Calendar c = new GregorianCalendar();

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                Runnable updater = new Runnable() {

                    @Override
                    public void run() {
                        lblmonthtop.setText(monthName[monthCalendar]);
                        lblyear.setText(String.valueOf(c.get(Calendar.YEAR)));
                        int dayofmon = c.get(Calendar.DAY_OF_WEEK);
                        switch (dayofmon) {
                            case 1:
                                lbldayofmonth.setText("Domingo");
                                break;
                            case 2:
                                lbldayofmonth.setText("Segunda");
                                break;
                            case 3:
                                lbldayofmonth.setText("Terça");
                                break;
                            case 4:
                                lbldayofmonth.setText("Quarta");
                                break;
                            case 5:
                                lbldayofmonth.setText("Quinta");
                                break;
                            case 6:
                                lbldayofmonth.setText("Sexta");
                                break;
                            case 7:
                                lbldayofmonth.setText("Sabado");
                                break;
                            default:
                                break;
                        }
                        populateCalendar();
                        populateNotificationVbox();
                        getRegistros(6, 7, 2019);
                    }
                };

                // UI update is run on the Application thread
                Platform.runLater(updater);

            }

        });
        thread.start();
    }

}
