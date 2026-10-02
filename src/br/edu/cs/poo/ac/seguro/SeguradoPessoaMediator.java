package br.edu.cs.poo.ac.seguro.mediators;

import br.edu.cs.poo.ac.seguro.daos.SeguradoPessoaDAO;
import br.edu.cs.poo.ac.seguro.entidades.SeguradoPessoa;

public class SeguradoPessoaMediator {
    private static final SeguradoPessoaMediator instancia = new SeguradoPessoaMediator();
    private final SeguradoMediator seguradoMediator = SeguradoMediator.getInstancia();
    private final SeguradoPessoaDAO dao = new SeguradoPessoaDAO();

    private SeguradoPessoaMediator() { }

    public static SeguradoPessoaMediator getInstancia() {
        return instancia;
    }

    public String validarCpf(String cpf) {
        if (StringUtils.ehNuloOuBranco(cpf)) return "CPF deve ser informado";
        if (cpf.length() != 11) return "CPF deve ter 11 caracteres";
        if (!ValidadorCpfCnpj.ehCpfValido(cpf)) return "CPF com dígito inválido";
        return null;
    }

    public String validarRenda(double renda) {
        if (renda < 0) return "Renda deve ser maior ou igual à zero";
        return null;
    }

    public String incluirSeguradoPessoa(SeguradoPessoa seg) {
        String msg = validarSeguradoPessoa(seg);
        if (msg != null) return msg;
        if (buscarSeguradoPessoa(seg.getCpf()) != null) return "CPF do segurado pessoa já existente";
        return dao.incluir(seg) ? null : "Erro ao incluir segurado pessoa";
    }

    public String alterarSeguradoPessoa(SeguradoPessoa seg) {
        String msg = validarSeguradoPessoa(seg);
        if (msg != null) return msg;
        if (buscarSeguradoPessoa(seg.getCpf()) == null) return "CPF do segurado pessoa não existente";
        return dao.alterar(seg) ? null : "Erro ao alterar segurado pessoa";
    }

    public String excluirSeguradoPessoa(String cpf) {
        if (buscarSeguradoPessoa(cpf) == null) return "CPF do segurado pessoa não existente";
        return dao.excluir(cpf) ? null : "Erro ao excluir segurado pessoa";
    }

    public SeguradoPessoa buscarSeguradoPessoa(String cpf) {
        return dao.buscar(cpf);
    }

    public String validarSeguradoPessoa(SeguradoPessoa seg) {
        if (seg == null) return "Segurado pessoa deve ser informado";
        String msg = seguradoMediator.validarNome(seg.getNome());
        if (msg != null) return msg;
        msg = seguradoMediator.validarEndereco(seg.getEndereco());
        if (msg != null) return msg;
        if (seg.getDataNascimento() == null) return "Data do nascimento deve ser informada";
        msg = seguradoMediator.validarDataCriacao(seg.getDataNascimento());
        if (msg != null) return msg;
        msg = validarCpf(seg.getCpf());
        if (msg != null) return msg;
        return validarRenda(seg.getRenda());
    }
}
