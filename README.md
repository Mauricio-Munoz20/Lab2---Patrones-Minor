# Laboratorio II – Diseño y Evaluación Avanzada de Casos de Prueba

---

## Nombres

- Mauricio Muñoz (mauricio.munoz01@alumnos.ucn.cl)
- Julian Honores (julian.honores@alumnos.ucn.cl)

## Objetivo General

Este laboratorio tiene como objetivo que el estudiante aplique técnicas avanzadas de diseño de casos de prueba, cubriendo 
no solo tests tradicionales unitarios, sino también particiones de equivalencia, análisis de frontera y property-based 
testing, además de proponer una mejora y refactor sobre el método provisto.

El objetivo final es lograr un diseño de pruebas que pueda justificar cobertura, correctitud y robustez en un proceso 
real de testing profesional.

---

## Contexto del Código

Se entrega una clase `Identifier` que valida un identificador según ciertas reglas. Esta clase debe ser modificada, 
extendida y testeada rigurosamente.  Además, se requiere implementar una nueva clase adicional llamada `IdentifierFixer`.

---

## Descripción del Código

La clase `Identifier` contiene un método `validateIdentifier(String identificador)` que valida si una cadena cumple con las reglas 
siguientes:

1. El identificador debe comenzar con una **letra** (mayúscula o minúscula).
2. Los caracteres siguientes (si los hay) pueden ser **letras o dígitos**.
3. La longitud total del identificador debe ser **de 1 a 5 caracteres** (inclusive).
4. No se permiten caracteres especiales, espacios ni símbolos.

**Ejemplos de identificadores válidos:**
- `"a"` → Válido (1 letra minúscula)
- `"A"` → Válido (1 letra mayúscula)
- `"abc"` → Válido (3 letras)
- `"a1"` → Válido (letra seguida de dígito)
- `"Ab1C2"` → Válido (mezcla de mayúsculas, minúsculas y dígitos)
- `"abcde"` → Válido (5 caracteres - longitud máxima)

**Ejemplos de identificadores inválidos:**
- `"1de"` → Inválido (comienza con un dígito)
- `"abcdef"` → Inválido (más de 5 caracteres)
- `""` → Inválido (cadena vacía)
- `"ab-c"` → Inválido (contiene carácter especial)
- `"a b"` → Inválido (contiene espacio)
- `null` → Inválido (valor null)

---

## Entregables Obligatorios

El estudiante debe entregar lo siguiente en `src/test/java/...`:

