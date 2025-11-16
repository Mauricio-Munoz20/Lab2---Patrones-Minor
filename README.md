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

## Estado Actual de la Implementación

### ✅ Implementado

#### 1. Clase `Identifier` (`src/main/java/cl/ucn/modelo/Identifier.java`)

**Método principal:**
- `validateIdentifier(String identificador)`: Valida si un identificador cumple con las reglas establecidas

**Métodos auxiliares:**
- `esLetra(char caracter)`: Verifica si un carácter es una letra (A-Z, a-z)
- `esAlfanumerico(char caracter)`: Verifica si un carácter es alfanumérico (letra o dígito)

**Características:**
- ✅ Todos los identificadores (variables y métodos) traducidos al español
- ✅ Validación de cadena vacía o null
- ✅ Validación de longitud (1-5 caracteres)
- ✅ Validación de primer carácter (debe ser letra)
- ✅ Validación de caracteres siguientes (letras o dígitos)

#### 2. Tests Unitarios (`src/test/java/cl/ucn/modelo/IdentifierUnitTest.java`)

**Total: 19 tests unitarios no parametrizados**

**Tests de casos válidos (9):**
- `testValidSingleLetterLowerCase`: Letra minúscula única
- `testValidSingleLetterUpperCase`: Letra mayúscula única
- `testValidTwoLetters`: Dos letras
- `testValidLetterFollowedByDigit`: Letra seguida de dígito
- `testValidMultipleDigits`: Letra seguida de múltiples dígitos
- `testValidMixedCaseAndDigits`: Mezcla de mayúsculas, minúsculas y dígitos
- `testValidMaximumLength`: Exactamente 5 caracteres
- `testValidAllDigitsAfterFirstLetter`: Letra inicial seguida de solo dígitos
- `testValidBoundaryCaseOneCharacter`: Un solo carácter válido
- `testValidBoundaryCaseFiveCharacters`: Exactamente 5 caracteres válidos

**Tests de casos inválidos (10):**
- `testInvalidEmptyString`: Cadena vacía
- `testInvalidStartsWithDigit`: Comienza con dígito
- `testInvalidTooLong`: Más de 5 caracteres
- `testInvalidContainsSpecialCharacter`: Contiene carácter especial
- `testInvalidContainsSpace`: Contiene espacio
- `testInvalidStartsWithUnderscore`: Comienza con guion bajo
- `testInvalidJustOverMaximumLength`: 6 caracteres (excede máximo)
- `testInvalidOnlySpecialCharacters`: Solo caracteres especiales
- `testInvalidNullInput`: Entrada null

#### 3. Tests Parametrizados (`src/test/java/cl/ucn/modelo/IdentifierParameterizedTest.java`)

**Total: 24 casos de prueba parametrizados** (usando `@RunWith(Parameterized.class)`)

**Particiones de Equivalencia implementadas:**

- **P1: Identificadores válidos** (9 casos)
  - Frontera inferior: 1 carácter (minúscula y mayúscula)
  - Frontera superior: 5 caracteres (letras, mayúsculas, alfanuméricos)
  - Casos intermedios: 2, 3, 4 caracteres con diferentes combinaciones

- **P2: Inválidos - comienzan con dígito** (2 casos)
  - `"1abc"`, `"9"`

- **P3: Inválidos - comienzan con carácter especial** (2 casos)
  - `"@abc"`, `"_test"`

- **P4: Inválidos - longitud 0** (1 caso)
  - Cadena vacía `""`

- **P5: Inválidos - longitud > 5** (2 casos)
  - `"abcdef"` (6 caracteres), `"abcdefgh"` (8 caracteres)

- **P6: Inválidos - contienen caracteres especiales** (4 casos)
  - Guion `"ab-c"`, arroba `"a@b"`, espacio `"a b"`, punto `"a.b"`

- **Casos adicionales de frontera** (4 casos)
  - `"a1234"` (válido - 5 caracteres), `"1a234"` (inválido), `"a1b2c3"` (inválido - 6 caracteres), `"a#b"` (inválido)

**Análisis de frontera cubierto:**
- Frontera de longitud: 0, 1, 5, 6 caracteres
- Frontera de caracteres: letra inicial válida, dígito inicial inválido

#### 4. Property-Based Testing (`src/test/java/cl/ucn/modelo/IdentifierPropertiesTest.java`)

**Total: 4 propiedades definidas y testeadas usando jqwik**

