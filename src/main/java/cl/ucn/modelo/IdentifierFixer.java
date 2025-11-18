package cl.ucn.modelo;

public class IdentifierFixer {

    private final Identifier validator;

    public IdentifierFixer() {
        this(new Identifier());
    }

    public IdentifierFixer(Identifier validator) {
        this.validator = validator;
    }

    public String corregir(String original) {
        if (original == null) return null;
        if (validator.validateIdentifier(original)) return original;

        String r = original;

        r = eliminarSimbolos(r);
        if (r.isEmpty()) r = "a";

        r = arreglarPrimerCaracter(r);
        r = eliminarDigitosConsecutivos(r);
        r = truncar(r, 5);

        if (!validator.validateIdentifier(r)) return "a";
        return r;
    }

    private String eliminarSimbolos(String s) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (validator.esAlfanumerico(c)) sb.append(c);
        }
        return sb.toString();
    }

    private String arreglarPrimerCaracter(String s) {
        char c = s.charAt(0);
        if (validator.esLetra(c)) return s;

        if (s.length() == 1) return "a";
        return "a" + s.substring(1);
    }

    private String eliminarDigitosConsecutivos(String s) {
        StringBuilder sb = new StringBuilder();
        boolean ultimoEraDigito = false;

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            boolean esDigito = Character.isDigit(c);

            if (esDigito && !ultimoEraDigito) {
                sb.append(c);
                ultimoEraDigito = true;
            } else if (!esDigito) {
                sb.append(c);
                ultimoEraDigito = false;
            }
        }

        return sb.toString();
    }

    private String truncar(String s, int max) {
        if (s.length() <= max) return s;
        return s.substring(0, max);
    }
}

