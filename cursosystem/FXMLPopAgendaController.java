package cursosystem;

import static com.sun.deploy.uitoolkit.ToolkitStore.dispose;
import java.awt.Panel;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class FXMLPopAgendaController implements Initializable {
    LocalOperations lo=new LocalOperations();
    Map<String, Integer> map = lo.getMapCurso(); //ESTA HASHMAP DEVE RECUPERAR O CODIGO DOS CURSOS SELECIONADOS NA COMBOBOX
    ObservableList<String> horarios = lo.populateComboBoxHorario();
    

    String dataCho = null;
    int i = 0;
    DateFormat dateF = new SimpleDateFormat("yyyy-MM-dd");
    Date d = new Date();
    String dat = dateF.format(d);
    
    @FXML
    private ComboBox cmbhora, cmbCurso;

    @FXML
    private DatePicker dataage;

    @FXML
    private Label lblstatus;

    @FXML
    private TextField txtnome;

    @FXML
    private TableView<AgendaData> tablecurso;

    @FXML
    private TableColumn<AgendaData, String> nome;

    @FXML
    private TableColumn<AgendaData, String> curso;

    @FXML
    private Label lbldata, lblaviso, lblnomeaviso;

    @FXML
    private TitledPane titagenda;
    sessionUser s = new sessionUser();//FK_ID_CLIENTE

    @FXML
    private void choseDate() {
        LocalDate local = dataage.getValue();//datapicker
        dataCho = String.valueOf(local);
       //lbldata.setText(dataCho);
       populateTableAgenda(horarios.get(i), dataCho);
       lbldata.setText(dataage.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
      }

    @FXML
    private void nextTime() {

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                Runnable updater = new Runnable() {

                    @Override
                    public void run() {
                        int test = i;
                        if (test + 1 >= horarios.size()) {
                            i = 0;
                            //System.out.println("dentro do pri if:" + i);
                        }
                        if (test + 1 < horarios.size()) {
                            i++;
                            // System.out.println("Dentro do seg if:" + i);
                        }
                        populateTableAgenda(horarios.get(i), dataCho);
                        // System.out.println("Fora do if:"+i);
                    }
                };

                // UI update is run on the Application thread
                Platform.runLater(updater);

            }

        });
        thread.start();

    }

    @FXML
    private void prevTime() {
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                Runnable updater = new Runnable() {

                    @Override
                    public void run() {
                        int test = i;

                        if (test - 1 < 0) {
                            i = horarios.size() - 1;
                        } else {
                            i--;
                        }
                        populateTableAgenda(horarios.get(i), dataCho);

                    }
                };

                // UI update is run on the Application thread
                Platform.runLater(updater);

            }

        });
        thread.start();

    }

    @FXML
    private void agendarCurso() throws Exception {
        Http http = new Http();

        String curso = cmbCurso.getSelectionModel().getSelectedItem().toString();
        int cod_curso = map.get(curso);//RECUPERANDO O COD OD CURSO
        String data = dataage.getValue().toString();
        String hora = cmbhora.getSelectionModel().getSelectedItem().toString();
        String idAgendamento = http.insertAgendaData(s.getIdClienteAgenda(), data, hora, String.valueOf(cod_curso));

        if (idAgendamento != null) {
            if (inserirAgendamento(Integer.parseInt(idAgendamento), s.getIdClienteAgenda(), cod_curso, data, hora) == true) {
                s.setIdClienteAgenda(0);
                s.setNomeAgenda(null);
                lblstatus.setText("Agendado com sucesso!");

            } else if (inserirAgendamento(Integer.parseInt(idAgendamento), s.getIdClienteAgenda(), cod_curso, data, hora) == false) {

                lblstatus.setText("Algo deu errado");
            }

        }

    }

    private boolean inserirAgendamento(int id_agenda, int id_cliente, int cod_curso, String data, String hora) {
        try {

            OpenConnection op = new OpenConnection();

            Connection con = op.getConnetion();
            String sql = "insert into agenda_curso (id_agenda,FK_ID_CLIENTE,FK_COD_CURSO,data,hora) values (?, ?, ?, ?, ?)";
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setInt(1, id_agenda);
            stm.setInt(2, id_cliente);
            stm.setInt(3, cod_curso);
            stm.setString(4, data);
            stm.setString(5, hora);
            stm.execute();
            populateTableAgenda(hora, data);
            return true;
        } catch (Exception es) {
            es.printStackTrace();
            return false;
        }

    }

    private void populateTableAgenda(String hora, String datacurso) {
        String nome = null, curso = null;
        List list = new ArrayList();
        try {
            OpenConnection op = new OpenConnection();
            Connection con = op.getConnetion();
            ResultSet rs = null;
            String sql = "SELECT * FROM agenda_curso INNER JOIN cliente ON agenda_curso.FK_ID_CLIENTE=cliente.id_cliente INNER JOIN cursos ON agenda_curso.FK_COD_CURSO=cursos.cod_curso WHERE strftime('%Y-%m-%d',data) in ( ? ) AND hora=?";
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setString(1, datacurso);
            stm.setString(2, hora);
            rs = stm.executeQuery();

            while (rs.next()) {
                nome = rs.getString("nome_cliente");
                curso = rs.getString("nome_curso");

                list.add(new AgendaData(nome, curso));

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        this.nome.setCellValueFactory(new PropertyValueFactory<>("Nome"));
        this.curso.setCellValueFactory(new PropertyValueFactory<>("curso"));
        ObservableList<AgendaData> data = null;
        //Reaproveitando a observable list "horarios" para definir o titulo do TitlePane
        if (horarios.isEmpty()) {
            titagenda.setText("00");
        } else {
            titagenda.setText(horarios.get(i));
        }
        data = FXCollections.observableArrayList(list);
        tablecurso.setItems(data);

    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
     
       cmbhora.setItems(horarios);
        cmbCurso.setItems(lo.populateComboBoxCurso());
        if (horarios.isEmpty()) {
            lblaviso.setVisible(true);
            horarios = FXCollections.observableArrayList("00:00");
            populateTableAgenda(horarios.get(0), dat);
        } else {
            populateTableAgenda(horarios.get(0), dat);
        }

        dataCho = dat;
        txtnome.setText(s.getNomeAgenda());
        DateFormat form = new SimpleDateFormat("dd/MM/yyyy");
        dat = form.format(d);
        lbldata.setText(dat);

    }

}
