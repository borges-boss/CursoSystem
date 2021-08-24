/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cursosystem;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class FXMLPopHistoryController implements Initializable {

    @FXML
    private TableView tableHistory;

    @FXML
    private TableColumn columunnome;

    @FXML
    private TableColumn columuncurso;

    @FXML
    private TableColumn columunhora;

    @FXML
    private TableColumn columundata;
    
    @FXML
    private TextField txtpalavra;
    
    @FXML
    private void buscar(){
    search(txtpalavra.getText());
    
    }

    private void populateTableHistory() {
        List list = new ArrayList();
        try {
            OpenConnection op = new OpenConnection();
            Connection con = op.getConnetion();
            ResultSet rs = null;
            String sql = "SELECT * FROM agenda_curso INNER JOIN cliente ON agenda_curso.FK_ID_CLIENTE=cliente.id_cliente INNER JOIN cursos ON agenda_curso.FK_COD_CURSO=cursos.cod_curso ";
            PreparedStatement stm = con.prepareStatement(sql);

            rs = stm.executeQuery();

            while (rs.next()) {
                list.add(new AgendaData(rs.getString("nome_cliente"), rs.getString("nome_curso"), rs.getString("data"), rs.getString("hora")));
            }

        } catch (SQLException e) {
        }
        columunnome.setCellValueFactory(new PropertyValueFactory<>("Nome"));
        columuncurso.setCellValueFactory(new PropertyValueFactory<>("Curso"));
        columunhora.setCellValueFactory(new PropertyValueFactory<>("Hora"));
        columundata.setCellValueFactory(new PropertyValueFactory<>("Data"));
        ObservableList<AgendaData> data = FXCollections.observableArrayList(list);
        tableHistory.setItems(data);
    }

    private void search(String palavra) {
        String nome = null, cpf = null, tel = null, datacompra = null;
        List list = new ArrayList();
        String sql = null;
        try {
            OpenConnection opa = new OpenConnection();
            Connection con = opa.getConnetion();
            ResultSet rs = null;
            sql = "SELECT * FROM agenda_curso INNER JOIN cliente ON agenda_curso.FK_ID_CLIENTE=cliente.id_cliente INNER JOIN cursos ON agenda_curso.FK_COD_CURSO=cursos.cod_curso WHERE nome_cliente LIKE ?";

            PreparedStatement stm = con.prepareStatement(sql);
            stm.setString(1, palavra + "%");
            rs = stm.executeQuery();

            while (rs.next()) {
                list.add(new AgendaData(rs.getString("nome_cliente"), rs.getString("nome_curso"), rs.getString("data"), rs.getString("hora")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        ObservableList<AgendaData> d = FXCollections.observableArrayList(list);
        tableHistory.setItems(d);

    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        populateTableHistory();
    }

}
