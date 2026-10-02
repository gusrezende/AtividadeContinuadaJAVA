package br.edu.cs.poo.ac.seguro.entidades;
import java.io.Serializable;
public class Endereco implements Serializable {
 private static final long serialVersionUID=1L; private String logradouro,cep,numero,complemento,pais,estado,cidade;
 public Endereco(String logradouro,String cep,String numero,String complemento,String pais,String estado,String cidade){this.logradouro=logradouro;this.cep=cep;this.numero=numero;this.complemento=complemento;this.pais=pais;this.estado=estado;this.cidade=cidade;}
 public String getLogradouro(){return logradouro;} public void setLogradouro(String v){logradouro=v;} public String getCep(){return cep;} public void setCep(String v){cep=v;} public String getNumero(){return numero;} public void setNumero(String v){numero=v;} public String getComplemento(){return complemento;} public void setComplemento(String v){complemento=v;} public String getPais(){return pais;} public void setPais(String v){pais=v;} public String getEstado(){return estado;} public void setEstado(String v){estado=v;} public String getCidade(){return cidade;} public void setCidade(String v){cidade=v;}
}
