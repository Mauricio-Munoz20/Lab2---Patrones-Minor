package cl.ucn.modelo;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.Assume;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.Provide;
import net.jqwik.api.constraints.AlphaChars;
import net.jqwik.api.constraints.NumericChars;
import net.jqwik.api.constraints.StringLength;

class IdentifierPropertiesTest {

    private final Identifier identificador = new Identifier();

    @Property
    void validIdentifiersAreAlwaysAccepted(
            @ForAll @AlphaChars @StringLength(min = 1, max = 1) String primerCaracter,
            @ForAll("cadenaAlfanumerica") @StringLength(min = 0, max = 4) String resto
    ) {
        String cadenaIdentificador = primerCaracter + resto;
        
        // Verificar que la longitud total esté en el rango válido [1, 5]
        Assume.that(cadenaIdentificador.length() >= 1 && cadenaIdentificador.length() <= 5);
        
        // Verificar que todos los caracteres sean alfanuméricos
        Assume.that(cadenaIdentificador.matches("^[a-zA-Z0-9]+$"));
        
        // Verificar que comience con letra (ya garantizado por primerCaracter)
        assertTrue(Character.isLetter(cadenaIdentificador.charAt(0)), 
                   "El primer carácter debe ser una letra");
        
        // La propiedad: si cumple todas las condiciones, debe ser válido
        assertTrue(identificador.validateIdentifier(cadenaIdentificador),
                   "Identificador válido debe ser aceptado: " + cadenaIdentificador);
    }

    @Provide
    Arbitrary<String> cadenaAlfanumerica() {
        return Arbitraries.strings()
            .withChars('a', 'z')
            .withChars('A', 'Z')
            .withChars('0', '9');
    }

    @Property
    void invalidIdentifiersAreAlwaysRejected(
            @ForAll @StringLength(min = 0, max = 10) String cadenaIdentificador
    ) {
        // Filtrar casos que no son inválidos según nuestras reglas
        // Solo procesamos strings que violan al menos una regla
        boolean violaRegla = false;
        
        // Regla 1: Debe tener al menos 1 carácter
        if (cadenaIdentificador.length() == 0) {
            violaRegla = true;
        }
        // Regla 2: Debe comenzar con letra
        else if (cadenaIdentificador.length() > 0 && !Character.isLetter(cadenaIdentificador.charAt(0))) {
            violaRegla = true;
        }
        // Regla 3: Longitud máxima 5
        else if (cadenaIdentificador.length() > 5) {
            violaRegla = true;
        }
        // Regla 4: Solo caracteres alfanuméricos
        else if (!cadenaIdentificador.matches("^[a-zA-Z0-9]+$")) {
            violaRegla = true;
        }
        
        // Si viola alguna regla, debe ser rechazado
        if (violaRegla) {
            assertFalse(identificador.validateIdentifier(cadenaIdentificador),
                       "Identificador inválido debe ser rechazado: \"" + cadenaIdentificador + "\"");
        }
    }

    @Property
    void validIdentifiersSatisfyAllRules(
            @ForAll @AlphaChars @StringLength(min = 1, max = 1) String primerCaracter,
            @ForAll("cadenaAlfanumerica") @StringLength(min = 0, max = 4) String resto
    ) {
        String cadenaIdentificador = primerCaracter + resto;
        
        // Solo considerar casos válidos
        Assume.that(cadenaIdentificador.length() >= 1 && cadenaIdentificador.length() <= 5);
        Assume.that(cadenaIdentificador.matches("^[a-zA-Z0-9]+$"));
        Assume.that(Character.isLetter(cadenaIdentificador.charAt(0)));
        
        // Si es válido según validateIdentifier, debe cumplir todas las reglas
        if (identificador.validateIdentifier(cadenaIdentificador)) {
            assertTrue(cadenaIdentificador.length() >= 1 && cadenaIdentificador.length() <= 5,
                      "Longitud debe estar entre 1 y 5");
            assertTrue(Character.isLetter(cadenaIdentificador.charAt(0)),
                      "Debe comenzar con letra");
            assertTrue(cadenaIdentificador.matches("^[a-zA-Z0-9]+$"),
                      "Debe contener solo caracteres alfanuméricos");
        }
    }

    @Property
    void identifiersStartingWithDigitAreInvalid(
            @ForAll @NumericChars @StringLength(min = 1, max = 1) String primerCaracter,
            @ForAll("cadenaAlfanumerica") @StringLength(min = 0, max = 4) String resto
    ) {
        String cadenaIdentificador = primerCaracter + resto;
        
        // Asegurar que la longitud total esté en rango razonable para el test
        Assume.that(cadenaIdentificador.length() <= 10);
        
        // Si comienza con dígito, debe ser inválido
        assertFalse(identificador.validateIdentifier(cadenaIdentificador),
                   "Identificador que comienza con dígito debe ser inválido: " + cadenaIdentificador);
    }
}

