package br.edu.cs.poo.ac.seguro.entidades;
import java.io.Serializable;
public class Veiculo implements Serializable {
 private static final long serialVersionUID=1L; private String placa; private int ano; private SeguradoEmpresa proprietarioEmpresa; private SeguradoPessoa proprietarioPessoa; private CategoriaVeiculo categoria;
 public Veiculo(String placa,int ano,SeguradoEmpresa proprietarioEmpresa,SeguradoPessoa proprietarioPessoa,CategoriaVeiculo categoria){this.placa=placa;this.ano=ano;this.proprietarioEmpresa=proprietarioEmpresa;this.proprietarioPessoa=proprietarioPessoa;this.categoria=categoria;}
 public String getPlaca(){return placa;} public void setPlaca(String v){placa=v;} public int getAno(){return ano;} public void setAno(int v){ano=v;} public SeguradoEmpresa getProprietarioEmpresa(){return proprietarioEmpresa;} public void setProprietarioEmpresa(SeguradoEmpresa v){proprietarioEmpresa=v;} public SeguradoPessoa getProprietarioPessoa(){return proprietarioPessoa;} public void setProprietarioPessoa(SeguradoPessoa v){proprietarioPessoa=v;} public CategoriaVeiculo getCategoria(){return categoria;} public void setCategoria(CategoriaVeiculo v){categoria=v;}
}
