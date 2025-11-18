package cl.ucn.modelo;

public class Identifier {

    public IdentifierError getError(String identificador) {
        if (identificador == null || identificador.length() == 0) return IdentifierError.NULO_O_VACIO;
        if (identificador.length() > 5) return IdentifierError.MUY_LARGO;
        if (!esLetra(identificador.charAt(0))) return IdentifierError.PRIMER_CARACTER_NO_LETRA;
        if (!validateChars(identificador)) return IdentifierError.CARACTER_INVALIDO;

        return IdentifierError.DESCONOCIDO;
    }

    private boolean validateChars(String identificador) {
        int i = 1;
        while (i < identificador.length()) {
            if (!esAlfanumerico(identificador.charAt(i))) {
                return false;
            }
            i++;
        }
        return true;
    }

    // refactoring, ahora la deje muchos mas limpia y simple, separe la logica en un nuevo metodo validateChars
    // y elimine variables innecesarias
    public boolean validateIdentifier(String identificador) {
        // Validar cadena vacía o null
        if (identificador == null || identificador.length() == 0 || identificador.length() > 5) return false;
        if (!esLetra(identificador.charAt(0))) return false;
        if (identificador.length() > 1 && !validateChars(identificador)) return false;

        return true;
    }

    public boolean esLetra(char caracter) {
        if (((caracter >= 'A') && (caracter <= 'Z')) || ((caracter >= 'a') && (caracter <= 'z'))) {
            return true;
        }

        return false;
    }

    public boolean esAlfanumerico(char caracter) {
        if (((caracter >= 'A') && (caracter <= 'Z')) || ((caracter >= 'a') && (caracter <= 'z')) ||
                ((caracter >= '0') && (caracter <= '9'))) {
            return true;
        }

        return false;
    }
}