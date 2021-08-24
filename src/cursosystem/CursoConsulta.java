package cursosystem;

import javafx.beans.property.SimpleStringProperty;


public class CursoConsulta {
    
   private SimpleStringProperty id;
    private SimpleStringProperty curso;
    private SimpleStringProperty descricao;   
    
    
  public CursoConsulta(String id,String curso,String descricao){
  
      this.id=new SimpleStringProperty(id);
      this.curso=new SimpleStringProperty(curso);
      this.descricao=new SimpleStringProperty(descricao);
  }  
    
    
  
  public String getId(){
  
  
  return id.get();
  }
  
    public String getCurso(){
  
  
  return curso.get();
  }
    
 public String getDescricao(){
  
  
  return descricao.get();
  }
 
 
 public void setId(String id){
 
     this.id=new SimpleStringProperty(id);
 
 }
 
  public void setCurso(String curso){
 
     this.curso=new SimpleStringProperty(curso);
 
 }
  
  public void setDescri(String descri){
 
     this.descricao=new SimpleStringProperty(descri);
 
 }  
  
}
