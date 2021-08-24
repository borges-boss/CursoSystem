package cursosystem;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.GregorianCalendar;
import org.json.JSONObject;

/*
Esta classe tem a funçao de criar o banco de dados local e fazer o download de todos os dados do servidor
para a maquina do usuario;


 */


public class DownloadData {
/**
     * @param args the command line arguments
     * 
     * Nesta classe estao contidos todos os metodos relacionados a criaçao do BD e suas tabelas 
     * 
     * initializeDataBank()
     * O METODO initializeDataBank() TEM A FUNÇAO DE CRIAR O BANCO DE DADOS LOCAL NO PC DO USUARIO CASO ELE AINDA NAO EXISTA
     * OBS:NEM TODOS AS TABELAS PRESENTES NO BD LOCAL ESTAO PRESENTES DO BD DO SERVIDOR
     *
     * 
     */
     OpenConnection o=new OpenConnection();
    private boolean initializeDataBank() throws SQLException {//O METODO ABAIXO TEM A FUNÇAO DE VERIFICAR A EXISTENCIA DO BANCO DE DADOS LOCAL E CRIA-LO SE NECESSARIO
        //CRIAR BANCO DE DADOS;
             Connection con = null;
        try {
            
            con = DriverManager.getConnection("jdbc:mysql://localhost:8088/information_schema", "root", "toor123");
            String check = null;
            if (con != null) {

                String sql = "select schema_name from information_schema.schemata where schema_name='data_curso_system'";
                PreparedStatement stm = con.prepareStatement(sql);
                ResultSet rs = stm.executeQuery();

                while (rs.next()) {
                    check = rs.getString("schema_name");
                    System.out.println("Erro aqui");
                }
                System.out.print("teste aqui");
                if (check==null) {
                    //CREATING DATABASE
                    sql = "CREATE DATABASE data_curso_system";
                    PreparedStatement st = con.prepareStatement(sql);
                    st.execute();
                    st.close();
                    //SERVER MIRROR TABLES=Tabelas que obrigatoriamente devem existir no bd server e no local
                    //LOCAL TABLES=Tabelas que existem somente no BD LOCAL
                    //AVISO: FOI ADICIONADO MAIS UM CAMPO DO TIPO DATE AO BD LOCAL data_compra
                    
                    //CREATING SERVER MIRROR TABLES
                    Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:8088/data_curso_system?useSSL=false", "root", "toor123");
                    //cliente
                    String tablesql = "CREATE TABLE cliente (id_cliente int primary key,nome_cliente varchar(100),cpf_cliente char(11),telefone_cliente varchar(20),data_compra DATE)";
                    Statement tablest = conn.createStatement();
                    tablest.executeUpdate(tablesql);
                    System.err.println("Table created");
                    //cursos
                    tablesql = "CREATE TABLE cursos (cod_curso int(11) primary key, nome_curso varchar(30)) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
                    tablest = conn.createStatement();
                    tablest.executeUpdate(tablesql);
                    System.err.println("Table created");
                    //agenda_curso
                    tablesql = " create table agenda_curso (id_agenda int primary key not null,FK_ID_CLIENTE int, foreign key(FK_ID_CLIENTE) references cliente(id_cliente),FK_COD_CURSO int,data date,hora varchar(6))";
                    tablest = conn.createStatement();
                    tablest.executeUpdate(tablesql);
                    tablesql = "alter table agenda_curso add foreign key(FK_COD_CURSO) references cursos(cod_curso)";
                    tablest = conn.createStatement();
                    tablest.executeUpdate(tablesql);
                
                    //CREATING LOCAL TABLES ONLY
                    //horarios-TABELA ESTA PRESENTE SOMENTE NO BD LOCAL PARA OPERAÇOES DO SOFTWARE
                    tablesql = "CREATE TABLE horario (cod_horario int primary key auto_increment,hora char(5))";
                    tablest = conn.createStatement();
                    tablest.executeUpdate(tablesql);
                    System.err.println("Table created");
                    //bug_report
                    tablesql = "create table bug_report (id_cate int primary key,nome_cate varchar(50));";
                    tablest = conn.createStatement();
                    tablest.executeUpdate(tablesql);
                    System.err.println("Table created");
                    OpenConnection op=new OpenConnection();
                    PreparedStatement in=null;
                     
                    Connection con2 =op.getConnetion();
                    
                    tablesql="insert into bug_report (id_cate,nome_cate) values (?,?)";
                    in=con2.prepareStatement(tablesql);
                    in.setInt(1, 1);
                    in.setString(2, "Layout");
                    in.execute();
                    tablesql="insert into bug_report (id_cate,nome_cate) values (?,?)";
                    in=con2.prepareStatement(tablesql);
                    in.setInt(1, 2);
                    in.setString(2, "Funçao");
                    in.execute();
                    tablesql="insert into bug_report (id_cate,nome_cate) values (?,?)";
                    in=con2.prepareStatement(tablesql);
                    in.setInt(1, 3);
                    in.setString(2, "Outro");
                    in.execute();
                    
                    //conn.close();
                    return true;

                }  else {
                    return true;
                }

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally{
        if(con!=null){
       // con.close();
        }
        
        }

        return false;
    }

  

    //O METODO A SEGUIR TEM A FUNÇAO DE GRAVAR OS DADOS DE CLIENTES DO SERVIDOR NO BANCO DE DADOS LOCAL
    //PARA UMA MAIOR EFICIENCIA NA CONSULTA DESTES DADOS
    public void downloadClientsData() {
        Connection con = o.getConnetion();
        Http httpSelectData = new Http();
        int amountDataLocal = 0;

        int amountDataServer = httpSelectData.countData("controle_cliente_uni");//QUANTIDADE DE DADOS DO SERVER
        //COMPARANDO QUANTIDADE DE DADOS DO SERVER E DO DB LOCAL
        //PARA OTIMIZAR A INICIALIZAÇAO DO PROGRAMA
        try {
            if (con != null) {

                String sql = "SELECT id_cliente,COUNT(*) FROM cliente";
                PreparedStatement stm = con.prepareStatement(sql);
                ResultSet rs = stm.executeQuery();

                while (rs.next()) {
                    amountDataLocal = rs.getInt("COUNT(*)");

                }
                System.err.println("Quantidade local" + amountDataLocal);
                System.err.println("Quantidade server" + amountDataServer);

            } else {

            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        //SE A QUANTIDADE DE DADOS DO SERVIDOR FOR MAIOR DO QUE O BANCO DE DADOS LOCAL O DOWNLOAD SERA FEITO 
        if (con != null && amountDataLocal < amountDataServer || amountDataLocal>amountDataServer) {
            //reciving data from the server 
            JSONObject resp = new JSONObject(httpSelectData.selectData().trim());

            //insert data into the local databank
            try {
                //delete old data
                String deleteQuery = null;
                PreparedStatement deletest =null;
                String deleteQuery2 = "DELETE FROM cliente";
                deletest = con.prepareStatement(deleteQuery2);
                deletest.execute();
                //deletest.close();

                //insert new data
                String sql = "INSERT INTO cliente(id_cliente,nome_cliente,cpf_cliente,telefone_cliente,data_compra) VALUES (?,?,?,?,?)";
                PreparedStatement stm = con.prepareStatement(sql);
                int count = 0;
                String nome = null, cpf = null, tel = null, idCliente = null,datacompra=null;
                for (int i = 0; i < amountDataServer; i++) {

                    idCliente = resp.getJSONArray("dados").optString(count).toString();
                    count++;
                    nome = resp.getJSONArray("dados").optString(count).toString();
                    count++;
                    cpf = resp.getJSONArray("dados").optString(count).toString();
                    count++;
                    tel = resp.getJSONArray("dados").optString(count).toString();
                    count++;
                    datacompra= resp.getJSONArray("dados").optString(count).toString();//NOVO CAMPO INSERIDO DO TIPO DATE
                    count++;

                    if (idCliente != null && nome != null && cpf != null && tel != null) {
                        stm.setString(1, idCliente);
                        stm.setString(2, nome);
                        stm.setString(3, cpf);
                        stm.setString(4, tel);
                        stm.setString(5, datacompra);//NOVO CAMPO DO TIPO DATE ACRESCENTADO
                        stm.execute();

                    } else {
                        stm.close();
                        con.close();
                        break;
                    }

                }
               // con.close();

            } catch (SQLException e) {
                e.printStackTrace();
            }
           

        } else {

        }

    }

    public void downloadCursosData() {
        Connection con = o.getConnetion();
        Http httpSelectData = new Http();
        int amountDataLocal = 0;

        int amountDataServer = httpSelectData.countData("cursos");//QUANTIDADE DE DADOS DO SERVER
        //COMPARANDO QUANTIDADE DE DADOS DO SERVER E DO DB LOCAL
        //PARA OTIMIZAR A INICIALIZAÇAO DO PROGRAMA
        try {
            if (con != null) {

                String sql = "SELECT cod_curso,COUNT(*) FROM cursos";
                PreparedStatement stm = con.prepareStatement(sql);
                ResultSet rs = stm.executeQuery();

                while (rs.next()) {
                    amountDataLocal = rs.getInt("COUNT(*)");

                }
                System.err.println("Quantidade local " + amountDataLocal);
                System.err.println("Quantidade server " + amountDataServer);

            } else {

            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        //SE A QUANTIDADE DE DADOS DO SERVIDOR FOR MAIOR DO QUE O BANCO DE DADOS LOCAL O DOWNLOAD SERA FEITO 
        if (con != null && amountDataLocal < amountDataServer || amountDataLocal>amountDataServer) {
            //reciving data from the server 
            JSONObject resp = new JSONObject(httpSelectData.consultaCursosData());

            //insert data into the local databank
            try {
                //delete old data
                String deleteQuery = "DELETE FROM agenda_curso";
                PreparedStatement deletest = con.prepareStatement(deleteQuery);
                deletest.execute();
                deleteQuery="DELETE FROM cursos";
                deletest = con.prepareStatement(deleteQuery);
                deletest.execute();
                //deletest.close();
                

                //insert new data
                String sql = "INSERT INTO cursos(cod_curso,nome_curso) VALUES (?,?)";
                PreparedStatement stm = con.prepareStatement(sql);
                int count = 0;
                String cod_curso = null, nome_curso = null;
                for (int i = 0; i < amountDataServer; i++) {

                    cod_curso = resp.getJSONArray("dados").optString(count).toString();
                    count++;
                    nome_curso = resp.getJSONArray("dados").optString(count).toString();
                    count++;

                    if (cod_curso != null && nome_curso != null) {
                        stm.setString(1, cod_curso);
                        stm.setString(2, nome_curso);
                        stm.execute();
                        downloadAgendaData();

                    } else {
                        stm.close();
                        con.close();
                        break;
                    }

                }
               // con.close();

            } catch (SQLException e) {
                e.printStackTrace();
            }

        } else {
            System.out.println("Nulo");

        }

    }

    public void downloadAgendaData() {
        System.err.print("Entrou");
        Http selectAgendaData = new Http();
        int amountDataLocal = 0, amountDataServer = 0;
        Connection con =o.getConnetion();
        amountDataServer = selectAgendaData.countData("agenda_cursos");//QUANTIDADE DE DADOS DO SERVER
        //COMPARANDO QUANTIDADE DE DADOS DO SERVER E DO DB LOCAL
        //PARA OTIMIZAR A INICIALIZAÇAO DO PROGRAMA
        try {
            if (con != null) {

                String sql = "SELECT id_agenda,COUNT(*) FROM agenda_curso";
                PreparedStatement stm = con.prepareStatement(sql);
                ResultSet rs = stm.executeQuery();

                while (rs.next()) {
                    amountDataLocal = rs.getInt("COUNT(*)");

                }
                System.err.println("Quantidade local " + amountDataLocal);
                System.err.println("Quantidade server " + amountDataServer);

            } else {

            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        //insert data into the local databank
        if (amountDataLocal < amountDataServer || amountDataLocal>amountDataServer && con != null) {
            JSONObject resp = new JSONObject(selectAgendaData.consultaAgendaData().trim());
            try {
                //delete old data
                String deleteQuery = "DELETE FROM agenda_curso";
                PreparedStatement deletest = con.prepareStatement(deleteQuery);
                deletest.execute();
                //deletest.close();

                //insert new data
                String sql = "INSERT INTO agenda_curso(id_agenda,FK_ID_CLIENTE,FK_COD_CURSO,data,hora) VALUES (?,?,?,?,?)";
                PreparedStatement stm = con.prepareStatement(sql);
                int count = 0;
                String id_cliente = null, idagenda = null, data = null, hora = null, idCurso = null;
                for (int i = 0; i < amountDataServer; i++) {
                    //DADOS DO SERVER 
                    //O id_cliente SERA INSERIDO COMO UMA FOREIGN KEY NO BD LOCAL

                    idagenda = resp.getJSONArray("dados").optString(count).toString();
                    count++;
                    id_cliente = resp.getJSONArray("dados").optString(count).toString();
                    count++;
                    idCurso = resp.getJSONArray("dados").optString(count).toString();
                    count++;
                    data = resp.getJSONArray("dados").optString(count).toString();
                    count++;
                    hora = resp.getJSONArray("dados").optString(count).toString();
                    count++;

                    if (id_cliente != null && idCurso != null && hora != null && data != null) {

                        stm.setInt(1, Integer.parseInt(idagenda));
                        stm.setString(2, id_cliente);
                        stm.setInt(3, Integer.parseInt(idCurso));
                        stm.setString(4, data);
                        stm.setString(5, hora);
                        stm.execute();
                       

                    } else {
                       stm.close();
                       con.close();
                        break;
                    }

                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
         
        }
    }

}
