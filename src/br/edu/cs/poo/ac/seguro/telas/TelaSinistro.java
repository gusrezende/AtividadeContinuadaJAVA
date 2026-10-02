package br.edu.cs.poo.ac.seguro.telas;

import java.awt.Component;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;

import br.edu.cs.poo.ac.seguro.daos.SinistroDAO;
import br.edu.cs.poo.ac.seguro.daos.VeiculoDAO;
import br.edu.cs.poo.ac.seguro.entidades.Sinistro;
import br.edu.cs.poo.ac.seguro.entidades.TipoSinistro;
import br.edu.cs.poo.ac.seguro.entidades.Veiculo;

/**
 * CRUD de sinistro em uma unica tela. Ainda nao existe mediator de sinistro,
 * entao a tela usa os DAOs de sinistro e de veiculo.
 */
@SuppressWarnings("serial")
public class TelaSinistro extends JFrame {
	private final SinistroDAO dao = new SinistroDAO();
	private final VeiculoDAO veiculoDAO = new VeiculoDAO();

	private final JTextField txtNumero = new JTextField(16);
	private final JTextField txtPlaca = new JTextField(12);
	private final JFormattedTextField txtDataHoraSinistro = UtilTela.campoDataHora();
	private final JTextField txtDataHoraRegistro = new JTextField(16);
	private final JTextField txtUsuario = new JTextField(20);
	private final JSpinner spValor = UtilTela.campoValor();
	private final JComboBox<TipoSinistro> cmbTipo = new JComboBox<>(TipoSinistro.values());

	private final JButton btnBuscar = new JButton("Buscar");
	private final JButton btnIncluir = new JButton("Incluir");
	private final JButton btnAlterar = new JButton("Alterar");
	private final JButton btnExcluir = new JButton("Excluir");
	private final JButton btnLimpar = new JButton("Limpar");

	public TelaSinistro() {
		UtilTela.titulo(this, "Sinistro");
		txtDataHoraRegistro.setEditable(false);
		cmbTipo.setRenderer(new DefaultListCellRenderer() {
			@Override
			public Component getListCellRendererComponent(JList<?> lista, Object valor, int indice,
					boolean selecionado, boolean foco) {
				Object texto = valor instanceof TipoSinistro ? ((TipoSinistro) valor).getNome() : valor;
				return super.getListCellRendererComponent(lista, texto, indice, selecionado, foco);
			}
		});

		JPanel form = UtilTela.painelForm();
		int y = 0;
		UtilTela.linha(form, y++, "Número:", txtNumero);
		UtilTela.linha(form, y++, "Placa do veículo:", txtPlaca);
		UtilTela.linha(form, y++, "Tipo:", cmbTipo);
		UtilTela.linha(form, y++, "Data/hora do sinistro:", txtDataHoraSinistro);
		UtilTela.linha(form, y++, "Valor do sinistro (R$):", spValor);
		UtilTela.linha(form, y++, "Usuário do registro:", txtUsuario);
		UtilTela.linha(form, y++, "Registrado em (automático):", txtDataHoraRegistro);

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

	private Sinistro montar(LocalDateTime registro) {
		if (numero().isEmpty()) {
			throw new IllegalArgumentException("Número do sinistro deve ser informado");
		}
		String placa = txtPlaca.getText().trim().toUpperCase();
		if (placa.isEmpty()) {
			throw new IllegalArgumentException("Placa do veículo deve ser informada");
		}
		Veiculo veiculo = veiculoDAO.buscar(placa);
		if (veiculo == null) {
			throw new IllegalArgumentException("Placa do veículo não existente");
		}
		LocalDateTime dataHora = UtilTela.lerDataHora(txtDataHoraSinistro, "Data/hora do sinistro");
		if (dataHora == null) {
			throw new IllegalArgumentException("Data/hora do sinistro deve ser informada");
		}
		if (dataHora.isAfter(LocalDateTime.now())) {
			throw new IllegalArgumentException("Data/hora do sinistro deve ser menor ou igual à data/hora atual");
		}
		String usuario = txtUsuario.getText().trim();
		if (usuario.isEmpty()) {
			throw new IllegalArgumentException("Usuário do registro deve ser informado");
		}
		double valor = UtilTela.lerValor(spValor, "Valor do sinistro");
		if (valor <= 0) {
			throw new IllegalArgumentException("Valor do sinistro deve ser maior que zero");
		}
		return new Sinistro(numero(), veiculo, dataHora, registro, usuario, BigDecimal.valueOf(valor),
				(TipoSinistro) cmbTipo.getSelectedItem());
	}

	private void buscar() {
		if (numero().isEmpty()) {
			UtilTela.aviso(this, "Número do sinistro deve ser informado");
			return;
		}
		Sinistro s = dao.buscar(numero());
		if (s == null) {
			UtilTela.aviso(this, "Número do sinistro não existente");
			return;
		}
		txtPlaca.setText(s.getVeiculo() == null ? "" : s.getVeiculo().getPlaca());
		cmbTipo.setSelectedItem(s.getTipo());
		UtilTela.mostrarDataHora(txtDataHoraSinistro, s.getDataHoraSinistro());
		spValor.setValue(s.getValorSinistro().doubleValue());
		txtUsuario.setText(s.getUsuarioRegistro());
		txtDataHoraRegistro.setText(
				s.getDataHoraRegistro() == null ? "" : s.getDataHoraRegistro().format(UtilTela.FMT_DATA_HORA));
	}

	private void incluir() {
		LocalDateTime agora = LocalDateTime.now();
		boolean ok = dao.incluir(montar(agora));
		if (UtilTela.resultado(this, ok ? null : "Número do sinistro já existente", "Sinistro incluído com sucesso.")) {
			txtDataHoraRegistro.setText(agora.format(UtilTela.FMT_DATA_HORA));
		}
	}

	private void alterar() {
		Sinistro existente = dao.buscar(numero());
		if (existente == null) {
			UtilTela.aviso(this, "Número do sinistro não existente");
			return;
		}
		boolean ok = dao.alterar(montar(existente.getDataHoraRegistro()));
		UtilTela.resultado(this, ok ? null : "Número do sinistro não existente", "Sinistro alterado com sucesso.");
	}

	private void excluir() {
		if (numero().isEmpty()) {
			UtilTela.aviso(this, "Número do sinistro deve ser informado");
			return;
		}
		if (!UtilTela.confirmar(this, "Excluir o sinistro " + numero() + "?")) {
			return;
		}
		boolean ok = dao.excluir(numero());
		if (UtilTela.resultado(this, ok ? null : "Número do sinistro não existente", "Sinistro excluído com sucesso.")) {
			limpar();
		}
	}

	private void limpar() {
		txtNumero.setText("");
		txtPlaca.setText("");
		cmbTipo.setSelectedIndex(0);
		txtDataHoraSinistro.setValue(null);
		spValor.setValue(0.0);
		txtUsuario.setText("");
		txtDataHoraRegistro.setText("");
		txtNumero.requestFocusInWindow();
	}
}
