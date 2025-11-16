package cl.ucn.modelo;

public class Identifier {

    public boolean validateIdentifier(String identificador) {
        // Validar cadena vacía o null
        if (identificador == null || identificador.length() == 0) {
            return false;
        }

        char caracter;
        boolean identificadorValido = false;

        caracter = identificador.charAt(0);
        identificadorValido = esLetra(caracter);

        if (identificador.length() > 1) {
            int i = 1;
            while (i < identificador.length()) {
                caracter = identificador.charAt(i);
                if (!esAlfanumerico(caracter)) {
                    identificadorValido = false;
                }
                i++;
            }
        }

        if (identificadorValido && (identificador.length() >= 1) && (identificador.length() < 6)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean esLetra(char caracter) {
        if (((caracter >= 'A') && (caracter <= 'Z')) || ((caracter >= 'a') && (caracter <= 'z'))) {
            return true;
        } else {
            return false;
        }
    }

    public boolean esAlfanumerico(char caracter) {
        if (((caracter >= 'A') && (caracter <= 'Z')) || ((caracter >= 'a') && (caracter <= 'z')) ||
                ((caracter >= '0') && (caracter <= '9'))) {
            return true;
        } else {
            return false;
        }
    }
}
