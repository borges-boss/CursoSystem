
package cursosystem;

import javafx.beans.property.SimpleStringProperty;


public class ConsultaData {
    
    private SimpleStringProperty nome;
    private SimpleStringProperty cpf;
    private SimpleStringProperty telefone;
    private SimpleStringProperty nameage;
    private SimpleStringProperty hora;
    
    public ConsultaData(String nome,String cpf,String telefone){
    
    this.nome=new SimpleStringProperty(nome);
    this.cpf=new SimpleStringProperty(cpf);
    this.telefone=new SimpleStringProperty(telefone);
        
        
    }
    
    //ESTE CONSTRUTOR DESTINA-SE AO FXMLAgenda
    public ConsultaData(String nameAge,String hora,String cpf,String i){
    this.nome=new SimpleStringProperty(nameAge);
    this.hora=new SimpleStringProperty(hora);
    this.cpf=new SimpleStringProperty(cpf);//CURSO
    
    }
    
   
    
    public String getHora(){
    return hora.get();
    }
    
    public String getNome(){
    
    return nome.get();
    }
    
    public String getCpf(){
    
    return cpf.get();
    }
    
    public String getTel(){
    
    return telefone.get();
    }
    
    public void setNome(String nome){
    
    this.nome=new SimpleStringProperty(nome);
    }
  
    
      public void setCpf(String cpf){
    this.cpf=new SimpleStringProperty(cpf);
   
    }
    
        public void setTel(String telefone){
    this.telefone=new SimpleStringProperty(telefone);
   
    }
}
  
    