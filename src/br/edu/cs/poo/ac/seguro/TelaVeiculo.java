package br.edu.cs.poo.ac.seguro.telas;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.function.Supplier;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import br.edu.cs.poo.ac.seguro.daos.VeiculoDAO;
import br.edu.cs.poo.ac.seguro.entidades.CategoriaVeiculo;
import br.edu.cs.poo.ac.seguro.entidades.SeguradoEmpresa;
import br.edu.cs.poo.ac.seguro.entidades.SeguradoPessoa;
import br.edu.cs.poo.ac.seguro.entidades.Veiculo;
import br.edu.cs.poo.ac.seguro.mediators.SeguradoEmpresaMediator;
import br.edu.cs.poo.ac.seguro.mediators.SeguradoPessoaMediator;

public class TelaVeiculo extends JFrame {
    private final VeiculoDAO dao = new VeiculoDAO();
    private final SeguradoPessoaMediator pessoaMediator = SeguradoPessoaMediator.getInstancia();
    private final SeguradoEmpresaMediator empresaMediator = SeguradoEmpresaMediator.getInstancia();
    private final JTextField placa=new JTextField(12), ano=new JTextField(6), cpf=new JTextField(16), cnpj=new JTextField(18);
    private final JComboBox<CategoriaVeiculo> categoria=new JComboBox<>(CategoriaVeiculo.values());

    public TelaVeiculo(){setTitle("Cadastro de Veículo");setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);setSize(560,350);setLocationRelativeTo(null);add(formulario(),BorderLayout.CENTER);}
    private JPanel formulario(){JPanel p=new JPanel(new GridBagLayout());int l=0;campo(p,l++,"Placa:",placa);campo(p,l++,"Ano:",ano);campo(p,l++,"Categoria:",categoria);campo(p,l++,"CPF do proprietário (opcional):",cpf);campo(p,l++,"CNPJ do proprietário (opcional):",cnpj);JPanel b=new JPanel();JButton buscar=new JButton("Buscar"),incluir=new JButton("Incluir"),alterar=new JButton("Alterar"),excluir=new JButton("Excluir"),limpar=new JButton("Limpar");b.add(buscar);b.add(incluir);b.add(alterar);b.add(excluir);b.add(limpar);GridBagConstraints g=new GridBagConstraints();g.gridx=0;g.gridy=l;g.gridwidth=2;g.insets=new Insets(12,4,4,4);p.add(b,g);buscar.addActionListener(e->buscar());incluir.addActionListener(e->executar(()->incluir()));alterar.addActionListener(e->executar(()->alterar()));excluir.addActionListener(e->mensagem(dao.excluir(placa.getText())?null:"Veículo não existente"));limpar.addActionListener(e->limpar());return p;}
    private void campo(JPanel p,int l,String t,java.awt.Component c){GridBagConstraints a=new GridBagConstraints();a.gridx=0;a.gridy=l;a.anchor=GridBagConstraints.WEST;a.insets=new Insets(4,8,4,8);p.add(new JLabel(t),a);GridBagConstraints b=new GridBagConstraints();b.gridx=1;b.gridy=l;b.weightx=1;b.fill=GridBagConstraints.HORIZONTAL;b.insets=new Insets(4,8,4,8);p.add(c,b);}
    private Veiculo ler(){int a;try{a=Integer.parseInt(ano.getText().trim());}catch(NumberFormatException e){throw new IllegalArgumentException("Ano inválido");}SeguradoPessoa pessoa=null;SeguradoEmpresa empresa=null;if(!cpf.getText().trim().isEmpty())pessoa=pessoaMediator.buscarSeguradoPessoa(cpf.getText().trim());if(!cnpj.getText().trim().isEmpty())empresa=empresaMediator.buscarSeguradoEmpresa(cnpj.getText().trim());if(!cpf.getText().trim().isEmpty()&&pessoa==null)throw new IllegalArgumentException("Proprietário pessoa não encontrado");if(!cnpj.getText().trim().isEmpty()&&empresa==null)throw new IllegalArgumentException("Proprietário empresa não encontrado");if(pessoa!=null&&empresa!=null)throw new IllegalArgumentException("Informe CPF ou CNPJ, não os dois");return new Veiculo(placa.getText().trim(),a,empresa,pessoa,(CategoriaVeiculo)categoria.getSelectedItem());}
    private String incluir(){return dao.incluir(ler())?null:"Placa já existente";}
    private String alterar(){return dao.alterar(ler())?null:"Placa não existente";}
    private void buscar(){Veiculo v=dao.buscar(placa.getText().trim());if(v==null){mensagem("Veículo não encontrado");return;}ano.setText(Integer.toString(v.getAno()));categoria.setSelectedItem(v.getCategoria());cpf.setText(v.getProprietarioPessoa()==null?"":v.getProprietarioPessoa().getCpf());cnpj.setText(v.getProprietarioEmpresa()==null?"":v.getProprietarioEmpresa().getCnpj());}
    private void executar(Supplier<String> s){try{mensagem(s.get());}catch(RuntimeException e){JOptionPane.showMessageDialog(this,e.getMessage(),"Erro",JOptionPane.ERROR_MESSAGE);}}
    private void mensagem(String s){JOptionPane.showMessageDialog(this,s==null?"Operação realizada com sucesso":s);}
    private void limpar(){placa.setText("");ano.setText("");cpf.setText("");cnpj.setText("");categoria.setSelectedIndex(0);}
}
