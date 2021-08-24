package cursosystem;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import org.json.JSONException;
import org.json.JSONObject;
import org.apache.commons.io.IOUtils;
import javax.net.ssl.HttpsURLConnection;

public class Http {

    /**
     * @param args the command line arguments
     *
     * Nesta classe estao contidos todos os metodos relacionados a operaçoes com
     * o servidor
     *
     *
     * AVISO: Corrigir o metodo insertData: Enviar o id_cliente para servidor.
     *
     */
    private HttpsURLConnection openHttpsConnection(String ur) {
        HttpsURLConnection con = null;
        try {
            URL url = new URL(ur);
            con = (HttpsURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);
            con.setDoOutput(true);
            con.setUseCaches(false);

        } catch (ConnectException ex) {
        } catch (IOException e) {
        }
        return con;
    }

    public String validateLogin(String login, String senha) {
        String retorno = null;
        try {
            String postData = URLEncoder.encode("login", "UTF-8") + "=" + URLEncoder.encode(login, "UTF-8") + "&" + URLEncoder.encode("senha", "UTF-8") + "=" + URLEncoder.encode(senha, "UTF-8");
            //https://ikkeboss.000webhostapp.com/
            URL url = new URL("https://ikkeboss.000webhostapp.com/validateLogin.php");
            URL urlStatus = new URL("https://ikkeboss.000webhostapp.com/statusServer.php");

            //openning connection
            HttpsURLConnection status = (HttpsURLConnection) urlStatus.openConnection();
            int responseCode = status.getResponseCode();
            String responseMessage = status.getResponseMessage();
            status.disconnect();

            if (responseCode == 200) {
                //setting connection
                HttpsURLConnection con = openHttpsConnection("https://ikkeboss.000webhostapp.com/validateLogin.php");

                //sending request
                OutputStream output = con.getOutputStream();
                PrintWriter escre = new PrintWriter(new OutputStreamWriter(output));
                escre.write(postData);
                escre.flush();

                //reciving response
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));
                String result = IOUtils.toString(in);
                JSONObject myresponse = new JSONObject(result.trim());
                //variavel retorno sera retornada
                retorno = myresponse.getJSONArray("unidade").optString(0);

                in.close();
                in.close();
                con.disconnect();

            } else if (responseCode == 400) {
                retorno = "Erro: " + responseMessage;

            } else {
                retorno = "Erro";

            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            retorno = "Erro";
        } catch (ConnectException ex) {
            ex.printStackTrace();
            retorno = "Erro";
        } catch (IOException ex) {
            ex.printStackTrace();
            retorno = "Erro";
        } catch (JSONException ei) {
            ei.printStackTrace();
            retorno = "Erro";
        }
        return retorno;
    }

    //METODO TEM A FUNCAO DE MANDAR OS DADOS DE CADASTRO PARA O SERVER 
    public boolean insertData(String nome, String cpf, String tel, String data) {
        boolean retorno = false;
        String unidade = null;
        String test[] = new String[2];

        /*
        INSTANCIA DA CLASSE sessionUser DEVE RETORNAR O ID DA UNIDADE 
        QUE O USUARIO ESTA LOGADO E PASSAR AO SERVIDOR COMO UM VALOR OCULTO
         */
        sessionUser s = new sessionUser();
        unidade = s.getidUnidade();
        try {
            String postData = null;
            HttpsURLConnection con = null;
            URL urlStatus = new URL("https://ikkeboss.000webhostapp.com/statusServer.php");

            postData = URLEncoder.encode("nome", "UTF-8") + "=" + URLEncoder.encode(nome, "UTF-8") + "&" + URLEncoder.encode("cpf", "UTF-8") + "=" + URLEncoder.encode(cpf, "UTF-8") + "&" + URLEncoder.encode("tel", "UTF-8") + "=" + URLEncoder.encode(tel, "UTF-8") + "&" + URLEncoder.encode("unidade", "UTF-8") + "=" + URLEncoder.encode(unidade, "UTF-8") + "&" + URLEncoder.encode("datacompra", "UTF-8") + "=" + URLEncoder.encode(data, "UTF-8");
            //checking status 
            HttpsURLConnection status = (HttpsURLConnection) urlStatus.openConnection();
            int responseCode = status.getResponseCode();
            String responseMessage = status.getResponseMessage();
            status.disconnect();

            //Openning connection
            con = openHttpsConnection("https://ikkeboss.000webhostapp.com/insertData.php");

            //checking and sending data
            OutputStream output = con.getOutputStream();
            PrintWriter print = new PrintWriter(new OutputStreamWriter(output));
            if (con != null && nome != null && cpf != null && tel != null && responseCode == 200) {
                print.write(postData);
                print.flush();
                print.close();

                //reciving response
                //OBS:A RESPONSE RECEBIDA DO SERVER(ID) VAI SER UTILIZADA NO PROCESSO DE INCERÇAO DE DADOS NO DB LOCAL 
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String retornoT = null;
                String line = null;
                StringBuilder content = null;
                while ((line = in.readLine()) != null) {
                    retornoT = line;
                }
                s.setIdCliente(Integer.parseInt(retornoT));// E NECESSARIO QUE O ID DO SERVER E O LOCAL SEJAM CORRESPONDENTES
                con.disconnect();
                retorno = true;

            } else {

                con.disconnect();
                retorno = false;

            }
        } catch (MalformedURLException | ConnectException e) {
            e.printStackTrace();
        } catch (IOException er) {
        }

        return retorno;
    }

    public String insertAgendaData(int idCliente, String data, String hora, String curso) {
        String retorno = null;
        String unidade = null;
        String test[] = new String[2];

        /*
        INSTANCIA DA CLASSE sessionUser DEVE RETORNAR O ID DA UNIDADE 
        QUE O USUARIO ESTA LOGADO E PASSAR AO SERVIDOR COMO UM VALOR OCULTO
         */
        sessionUser s = new sessionUser();
        unidade = s.getidUnidade();
        try {
            String postData = null;
            HttpsURLConnection con = null;
            URL url = null;
            URL urlStatus = new URL("https://ikkeboss.000webhostapp.com/statusServer.php");

            postData = URLEncoder.encode("idcliente", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(idCliente), "UTF-8") + "&" + URLEncoder.encode("data", "UTF-8") + "=" + URLEncoder.encode(data, "UTF-8") + "&"
                    + URLEncoder.encode("hora", "UTF-8") + "=" + URLEncoder.encode(hora, "UTF-8") + "&" + URLEncoder.encode("unidade", "UTF-8") + "=" + URLEncoder.encode(unidade, "UTF-8") + "&" + URLEncoder.encode("curso", "UTF-8") + "=" + URLEncoder.encode(curso, "UTF-8");
            //checking status 
            HttpsURLConnection status = (HttpsURLConnection) urlStatus.openConnection();
            int responseCode = status.getResponseCode();
            String responseMessage = status.getResponseMessage();
            status.disconnect();

            //Openning connection
            con = openHttpsConnection("https://ikkeboss.000webhostapp.com/insertAgenda.php");

//checking and sending data
            OutputStream output = con.getOutputStream();
            PrintWriter print = new PrintWriter(new OutputStreamWriter(output));
            if (con != null && data != null && hora != null && responseCode == 200) {
                print.write(postData);
                print.flush();
                print.close();

                //reciving response
                //OBS:A RESPONSE RECEBIDA DO SERVER VAI SER UTILIZADA NO PROCESSO DE INCERÇAO DE DADOS NO DB LOCAL 
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String retornoT = null;
                String line = null;
                StringBuilder content = null;
                while ((line = in.readLine()) != null) {
                    retornoT = line;
                }

                con.disconnect();
                retorno = retornoT;

            } else {

                con.disconnect();

            }
        } catch (MalformedURLException | ConnectException e) {
            e.printStackTrace();
        } catch (IOException er) {
            er.printStackTrace();
        }

        return retorno;

    }

    //ESTE METODO TEM A FUNÇAO DE OBTER A LISTA DE VALORES DO SERVIDOR EM FORMATO JSON E RETORNA-LO PARA SER USSADO
    //POSTERORMENTE NA POPULAÇAO DA TABELA
    public String selectData() {
        HttpsURLConnection con = null;
        sessionUser s = new sessionUser();
        String retorno = null;
        try {

            URL url = new URL("https://ikkeboss.000webhostapp.com/consultaTable.php");
            URL urlStatus = new URL("https://ikkeboss.000webhostapp.com/statusServer.php");
            //checking server status 
            HttpsURLConnection status = (HttpsURLConnection) urlStatus.openConnection();
            int responseCode = status.getResponseCode();
            String responseMessage = status.getResponseMessage();
            status.disconnect();

            //connection open 
            con = (HttpsURLConnection) url.openConnection();

            //config connection
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);
            if (con != null && responseCode == 200) {
                //sending data
                OutputStream output = con.getOutputStream();
                PrintWriter escre = new PrintWriter(new OutputStreamWriter(output));
                String id = s.getidUnidade();//OBTENDO O ID DO CLIENTE NA CLASSE sessionUser
                String postData = URLEncoder.encode("idunidade", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8") + "&" + URLEncoder.encode("client_confirm", "UTF-8") + "=" + URLEncoder.encode("client0000324hjk", "UTF-8");
                escre.write(postData);
                escre.flush();
                escre.close();

                //recieving response
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));
                String result = IOUtils.toString(in);
                //variavel retorno e carregada com o JSONArray recebido do servidor 
                retorno = result;//O JSONArray sera tratado na classe DownloadData
                in.close();
                escre.close();
                output.close();
                con.disconnect();

            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ConnectException es) {
            es.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return retorno;
    }

