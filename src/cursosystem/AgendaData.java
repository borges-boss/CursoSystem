
package cursosystem;

import javafx.beans.property.SimpleStringProperty;


public class AgendaData {
    private SimpleStringProperty nome;
    private SimpleStringProperty curso;
    private SimpleStringProperty data;
    private SimpleStringProperty hora;

    
    
    
    
  public AgendaData(String nome,String curso){
    this.nome=new SimpleStringProperty(nome);
    this.curso=new SimpleStringProperty(curso);
 
    
    
    
    }

    public AgendaData(String nome, String curso, String data, String hora) {
        this.nome = new SimpleStringProperty(nome);
        this.curso = new SimpleStringProperty(curso);
        this.data = new SimpleStringProperty(data);
        this.hora = new SimpleStringProperty(hora);

    }

    public String getNome(){
    
    return nome.get();
    
    }
    
    public void setNome(String nome){
    this.nome=new SimpleStringProperty(nome);
    
    
    }
    
    
    public String getCurso(){
    
    return curso.get();
    }
    
    public void setCurso(String curso){
    this.curso=new SimpleStringProperty(curso);
        
    
    }
    
    public String getData(){
    return data.get();
    
    }
    public void setData(String data){
    this.data=new SimpleStringProperty(data);
    
    }
    
    
    public String getHora(){
    
    return hora.get();
    }
    
    public void setHora(String hora){
    
    this.hora=new SimpleStringProperty(hora);    
    
    }
}
