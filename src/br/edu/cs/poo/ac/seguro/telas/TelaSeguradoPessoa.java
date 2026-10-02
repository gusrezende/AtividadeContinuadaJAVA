package br.edu.cs.poo.ac.seguro.telas;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import br.edu.cs.poo.ac.seguro.entidades.Endereco;
import br.edu.cs.poo.ac.seguro.entidades.SeguradoPessoa;
import br.edu.cs.poo.ac.seguro.mediators.SeguradoPessoaMediator;

public class TelaSeguradoPessoa extends JFrame {
    private final SeguradoPessoaMediator mediator = SeguradoPessoaMediator.getInstancia();

    private final JTextField cpf = new JTextField(16);
    private final JTextField nome = new JTextField(25);
    private final JTextField nascimento = new JTextField(10);
    private final JTextField renda = new JTextField(12);
    private final JTextField logradouro = new JTextField(25);
    private final JTextField cep = new JTextField(10);
    private final JTextField numero = new JTextField(10);
    private final JTextField complemento = new JTextField(20);
    private final JTextField pais = new JTextField("Brasil", 15);
    private final JComboBox<String> estado = new JComboBox<>(new String[]{"PE","SP","RJ","MG","BA","PR","RS","SC","CE","PB","AL","RN","SE","ES","GO","DF","MT","MS","TO","PA","AM","RO","RR","AC","AP","MA","PI"});
    private final JTextField cidade = new JTextField(20);

    public TelaSeguradoPessoa() {
        setTitle("Cadastro de Segurado Pessoa");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(720, 560);
        setLocationRelativeTo(null);
        add(criarFormulario(), BorderLayout.CENTER);
    }

    private JPanel criarFormulario() {
        JPanel painel = new JPanel(new GridBagLayout());
        int linha = 0;
        addCampo(painel, linha++, "CPF:", cpf);
        addCampo(painel, linha++, "Nome:", nome);
        addCampo(painel, linha++, "Nascimento (AAAA-MM-DD):", nascimento);
        addCampo(painel, linha++, "Renda:", renda);
        addCampo(painel, linha++, "Logradouro:", logradouro);
        addCampo(painel, linha++, "CEP:", cep);
        addCampo(painel, linha++, "Número:", numero);
        addCampo(painel, linha++, "Complemento:", complemento);
        addCampo(painel, linha++, "País:", pais);
        addCampo(painel, linha++, "Estado:", estado);
        addCampo(painel, linha++, "Cidade:", cidade);

        JPanel botoes = new JPanel();
        JButton buscar = new JButton("Buscar");
        JButton incluir = new JButton("Incluir");
        JButton alterar = new JButton("Alterar");
        JButton excluir = new JButton("Excluir");
        JButton limpar = new JButton("Limpar");
        botoes.add(buscar); botoes.add(incluir); botoes.add(alterar); botoes.add(excluir); botoes.add(limpar);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = linha; gbc.gridwidth = 2; gbc.insets = new Insets(12, 4, 4, 4);
        painel.add(botoes, gbc);

        buscar.addActionListener(e -> buscar());
        incluir.addActionListener(e -> incluir());
        alterar.addActionListener(e -> alterar());
        excluir.addActionListener(e -> excluir());
        limpar.addActionListener(e -> limpar());
        return painel;
    }

    private void addCampo(JPanel painel, int linha, String label, java.awt.Component campo) {
        GridBagConstraints l = new GridBagConstraints();
        l.gridx = 0; l.gridy = linha; l.anchor = GridBagConstraints.WEST; l.insets = new Insets(4, 8, 4, 8);
        painel.add(new JLabel(label), l);
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 1; c.gridy = linha; c.weightx = 1; c.fill = GridBagConstraints.HORIZONTAL; c.insets = new Insets(4, 8, 4, 8);
        painel.add(campo, c);
    }

    private SeguradoPessoa lerTela() {
        LocalDate data;
        try { data = LocalDate.parse(nascimento.getText().trim()); }
        catch (DateTimeParseException ex) { throw new IllegalArgumentException("Data deve estar no formato AAAA-MM-DD"); }
        double valor;
        try { valor = Double.parseDouble(renda.getText().replace(',', '.').trim()); }
        catch (NumberFormatException ex) { throw new IllegalArgumentException("Renda inválida"); }
        Endereco endereco = new Endereco(logradouro.getText(), cep.getText(), numero.getText(), complemento.getText(), pais.getText(), (String) estado.getSelectedItem(), cidade.getText());
        return new SeguradoPessoa(nome.getText(), endereco, data, BigDecimal.ZERO, cpf.getText(), valor);
    }

    private void incluir() { executar(() -> mediator.incluirSeguradoPessoa(lerTela())); }
    private void alterar() { executar(() -> mediator.alterarSeguradoPessoa(lerTela())); }
    private void excluir() { executarMensagem(mediator.excluirSeguradoPessoa(cpf.getText())); }

    private void buscar() {
        SeguradoPessoa seg = mediator.buscarSeguradoPessoa(cpf.getText());
        if (seg == null) { JOptionPane.showMessageDialog(this, "Segurado pessoa não encontrado"); return; }
        preencher(seg);
    }

    private void preencher(SeguradoPessoa seg) {
        cpf.setText(seg.getCpf()); nome.setText(seg.getNome());
        nascimento.setText(seg.getDataNascimento() == null ? "" : seg.getDataNascimento().toString());
        renda.setText(Double.toString(seg.getRenda()));
        Endereco e = seg.getEndereco();
        if (e != null) { logradouro.setText(e.getLogradouro()); cep.setText(e.getCep()); numero.setText(e.getNumero()); complemento.setText(e.getComplemento()); pais.setText(e.getPais()); estado.setSelectedItem(e.getEstado()); cidade.setText(e.getCidade()); }
    }

    private void executar(java.util.function.Supplier<String> acao) {
        try { executarMensagem(acao.get()); }
        catch (RuntimeException ex) { JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE); }
    }
    private void executarMensagem(String msg) { JOptionPane.showMessageDialog(this, msg == null ? "Operação realizada com sucesso" : msg); }
    private void limpar() { cpf.setText(""); nome.setText(""); nascimento.setText(""); renda.setText(""); logradouro.setText(""); cep.setText(""); numero.setText(""); complemento.setText(""); pais.setText("Brasil"); cidade.setText(""); }

    public static void main(String[] args) { javax.swing.SwingUtilities.invokeLater(() -> new TelaSeguradoPessoa().setVisible(true)); }
}
