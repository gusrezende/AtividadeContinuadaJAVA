package br.edu.cs.poo.ac.seguro.telas;

import java.math.BigDecimal;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;

import br.edu.cs.poo.ac.seguro.entidades.SeguradoPessoa;
import br.edu.cs.poo.ac.seguro.mediators.SeguradoPessoaMediator;

/**
 * CRUD de segurado pessoa em uma unica tela.
 */
@SuppressWarnings("serial")
public class TelaSeguradoPessoa extends JFrame {
	private final SeguradoPessoaMediator mediator = SeguradoPessoaMediator.getInstancia();

	private final JFormattedTextField txtCpf = UtilTela.campoMascara("###########");
	private final JTextField txtNome = new JTextField(28);
	private final JFormattedTextField txtDataNascimento = UtilTela.campoData();
	private final JSpinner spRenda = UtilTela.campoValor();
	private final JTextField txtBonus = new JTextField("0.00", 10);
	private final PainelEndereco painelEndereco = new PainelEndereco();

	private final JButton btnBuscar = new JButton("Buscar");
	private final JButton btnIncluir = new JButton("Incluir");
	private final JButton btnAlterar = new JButton("Alterar");
	private final JButton btnExcluir = new JButton("Excluir");
	private final JButton btnLimpar = new JButton("Limpar");

	public TelaSeguradoPessoa() {
		UtilTela.titulo(this, "Segurado Pessoa");
		txtBonus.setEditable(false);

		JPanel form = UtilTela.painelForm();
		int y = 0;
		UtilTela.linha(form, y++, "CPF:", txtCpf);
		UtilTela.linha(form, y++, "Nome:", txtNome);
		UtilTela.linha(form, y++, "Data de nascimento:", txtDataNascimento);
		UtilTela.linha(form, y++, "Renda (R$):", spRenda);
		UtilTela.linha(form, y++, "Bônus (somente leitura):", txtBonus);
		java.awt.GridBagConstraints c = new java.awt.GridBagConstraints();
		c.gridy = y;
		c.gridx = 0;
		c.gridwidth = 2;
		c.fill = java.awt.GridBagConstraints.HORIZONTAL;
		c.insets = new java.awt.Insets(8, 6, 3, 6);
		form.add(painelEndereco, c);

		btnBuscar.addActionListener(e -> UtilTela.executar(this, this::buscar));
		btnIncluir.addActionListener(e -> UtilTela.executar(this, this::incluir));
		btnAlterar.addActionListener(e -> UtilTela.executar(this, this::alterar));
		btnExcluir.addActionListener(e -> UtilTela.executar(this, this::excluir));
		btnLimpar.addActionListener(e -> limpar());

		UtilTela.montarJanela(this, form, UtilTela.painelBotoes(btnBuscar, btnIncluir, btnAlterar, btnExcluir, btnLimpar));
	}

	private String cpf() {
		return UtilTela.textoMascarado(txtCpf);
	}

	private SeguradoPessoa montar(BigDecimal bonus) {
		return new SeguradoPessoa(txtNome.getText(), painelEndereco.obter(),
				UtilTela.lerData(txtDataNascimento, "Data de nascimento"), bonus, cpf(),
				UtilTela.lerValor(spRenda, "Renda"));
	}

	private void buscar() {
		if (cpf().isEmpty()) {
			UtilTela.aviso(this, "CPF deve ser informado");
			return;
		}
		SeguradoPessoa seg = mediator.buscarSeguradoPessoa(cpf());
		if (seg == null) {
			UtilTela.aviso(this, "CPF do segurado pessoa não existente");
			return;
		}
		txtNome.setText(seg.getNome());
		UtilTela.mostrarData(txtDataNascimento, seg.getDataNascimento());
		spRenda.setValue(seg.getRenda());
		txtBonus.setText(seg.getBonus().toPlainString());
		painelEndereco.preencher(seg.getEndereco());
	}

	private void incluir() {
		String erro = mediator.incluirSeguradoPessoa(montar(BigDecimal.ZERO));
		UtilTela.resultado(this, erro, "Segurado pessoa incluído com sucesso.");
	}

	private void alterar() {
		SeguradoPessoa existente = mediator.buscarSeguradoPessoa(cpf());
		BigDecimal bonus = existente == null ? BigDecimal.ZERO : existente.getBonus();
		String erro = mediator.alterarSeguradoPessoa(montar(bonus));
		UtilTela.resultado(this, erro, "Segurado pessoa alterado com sucesso.");
	}

	private void excluir() {
		if (cpf().isEmpty()) {
			UtilTela.aviso(this, "CPF deve ser informado");
			return;
		}
		if (!UtilTela.confirmar(this, "Excluir o segurado de CPF " + cpf() + "?")) {
			return;
		}
		String erro = mediator.excluirSeguradoPessoa(cpf());
		if (UtilTela.resultado(this, erro, "Segurado pessoa excluído com sucesso.")) {
			limpar();
		}
	}

	private void limpar() {
		txtCpf.setValue(null);
		txtNome.setText("");
		txtDataNascimento.setValue(null);
		spRenda.setValue(0.0);
		txtBonus.setText("0.00");
		painelEndereco.limpar();
		txtCpf.requestFocusInWindow();
	}
}
