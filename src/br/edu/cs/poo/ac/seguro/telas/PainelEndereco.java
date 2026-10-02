package br.edu.cs.poo.ac.seguro.telas;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.JTextField;

import br.edu.cs.poo.ac.seguro.entidades.Endereco;

/**
 * Painel reutilizado pelas telas de segurado para digitar um endereco.
 */
@SuppressWarnings("serial")
class PainelEndereco extends JPanel {
	private final JTextField txtLogradouro = new JTextField(28);
	private final JFormattedTextField txtCep = UtilTela.campoMascara("########");
	private final JTextField txtNumero = new JTextField(10);
	private final JTextField txtComplemento = new JTextField(15);
	private final JTextField txtCidade = new JTextField(20);
	private final JComboBox<String> cmbEstado = new JComboBox<>(UtilTela.UFS);
	private final JTextField txtPais = new JTextField("Brasil", 15);

	PainelEndereco() {
		super(new java.awt.GridBagLayout());
		setBorder(BorderFactory.createTitledBorder("Endereço"));
		cmbEstado.setEditable(true);
		cmbEstado.setSelectedItem("PE");
		int y = 0;
		UtilTela.linha(this, y++, "Logradouro:", txtLogradouro);
		UtilTela.linha(this, y++, "CEP:", txtCep);
		UtilTela.linha(this, y++, "Número:", txtNumero);
		UtilTela.linha(this, y++, "Complemento:", txtComplemento);
		UtilTela.linha(this, y++, "Cidade:", txtCidade);
		UtilTela.linha(this, y++, "Estado (UF):", cmbEstado);
		UtilTela.linha(this, y++, "País:", txtPais);
	}

	Endereco obter() {
		return new Endereco(txtLogradouro.getText(), UtilTela.textoMascarado(txtCep), txtNumero.getText(),
				txtComplemento.getText(), txtPais.getText(), UtilTela.textoCombo(cmbEstado), txtCidade.getText());
	}

	void preencher(Endereco e) {
		if (e == null) {
			limpar();
			return;
		}
		txtLogradouro.setText(e.getLogradouro());
		if (e.getCep() == null) {
			txtCep.setValue(null);
		} else {
			txtCep.setText(e.getCep());
		}
		txtNumero.setText(e.getNumero());
		txtComplemento.setText(e.getComplemento());
		txtCidade.setText(e.getCidade());
		cmbEstado.setSelectedItem(e.getEstado());
		txtPais.setText(e.getPais());
	}

	void limpar() {
		txtLogradouro.setText("");
		txtCep.setValue(null);
		txtNumero.setText("");
		txtComplemento.setText("");
		txtCidade.setText("");
		cmbEstado.setSelectedItem("PE");
		txtPais.setText("Brasil");
	}
}