**Propiedad 1: `validIdentifiersAreAlwaysAccepted`**
- Verifica que identificadores válidos (comienzan con letra, longitud 1-5, solo alfanuméricos) son siempre aceptados
- Genera automáticamente cientos de casos de prueba

**Propiedad 2: `invalidIdentifiersAreAlwaysRejected`**
- Verifica que identificadores inválidos (violan alguna regla) son siempre rechazados
- Cubre: cadena vacía, no comienza con letra, longitud > 5, caracteres no alfanuméricos

**Propiedad 3: `validIdentifiersSatisfyAllRules`**
- Verifica consistencia: si un identificador es válido, debe cumplir todas las reglas
- Valida longitud, primer carácter y caracteres alfanuméricos

**Propiedad 4: `identifiersStartingWithDigitAreInvalid`**
- Verifica que identificadores que comienzan con dígito son siempre inválidos
- Genera casos con primer carácter numérico seguido de alfanuméricos

**Generador personalizado:**
- `cadenaAlfanumerica()`: Genera strings alfanuméricos para las pruebas

### ⚠️ Pendiente // JH lo hace buzz

1. **Clase `IdentifierFixer`** - No implementada
   - Debe transformar identificadores inválidos en válidos según reglas definidas

2. **Enum de Código de Error** - No implementado
   - Debe indicar por qué falla un identificador inválido (mínimo 4 causas)

3. **Refactor del método base** - Pendiente documentación
   - Mejoras aplicadas al método `validateIdentifier` deben documentarse

4. **Tabla de particiones de equivalencia** - Pendiente documentación en README
   - Tabla completa que muestre el diseño de los tests

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


### 3) Nueva Clase: `IdentifierFixer` ⚠️ Pendiente
Esta clase debe poder transformar un identificador inválido en uno válido según reglas que tú defines.

Debes definir las reglas de corrección en README (sección abajo).

### 4) Enum de Código de Error ⚠️ Pendiente
Cuando el identificador es inválido, el método `validateIdentifier` debe indicar POR QUÉ falla (enum con mínimo 4 causas).

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

**Estado:** ⚠️ Requiere implementar IdentifierFixer primero

Una vez implementada la clase `IdentifierFixer`, se debe agregar una tabla con ejemplos como:
- `"1abc"` → `"aabc"` (reemplazo de dígito inicial)
- `"abcdef"` → `"abcde"` (truncado a 5 caracteres)
- `"ab-c"` → `"abc"` (eliminación de carácter especial)

### Justificación de Refactor aplicado al método base

**Estado:** ⚠️ Pendiente de documentación

Se debe explicar:
- Qué mejoras se aplicaron al método `validateIdentifier`
- Por qué se realizaron esos cambios
- Cómo mejoran la legibilidad, mantenibilidad o rendimiento del código

---

## Reglas de corrección propuestas para IdentifierFixer

Las siguientes reglas se proponen para la implementación de `IdentifierFixer`:

- Si comienza con número, reemplazar por una letra (por defecto 'a').
- Si hay 2 dígitos consecutivos, borrar uno de ellos.
- Si contiene símbolos no permitidos, eliminarlos.
- Si supera la longitud máxima (5 caracteres), truncar hasta 5 caracteres.

**Nota:** Estas reglas deben implementarse en la clase `IdentifierFixer` cuando se desarrolle. Puede proponer otras reglas, pero deben quedar documentadas.

---

**Cobertura de Tests:**
- ✅ Tests unitarios: 19 tests (supera el mínimo de 10 requerido)
- ✅ Tests parametrizados: 24 casos (supera el mínimo de 12 requerido)
- ✅ Property-Based Testing: 4 propiedades (supera el mínimo de 2 requerido)
- ✅ Particiones de equivalencia: 6 particiones identificadas y testeadas
- ✅ Análisis de frontera: Cubierto en tests parametrizados y unitarios

**Archivos del Proyecto:**
- `src/main/java/cl/ucn/modelo/Identifier.java` - Clase principal
- `src/main/java/cl/ucn/main/Main.java` - Clase principal de ejecución
- `src/test/java/cl/ucn/modelo/IdentifierUnitTest.java` - Tests unitarios
- `src/test/java/cl/ucn/modelo/IdentifierParameterizedTest.java` - Tests parametrizados
- `src/test/java/cl/ucn/modelo/IdentifierPropertiesTest.java` - Property-Based Testing

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

---

