package br.edu.cs.poo.ac.seguro.mediators;

public final class ValidadorCpfCnpj {
    private ValidadorCpfCnpj() { }

    public static boolean ehCpfValido(String cpf) {
        if (cpf == null || cpf.length() != 11 || !StringUtils.temSomenteNumeros(cpf)) return false;
        if (todosIguais(cpf)) return false;

        int soma = 0;
        for (int i = 0; i < 9; i++) soma += (cpf.charAt(i) - '0') * (10 - i);
        int resto = soma % 11;
        int dig1 = resto < 2 ? 0 : 11 - resto;
        if (dig1 != cpf.charAt(9) - '0') return false;

        soma = 0;
        for (int i = 0; i < 10; i++) soma += (cpf.charAt(i) - '0') * (11 - i);
        resto = soma % 11;
        int dig2 = resto < 2 ? 0 : 11 - resto;
        return dig2 == cpf.charAt(10) - '0';
    }

    public static boolean ehCnpjValido(String cnpj) {
        if (cnpj == null || cnpj.length() != 14 || !StringUtils.temSomenteNumeros(cnpj)) return false;
        if (todosIguais(cnpj)) return false;

        int[] pesos1 = {5,4,3,2,9,8,7,6,5,4,3,2};
        int[] pesos2 = {6,5,4,3,2,9,8,7,6,5,4,3,2};

        int soma = 0;
        for (int i = 0; i < 12; i++) soma += (cnpj.charAt(i) - '0') * pesos1[i];
        int resto = soma % 11;
        int dig1 = resto < 2 ? 0 : 11 - resto;
        if (dig1 != cnpj.charAt(12) - '0') return false;

        soma = 0;
        for (int i = 0; i < 13; i++) soma += (cnpj.charAt(i) - '0') * pesos2[i];
        resto = soma % 11;
        int dig2 = resto < 2 ? 0 : 11 - resto;
        return dig2 == cnpj.charAt(13) - '0';
    }

    private static boolean todosIguais(String valor) {
        char primeiro = valor.charAt(0);
        for (int i = 1; i < valor.length(); i++) {
            if (valor.charAt(i) != primeiro) return false;
        }
        return true;
    }
}
