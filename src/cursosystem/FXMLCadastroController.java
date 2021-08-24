package cursosystem;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javax.swing.JOptionPane;

public class FXMLCadastroController {

    String retorno = null;
    @FXML
    Label checklabel1;

    @FXML
    Label checklabel2;

    @FXML
    Label checklabel3;

    @FXML
    Button btnenviar;

    @FXML
    TextField txtnome;

    @FXML
    TextField txtcpf;

    @FXML
    TextField txttel;

  

    @FXML
    private void sendData(ActionEvent event) {

        validateFields();

    }

    public String validateFields() {
        String cpf = txtcpf.getText(), tel = txttel.getText(), nome = null;

        //VERIFICACAO DE CAMPOS 
        if (txtnome.getText().equals("")) {
            checklabel1.setText("*Obrigatorio");
            nome = null;
        } else {

            nome = txtnome.getText();
        }

        if (cpf.equals("")) {

            checklabel2.setText("*Obrigatorio");
            cpf = null;
            System.out.println(cpf);
        } else if (cpf.length() < 14) {
            checklabel2.setText("*Formato de CPF invalido");
            cpf = null;
            System.out.println(cpf);

        } else {
            checklabel2.setText("");
            cpf = txtcpf.getText();
            System.out.println(cpf);
        }

        if (tel.equals("")) {

            checklabel3.setText("*Obrigatorio");
            tel = null;
        } else if (tel.length() < 12) {

            checklabel3.setText("*Formato de telefone invalido");
            tel = null;
        } else {
            checklabel3.setText("");
            tel = txttel.getText();
        }

        if (nome != null && cpf != null && tel != null) {
            Http httpInsert = new Http();
            LocalOperations dw = new LocalOperations();
            boolean retornoInsert = httpInsert.insertData(nome, cpf, tel,"");

            if (retornoInsert == true) {
                //dw.updateCadastroLocalData(nome, cpf, tel);//ATUALIZA OS DADOS DO BANCO LOCAL

                Thread thread = new Thread(new Runnable() {

                    @Override
                    public void run() {
                        Runnable updater = new Runnable() {

                            @Override
                            public void run() {

                                txtnome.setText("");
                                txtcpf.setText("");
                                txttel.setText("");
                                JOptionPane.showMessageDialog(null, "Cadastro realizado com sucesso!");

                            }
                        };

                        // UI update is run on the Application thread
                        Platform.runLater(updater);

                    }

                });
                thread.start();
                Task<Void> task = new Task<Void>() {

                    @Override
                    protected Void call() throws Exception {

                        return null;
                    }

                };
            } else if (retornoInsert == false) {

            }

        }

        return retorno;
    }

    @FXML
    private void initialize(URL arg0, ResourceBundle arg1) {

    }

}