//TEM A FUNÇAO DE CONTAR A QUANTIDADE DE DADOS NO SERVER PARA FAZER A COMPARAÇAO NA CLASSE DonloadData
//ASSIM O METODO SO FARA O DOWNLOAD SE A QUNATIDADE DE DADOS LOCAL FOR MENOR DA QUNATIDADE DO SERVIDOR    
    public int countData(String opc) {
        int retorno = 0;
        sessionUser s = new sessionUser();
        String id = s.getidUnidade();

        try {
            //VARIAVEL opc DEFINE A TABELA QUE SERA SELECIONADA
            String postData = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8") + "&" + URLEncoder.encode("opc", "UTF-8") + "=" + URLEncoder.encode(opc, "UTF-8");
            URL url = new URL("https://ikkeboss.000webhostapp.com/countJosnArray.php");

            //openning connection
            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);

            if (con != null) {
                //sending data
                OutputStream output = con.getOutputStream();
                PrintWriter escre = new PrintWriter(new OutputStreamWriter(output));
                escre.write(postData);
                escre.flush();
                escre.close();

                //recieving response
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));
                String line = null;
                StringBuilder content = null;
                while ((line = in.readLine()) != null) {
                    retorno = Integer.parseInt(line);
                }

            }
            con.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        } catch (IOException es) {
            es.printStackTrace();
        }

        return retorno;
    }

    //OBTENDO JSONARRAY COM OS DADOS DA AGENDA
    //O JSON ARRAY SERA TRATADO NA CLASSE DownloadData
    public String consultaAgendaData() {
        String retorno = null;
        try {
            sessionUser s = new sessionUser();
            String id = s.getidUnidade();
            System.err.print("Entrou no metodo");
            URL url = new URL("https://ikkeboss.000webhostapp.com/consultaAgendaData.php");
            String postData = URLEncoder.encode("id_unidade", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8");
            //openning connection
            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);
            if (con != null) {
                //sending data
                OutputStream output = con.getOutputStream();
                PrintWriter escre = new PrintWriter(new OutputStreamWriter(output));
                escre.write(postData);
                escre.flush();
                escre.close();

                //reciving response
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));
                String result = IOUtils.toString(in);//RECEBENDO EM FORMATO JSON
                retorno = result;//O RETORNO SERA TRATADO NA CLASSE DownloadData
                System.err.print(retorno);

                con.disconnect();
            }

        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return retorno;

    }

    public String consultaCursosData() {
        String retorno = null;
        try {
            System.err.print("Entrou no metodo");
            URL url = new URL("https://ikkeboss.000webhostapp.com/consultaCursos.php");

            //openning connection
            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);
            if (con != null) {
                //reciving response
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));
                String result = IOUtils.toString(in);
                retorno = result;//O RETORNO SERA TRATADO NA CLASSE DownloadData
                System.err.print(retorno);

            }
            con.disconnect();
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return retorno;

    }

    //O METODO ABAIXO TEM A FUNÇAO DE ATUALIZAR OS DADOS DO DB DO SERVIDOR
    //SE A OPERAÇAO RETORNAR TRUE O BD LOCAL SERA ATUALIZADO LOGO EM SEGUIDA NA CLASSE DownloadData
    public boolean updateData(String table, String campo, String newValor, String whereUpdate) {

        HttpsURLConnection con = null;
        sessionUser s = new sessionUser();
        String retornoT = null;
        try {

            URL url = new URL("https://ikkeboss.000webhostapp.com/updateData.php");
            String postData = URLEncoder.encode("table", "UTF-8") + "=" + URLEncoder.encode(table, "UTF-8") + "&" + URLEncoder.encode("campo", "UTF-8") + "=" + URLEncoder.encode(campo, "UTF-8") + "&"
                    + URLEncoder.encode("newValor", "UTF-8") + "=" + URLEncoder.encode(newValor, "UTF-8") + "&" + URLEncoder.encode("whereUpdate", "UTF-8") + "=" + URLEncoder.encode(whereUpdate, "UTF-8");
            URL urlStatus = new URL("https://ikkeboss.000webhostapp.com/statusServer.php");
            //checking server status 
            HttpsURLConnection status = (HttpsURLConnection) urlStatus.openConnection();
            int responseCode = status.getResponseCode();
            String responseMessage = status.getResponseMessage();
            status.disconnect();

            //connection open 
            con = (HttpsURLConnection) url.openConnection();

            //config connection
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);

            if (con != null && responseCode == 200) {
                OutputStream output = con.getOutputStream();
                PrintWriter p = new PrintWriter(new OutputStreamWriter(output));
                //sending post request
                p.write(postData);
                p.flush();
                p.close();
                System.out.println(postData);

//reciving response
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

                String line = null;
                StringBuilder content = null;
                while ((line = in.readLine()) != null) {
                    retornoT = line;
                }
                in.close();
                con.disconnect();
                return true;
            }

        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    public boolean deleteData(String table, String id) {
        HttpsURLConnection con = null;
        sessionUser s = new sessionUser();
        String retornoT = null;
        try {

            URL url = new URL("https://ikkeboss.000webhostapp.com/deleteData.php");
            String postData = URLEncoder.encode("id_delete", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8") + "&" + URLEncoder.encode("table_delete", "UTF-8") + "=" + URLEncoder.encode(table, "UTF-8");
            URL urlStatus = new URL("https://ikkeboss.000webhostapp.com/statusServer.php");
            //checking server status 
            HttpsURLConnection status = (HttpsURLConnection) urlStatus.openConnection();
            int responseCode = status.getResponseCode();
            String responseMessage = status.getResponseMessage();
            status.disconnect();

            //connection open 
            con = (HttpsURLConnection) url.openConnection();

            //config connection
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);

            if (con != null && responseCode == 200) {
                OutputStream output = con.getOutputStream();
                PrintWriter p = new PrintWriter(new OutputStreamWriter(output));
                //sending post request
                p.write(postData);
                p.flush();
                p.close();

//reciving response
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

                String line = null;
                StringBuilder content = null;
                while ((line = in.readLine()) != null) {
                    retornoT = line;
                }
                System.out.println("Message:" + retornoT);
                in.close();
                con.disconnect();
                return true;

            }

        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();

        }

        return false;
    }

    public boolean bugReport(String id_cate, String desc, String prob) {
        HttpsURLConnection con;
        try {
            URL urlStatus = new URL("https://ikkeboss.000webhostapp.com/statusServer.php");
            //checking server status 
            HttpsURLConnection status = (HttpsURLConnection) urlStatus.openConnection();
            int responseCode = status.getResponseCode();
            String responseMessage = status.getResponseMessage();
            status.disconnect();

            URL url = new URL("https://ikkeboss.000webhostapp.com/bugreport.php");
            con = (HttpsURLConnection) url.openConnection();
            String postdata = URLEncoder.encode("cate_id", "UTF-8") + "=" + URLEncoder.encode(id_cate, "UTF-8") + "&" + URLEncoder.encode("bug_desc", "UTF-8") + "=" + URLEncoder.encode(desc, "UTF-8") + "&" + URLEncoder.encode("prob", "UTF-8") + "=" + URLEncoder.encode(prob, "UTF-8");
            System.err.print(postdata);
            //Config
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setConnectTimeout(5000);
            con.setReadTimeout(7000);

            //send
            OutputStream output = con.getOutputStream();
            PrintWriter print = new PrintWriter(new OutputStreamWriter(output));
            print.write(postdata);
            print.flush();
            //response
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

            String line = null;
            StringBuilder content = null;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }

            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public String checkForUpdatesOnServer(String myversion ) {
        String line = null;
        HttpsURLConnection con;
        try {
            

            URL urlStatus = new URL("https://ikkeboss.000webhostapp.com/statusServer.php");
            //checking server status 
            HttpsURLConnection status = (HttpsURLConnection) urlStatus.openConnection();
            int responseCode = status.getResponseCode();
            String responseMessage = status.getResponseMessage();
            status.disconnect();

            URL url = new URL("https://ikkeboss.000webhostapp.com/CheckUpdates.php");
            con = (HttpsURLConnection) url.openConnection();
            String postdata = URLEncoder.encode("myversion","UTF-8")+"="+URLEncoder.encode(myversion,"UTF-8");

            //Config
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setConnectTimeout(5000);
            con.setReadTimeout(7000);

            //send
            OutputStream output = con.getOutputStream();
            PrintWriter print = new PrintWriter(new OutputStreamWriter(output));
            print.write(postdata);
            print.flush();
            //response
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

            StringBuilder content = null;
           line = in.readLine();
System.err.println("Line: "+line);
            

        } catch (Exception e) {
            e.printStackTrace();

        }
        return line;

    }

}
