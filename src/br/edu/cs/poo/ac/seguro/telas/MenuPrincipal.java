package br.edu.cs.poo.ac.seguro.telas;

import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * Janela inicial: um botao para abrir cada tela do sistema.
 */
@SuppressWarnings("serial")
public class MenuPrincipal extends JFrame {
	private final JButton btnPessoa = new JButton("Segurado Pessoa");
	private final JButton btnEmpresa = new JButton("Segurado Empresa");
	private final JButton btnVeiculo = new JButton("Veículo");
	private final JButton btnSinistro = new JButton("Sinistro");
	private final JButton btnApolice = new JButton("Apólice");

	public MenuPrincipal() {
		setTitle("Sistema de Seguros");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel painel = new JPanel(new GridLayout(0, 1, 8, 8));
		painel.setBorder(BorderFactory.createEmptyBorder(16, 24, 16, 24));
		painel.add(btnPessoa);
		painel.add(btnEmpresa);
		painel.add(btnVeiculo);
		painel.add(btnSinistro);
		painel.add(btnApolice);
		setContentPane(painel);

		btnPessoa.addActionListener(e -> new TelaSeguradoPessoa().setVisible(true));
		btnEmpresa.addActionListener(e -> new TelaSeguradoEmpresa().setVisible(true));
		btnVeiculo.addActionListener(e -> new TelaVeiculo().setVisible(true));
		btnSinistro.addActionListener(e -> new TelaSinistro().setVisible(true));
		btnApolice.addActionListener(e -> new TelaApolice().setVisible(true));

		pack();
		setLocationRelativeTo(null);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new MenuPrincipal().setVisible(true));
	}
}
