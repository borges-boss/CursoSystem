
package cursosystem;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class OpenConnection {

    private Connection con;
    private String url;


public OpenConnection(){
      try{
            
             url = "jdbc:sqlite:C:/CursoSystem/data_curso_system.db";

            con = DriverManager.getConnection(url);
           
      }catch(SQLException e){e.printStackTrace();} 
      }

public Connection getConnetion(){
return con;
}

public void closeAll() throws SQLException{
con.close();
}


    
}
