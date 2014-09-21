package gutenberg.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.technbolts.junit.runners.Runner;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Runner.class)
public class RomanNumeralTest {

    @Runner.DataProvider(name = "1-to-20")
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {1, "i"},
                {2, "ii"},
                {3, "iii"},
                {4, "iv"},
                {5, "v"},
                {6, "vi"},
                {7, "vii"},
                {8, "viii"},
                {9, "ix"},
                {10, "x"},
                {11, "xi"},
                {12, "xii"},
                {13, "xiii"},
                {14, "xiv"},
                {15, "xv"},
                {16, "xvi"},
                {17, "xvii"},
                {18, "xviii"},
                {19, "xix"},
                {20, "xx"},
        });
    }

    @Test
    @Runner.Parameterized(dataProvider = "1-to-20", namePattern = "{0} = {1}")
    public void first_numbers(int value, String expected) {
        assertThat(new RomanNumeral().format(value)).isEqualTo(expected);
    }

    @Runner.DataProvider(name = "symbols")
    public static Iterable<Object[]> symbols() {
        return Arrays.asList(new Object[][]{
                {1, "i"},
                {5, "v"},
                {10, "x"},
                {50, "l"},
                {100, "c"},
                {500, "d"},
                {1000, "m"}
        });
    }

    @Test
    @Runner.Parameterized(dataProvider = "symbols", namePattern = "{0} = {1}")
    public void symbols(int value, String expected) {
        assertThat(new RomanNumeral().format(value)).isEqualTo(expected);
    }

    @Runner.DataProvider(name = "big-numbers")
    public static Iterable<Object[]> bigNumbers() {
        return Arrays.asList(new Object[][]{
                {2014, "mmxiv"},
        });
    }

    @Test
    @Runner.Parameterized(dataProvider = "big-numbers", namePattern = "{0} = {1}")
    public void big_numbers(int value, String expected) {
        assertThat(new RomanNumeral().format(value)).isEqualTo(expected);
    }

}