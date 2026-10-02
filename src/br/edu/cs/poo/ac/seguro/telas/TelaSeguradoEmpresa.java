package br.edu.cs.poo.ac.seguro.telas;

import java.math.BigDecimal;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;

import br.edu.cs.poo.ac.seguro.entidades.SeguradoEmpresa;
import br.edu.cs.poo.ac.seguro.mediators.SeguradoEmpresaMediator;

/**
 * CRUD de segurado empresa em uma unica tela.
 */
@SuppressWarnings("serial")
public class TelaSeguradoEmpresa extends JFrame {
	private final SeguradoEmpresaMediator mediator = SeguradoEmpresaMediator.getInstancia();

	private final JFormattedTextField txtCnpj = UtilTela.campoMascara("##############");
	private final JTextField txtNome = new JTextField(28);
	private final JFormattedTextField txtDataAbertura = UtilTela.campoData();
	private final JSpinner spFaturamento = UtilTela.campoValor();
	private final JCheckBox chkLocadora = new JCheckBox("É locadora de veículos");
	private final JTextField txtBonus = new JTextField("0.00", 10);
	private final PainelEndereco painelEndereco = new PainelEndereco();

	private final JButton btnBuscar = new JButton("Buscar");
	private final JButton btnIncluir = new JButton("Incluir");
	private final JButton btnAlterar = new JButton("Alterar");
	private final JButton btnExcluir = new JButton("Excluir");
	private final JButton btnLimpar = new JButton("Limpar");

	public TelaSeguradoEmpresa() {
		UtilTela.titulo(this, "Segurado Empresa");
		txtBonus.setEditable(false);

		JPanel form = UtilTela.painelForm();
		int y = 0;
		UtilTela.linha(form, y++, "CNPJ:", txtCnpj);
		UtilTela.linha(form, y++, "Nome:", txtNome);
		UtilTela.linha(form, y++, "Data de abertura:", txtDataAbertura);
		UtilTela.linha(form, y++, "Faturamento (R$):", spFaturamento);
		UtilTela.linha(form, y++, "", chkLocadora);
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

	private String cnpj() {
		return UtilTela.textoMascarado(txtCnpj);
	}

	private SeguradoEmpresa montar(BigDecimal bonus) {
		return new SeguradoEmpresa(txtNome.getText(), painelEndereco.obter(),
				UtilTela.lerData(txtDataAbertura, "Data de abertura"), bonus, cnpj(),
				UtilTela.lerValor(spFaturamento, "Faturamento"), chkLocadora.isSelected());
	}

	private void buscar() {
		if (cnpj().isEmpty()) {
			UtilTela.aviso(this, "CNPJ deve ser informado");
			return;
		}
		SeguradoEmpresa seg = mediator.buscarSeguradoEmpresa(cnpj());
		if (seg == null) {
			UtilTela.aviso(this, "CNPJ do segurado empresa não existente");
			return;
		}
		txtNome.setText(seg.getNome());
		UtilTela.mostrarData(txtDataAbertura, seg.getDataAbertura());
		spFaturamento.setValue(seg.getFaturamento());
		chkLocadora.setSelected(seg.isEhLocadoraDeVeiculos());
		txtBonus.setText(seg.getBonus().toPlainString());
		painelEndereco.preencher(seg.getEndereco());
	}

	private void incluir() {
		String erro = mediator.incluirSeguradoEmpresa(montar(BigDecimal.ZERO));
		UtilTela.resultado(this, erro, "Segurado empresa incluído com sucesso.");
	}

	private void alterar() {
		SeguradoEmpresa existente = mediator.buscarSeguradoEmpresa(cnpj());
		BigDecimal bonus = existente == null ? BigDecimal.ZERO : existente.getBonus();
		String erro = mediator.alterarSeguradoEmpresa(montar(bonus));
		UtilTela.resultado(this, erro, "Segurado empresa alterado com sucesso.");
	}

	private void excluir() {
		if (cnpj().isEmpty()) {
			UtilTela.aviso(this, "CNPJ deve ser informado");
			return;
		}
		if (!UtilTela.confirmar(this, "Excluir o segurado de CNPJ " + cnpj() + "?")) {
			return;
		}
		String erro = mediator.excluirSeguradoEmpresa(cnpj());
		if (UtilTela.resultado(this, erro, "Segurado empresa excluído com sucesso.")) {
			limpar();
		}
	}

	private void limpar() {
		txtCnpj.setValue(null);
		txtNome.setText("");
		txtDataAbertura.setValue(null);
		spFaturamento.setValue(0.0);
		chkLocadora.setSelected(false);
		txtBonus.setText("0.00");
		painelEndereco.limpar();
		txtCnpj.requestFocusInWindow();
	}
}
