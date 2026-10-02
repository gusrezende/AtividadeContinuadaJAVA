package br.edu.cs.poo.ac.seguro.testes;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import br.edu.cs.poo.ac.seguro.daos.SinistroDAO;
import br.edu.cs.poo.ac.seguro.entidades.Sinistro;
import br.edu.cs.poo.ac.seguro.entidades.TipoSinistro;

public class TesteSinistroDAO extends TesteDAO {
	private SinistroDAO dao = new SinistroDAO();
	protected Class getClasse() {
		return Sinistro.class;
	}

	private Sinistro criar(String numero, String usuario, double valor, TipoSinistro tipo) {
		return new Sinistro(numero, null, LocalDateTime.now(), LocalDateTime.now(), usuario,
				BigDecimal.valueOf(valor), tipo);
	}

	@Test
	public void teste01() {
		String numero = "00000000";
		cadastro.incluir(criar(numero, "USUARIO1", 1000.0, TipoSinistro.COLISAO), numero);
		Sinistro sin = dao.buscar(numero);
		Assertions.assertNotNull(sin);
	}
	@Test
	public void teste02() {
		String numero = "10000000";
		cadastro.incluir(criar(numero, "USUARIO2", 1001.0, TipoSinistro.INCENDIO), numero);
		Sinistro sin = dao.buscar("11000000");
		Assertions.assertNull(sin);
	}
	@Test
	public void teste03() {
		String numero = "20000000";
		cadastro.incluir(criar(numero, "USUARIO3", 1002.0, TipoSinistro.FURTO), numero);
		boolean ret = dao.excluir(numero);
		Assertions.assertTrue(ret);
	}
	@Test
	public void teste04() {
		String numero = "30000000";
		cadastro.incluir(criar(numero, "USUARIO4", 1003.0, TipoSinistro.ENCHENTE), numero);
		boolean ret = dao.excluir("31000000");
		Assertions.assertFalse(ret);
	}
	@Test
	public void teste05() {
		String numero = "40000000";
		boolean ret = dao.incluir(criar(numero, "USUARIO5", 1004.0, TipoSinistro.DEPREDACAO));
		Assertions.assertTrue(ret);
		Sinistro sin = dao.buscar(numero);
		Assertions.assertNotNull(sin);
	}
	@Test
	public void teste06() {
		String numero = "50000000";
		Sinistro sin = criar(numero, "USUARIO6", 1005.0, TipoSinistro.COLISAO);
		cadastro.incluir(sin, numero);
		boolean ret = dao.incluir(sin);
		Assertions.assertFalse(ret);
	}
	@Test
	public void teste07() {
		String numero = "60000000";
		boolean ret = dao.alterar(criar(numero, "USUARIO7", 1006.0, TipoSinistro.INCENDIO));
		Assertions.assertFalse(ret);
		Sinistro sin = dao.buscar(numero);
		Assertions.assertNull(sin);
	}
	@Test
	public void teste08() {
		String numero = "70000000";
		Sinistro sin = criar(numero, "USUARIO8", 1007.0, TipoSinistro.FURTO);
		cadastro.incluir(sin, numero);
		sin = criar(numero, "USUARIO9", 1008.0, TipoSinistro.ENCHENTE);
		boolean ret = dao.alterar(sin);
		Assertions.assertTrue(ret);
	}
}
