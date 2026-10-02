package br.edu.cesarschool.next.oo.persistenciaobjetos;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementação compatível com a API usada pelos DAOs desta atividade.
 * O armazenamento é feito em ./NomeSimplesDaClasse/chave.dat.
 */
public class CadastroObjetos {
    private static final String FILE_SEP = System.getProperty("file.separator");
    private static final String FILE_EXT = ".dat";
    private final Class<?> tipo;

    public CadastroObjetos(Class<?> tipo) {
        this.tipo = tipo;
    }

    public void incluir(Serializable objeto, String chave) {
        File arquivo = criarObterArquivo(tipo, chave);
        if (arquivo.exists()) throw new RuntimeException("Arquivo " + arquivo.getName() + " já existe!");
        try (FileOutputStream fos = new FileOutputStream(arquivo);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(objeto);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public void alterar(Serializable objeto, String chave) {
        excluir(chave);
        incluir(objeto, chave);
    }

    public void excluir(String chave) {
        File arquivo = criarObterArquivo(tipo, chave);
        if (!arquivo.exists() || !arquivo.delete()) {
            throw new RuntimeException("Arquivo " + arquivo.getName() + " não existe ou não pôde ser apagado!");
        }
    }

    public Serializable buscar(String chave) {
        return ler(criarObterArquivo(tipo, chave));
    }

    public Serializable[] buscarTodos(Class<?> tipo) {
        return buscarTodos();
    }

    public Serializable[] buscarTodos() {
        File dir = new File(obterCaminhoDiretorio());
        if (!dir.exists()) return new Serializable[0];
        File[] arquivos = dir.listFiles();
        if (arquivos == null) return new Serializable[0];
        List<Serializable> resultado = new ArrayList<>();
        for (File arquivo : arquivos) {
            if (arquivo.isFile() && arquivo.getName().endsWith(FILE_EXT)) {
                Serializable objeto = ler(arquivo);
                if (objeto != null) resultado.add(objeto);
            }
        }
        return resultado.toArray(new Serializable[0]);
    }

    private Serializable ler(File arquivo) {
        if (!arquivo.exists()) return null;
        try (FileInputStream fis = new FileInputStream(arquivo);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            return (Serializable) ois.readObject();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private void close(InputStream input) {
        if (input != null) try { input.close(); } catch (Exception ignored) { }
    }

    private void close(OutputStream output) {
        if (output != null) try { output.close(); } catch (Exception ignored) { }
    }

    private File criarObterArquivo(Class<?> classe, String chave) {
        String caminho = obterCaminhoDiretorio();
        File dir = new File(caminho);
        if (!dir.exists() && !dir.mkdir()) {
            throw new RuntimeException("Não foi possível criar o diretório " + dir.getName() + " no sistema de arquivos!");
        }
        return new File(caminho + FILE_SEP + chave + FILE_EXT);
    }

    private String obterCaminhoDiretorio() {
        return "." + FILE_SEP + tipo.getSimpleName();
    }
}