### 1) JUnit Test Suite Completo
- mínimo 12 casos parametrizados obligatorios usando `@RunWith(Parameterized.class)`. Debe investigar como crear tests
parametrizados en JUnit 4 [(documentación)]((https://junit.org/junit4/javadoc/4.12/org/junit/runners/Parameterized.html).).
- tests adicionales unitarios NO parametrizados (mínimo 10)
- casos diseñados explícitamente desde particiones de equivalencia + frontera

### 2) Property-Based Testing

Property Based Testing (PBT) es una técnica de testing donde en vez de escribir casos de prueba específicos manuales,
el programador define propiedades generales que siempre deben cumplirse, independiente del input.

Luego una librería como jqwik o junit-quickcheck automáticamente genera cientos o miles de
entradas aleatorias para intentar romper la propiedad.

Ejemplo:

``` java
// Importa la API de jqwik: anotaciones (@Property, @ForAll), generadores y utilidades.
import net.jqwik.api.*;
// Usamos las aserciones de JUnit Jupiter (la plataforma donde corre jqwik).
import static org.junit.jupiter.api.Assertions.*;

// No es necesario que esta clase extienda nada. jqwik detecta métodos @Property.
class IdentifierProperties {

  // --- IDEA DE LA PROPIEDAD ---
  // "Si un string comienza con letra, NO tiene dos dígitos consecutivos
  //  y su largo total ≤ 8, entonces debe ser un identificador válido".
  //
  // jqwik generará MUCHOS valores diferentes para 'first' y 'tail' que cumplan
  // las restricciones y verificará que TODOS pasen la aserción.

  @Property // <- Indica a jqwik que este método define una propiedad a verificar muchas veces
  void validIdentifierIsAccepted(
      // first: exactamente 1 carácter y debe ser una letra (A–Z o a–z)
      @ForAll @AlphaChars @StringLength(min = 1, max = 1) String first,
      // tail: 0..7 caracteres alfanuméricos (letras o dígitos)
      @ForAll @AlphaNumericChars @StringLength(min = 0, max = 7) String tail
  ) {
    // Concatenamos: total ≡ [1..8] caracteres
    String s = first + tail;

    // Precondición: descartamos cualquier caso que tenga dos dígitos consecutivos.
    // 'Assume.that' NO falla el test; simplemente le dice a jqwik:
    // "este caso no sirve, genera otro". Útil cuando es más fácil filtrar que generar.
    Assume.that(!s.matches(".*\\d{2}.*"));

    // Aserción: bajo estas condiciones, nuestra función debe aceptar el identificador.
    assertTrue(Identifier.validateIdentifier(s));
  }
}
```
Usar  [**jqwik** ](https://jqwik.net/). Debe definir al menos 2 propiedades del sistema y testearlas.


### 3) Nueva Clase: `IdentifierFixer`
Esta clase debe poder transformar un identificador inválido en uno válido según reglas que tú defines.

Debes definir las reglas de corrección en README (sección abajo).

### 4) Enum de Código de Error 
Cuando el identificador es inválido, el método `validateIdentifier` debe indicar POR QUÉ falla (enum con mínimo 4 causas).

---

## Estado Actual de la Implementación

#### 1. Clase `Identifier` (`src/main/java/cl/ucn/modelo/Identifier.java`)

**Método principal:**
- `validateIdentifier(String identificador)`: Valida si un identificador cumple con las reglas establecidas. Retorna `true` si es válido, `false` en caso contrario.

**Métodos públicos auxiliares:**
- `esLetra(char caracter)`: Verifica si un carácter es una letra (A-Z, a-z). Retorna `true` si es letra, `false` en caso contrario.
- `esAlfanumerico(char caracter)`: Verifica si un carácter es alfanumérico (letra A-Z, a-z o dígito 0-9). Retorna `true` si es alfanumérico, `false` en caso contrario.
- `getError(String identificador)`: Retorna el tipo de error (`IdentifierError`) cuando un identificador es inválido. Permite identificar la causa específica de la invalidación.

**Métodos privados:**
- `validateChars(String identificador)`: Método privado que valida que todos los caracteres desde la posición 1 en adelante sean alfanuméricos. Parte del refactor para mejorar la legibilidad del código.

**Características implementadas:**
- ✅ Todos los identificadores (variables y métodos) traducidos al español
- ✅ Validación de cadena vacía o null (retorna `false`)
- ✅ Validación de longitud (1-5 caracteres inclusive)
- ✅ Validación de primer carácter (debe ser letra, no dígito ni símbolo)
- ✅ Validación de caracteres siguientes (letras o dígitos únicamente, no símbolos)
- ✅ Método `getError()` para identificar el tipo específico de error (5 tipos de error definidos)
- ✅ Refactor aplicado: código más limpio, separación de responsabilidades, eliminación de variables innecesarias

#### 2. Tests Unitarios (`src/test/java/cl/ucn/modelo/IdentifierUnitTest.java`)

**Total: 19 tests unitarios no parametrizados**

**Tests de casos válidos (9 tests):**
- `testValidSingleLetterLowerCase`: Valida que una letra minúscula única (`"a"`) sea válida
- `testValidSingleLetterUpperCase`: Valida que una letra mayúscula única (`"Z"`) sea válida
- `testValidTwoLetters`: Valida que dos letras (`"ab"`) sean válidas
- `testValidLetterFollowedByDigit`: Valida que letra seguida de dígito (`"a1"`) sea válida
- `testValidMultipleDigits`: Valida que letra seguida de múltiples dígitos (`"a123"`) sea válida
- `testValidMixedCaseAndDigits`: Valida que mezcla de mayúsculas, minúsculas y dígitos (`"Ab1C2"`) sea válida
- `testValidMaximumLength`: Valida que exactamente 5 caracteres (`"abcde"`) sea válido
- `testValidAllDigitsAfterFirstLetter`: Valida que letra inicial seguida de solo dígitos (`"a1234"`) sea válida
- `testValidBoundaryCaseOneCharacter`: Valida que un solo carácter válido (`"x"`) sea válido
- `testValidBoundaryCaseFiveCharacters`: Valida que exactamente 5 caracteres válidos (`"A1b2C"`) sea válido

**Tests de casos inválidos (10 tests):**
- `testInvalidEmptyString`: Verifica que cadena vacía (`""`) sea inválida
- `testInvalidStartsWithDigit`: Verifica que identificador que comienza con dígito (`"1abc"`) sea inválido
- `testInvalidTooLong`: Verifica que identificador con más de 5 caracteres (`"abcdef"`) sea inválido
- `testInvalidContainsSpecialCharacter`: Verifica que identificador con carácter especial (`"ab-c"`) sea inválido
- `testInvalidContainsSpace`: Verifica que identificador con espacio (`"a b"`) sea inválido
- `testInvalidStartsWithUnderscore`: Verifica que identificador que comienza con guion bajo (`"_test"`) sea inválido
- `testInvalidJustOverMaximumLength`: Verifica que identificador con 6 caracteres (`"abcdef"`) sea inválido
- `testInvalidOnlySpecialCharacters`: Verifica que solo caracteres especiales (`"@#$"`) sea inválido
- `testInvalidNullInput`: Verifica que entrada `null` sea inválida

**Cobertura:**
- ✅ Casos válidos: cubre fronteras (1 y 5 caracteres) y casos intermedios
- ✅ Casos inválidos: cubre todas las reglas de invalidación (null, vacío, longitud, primer carácter, caracteres especiales)
- ✅ Todos los tests incluyen mensajes descriptivos en las aserciones

#### 3. Tests Parametrizados (`src/test/java/cl/ucn/modelo/IdentifierParameterizedTest.java`)

**Total: 24 casos de prueba parametrizados** (usando `@RunWith(Parameterized.class)`)

**Implementación:**
- Usa la anotación `@RunWith(Parameterized.class)` de JUnit 4
- Método `@Parameters` con nombre descriptivo para cada caso: `"{index}: {2} - Input: \"{0}\" -> {1}"`
- Constructor parametrizado que recibe: entrada, resultado esperado y descripción

**Particiones de Equivalencia implementadas:**

- **P1: Identificadores válidos** (10 casos)
  - Frontera inferior: 1 carácter
    - `"a"` (minúscula)
    - `"A"` (mayúscula)
  - Frontera superior: 5 caracteres
    - `"abcde"` (5 letras minúsculas)
    - `"ABCDE"` (5 letras mayúsculas)
    - `"a1b2c"` (5 caracteres alfanuméricos)
    - `"a1234"` (letra + 4 dígitos)
  - Casos intermedios: 2, 3, 4 caracteres
    - `"ab"` (2 letras)
    - `"a1"` (letra + dígito)
    - `"A1B2"` (4 caracteres alfanuméricos)
    - `"x9y"` (3 caracteres con dígitos)

- **P2: Inválidos - comienzan con dígito** (3 casos)
  - `"1abc"` (dígito + letras)
  - `"9"` (solo dígito)
  - `"1a234"` (dígito inicial con letras y números)

- **P3: Inválidos - comienzan con carácter especial** (2 casos)
  - `"@abc"` (arroba inicial)
  - `"_test"` (guion bajo inicial)

- **P4: Inválidos - longitud 0** (1 caso)
  - `""` (cadena vacía - frontera inferior absoluta)

- **P5: Inválidos - longitud > 5** (3 casos)
  - `"abcdef"` (6 caracteres - frontera superior)
  - `"abcdefgh"` (8 caracteres)
  - `"a1b2c3"` (6 caracteres alfanuméricos)

- **P6: Inválidos - contienen caracteres especiales** (5 casos)
  - `"ab-c"` (guion)
  - `"a@b"` (arroba)
  - `"a b"` (espacio)
  - `"a.b"` (punto)
  - `"a#b"` (numeral)

**Análisis de frontera cubierto:**
- **Frontera de longitud:**
  - 0 caracteres: `""` (inválido)
  - 1 carácter: `"a"`, `"A"` (válido - mínimo)
  - 5 caracteres: `"abcde"`, `"ABCDE"`, `"a1b2c"`, `"a1234"` (válido - máximo)
  - 6 caracteres: `"abcdef"`, `"a1b2c3"` (inválido - excede máximo)
- **Frontera de caracteres:**
  - Letra inicial: `"a"`, `"A"` (válido)
  - Dígito inicial: `"1abc"`, `"9"` (inválido)
  - Símbolo inicial: `"@abc"`, `"_test"` (inválido)
  - Caracteres especiales intermedios: `"ab-c"`, `"a@b"`, `"a b"` (inválido)

#### 4. Property-Based Testing (`src/test/java/cl/ucn/modelo/IdentifierPropertiesTest.java`)

**Total: 4 propiedades definidas y testeadas usando jqwik**

**Framework utilizado:**
- **jqwik** (versión 1.8.2) - librería de Property-Based Testing para Java
- Usa anotaciones `@Property` para marcar métodos como propiedades a verificar
- Usa `@ForAll` para generar valores aleatorios automáticamente
- Usa `Assume.that()` para filtrar casos que no cumplen precondiciones

**Propiedad 1: `validIdentifiersAreAlwaysAccepted`**
- **Objetivo:** Verifica que identificadores válidos (comienzan con letra, longitud 1-5, solo alfanuméricos) son siempre aceptados por `validateIdentifier`
- **Generación:**
  - Primer carácter: letra (A-Z, a-z) usando `@AlphaChars @StringLength(min = 1, max = 1)`
  - Resto: 0-4 caracteres alfanuméricos usando generador personalizado `cadenaAlfanumerica()`
- **Validaciones:** Verifica longitud [1, 5], caracteres alfanuméricos, y que comienza con letra
- **Cobertura:** Genera automáticamente cientos o miles de casos de prueba

**Propiedad 2: `invalidIdentifiersAreAlwaysRejected`**
- **Objetivo:** Verifica que identificadores inválidos (violan alguna regla) son siempre rechazados
- **Generación:** Strings de longitud 0-10 caracteres (sin restricciones)
- **Filtrado:** Solo procesa strings que violan al menos una regla:
  - Longitud 0 (cadena vacía)
  - No comienza con letra
  - Longitud > 5
  - Contiene caracteres no alfanuméricos
- **Cobertura:** Genera casos aleatorios que cubren todas las violaciones posibles

**Propiedad 3: `validIdentifiersSatisfyAllRules`**
- **Objetivo:** Verifica consistencia - si un identificador es válido según `validateIdentifier`, debe cumplir todas las reglas
- **Generación:** Similar a Propiedad 1 (primer carácter letra + resto alfanumérico)
- **Validaciones:** Si es válido, verifica que:
  - Longitud está entre 1 y 5
  - Comienza con letra
  - Todos los caracteres son alfanuméricos
- **Propósito:** Garantiza coherencia entre la validación y las reglas

**Propiedad 4: `identifiersStartingWithDigitAreInvalid`**
- **Objetivo:** Verifica que identificadores que comienzan con dígito son siempre inválidos
- **Generación:**
  - Primer carácter: dígito (0-9) usando `@NumericChars @StringLength(min = 1, max = 1)`
  - Resto: 0-4 caracteres alfanuméricos
- **Validación:** Cualquier string que comience con dígito debe ser rechazado
- **Cobertura:** Genera casos con diferentes dígitos iniciales y combinaciones

**Generador personalizado:**
- `cadenaAlfanumerica()`: Método anotado con `@Provide` que genera strings alfanuméricos
  - Incluye letras minúsculas (a-z)
  - Incluye letras mayúsculas (A-Z)
  - Incluye dígitos (0-9)
  - Usado en las propiedades 1, 3 y 4 para generar el resto de caracteres después del primero

**Ventajas del Property-Based Testing:**
- ✅ Descubre casos edge que no se consideraron manualmente
- ✅ Genera cientos o miles de casos automáticamente
- ✅ Verifica propiedades generales del sistema, no solo casos específicos
- ✅ Facilita la detección de bugs sutiles

#### 5. Clase `IdentifierFixer` (`src/main/java/cl/ucn/modelo/IdentifierFixer.java`)

**Método principal:**
- `corregir(String original)`: Transforma un identificador inválido en uno válido aplicando reglas de corrección

**Reglas de corrección implementadas:**
1. **Eliminación de símbolos**: Elimina todos los caracteres no alfanuméricos
2. **Corrección de cadena vacía**: Si después de eliminar símbolos queda vacío, reemplaza por "a"
3. **Corrección de primer carácter**: Si no comienza con letra, reemplaza el primer carácter por "a"
4. **Eliminación de dígitos consecutivos**: Elimina dígitos consecutivos, dejando solo el primero
5. **Truncado**: Si excede 5 caracteres, trunca a 5 caracteres
6. **Validación final**: Si después de todas las correcciones aún es inválido, retorna "a"

**Características:**
- ✅ Implementación completa con todas las reglas de corrección
- ✅ Manejo de casos edge (null, cadena vacía, solo símbolos)
- ✅ Validación final para garantizar que el resultado sea siempre válido

#### 6. Enum `IdentifierError` (`src/main/java/cl/ucn/modelo/IdentifierError.java`)

**Valores del enum (5 valores, mínimo 4 requerido):**
- `NULO_O_VACIO`: Identificador es null o cadena vacía
- `MUY_LARGO`: Identificador excede la longitud máxima de 5 caracteres
- `PRIMER_CARACTER_NO_LETRA`: El primer carácter no es una letra
- `CARACTER_INVALIDO`: Contiene caracteres no alfanuméricos
- `DESCONOCIDO`: Error desconocido (caso no cubierto)

**Uso:**
- El método `getError(String identificador)` de la clase `Identifier` retorna el tipo específico de error
- Permite identificar la causa exacta de la invalidación de un identificador

---

## Tabla Final de Particiones de Equivalencia + Análisis de Frontera

Esta tabla muestra el diseño completo de los casos de prueba basados en particiones de equivalencia y análisis de frontera implementados en `IdentifierParameterizedTest.java`.

### Tabla Principal de Particiones

| Partición | Descripción | Tipo | Casos de Frontera | Casos de Prueba | Resultado Esperado |
|-----------|-------------|------|-------------------|-----------------|-------------------|
| **P1** | Identificadores válidos: comienzan con letra, longitud 1-5, solo alfanuméricos | ✅ Válido | Longitud: 1 (mínimo), 5 (máximo)<br>Carácter inicial: letra minúscula/mayúscula | `"a"`, `"A"` (frontera inferior)<br>`"abcde"`, `"ABCDE"`, `"a1b2c"` (frontera superior)<br>`"ab"`, `"a1"`, `"A1B2"`, `"x9y"` (intermedios)<br>`"a1234"` (frontera - 5 caracteres) | `true` |
| **P2** | Inválidos: comienzan con dígito | ❌ Inválido | Primer carácter: dígito | `"1abc"` (dígito + letras)<br>`"9"` (solo dígito)<br>`"1a234"` (dígito inicial con letras) | `false` |
| **P3** | Inválidos: comienzan con carácter especial | ❌ Inválido | Primer carácter: símbolo especial | `"@abc"` (arroba inicial)<br>`"_test"` (guion bajo inicial) | `false` |
| **P4** | Inválidos: longitud 0 (cadena vacía) | ❌ Inválido | **Frontera inferior absoluta** | `""` (cadena vacía) | `false` |
| **P5** | Inválidos: longitud > 5 | ❌ Inválido | **Frontera superior**: 6 caracteres (justo sobre el límite) | `"abcdef"` (6 caracteres - frontera)<br>`"abcdefgh"` (8 caracteres)<br>`"a1b2c3"` (6 caracteres alfanuméricos) | `false` |
| **P6** | Inválidos: contienen caracteres especiales (pero comienzan con letra) | ❌ Inválido | Caracteres especiales en posición > 0 | `"ab-c"` (guion)<br>`"a@b"` (arroba)<br>`"a b"` (espacio)<br>`"a.b"` (punto)<br>`"a#b"` (numeral) | `false` |

### Análisis Detallado de Fronteras

#### Frontera de Longitud

| Frontera | Valor | Casos de Prueba | Resultado | Justificación |
|----------|-------|-----------------|-----------|---------------|
| **Frontera inferior absoluta** | 0 caracteres | `""` | `false` | Cadena vacía no es válida |
| **Frontera inferior válida** | 1 carácter | `"a"`, `"A"` | `true` | Longitud mínima permitida |
| **Frontera superior válida** | 5 caracteres | `"abcde"`, `"ABCDE"`, `"a1b2c"`, `"a1234"` | `true` | Longitud máxima permitida |
| **Frontera superior inválida** | 6 caracteres | `"abcdef"`, `"a1b2c3"` | `false` | Justo sobre el límite máximo |

#### Frontera de Caracteres Iniciales

| Tipo de Carácter Inicial | Ejemplos | Resultado | Justificación |
|---------------------------|----------|-----------|---------------|
| **Letra minúscula** | `"a"`, `"abc"`, `"a1"` | `true` | Válido según regla 1 |
| **Letra mayúscula** | `"A"`, `"ABC"`, `"A1"` | `true` | Válido según regla 1 |
| **Dígito** | `"1abc"`, `"9"`, `"1a234"` | `false` | Violación de regla 1 |
| **Carácter especial** | `"@abc"`, `"_test"` | `false` | Violación de regla 1 |

#### Frontera de Caracteres Intermedios

| Posición | Caracteres Válidos | Caracteres Inválidos | Ejemplos Válidos | Ejemplos Inválidos |
|----------|-------------------|---------------------|------------------|-------------------|
| **Posición 0** | Letras (A-Z, a-z) | Dígitos, símbolos | `"a"`, `"A"` | `"1"`, `"@"` |
| **Posición 1-4** | Letras (A-Z, a-z), Dígitos (0-9) | Símbolos, espacios | `"a1"`, `"Ab2"`, `"a123"` | `"a-b"`, `"a b"`, `"a@b"` |

### Resumen de Cobertura

| Categoría | Cantidad | Detalles |
|-----------|----------|----------|
| **Particiones identificadas** | 6 | P1 (válidos), P2-P6 (inválidos) |
| **Casos de prueba totales** | 24 | Distribuidos en las 6 particiones |
| **Fronteras de longitud** | 4 | 0, 1, 5, 6 caracteres |
| **Fronteras de caracteres** | 4 | Letra min/may, dígito, símbolo |
| **Casos válidos** | 10 | Cubren todas las combinaciones válidas |
| **Casos inválidos** | 14 | Cubren todas las violaciones de reglas |

### Justificación del Diseño

1. **Cobertura completa de reglas**: Cada regla del identificador tiene al menos un caso de prueba que la valida o viola.

2. **Análisis de frontera exhaustivo**: Se prueban los valores límite (0, 1, 5, 6 caracteres) para detectar errores de off-by-one.

3. **Particiones de equivalencia bien definidas**: Cada partición agrupa casos que deben comportarse de la misma manera, reduciendo redundancia.

4. **Casos representativos**: Se seleccionan casos que representan cada partición sin necesidad de probar todas las combinaciones posibles.

5. **Cobertura de casos extremos**: Se incluyen casos como cadena vacía, null (en tests unitarios), y longitudes justo en los límites.

### Lista de identificadores inválidos y versión corregida por IdentifierFixer

La siguiente tabla muestra ejemplos reales de identificadores inválidos y cómo `IdentifierFixer` los corrige aplicando las reglas implementadas:

| Identificador Inválido | Versión Corregida | Regla(s) Aplicada(s) |
|------------------------|-------------------|---------------------|
| `"1abc"` | `"aabc"` | Reemplazo de dígito inicial por 'a' |
| `"abcdef"` | `"abcde"` | Truncado a 5 caracteres (longitud máxima) |
| `"ab-c"` | `"abc"` | Eliminación de carácter especial (guion) |
| `"@test"` | `"a"` | Eliminación de símbolos y corrección de primer carácter |
| `"a12b"` | `"a1b"` | Eliminación de dígitos consecutivos (se elimina el segundo '2') |
| `"a@b#c"` | `"abc"` | Eliminación de múltiples símbolos especiales |
| `""` | `"a"` | Cadena vacía reemplazada por 'a' |
| `"12345"` | `"a"` | Solo dígitos: se eliminan todos y se reemplaza por 'a' |
| `"a1b2c3"` | `"a1b2c"` | Truncado a 5 caracteres (6 caracteres originales) |
| `"a-b-c"` | `"abc"` | Eliminación de múltiples guiones |
| `"9xyz"` | `"axyz"` | Reemplazo de dígito inicial por 'a' |
| `"a  b"` | `"ab"` | Eliminación de espacios |
| `"abcde"` | `"abcde"` | Ya era válido, no se modifica |
| `"a11b"` | `"a1b"` | Eliminación de dígitos consecutivos (se elimina el segundo '1') |
| `"@#$%^"` | `"a"` | Solo símbolos especiales: se eliminan todos y se reemplaza por 'a' |


**Notas sobre el comportamiento:**
- Si el identificador ya es válido, `IdentifierFixer` lo retorna sin modificar
- Si después de aplicar todas las reglas el resultado sigue siendo inválido, se retorna `"a"` como fallback
- El orden de aplicación de las reglas es: eliminación de símbolos → corrección de vacío → corrección de primer carácter → eliminación de dígitos consecutivos → truncado → validación final

### Justificación de Refactor aplicado al método base

**Mejoras aplicadas al método `validateIdentifier`:**

1. **Separación de responsabilidades mediante método auxiliar `validateChars()`:**
   - Se extrajo la lógica de validación de caracteres (posiciones 1 en adelante) a un método privado `validateChars(String identificador)`
   - Esto mejora la legibilidad del método principal, que ahora tiene una estructura más clara y fácil de seguir

2. **Eliminación de variables innecesarias:**
   - Se simplificó el método eliminando variables intermedias que no aportaban valor
   - El código es más directo y conciso

3. **Estructura más clara y legible:**
   - El método principal ahora sigue un flujo más lineal: validación de null/vacío/longitud → validación de primer carácter → validación de caracteres restantes
   - Cada validación es independiente y fácil de entender

4. **Adición del método `getError()` para diagnóstico:**
   - Se agregó un método adicional `getError(String identificador)` que permite identificar el tipo específico de error cuando un identificador es inválido
   - Esto mejora significativamente la capacidad de diagnóstico y debugging

**Código antes del refactor (versión original):**
´´´java
public boolean validateIdentifier(String s) {
        char achar;
        boolean valid_id = false;

        achar = s.charAt(0);
        valid_id = valid_s(achar);

        if (s.length() > 1) {
            int i = 1;
            while (i < s.length()) {
                achar = s.charAt(i);
                if (!valid_f(achar)) {
                    valid_id = false;
                }
                i++;
            }
        }

        if (valid_id && (s.length() >= 1) && (s.length() < 6)) {
            return true;
        } else {
            return false;
        }
    }
´´´
**Código después del refactor (versión actual):**
```java
// Versión refactorizada: más limpia, clara y mantenible
public boolean validateIdentifier(String identificador) {
    if (identificador == null || identificador.length() == 0 || identificador.length() > 5) return false;
    if (!esLetra(identificador.charAt(0))) return false;
    if (identificador.length() > 1 && !validateChars(identificador)) return false;
    return true;
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
```

**Beneficios del refactor:**

1. **Legibilidad mejorada:**
   - El código es más fácil de leer y entender para otros desarrolladores
   - Los nombres de variables y métodos son más descriptivos (`identificador` vs `s`, `esLetra` vs `valid_s`)

2. **Mantenibilidad:**
   - Cambios futuros son más fáciles de implementar
   - La separación de lógica en métodos auxiliares facilita la modificación de reglas específicas

3. **Testabilidad:**
   - La separación de lógica facilita las pruebas unitarias
   - Cada método puede ser testeado de forma independiente

4. **Extensibilidad:**
   - El método `getError()` permite futuras mejoras en el manejo de errores
   - La estructura modular facilita agregar nuevas validaciones

5. **Rendimiento:**
   - El código es más eficiente al evitar operaciones innecesarias
   - Las validaciones tempranas (early returns) evitan procesamiento adicional cuando ya se sabe que es inválido

**Justificación técnica:**
Este refactor sigue principios de Clean Code y SOLID, específicamente el principio de responsabilidad única (SRP), donde cada método tiene una responsabilidad clara y bien definida.

## Reglas de corrección propuestas para IdentifierFixer

## Reglas de corrección implementadas en IdentifierFixer

Las siguientes reglas están **implementadas y funcionando** en la clase `IdentifierFixer`:

1. **Eliminación de símbolos no permitidos:**
   - Si contiene caracteres especiales (guiones, arrobas, espacios, etc.), se eliminan todos
   - Solo se conservan letras (A-Z, a-z) y dígitos (0-9)

2. **Corrección de cadena vacía:**
   - Si después de eliminar símbolos la cadena queda vacía, se reemplaza por `"a"`

3. **Corrección de primer carácter:**
   - Si el primer carácter no es una letra (es un dígito o símbolo), se reemplaza por `"a"`
   - Si la cadena tiene solo un carácter y no es letra, se reemplaza completamente por `"a"`

4. **Eliminación de dígitos consecutivos:**
   - Si hay dos o más dígitos consecutivos, se eliminan todos excepto el primero
   - Ejemplo: `"a12b"` → `"a1b"`, `"a11b"` → `"a1b"`

5. **Truncado a longitud máxima:**
   - Si la cadena excede 5 caracteres después de las correcciones anteriores, se trunca a exactamente 5 caracteres
   - Se mantienen los primeros 5 caracteres

6. **Validación final y fallback:**
   - Después de aplicar todas las reglas, se valida que el resultado sea válido
   - Si aún es inválido (caso extremo), se retorna `"a"` como valor por defecto seguro

**Orden de aplicación de las reglas:**
1. Eliminación de símbolos
2. Corrección de cadena vacía (si aplica)
3. Corrección de primer carácter (si aplica)
4. Eliminación de dígitos consecutivos
5. Truncado a 5 caracteres (si aplica)
6. Validación final y fallback (si aplica)

---

**Cobertura de Tests:**
- ✅ Tests unitarios: 19 tests (supera el mínimo de 10 requerido)
- ✅ Tests parametrizados: 24 casos (supera el mínimo de 12 requerido)
- ✅ Property-Based Testing: 4 propiedades (supera el mínimo de 2 requerido)
- ✅ Particiones de equivalencia: 6 particiones identificadas y testeadas
- ✅ Análisis de frontera: Cubierto en tests parametrizados y unitarios

**Archivos del Proyecto:**
- `src/main/java/cl/ucn/modelo/Identifier.java` - Clase principal de validación
- `src/main/java/cl/ucn/modelo/IdentifierError.java` - Enum de códigos de error
- `src/main/java/cl/ucn/modelo/IdentifierFixer.java` - Clase para corregir identificadores inválidos
- `src/main/java/cl/ucn/main/Main.java` - Clase principal de ejecución
- `src/test/java/cl/ucn/modelo/IdentifierUnitTest.java` - Tests unitarios (19 tests)
- `src/test/java/cl/ucn/modelo/IdentifierParameterizedTest.java` - Tests parametrizados (24 casos)
- `src/test/java/cl/ucn/modelo/IdentifierPropertiesTest.java` - Property-Based Testing (4 propiedades)

---

## Método de Evaluación

| Criterio | Ponderación |
|----------|-------------|
| Diseño de casos basados en equivalencias + frontera | 35%         |
| Correcta implementación JUnit parametrizado | 15%         |
| Property Based Testing | 15%         |
| Implementación IdentifierFixer + Enum Errors + Refactor | 20%         |
| Documentación README según formato exigido | 15%         |

---

### Fecha límite: 

Entrega el **18-11-2025** por medio de campus virtual.
