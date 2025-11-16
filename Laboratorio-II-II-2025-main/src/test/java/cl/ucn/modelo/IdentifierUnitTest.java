package cl.ucn.modelo;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests unitarios no parametrizados para la clase Identifier.
 * Cubre casos específicos y edge cases adicionales.
 */
public class IdentifierUnitTest {

    private Identifier identificador;

    @Before
    public void setUp() {
        identificador = new Identifier();
    }

    @Test
    public void testValidSingleLetterLowerCase() {
        assertTrue("Una letra minúscula debe ser válida", 
                   identificador.validateIdentifier("a"));
    }

    @Test
    public void testValidSingleLetterUpperCase() {
        assertTrue("Una letra mayúscula debe ser válida", 
                   identificador.validateIdentifier("Z"));
    }

    @Test
    public void testValidTwoLetters() {
        assertTrue("Dos letras deben ser válidas", 
                   identificador.validateIdentifier("ab"));
    }

    @Test
    public void testValidLetterFollowedByDigit() {
        assertTrue("Letra seguida de dígito debe ser válida", 
                   identificador.validateIdentifier("a1"));
    }

    @Test
    public void testValidMultipleDigits() {
        assertTrue("Letra seguida de múltiples dígitos debe ser válida", 
                   identificador.validateIdentifier("a123"));
    }

    @Test
    public void testValidMixedCaseAndDigits() {
        assertTrue("Mezcla de mayúsculas, minúsculas y dígitos debe ser válida", 
                   identificador.validateIdentifier("Ab1C2"));
    }

    @Test
    public void testInvalidEmptyString() {
        assertFalse("Cadena vacía debe ser inválida", 
                    identificador.validateIdentifier(""));
    }

    @Test
    public void testInvalidStartsWithDigit() {
        assertFalse("Identificador que comienza con dígito debe ser inválido", 
                    identificador.validateIdentifier("1abc"));
    }

    @Test
    public void testInvalidTooLong() {
        assertFalse("Identificador con más de 5 caracteres debe ser inválido", 
                    identificador.validateIdentifier("abcdef"));
    }

    @Test
    public void testInvalidContainsSpecialCharacter() {
        assertFalse("Identificador con carácter especial debe ser inválido", 
                    identificador.validateIdentifier("ab-c"));
    }

    @Test
    public void testInvalidContainsSpace() {
        assertFalse("Identificador con espacio debe ser inválido", 
                    identificador.validateIdentifier("a b"));
    }

    @Test
    public void testInvalidStartsWithUnderscore() {
        assertFalse("Identificador que comienza con guion bajo debe ser inválido", 
                    identificador.validateIdentifier("_test"));
    }

    @Test
    public void testValidMaximumLength() {
        assertTrue("Identificador con exactamente 5 caracteres debe ser válido", 
                   identificador.validateIdentifier("abcde"));
    }

    @Test
    public void testInvalidJustOverMaximumLength() {
        assertFalse("Identificador con 6 caracteres debe ser inválido", 
                    identificador.validateIdentifier("abcdef"));
    }

    @Test
    public void testValidAllDigitsAfterFirstLetter() {
        assertTrue("Identificador con letra inicial seguida de solo dígitos debe ser válido", 
                   identificador.validateIdentifier("a1234"));
    }

    @Test
    public void testInvalidOnlySpecialCharacters() {
        assertFalse("Solo caracteres especiales debe ser inválido", 
                    identificador.validateIdentifier("@#$"));
    }

    @Test
    public void testInvalidNullInput() {
        assertFalse("Identificador null debe ser inválido",
                    identificador.validateIdentifier(null));
    }

    @Test
    public void testValidBoundaryCaseOneCharacter() {
        assertTrue("Un solo carácter válido (letra) debe ser válido", 
                   identificador.validateIdentifier("x"));
    }

    @Test
    public void testValidBoundaryCaseFiveCharacters() {
        assertTrue("Exactamente 5 caracteres válidos debe ser válido", 
                   identificador.validateIdentifier("A1b2C"));
    }
}

