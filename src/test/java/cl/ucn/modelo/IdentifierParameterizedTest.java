package cl.ucn.modelo;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;


@RunWith(Parameterized.class)
public class IdentifierParameterizedTest {

    private final String entrada;
    private final boolean resultadoEsperado;
    private final String descripcion;

    public IdentifierParameterizedTest(String entrada, boolean resultadoEsperado, String descripcion) {
        this.entrada = entrada;
        this.resultadoEsperado = resultadoEsperado;
        this.descripcion = descripcion;
    }

    @Parameters(name = "{index}: {2} - Input: \"{0}\" -> {1}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
            { "a", true, "Válido: 1 letra minúscula" },
            { "A", true, "Válido: 1 letra mayúscula" },

            { "abcde", true, "Válido: 5 letras minúsculas" },
            { "ABCDE", true, "Válido: 5 letras mayúsculas" },
            { "a1b2c", true, "Válido: 5 caracteres alfanuméricos" },

            { "ab", true, "Válido: 2 letras" },
            { "a1", true, "Válido: letra + dígito" },
            { "A1B2", true, "Válido: 4 caracteres alfanuméricos" },
            { "x9y", true, "Válido: 3 caracteres con dígitos" },

            { "1abc", false, "Inválido: comienza con dígito" },
            { "9", false, "Inválido: solo dígito" },

            { "@abc", false, "Inválido: comienza con carácter especial" },
            { "_test", false, "Inválido: comienza con guion bajo" },

            { "", false, "Inválido: cadena vacía (longitud 0)" },

            { "abcdef", false, "Inválido: 6 caracteres (excede máximo)" },
            { "abcdefgh", false, "Inválido: 8 caracteres" },

            { "ab-c", false, "Inválido: contiene guion" },
            { "a@b", false, "Inválido: contiene arroba" },
            { "a b", false, "Inválido: contiene espacio" },
            { "a.b", false, "Inválido: contiene punto" },

            { "a1234", true, "Válido: 5 caracteres, comienza con letra, solo alfanuméricos" },
            { "1a234", false, "Inválido: comienza con dígito aunque tenga letras" },
            { "a1b2c3", false, "Inválido: 6 caracteres (excede máximo)" },
            { "a#b", false, "Inválido: contiene carácter especial (#)" }
        });
    }

    @Test
    public void testValidateIdentifier() {
        Identifier identificador = new Identifier();
        assertEquals(descripcion, resultadoEsperado, identificador.validateIdentifier(entrada));
    }
}

