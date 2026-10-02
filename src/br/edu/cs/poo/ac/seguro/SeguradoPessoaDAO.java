package br.edu.cs.poo.ac.seguro.daos;

import br.edu.cesarschool.next.oo.persistenciaobjetos.CadastroObjetos;
import br.edu.cs.poo.ac.seguro.entidades.SeguradoPessoa;

public class SeguradoPessoaDAO {
    private final CadastroObjetos cadastro = new CadastroObjetos(SeguradoPessoa.class);

    public SeguradoPessoa buscar(String cpf) {
        return (SeguradoPessoa) cadastro.buscar(cpf);
    }

    public boolean incluir(SeguradoPessoa seg) {
        if (seg == null || buscar(seg.getCpf()) != null) return false;
        cadastro.incluir(seg, seg.getCpf());
        return true;
    }

    public boolean alterar(SeguradoPessoa seg) {
        if (seg == null || buscar(seg.getCpf()) == null) return false;
        cadastro.alterar(seg, seg.getCpf());
        return true;
    }

    public boolean excluir(String cpf) {
        if (buscar(cpf) == null) return false;
        cadastro.excluir(cpf);
        return true;
    }
}
