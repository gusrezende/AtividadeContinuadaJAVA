package br.edu.cs.poo.ac.seguro.telas;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;

import br.edu.cs.poo.ac.seguro.daos.VeiculoDAO;
import br.edu.cs.poo.ac.seguro.entidades.CategoriaVeiculo;
import br.edu.cs.poo.ac.seguro.entidades.SeguradoEmpresa;
import br.edu.cs.poo.ac.seguro.entidades.SeguradoPessoa;
import br.edu.cs.poo.ac.seguro.entidades.Veiculo;
import br.edu.cs.poo.ac.seguro.mediators.SeguradoEmpresaMediator;
import br.edu.cs.poo.ac.seguro.mediators.SeguradoPessoaMediator;

/**
 * CRUD de veiculo em uma unica tela. Ainda nao existe mediator de veiculo,
 * entao a tela usa o VeiculoDAO e os mediators de segurado para achar o dono.
 */
@SuppressWarnings("serial")
public class TelaVeiculo extends JFrame {
	private final VeiculoDAO dao = new VeiculoDAO();
	private final SeguradoPessoaMediator pessoaMediator = SeguradoPessoaMediator.getInstancia();
	private final SeguradoEmpresaMediator empresaMediator = SeguradoEmpresaMediator.getInstancia();

	private final JTextField txtPlaca = new JTextField(12);
	private final JSpinner spAno = UtilTela.campoAno();
	private final JComboBox<CategoriaVeiculo> cmbCategoria = new JComboBox<>(CategoriaVeiculo.values());
	private final JRadioButton rbPessoa = new JRadioButton("Pessoa", true);
	private final JRadioButton rbEmpresa = new JRadioButton("Empresa");
	private final JLabel lblDocumento = new JLabel("CPF do proprietário:");
	private final JTextField txtDocumento = new JTextField(16);

	private final JButton btnBuscar = new JButton("Buscar");
	private final JButton btnIncluir = new JButton("Incluir");
	private final JButton btnAlterar = new JButton("Alterar");
	private final JButton btnExcluir = new JButton("Excluir");
	private final JButton btnLimpar = new JButton("Limpar");

	public TelaVeiculo() {
		UtilTela.titulo(this, "Veículo");

		ButtonGroup grupo = new ButtonGroup();
		grupo.add(rbPessoa);
		grupo.add(rbEmpresa);
		JPanel tipoDono = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 8, 0));
		tipoDono.add(rbPessoa);
		tipoDono.add(rbEmpresa);
		rbPessoa.addActionListener(e -> lblDocumento.setText("CPF do proprietário:"));
		rbEmpresa.addActionListener(e -> lblDocumento.setText("CNPJ do proprietário:"));

		JPanel form = UtilTela.painelForm();
		int y = 0;
		UtilTela.linha(form, y++, "Placa:", txtPlaca);
		UtilTela.linha(form, y++, "Ano:", spAno);
		UtilTela.linha(form, y++, "Categoria:", cmbCategoria);
		UtilTela.linha(form, y++, "Tipo de proprietário:", tipoDono);
		UtilTela.linha(form, y++, lblDocumento, txtDocumento);

		btnBuscar.addActionListener(e -> UtilTela.executar(this, this::buscar));
		btnIncluir.addActionListener(e -> UtilTela.executar(this, this::incluir));
		btnAlterar.addActionListener(e -> UtilTela.executar(this, this::alterar));
		btnExcluir.addActionListener(e -> UtilTela.executar(this, this::excluir));
		btnLimpar.addActionListener(e -> limpar());

		UtilTela.montarJanela(this, form, UtilTela.painelBotoes(btnBuscar, btnIncluir, btnAlterar, btnExcluir, btnLimpar));
	}

	private String placa() {
		return txtPlaca.getText().trim().toUpperCase();
	}

	private Veiculo montar() {
		if (placa().isEmpty()) {
			throw new IllegalArgumentException("Placa deve ser informada");
		}
		String documento = txtDocumento.getText().trim();
		if (documento.isEmpty()) {
			throw new IllegalArgumentException(lblDocumento.getText().replace(":", "") + " deve ser informado");
		}
		SeguradoPessoa pessoa = null;
		SeguradoEmpresa empresa = null;
		if (rbPessoa.isSelected()) {
			pessoa = pessoaMediator.buscarSeguradoPessoa(documento);
			if (pessoa == null) {
				throw new IllegalArgumentException("CPF do segurado pessoa não existente");
			}
		} else {
			empresa = empresaMediator.buscarSeguradoEmpresa(documento);
			if (empresa == null) {
				throw new IllegalArgumentException("CNPJ do segurado empresa não existente");
			}
		}
		return new Veiculo(placa(), UtilTela.lerInteiro(spAno, "Ano"), empresa, pessoa,
				(CategoriaVeiculo) cmbCategoria.getSelectedItem());
	}

	private void buscar() {
		if (placa().isEmpty()) {
			UtilTela.aviso(this, "Placa deve ser informada");
			return;
		}
		Veiculo v = dao.buscar(placa());
		if (v == null) {
			UtilTela.aviso(this, "Placa do veículo não existente");
			return;
		}
		spAno.setValue(v.getAno());
		cmbCategoria.setSelectedItem(v.getCategoria());
		if (v.getProprietarioPessoa() != null) {
			rbPessoa.setSelected(true);
			lblDocumento.setText("CPF do proprietário:");
			txtDocumento.setText(v.getProprietarioPessoa().getCpf());
		} else if (v.getProprietarioEmpresa() != null) {
			rbEmpresa.setSelected(true);
			lblDocumento.setText("CNPJ do proprietário:");
			txtDocumento.setText(v.getProprietarioEmpresa().getCnpj());
		} else {
			txtDocumento.setText("");
		}
	}

	private void incluir() {
		boolean ok = dao.incluir(montar());
		UtilTela.resultado(this, ok ? null : "Placa do veículo já existente", "Veículo incluído com sucesso.");
	}

	private void alterar() {
		boolean ok = dao.alterar(montar());
		UtilTela.resultado(this, ok ? null : "Placa do veículo não existente", "Veículo alterado com sucesso.");
	}

	private void excluir() {
		if (placa().isEmpty()) {
			UtilTela.aviso(this, "Placa deve ser informada");
			return;
		}
		if (!UtilTela.confirmar(this, "Excluir o veículo de placa " + placa() + "?")) {
			return;
		}
		boolean ok = dao.excluir(placa());
		if (UtilTela.resultado(this, ok ? null : "Placa do veículo não existente", "Veículo excluído com sucesso.")) {
			limpar();
		}
	}

	private void limpar() {
		txtPlaca.setText("");
		spAno.setValue(java.time.Year.now().getValue());
		cmbCategoria.setSelectedIndex(0);
		rbPessoa.setSelected(true);
		lblDocumento.setText("CPF do proprietário:");
		txtDocumento.setText("");
		txtPlaca.requestFocusInWindow();
	}
}
