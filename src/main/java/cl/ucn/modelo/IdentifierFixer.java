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
        r = arreglarPrimerCaracter(r);
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
        if (s.isEmpty()) return "a";
        char c = s.charAt(0);
        if (validator.esLetra(c)) return s;
        if (s.length() == 1) return "a";
        return "a" + s.substring(1);
    }

    private String truncar(String s, int max) {
        if (s.length() <= max) return s;
        return s.substring(0, max);
    }
}
