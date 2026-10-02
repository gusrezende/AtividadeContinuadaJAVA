package br.edu.cs.poo.ac.seguro.testes;

import java.math.BigDecimal;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import br.edu.cs.poo.ac.seguro.daos.ApoliceDAO;
import br.edu.cs.poo.ac.seguro.entidades.Apolice;

public class TesteApoliceDAO extends TesteDAO {
	private ApoliceDAO dao = new ApoliceDAO();
	protected Class getClasse() {
		return Apolice.class;
	}

	private Apolice criar(String numero, double franquia, double premio, double maximo) {
		Apolice ap = new Apolice(null, BigDecimal.valueOf(franquia), BigDecimal.valueOf(premio),
				BigDecimal.valueOf(maximo));
		ap.setNumero(numero);
		return ap;
	}

	@Test
	public void teste01() {
		String numero = "00000000";
		cadastro.incluir(criar(numero, 1000.0, 500.0, 50000.0), numero);
		Apolice ap = dao.buscar(numero);
		Assertions.assertNotNull(ap);
	}
	@Test
	public void teste02() {
		String numero = "10000000";
		cadastro.incluir(criar(numero, 1001.0, 501.0, 50001.0), numero);
		Apolice ap = dao.buscar("11000000");
		Assertions.assertNull(ap);
	}
	@Test
	public void teste03() {
		String numero = "20000000";
		cadastro.incluir(criar(numero, 1002.0, 502.0, 50002.0), numero);
		boolean ret = dao.excluir(numero);
		Assertions.assertTrue(ret);
	}
	@Test
	public void teste04() {
		String numero = "30000000";
		cadastro.incluir(criar(numero, 1003.0, 503.0, 50003.0), numero);
		boolean ret = dao.excluir("31000000");
		Assertions.assertFalse(ret);
	}
	@Test
	public void teste05() {
		String numero = "40000000";
		boolean ret = dao.incluir(criar(numero, 1004.0, 504.0, 50004.0));
		Assertions.assertTrue(ret);
		Apolice ap = dao.buscar(numero);
		Assertions.assertNotNull(ap);
	}
	@Test
	public void teste06() {
		String numero = "50000000";
		Apolice ap = criar(numero, 1005.0, 505.0, 50005.0);
		cadastro.incluir(ap, numero);
		boolean ret = dao.incluir(ap);
		Assertions.assertFalse(ret);
	}
	@Test
	public void teste07() {
		String numero = "60000000";
		boolean ret = dao.alterar(criar(numero, 1006.0, 506.0, 50006.0));
		Assertions.assertFalse(ret);
		Apolice ap = dao.buscar(numero);
		Assertions.assertNull(ap);
	}
	@Test
	public void teste08() {
		String numero = "70000000";
		Apolice ap = criar(numero, 1007.0, 507.0, 50007.0);
		cadastro.incluir(ap, numero);
		ap = criar(numero, 1008.0, 508.0, 50008.0);
		boolean ret = dao.alterar(ap);
		Assertions.assertTrue(ret);
	}
}
