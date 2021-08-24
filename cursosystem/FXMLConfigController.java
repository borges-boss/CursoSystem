/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cursosystem;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import javax.swing.JOptionPane;

/**
 *
 * @author borge
 * 
 * CLASSE CONTROLE DAS CONFIGURAÇOES
 */
public class FXMLConfigController implements Initializable {

    AnchorPane anchor = null;
    Config config = new Config();
    static Map<String, Integer> map = new HashMap();
    static Map<String, Integer> id_bug_report = new HashMap();
    private String horaremove;
    MaskTextField txthora = new MaskTextField();
    @FXML
    private StackPane stackPane;

    @FXML
    private ColorPicker colorCalendar;

    @FXML
    private Pane pnlGeneral, pnlReport, pnlBackup, pnlAgenda;

    @FXML
    private ImageView imgokey1, imgokey;

    @FXML
    private AnchorPane anchorConfig;

    @FXML
    private ListView horarioListView = new ListView<String>();

    @FXML
    private ComboBox langbox, themebox, cmbcate;

    @FXML
    private TextField txtdesc;

    @FXML
    private TextArea taprob;

    @FXML
    private Label lblcheck1, lblcheck2;

    @FXML
    private void openGeneralConfig() {
        pnlGeneral.toFront();
    }

    @FXML
    private void openBackup() {
        pnlBackup.toFront();
    }

    @FXML
    private void openBugReport() {
        pnlReport.toFront();
    }

    @FXML
    private void openAgendaConfig() {
        pnlAgenda.toFront();
    }
    
//O METODO ABAIXO TEM A FUNÇAO DE OCULTAR A TELA DE CONFIGURAÇOES 
    @FXML
    private void backToMain() {
        try {

            Duration d = Duration.millis(1500);
            TranslateTransition tt = new TranslateTransition(d, anchorConfig);
            tt.setFromX(266.4);
            tt.setToX(1060);
            System.out.print("TESTE");

            tt.play();
            //anchorConfig.setVisible(false);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
     
    //AGENDA
    //pnlAgenda-CONFIGURAÇOES AGENDA
    private void populateListView() throws SQLException {
        List list = new ArrayList();
        OpenConnection op = new OpenConnection();
        Connection con = op.getConnetion();
        String hora = null;
        int codhora = 0;
        try {
            ResultSet rs = null;
            String sql = "SELECT * FROM horario ORDER BY hora;";
            PreparedStatement stm = con.prepareStatement(sql);
            rs = stm.executeQuery();

            while (rs.next()) {
                hora = rs.getString("hora");
                codhora = rs.getInt("cod_horario");
                list.add(hora);
                map.put(hora, codhora);

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        con.close();
        ObservableList<String> data = FXCollections.observableArrayList(list);

        horarioListView.setItems(data);

    }

    @FXML
    private void saveConfigAgenda(ActionEvent ev) {

        String hex2 = "#" + Integer.toHexString(colorCalendar.getValue().hashCode());
        config.setCalendarBackgroundColor(hex2);
        imgokey.setVisible(true);

    }

    @FXML
    private void addHora() {
        txthora.setDisable(false);

        txthora.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {

                try {
                    OpenConnection op = new OpenConnection();
                    Connection con = op.getConnetion();
                    PreparedStatement stm = null;
                    String sql = "insert into horario (hora) values (?)";
                    stm = con.prepareStatement(sql);
                    stm.setString(1, txthora.getText());
                    stm.execute();
                    txthora.setText("");
                    txthora.setDisable(true);
                    populateListView();
                    con.close();

                } catch (SQLException ex) {
                }

            }
        });

    }

    @FXML
    private void removeHora() {
        try {
            OpenConnection op = new OpenConnection();
            Connection con = op.getConnetion();
            PreparedStatement stm = null;
            String sql = "DELETE FROM horario WHERE cod_horario=?";
            int cod = map.get(horaremove);
            stm = con.prepareStatement(sql);
            stm.setInt(1, cod);
            stm.execute();
            populateListView();

        } catch (SQLException e) {
        }

    }

    //pnlGeneralConfig-CONFIGURAÇOES GERAIS
    @FXML
    private void saveGeneral() {
        imgokey1.setVisible(true);
    }

    //pnlAjdua-BUG REPORT 
    private void pupulateCategoriaBug() {
        List list = new ArrayList();
        try {
            ResultSet rs = null;
            OpenConnection op = new OpenConnection();
            Connection con = op.getConnetion();
            PreparedStatement stm = null;
            String sql = "select * from bug_report";
            stm = con.prepareStatement(sql);
            rs = stm.executeQuery();

            while (rs.next()) {
                list.add(rs.getString("nome_cate"));
                id_bug_report.put(rs.getString("nome_cate"), rs.getInt("id_cate"));

            }

        } catch (SQLException e) {
        }
        ObservableList<String> data = FXCollections.observableArrayList(list);
        cmbcate.setItems(data);

    }

    @FXML
    private void report() {
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                Runnable updater = new Runnable() {

                    @Override
                    public void run() {
                        Http h = new Http();
                        boolean status = false;
                        String id = String.valueOf(id_bug_report.get(cmbcate.getSelectionModel().getSelectedItem()));
                        String desc = null;
                        String prob = null;
                        String message = null;
                        //VALIDAÇAO
                        if (txtdesc.getText().equals("")) {
                            lblcheck1.setText("*Obrigatorio");
                            System.out.println("desc");

                        } else {
                            desc = txtdesc.getText();
                            lblcheck2.setText("");
                            System.out.println(desc);
                        }
                        if (taprob.getText().equals("")) {
                            lblcheck2.setText("*Obrigatorio");
                        } else {
                            prob = taprob.getText();
                            lblcheck2.setText("");
                        }

                        if (desc != null && prob != null && id != null) {
                            if (h.bugReport(id, desc, prob) == true) {

                                JOptionPane.showMessageDialog(null, "Seu report foi enviado e sera avaliado em breve, obrigado por contribuir para nossa melhoria!");
                                txtdesc.setText("");
                                taprob.setText("");

                            } else {
                                JOptionPane.showMessageDialog(null, "Algo deu errado, porfavor confira sua conexao com a internet");
                            }

                        }

                    }
                };

                // UI update is run on the Application thread
                Platform.runLater(updater);

            }

        });
        thread.start();

    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        try {
            ObservableList<String> idioma = FXCollections.observableArrayList("Portugues");
            ObservableList<String> theme = FXCollections.observableArrayList("Dark Theme");
            //MaskTextField
            txthora.setLayoutX(174);
            txthora.setLayoutY(276);
            txthora.setPrefWidth(102);
            txthora.setPrefHeight(25);
            txthora.setDisable(true);
            txthora.setMask("NN:NN");
            txthora.setPromptText("00:00");
            pnlAgenda.getChildren().add(txthora);

            populateListView();
            pupulateCategoriaBug();
            langbox.setItems(idioma);
            themebox.setItems(theme);

            colorCalendar.setValue(Color.CORNFLOWERBLUE);

            horarioListView.setOnMouseClicked(eve -> {

                horaremove = (String) horarioListView.getSelectionModel().getSelectedItem();
            });

        } catch (SQLException ex) {
            Logger.getLogger(FXMLConfigController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
