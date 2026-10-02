package br.edu.cs.poo.ac.seguro.entidades;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Veiculo implements Serializable {
	private String placa;
	private int ano;
	private SeguradoEmpresa proprietarioEmpresa;
	private SeguradoPessoa proprietarioPessoa;
	private CategoriaVeiculo categoria;
}
