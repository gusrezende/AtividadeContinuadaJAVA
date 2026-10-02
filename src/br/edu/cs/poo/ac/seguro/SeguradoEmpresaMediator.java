package br.edu.cs.poo.ac.seguro.mediators;

import br.edu.cs.poo.ac.seguro.daos.SeguradoEmpresaDAO;
import br.edu.cs.poo.ac.seguro.entidades.SeguradoEmpresa;

public class SeguradoEmpresaMediator {
    private static final SeguradoEmpresaMediator instancia = new SeguradoEmpresaMediator();
    private final SeguradoMediator seguradoMediator = SeguradoMediator.getInstancia();
    private final SeguradoEmpresaDAO dao = new SeguradoEmpresaDAO();

    private SeguradoEmpresaMediator() { }

    public static SeguradoEmpresaMediator getInstancia() {
        return instancia;
    }

    public String validarCnpj(String cnpj) {
        if (StringUtils.ehNuloOuBranco(cnpj)) return "CNPJ deve ser informado";
        if (cnpj.length() != 14) return "CNPJ deve ter 14 caracteres";
        if (!ValidadorCpfCnpj.ehCnpjValido(cnpj)) return "CNPJ com dígito inválido";
        return null;
    }

    public String validarFaturamento(double faturamento) {
        if (faturamento <= 0) return "Faturamento deve ser maior que zero";
        return null;
    }

    public String incluirSeguradoEmpresa(SeguradoEmpresa seg) {
        String msg = validarSeguradoEmpresa(seg);
        if (msg != null) return msg;
        if (buscarSeguradoEmpresa(seg.getCnpj()) != null) return "CNPJ do segurado empresa já existente";
        return dao.incluir(seg) ? null : "Erro ao incluir segurado empresa";
    }

    public String alterarSeguradoEmpresa(SeguradoEmpresa seg) {
        String msg = validarSeguradoEmpresa(seg);
        if (msg != null) return msg;
        if (buscarSeguradoEmpresa(seg.getCnpj()) == null) return "CNPJ do segurado empresa não existente";
        return dao.alterar(seg) ? null : "Erro ao alterar segurado empresa";
    }

    public String excluirSeguradoEmpresa(String cnpj) {
        if (buscarSeguradoEmpresa(cnpj) == null) return "CNPJ do segurado empresa não existente";
        return dao.excluir(cnpj) ? null : "Erro ao excluir segurado empresa";
    }

    public SeguradoEmpresa buscarSeguradoEmpresa(String cnpj) {
        return dao.buscar(cnpj);
    }

    public String validarSeguradoEmpresa(SeguradoEmpresa seg) {
        if (seg == null) return "Segurado empresa deve ser informado";
        String msg = seguradoMediator.validarNome(seg.getNome());
        if (msg != null) return msg;
        msg = seguradoMediator.validarEndereco(seg.getEndereco());
        if (msg != null) return msg;
        if (seg.getDataAbertura() == null) return "Data da abertura deve ser informada";
        msg = seguradoMediator.validarDataCriacao(seg.getDataAbertura());
        if (msg != null) return msg;
        msg = validarCnpj(seg.getCnpj());
        if (msg != null) return msg;
        return validarFaturamento(seg.getFaturamento());
    }
}
