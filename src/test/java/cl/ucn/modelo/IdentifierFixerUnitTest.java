package cl.ucn.modelo;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;


public class IdentifierFixerUnitTest {

    private IdentifierFixer fixer;

    @Before
    public void setup () {
        fixer = new IdentifierFixer();
    }

    @Test
    public void corregirCadenasValidasTest() {
        //IdentifierFixer fixer = new IdentifierFixer();
        String s1 = fixer.corregir("a");
        String s2 = fixer.corregir("abc");
        String s3 = fixer.corregir("A1b2");
        String s4 = fixer.corregir("Z999");
        assertEquals("a", s1);
        assertEquals("abc", s2);
        assertEquals("A1b2", s3);
        assertEquals("Z999", s4);
    }

    @Test
    public void corregirCadenaVaciaNulaTest() {
        //IdentifierFixer fixer = new IdentifierFixer();
        String s = fixer.corregir("");
        assertEquals("a",s);
    }

    @Test
    public void corregirExcederLargoTest() {
        //IdentifierFixer fixer = new IdentifierFixer();
        String s1 = fixer.corregir("abcdef");
        String s2 = fixer.corregir("a12345");
        String s3 = fixer.corregir("abcd12");
        String s4 = fixer.corregir("xyz1234");
        String s5 = fixer.corregir("1234@6");
        assertEquals("abcde", s1);
        assertEquals("a1234", s2);
        assertEquals("abcd1", s3);
        assertEquals("xyz12", s4);
        assertEquals("a2346", s5);
    }

    @Test
    public void corregirPrimerCaracterTest() {
        String s1 = fixer.corregir("1abc");
        String s2 = fixer.corregir("9abcd");
        String s3 = fixer.corregir("0a");
        String s4 = fixer.corregir("3");
        String s5 = fixer.corregir("3abc!!!!!!32fsafsa");
        assertEquals("aabc", s1);
        assertEquals("aabcd", s2);
        assertEquals("aa", s3);
        assertEquals("a", s4);
        assertEquals("aabc3", s5);
    }

    @Test
    public void corregirCaracterInvalidoInterno() {
        String out = fixer.corregir("ab-c");
        assertEquals("abc", out);
        assertTrue(new Identifier().validateIdentifier(out));
    }

    @Test
    public void corregirNullDevuelveNull() {
        String out = fixer.corregir(null);
        assertNull(out);
    }


}
