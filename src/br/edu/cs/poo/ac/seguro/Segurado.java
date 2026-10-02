package br.edu.cs.poo.ac.seguro.entidades;
import java.io.Serializable; import java.math.BigDecimal; import java.time.LocalDate; import java.time.Period;
public class Segurado implements Serializable {
 private static final long serialVersionUID=1L; private String nome; private Endereco endereco; private LocalDate dataCriacao; private BigDecimal bonus;
 public Segurado(String nome,Endereco endereco,LocalDate dataCriacao,BigDecimal bonus){this.nome=nome;this.endereco=endereco;this.dataCriacao=dataCriacao;this.bonus=bonus;}
 public String getNome(){return nome;} public void setNome(String v){nome=v;} public Endereco getEndereco(){return endereco;} public void setEndereco(Endereco v){endereco=v;}
 protected LocalDate getDataCriacao(){return dataCriacao;} protected void setDataCriacao(LocalDate v){dataCriacao=v;} public BigDecimal getBonus(){return bonus;}
 public int getIdade(){return dataCriacao==null?0:Period.between(dataCriacao,LocalDate.now()).getYears();}
 public void creditarBonus(BigDecimal valor){if(valor==null)return; bonus=(bonus==null?BigDecimal.ZERO:bonus).add(valor);}
 public void debitarBonus(BigDecimal valor){if(valor==null)return; BigDecimal atual=bonus==null?BigDecimal.ZERO:bonus; BigDecimal debito=valor.signum()<0?BigDecimal.ZERO:valor; bonus=atual.subtract(atual.min(debito));}
}
