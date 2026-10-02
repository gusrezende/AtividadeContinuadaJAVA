package br.edu.cs.poo.ac.seguro.telas;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.function.Supplier;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import br.edu.cs.poo.ac.seguro.entidades.Endereco;
import br.edu.cs.poo.ac.seguro.entidades.SeguradoEmpresa;
import br.edu.cs.poo.ac.seguro.mediators.SeguradoEmpresaMediator;

public class TelaSeguradoEmpresa extends JFrame {
    private final SeguradoEmpresaMediator mediator = SeguradoEmpresaMediator.getInstancia();
    private final JTextField cnpj = new JTextField(18), nome = new JTextField(25), abertura = new JTextField(10), faturamento = new JTextField(12);
    private final JTextField logradouro = new JTextField(25), cep = new JTextField(10), numero = new JTextField(10), complemento = new JTextField(20), pais = new JTextField("Brasil", 15), cidade = new JTextField(20);
    private final JComboBox<String> estado = new JComboBox<>(new String[]{"PE","SP","RJ","MG","BA","PR","RS","SC","CE","PB","AL","RN","SE","ES","GO","DF","MT","MS","TO","PA","AM","RO","RR","AC","AP","MA","PI"});
    private final JCheckBox locadora = new JCheckBox("É locadora de veículos");

    public TelaSeguradoEmpresa() {
        setTitle("Cadastro de Segurado Empresa"); setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); setSize(720, 600); setLocationRelativeTo(null); add(formulario(), BorderLayout.CENTER);
    }

    private JPanel formulario() {
        JPanel p = new JPanel(new GridBagLayout()); int l=0;
        campo(p,l++,"CNPJ:",cnpj); campo(p,l++,"Nome:",nome); campo(p,l++,"Data da abertura (AAAA-MM-DD):",abertura); campo(p,l++,"Faturamento:",faturamento);
        campo(p,l++,"Logradouro:",logradouro); campo(p,l++,"CEP:",cep); campo(p,l++,"Número:",numero); campo(p,l++,"Complemento:",complemento); campo(p,l++,"País:",pais); campo(p,l++,"Estado:",estado); campo(p,l++,"Cidade:",cidade); campo(p,l++,"",locadora);
        JPanel b=new JPanel(); JButton buscar=new JButton("Buscar"), incluir=new JButton("Incluir"), alterar=new JButton("Alterar"), excluir=new JButton("Excluir"), limpar=new JButton("Limpar"); b.add(buscar);b.add(incluir);b.add(alterar);b.add(excluir);b.add(limpar);
        GridBagConstraints g=new GridBagConstraints();g.gridx=0;g.gridy=l;g.gridwidth=2;g.insets=new Insets(12,4,4,4);p.add(b,g);
        buscar.addActionListener(e->buscar()); incluir.addActionListener(e->executar(()->mediator.incluirSeguradoEmpresa(ler()))); alterar.addActionListener(e->executar(()->mediator.alterarSeguradoEmpresa(ler()))); excluir.addActionListener(e->mensagem(mediator.excluirSeguradoEmpresa(cnpj.getText()))); limpar.addActionListener(e->limpar());
        return p;
    }
    private void campo(JPanel p,int l,String texto,java.awt.Component c){GridBagConstraints a=new GridBagConstraints();a.gridx=0;a.gridy=l;a.anchor=GridBagConstraints.WEST;a.insets=new Insets(4,8,4,8);p.add(new JLabel(texto),a);GridBagConstraints b=new GridBagConstraints();b.gridx=1;b.gridy=l;b.weightx=1;b.fill=GridBagConstraints.HORIZONTAL;b.insets=new Insets(4,8,4,8);p.add(c,b);}
    private SeguradoEmpresa ler(){LocalDate d;try{d=LocalDate.parse(abertura.getText().trim());}catch(DateTimeParseException e){throw new IllegalArgumentException("Data deve estar no formato AAAA-MM-DD");}double f;try{f=Double.parseDouble(faturamento.getText().replace(',','.').trim());}catch(NumberFormatException e){throw new IllegalArgumentException("Faturamento inválido");}Endereco e=new Endereco(logradouro.getText(),cep.getText(),numero.getText(),complemento.getText(),pais.getText(),(String)estado.getSelectedItem(),cidade.getText());return new SeguradoEmpresa(nome.getText(),e,d,BigDecimal.ZERO,cnpj.getText(),f,locadora.isSelected());}
    private void buscar(){SeguradoEmpresa s=mediator.buscarSeguradoEmpresa(cnpj.getText());if(s==null){mensagem("Segurado empresa não encontrado");return;}cnpj.setText(s.getCnpj());nome.setText(s.getNome());abertura.setText(s.getDataAbertura()==null?"":s.getDataAbertura().toString());faturamento.setText(Double.toString(s.getFaturamento()));locadora.setSelected(s.isEhLocadoraDeVeiculos());Endereco e=s.getEndereco();if(e!=null){logradouro.setText(e.getLogradouro());cep.setText(e.getCep());numero.setText(e.getNumero());complemento.setText(e.getComplemento());pais.setText(e.getPais());estado.setSelectedItem(e.getEstado());cidade.setText(e.getCidade());}}
    private void executar(Supplier<String> acao){try{mensagem(acao.get());}catch(RuntimeException e){JOptionPane.showMessageDialog(this,e.getMessage(),"Erro",JOptionPane.ERROR_MESSAGE);}}
    private void mensagem(String s){JOptionPane.showMessageDialog(this,s==null?"Operação realizada com sucesso":s);}
    private void limpar(){cnpj.setText("");nome.setText("");abertura.setText("");faturamento.setText("");logradouro.setText("");cep.setText("");numero.setText("");complemento.setText("");pais.setText("Brasil");cidade.setText("");locadora.setSelected(false);}
}
