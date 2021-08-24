
package cursosystem;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javax.swing.JOptionPane;

/**
 *
 * @author borge
 */
public class ClientData {
    
    private int idCLiente;
    private String nome;
    private String cpf;
    private String tel;
    private String data;
    private VBox pnItems;
    private static ToggleGroup tg;
    
    public ClientData(int idcli,String nome,String cpf,String tel,String data,VBox pnItems){
        this.idCLiente=idcli;
        this.nome=nome;
        this.cpf=cpf;
        this.data=data;
        this.tel=tel;
        this.pnItems=pnItems;
                
    }
    
    
    public int getIdCliente(){
    return idCLiente;
    }
    
    public String getNome(){
    return nome;
    }
    
    public String getCpf(){
    return cpf;
    }
    
   
   
    
    
    public HBox addItems() {

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
            
            Http http = new Http();
            int resp = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja fazer esta exclusao ?\n Aviso:Ao exluir este cliente voce tambem excluira todos os agendamentos relacionados a ele(a)", "Aviso", JOptionPane.YES_NO_OPTION);

            if (resp == JOptionPane.YES_OPTION) {
                //E necessario excluir os dados da agenda para evitar o erro na foreign key
                if (http.deleteData("agenda", String.valueOf(idCLiente)) == true && http.deleteData("clientes", String.valueOf(idCLiente)) == true) {
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
                                        stm2.setInt(1, idCLiente);
                                        stm2.execute();
                                        sql = "DELETE FROM cliente WHERE id_cliente=?";
                                        PreparedStatement stm1 = con.prepareStatement(sql);
                                        stm1.setInt(1, idCLiente);
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
            s.setIdClienteAgenda(idCLiente);//AO CLICAR O ID DO CLIENTE DEVE SER PASSADO AO AGENDADMENTO
            //ERRO DEVE SER CORRIGIDO COM O CPF
            s.setNomeAgenda(txtnome.getText());

        });
        //TextBox events
        int wherValor = idCLiente;//Recuperando o id do cliente na HashMap
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
    
    
    
}
