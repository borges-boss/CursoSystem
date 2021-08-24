package cursosystem;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author borge
 */
public class Config {

    private static String background_color = iniColor();
    private static boolean offline_mode=false;
    
//-Calendar/Agenda    
    private static String iniColor() {
        String line = null;
        try {
             
            FileReader file = new FileReader("C:/Windows/Temp/color.txt");
            BufferedReader bf = new BufferedReader(file);

            while (bf.ready()) {
                line = bf.readLine();

            }

        } catch (IOException e) {
        }

        return line;
    }

    public String getCalendarBackgroundColor() {

        return background_color;
    }

    public void setCalendarBackgroundColor(String back) {
        background_color = back;
        Thread t=new Thread(){ 
            @Override
            public void run(){
            try {
           
            File file = new File("C:/Windows/Temp/color.txt");
           
            // if file doesnt exists, then create it 
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile());

            BufferedWriter bw = new BufferedWriter(fw);
            System.err.print(file.getPath());
            bw.write(background_color);
            bw.close();
            //System.out.println("Done writing to " + fileName); //For testing 
        } catch (IOException e) {
            System.out.println("Error: " + e);
            e.printStackTrace();
        }
            
            }
        };
        t.start();
        
        
    }
    
    //Configuraçoes Gerais
    public void setOfflineMode(boolean state){
    this.offline_mode=state; 
    }
    
    public boolean getOfflineMode(){
    return offline_mode;
    }
    
    

}
