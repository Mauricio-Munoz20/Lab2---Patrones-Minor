package cl.ucn.modelo;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * Test parametrizado para Identifier usando particiones de equivalencia y análisis de frontera.
 * 
 * Particiones de equivalencia:
 * - P1: Identificadores válidos (comienzan con letra, longitud 1-5, solo letras/dígitos)
 * - P2: Inválidos - comienzan con dígito
 * - P3: Inválidos - comienzan con carácter especial
 * - P4: Inválidos - longitud 0 (cadena vacía)
 * - P5: Inválidos - longitud > 5
 * - P6: Inválidos - contienen caracteres especiales
 * 
 * Análisis de frontera:
 * - Frontera de longitud: 0, 1, 5, 6 caracteres
 * - Frontera de caracteres: letra inicial válida, dígito inicial inválido
 */
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
            // === PARTICIONES DE EQUIVALENCIA: Válidos (P1) ===
            // Frontera: longitud mínima (1 carácter)
            { "a", true, "Válido: 1 letra minúscula" },
            { "A", true, "Válido: 1 letra mayúscula" },
            
            // Frontera: longitud máxima (5 caracteres)
            { "abcde", true, "Válido: 5 letras minúsculas" },
            { "ABCDE", true, "Válido: 5 letras mayúsculas" },
            { "a1b2c", true, "Válido: 5 caracteres alfanuméricos" },
            
            // Casos válidos intermedios
            { "ab", true, "Válido: 2 letras" },
            { "a1", true, "Válido: letra + dígito" },
            { "A1B2", true, "Válido: 4 caracteres alfanuméricos" },
            { "x9y", true, "Válido: 3 caracteres con dígitos" },
            
            // === PARTICIONES DE EQUIVALENCIA: Inválidos ===
            // P2: Comienzan con dígito
            { "1abc", false, "Inválido: comienza con dígito" },
            { "9", false, "Inválido: solo dígito" },
            
            // P3: Comienzan con carácter especial
            { "@abc", false, "Inválido: comienza con carácter especial" },
            { "_test", false, "Inválido: comienza con guion bajo" },
            
            // P4: Longitud 0 (cadena vacía) - Frontera inferior
            { "", false, "Inválido: cadena vacía (longitud 0)" },
            
            // P5: Longitud > 5 - Frontera superior
            { "abcdef", false, "Inválido: 6 caracteres (excede máximo)" },
            { "abcdefgh", false, "Inválido: 8 caracteres" },
            
            // P6: Contienen caracteres especiales
            { "ab-c", false, "Inválido: contiene guion" },
            { "a@b", false, "Inválido: contiene arroba" },
            { "a b", false, "Inválido: contiene espacio" },
            { "a.b", false, "Inválido: contiene punto" },
            
            // Casos adicionales de frontera
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

