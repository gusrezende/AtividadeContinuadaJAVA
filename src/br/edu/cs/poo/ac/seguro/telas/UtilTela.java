package br.edu.cs.poo.ac.seguro.telas;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.text.MaskFormatter;

/**
 * Funcoes de apoio compartilhadas pelas telas (somente Swing).
 */
final class UtilTela {
	static final DateTimeFormatter FMT_DATA = DateTimeFormatter.ofPattern("dd/MM/uuuu")
			.withResolverStyle(ResolverStyle.STRICT);
	static final DateTimeFormatter FMT_DATA_HORA = DateTimeFormatter.ofPattern("dd/MM/uuuu HH:mm")
			.withResolverStyle(ResolverStyle.STRICT);

	static final String[] UFS = { "AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA", "MT", "MS", "MG", "PA",
			"PB", "PR", "PE", "PI", "RJ", "RN", "RS", "RO", "RR", "SC", "SP", "SE", "TO" };

	private UtilTela() {
	}

	// ---------- campos de entrada ----------

	static JFormattedTextField campoMascara(String mascara) {
		try {
			MaskFormatter mf = new MaskFormatter(mascara);
			mf.setPlaceholderCharacter('_');
			return new JFormattedTextField(mf);
		} catch (ParseException e) {
			throw new IllegalStateException(e);
		}
	}

	static JFormattedTextField campoData() {
		return campoMascara("##/##/####");
	}

	static JFormattedTextField campoDataHora() {
		return campoMascara("##/##/#### ##:##");
	}

	static JSpinner campoValor() {
		JSpinner spinner = new JSpinner(new SpinnerNumberModel(0.0, 0.0, 1_000_000_000_000.0, 100.0));
		spinner.setEditor(new JSpinner.NumberEditor(spinner, "#,##0.00"));
		return spinner;
	}

	static JSpinner campoAno() {
		int atual = Year.now().getValue();
		JSpinner spinner = new JSpinner(new SpinnerNumberModel(atual, 1900, atual + 1, 1));
		spinner.setEditor(new JSpinner.NumberEditor(spinner, "0"));
		return spinner;
	}

	// ---------- leitura dos campos ----------

	/** Texto de um campo com mascara numerica, sem os caracteres de preenchimento. */
	static String textoMascarado(JFormattedTextField campo) {
		return campo.getText().replace("_", "").trim();
	}

	static String textoCombo(JComboBox<?> combo) {
		Object item = combo.getSelectedItem();
		return item == null ? "" : item.toString().trim();
	}

	static LocalDate lerData(JFormattedTextField campo, String nome) {
		String texto = campo.getText().trim();
		if (texto.replaceAll("[_/: ]", "").isEmpty()) {
			return null;
		}
		try {
			return LocalDate.parse(texto, FMT_DATA);
		} catch (DateTimeParseException e) {
			throw new IllegalArgumentException(nome + " inválida. Use o formato dd/mm/aaaa.");
		}
	}

	static LocalDateTime lerDataHora(JFormattedTextField campo, String nome) {
		String texto = campo.getText().trim();
		if (texto.replaceAll("[_/: ]", "").isEmpty()) {
			return null;
		}
		try {
			return LocalDateTime.parse(texto, FMT_DATA_HORA);
		} catch (DateTimeParseException e) {
			throw new IllegalArgumentException(nome + " inválida. Use o formato dd/mm/aaaa hh:mm.");
		}
	}

	static double lerValor(JSpinner spinner, String nome) {
		try {
			spinner.commitEdit();
		} catch (ParseException e) {
			throw new IllegalArgumentException(nome + " inválido.");
		}
		return ((Number) spinner.getValue()).doubleValue();
	}

	static int lerInteiro(JSpinner spinner, String nome) {
		try {
			spinner.commitEdit();
		} catch (ParseException e) {
			throw new IllegalArgumentException(nome + " inválido.");
		}
		return ((Number) spinner.getValue()).intValue();
	}

	// ---------- exibicao nos campos ----------

	static void mostrarData(JFormattedTextField campo, LocalDate data) {
		if (data == null) {
			campo.setValue(null);
		} else {
			campo.setText(data.format(FMT_DATA));
		}
	}

	static void mostrarDataHora(JFormattedTextField campo, LocalDateTime dataHora) {
		if (dataHora == null) {
			campo.setValue(null);
		} else {
			campo.setText(dataHora.format(FMT_DATA_HORA));
		}
	}

	// ---------- mensagens ----------

	static void info(Component pai, String mensagem) {
		JOptionPane.showMessageDialog(pai, mensagem, "Informação", JOptionPane.INFORMATION_MESSAGE);
	}

	static void aviso(Component pai, String mensagem) {
		JOptionPane.showMessageDialog(pai, mensagem, "Atenção", JOptionPane.WARNING_MESSAGE);
	}

	static boolean confirmar(Component pai, String mensagem) {
		return JOptionPane.showConfirmDialog(pai, mensagem, "Confirmação",
				JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
	}

	/** Mostra a mensagem de erro do mediator ou, se for nula, a mensagem de sucesso. */
	static boolean resultado(Component pai, String erro, String sucesso) {
		if (erro == null) {
			info(pai, sucesso);
			return true;
		}
		aviso(pai, erro);
		return false;
	}

	/** Executa uma acao de botao tratando entradas invalidas e erros inesperados. */
	static void executar(Component pai, Runnable acao) {
		try {
			acao.run();
		} catch (IllegalArgumentException e) {
			aviso(pai, e.getMessage());
		} catch (RuntimeException e) {
			JOptionPane.showMessageDialog(pai, "Erro inesperado: " + e.getMessage(), "Erro",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	// ---------- montagem de layout ----------

	static void linha(JPanel painel, int y, JLabel rotulo, java.awt.Component campo) {
		GridBagConstraints c = new GridBagConstraints();
		c.gridy = y;
		c.insets = new Insets(3, 6, 3, 6);
		c.anchor = GridBagConstraints.WEST;
		c.gridx = 0;
		painel.add(rotulo, c);
		c.gridx = 1;
		c.weightx = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		painel.add(campo, c);
	}

	static void linha(JPanel painel, int y, String rotulo, java.awt.Component campo) {
		linha(painel, y, new JLabel(rotulo), campo);
	}

	static JPanel painelForm() {
		JPanel painel = new JPanel(new GridBagLayout());
		painel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
		return painel;
	}

	static JPanel painelBotoes(JButton... botoes) {
		JPanel painel = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 8, 8));
		for (JButton botao : botoes) {
			painel.add(botao);
		}
		return painel;
	}

	static void titulo(JFrame janela, String titulo) {
		janela.setTitle(titulo);
		janela.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	static void montarJanela(JFrame janela, JPanel form, JPanel botoes) {
		janela.getContentPane().setLayout(new BorderLayout());
		janela.getContentPane().add(form, BorderLayout.CENTER);
		janela.getContentPane().add(botoes, BorderLayout.SOUTH);
		janela.pack();
		janela.setLocationRelativeTo(null);
	}

	static JLabel rotuloNegrito(String texto) {
		JLabel rotulo = new JLabel(texto);
		rotulo.setFont(rotulo.getFont().deriveFont(Font.BOLD));
		return rotulo;
	}
}
