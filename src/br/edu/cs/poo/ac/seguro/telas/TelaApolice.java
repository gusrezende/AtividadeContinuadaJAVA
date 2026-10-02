package br.edu.cs.poo.ac.seguro.telas;

import java.math.BigDecimal;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;

import br.edu.cs.poo.ac.seguro.daos.ApoliceDAO;
import br.edu.cs.poo.ac.seguro.daos.VeiculoDAO;
import br.edu.cs.poo.ac.seguro.entidades.Apolice;
import br.edu.cs.poo.ac.seguro.entidades.Veiculo;

/**
 * CRUD de apolice em uma unica tela. Ainda nao existe mediator de apolice,
 * entao a tela usa os DAOs de apolice e de veiculo.
 */
@SuppressWarnings("serial")
public class TelaApolice extends JFrame {
	private final ApoliceDAO dao = new ApoliceDAO();
	private final VeiculoDAO veiculoDAO = new VeiculoDAO();

	private final JTextField txtNumero = new JTextField(16);
	private final JTextField txtPlaca = new JTextField(12);
	private final JSpinner spFranquia = UtilTela.campoValor();
	private final JSpinner spPremio = UtilTela.campoValor();
	private final JSpinner spValorMaximo = UtilTela.campoValor();

	private final JButton btnBuscar = new JButton("Buscar");
	private final JButton btnIncluir = new JButton("Incluir");
	private final JButton btnAlterar = new JButton("Alterar");
	private final JButton btnExcluir = new JButton("Excluir");
	private final JButton btnLimpar = new JButton("Limpar");

	public TelaApolice() {
		UtilTela.titulo(this, "Apólice");

		JPanel form = UtilTela.painelForm();
		int y = 0;
		UtilTela.linha(form, y++, "Número:", txtNumero);
		UtilTela.linha(form, y++, "Placa do veículo:", txtPlaca);
		UtilTela.linha(form, y++, "Valor da franquia (R$):", spFranquia);
		UtilTela.linha(form, y++, "Valor do prêmio (R$):", spPremio);
		UtilTela.linha(form, y++, "Valor máximo segurado (R$):", spValorMaximo);

		btnBuscar.addActionListener(e -> UtilTela.executar(this, this::buscar));
		btnIncluir.addActionListener(e -> UtilTela.executar(this, this::incluir));
		btnAlterar.addActionListener(e -> UtilTela.executar(this, this::alterar));
		btnExcluir.addActionListener(e -> UtilTela.executar(this, this::excluir));
		btnLimpar.addActionListener(e -> limpar());

		UtilTela.montarJanela(this, form, UtilTela.painelBotoes(btnBuscar, btnIncluir, btnAlterar, btnExcluir, btnLimpar));
	}

	private String numero() {
		return txtNumero.getText().trim();
	}

	private Apolice montar() {
		if (numero().isEmpty()) {
			throw new IllegalArgumentException("Número da apólice deve ser informado");
		}
		String placa = txtPlaca.getText().trim().toUpperCase();
		if (placa.isEmpty()) {
			throw new IllegalArgumentException("Placa do veículo deve ser informada");
		}
		Veiculo veiculo = veiculoDAO.buscar(placa);
		if (veiculo == null) {
			throw new IllegalArgumentException("Placa do veículo não existente");
		}
		double franquia = UtilTela.lerValor(spFranquia, "Valor da franquia");
		double premio = UtilTela.lerValor(spPremio, "Valor do prêmio");
		double maximo = UtilTela.lerValor(spValorMaximo, "Valor máximo segurado");
		if (premio <= 0) {
			throw new IllegalArgumentException("Valor do prêmio deve ser maior que zero");
		}
		if (maximo <= 0) {
			throw new IllegalArgumentException("Valor máximo segurado deve ser maior que zero");
		}
		Apolice apolice = new Apolice(veiculo, BigDecimal.valueOf(franquia), BigDecimal.valueOf(premio),
				BigDecimal.valueOf(maximo));
		apolice.setNumero(numero());
		return apolice;
	}

	private void buscar() {
		if (numero().isEmpty()) {
			UtilTela.aviso(this, "Número da apólice deve ser informado");
			return;
		}
		Apolice a = dao.buscar(numero());
		if (a == null) {
			UtilTela.aviso(this, "Número da apólice não existente");
			return;
		}
		txtPlaca.setText(a.getVeiculo() == null ? "" : a.getVeiculo().getPlaca());
		spFranquia.setValue(a.getValorFranquia().doubleValue());
		spPremio.setValue(a.getValorPremio().doubleValue());
		spValorMaximo.setValue(a.getValorMaximoSegurado().doubleValue());
	}

	private void incluir() {
		boolean ok = dao.incluir(montar());
		UtilTela.resultado(this, ok ? null : "Número da apólice já existente", "Apólice incluída com sucesso.");
	}

	private void alterar() {
		boolean ok = dao.alterar(montar());
		UtilTela.resultado(this, ok ? null : "Número da apólice não existente", "Apólice alterada com sucesso.");
	}

	private void excluir() {
		if (numero().isEmpty()) {
			UtilTela.aviso(this, "Número da apólice deve ser informado");
			return;
		}
		if (!UtilTela.confirmar(this, "Excluir a apólice " + numero() + "?")) {
			return;
		}
		boolean ok = dao.excluir(numero());
		if (UtilTela.resultado(this, ok ? null : "Número da apólice não existente", "Apólice excluída com sucesso.")) {
			limpar();
		}
	}

	private void limpar() {
		txtNumero.setText("");
		txtPlaca.setText("");
		spFranquia.setValue(0.0);
		spPremio.setValue(0.0);
		spValorMaximo.setValue(0.0);
		txtNumero.requestFocusInWindow();
	}
}
