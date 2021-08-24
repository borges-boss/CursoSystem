package cursosystem;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.DataTruncation;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/*
Nesta classe estao contidos todos os metodos relacionados a operaçoes locais no banco de dados.



 */
public class LocalOperations {
private Map<String, Integer> map = new HashMap<>(); //ESTA HASHMAP DEVE RECUPERAR O CODIGO DOS CURSOS SELECIONADOS NA COMBOBOX
    private Connection openConnection() {
        Connection con = null;
        try {
            String user = "root";
            String pass = "toor123";
            String url = "jdbc:mysql://localhost:8088/data_curso_system?useSSL=false";

            con = DriverManager.getConnection(url, user, pass);
        } catch (SQLException ex) {
            Logger.getLogger(LocalOperations.class.getName()).log(Level.SEVERE, null, ex);
        }

        return con;
    }
    
    //TEM A FUNÇAO DE ATUALIZAR OS DADOS DO BD LOCAL
    public void updateClientsLocalData(String campo, String newValor, String whereValor) {

        try {
            Connection con = openConnection();
            String sql = null;
            PreparedStatement st = null;

            if (campo.equals("nome")) {
                sql = "UPDATE cliente SET nome_cliente=? WHERE cpf_cliente=? ";

            }
            if (campo.equals("cpf")) {
                sql = "UPDATE cliente SET cpf_cliente=? WHERE cpf_cliente=? ";
            }
            if (campo.equals("tel")) {
                sql = "UPDATE cliente SET telefone_cliente=? WHERE cpf_cliente=? ";
            }
            st = con.prepareStatement(sql);
            st.setString(1, newValor);
            st.setString(2, whereValor);
            st.execute();
            st.close();
            con.close();

        } catch (SQLException ex) {
        }

    }



    public int deleteClientsLocalData(int id) {
        Connection con = openConnection();
        int idCliente = id;
        try {
            String sql =null;
            sql = "DELETE FROM cliente WHERE id_cliente=?";
            PreparedStatement stm2 = con.prepareStatement(sql);
            stm2.setInt(1, idCliente);
            stm2.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return idCliente;
    }
    
  
    //POPULA A COMBOBOX DO LAYOUT ancConsulta.fxml
 public ObservableList populateComboBoxCurso() {
     ObservableList<String> data = null;
        try {
            List list = new ArrayList();
            OpenConnection op = new OpenConnection();
            ResultSet rs = null;
            Connection con = op.getConnetion();
            String sql = "select * from cursos";
            PreparedStatement stm = con.prepareStatement(sql);
            rs = stm.executeQuery();
            while (rs.next()) {

                if (rs.getString("nome_curso") != null) {
                    list.add(rs.getString("nome_curso"));
                    map.put(rs.getString("nome_curso"), rs.getInt("cod_curso"));//PARA ACESSAR DEPOIS
                }

            }
             data = FXCollections.observableArrayList(list);
            

        } catch (SQLException e) {
            e.printStackTrace();
        }
return data;
    }
 
 public Map<String,Integer> getMapCurso(){
 return map;
 }
 
 public ObservableList<String> populateComboBoxHorario() {
        int tes = 0;
        ObservableList<String> horarios = null;
        try {
            List list = new ArrayList();
            OpenConnection op = new OpenConnection();
            ResultSet rs = null;
            Connection con = op.getConnetion();
            String sql = "select hora from horario";
            PreparedStatement stm = con.prepareStatement(sql);
            rs = stm.executeQuery();
            while (rs.next()) {

                if (rs.getString("hora") != null) {
                    list.add(rs.getString("hora"));
                }
//                hora[tes]=rs.getString("hora");//UTILIZADO NO NEXT AND PREV TIME
                // tes++;
            }

            horarios = FXCollections.observableArrayList(list);
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
return horarios;
    }


}
