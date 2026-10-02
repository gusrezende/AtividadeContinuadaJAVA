package br.edu.cs.poo.ac.seguro.telas;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class MenuPrincipal extends JFrame {
    public MenuPrincipal() {
        setTitle("Sistema de Seguros");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(420, 220);
        setLocationRelativeTo(null);
        JPanel p = new JPanel(new GridLayout(3, 1, 8, 8));
        JButton pessoa = new JButton("Segurado Pessoa");
        JButton empresa = new JButton("Segurado Empresa");
        JButton veiculo = new JButton("Veículo");
        p.add(pessoa); p.add(empresa); p.add(veiculo);
        pessoa.addActionListener(e -> new TelaSeguradoPessoa().setVisible(true));
        empresa.addActionListener(e -> new TelaSeguradoEmpresa().setVisible(true));
        veiculo.addActionListener(e -> new TelaVeiculo().setVisible(true));
        add(p);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MenuPrincipal().setVisible(true));
    }
}
