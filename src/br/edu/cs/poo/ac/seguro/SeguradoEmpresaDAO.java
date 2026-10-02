package br.edu.cs.poo.ac.seguro.daos;

import br.edu.cesarschool.next.oo.persistenciaobjetos.CadastroObjetos;
import br.edu.cs.poo.ac.seguro.entidades.SeguradoEmpresa;

public class SeguradoEmpresaDAO {
    private final CadastroObjetos cadastro = new CadastroObjetos(SeguradoEmpresa.class);

    public SeguradoEmpresa buscar(String cnpj) {
        return (SeguradoEmpresa) cadastro.buscar(cnpj);
    }

    public boolean incluir(SeguradoEmpresa seg) {
        if (seg == null || buscar(seg.getCnpj()) != null) return false;
        cadastro.incluir(seg, seg.getCnpj());
        return true;
    }

    public boolean alterar(SeguradoEmpresa seg) {
        if (seg == null || buscar(seg.getCnpj()) == null) return false;
        cadastro.alterar(seg, seg.getCnpj());
        return true;
    }

    public boolean excluir(String cnpj) {
        if (buscar(cnpj) == null) return false;
        cadastro.excluir(cnpj);
        return true;
    }
}
