package gutenberg.util;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class StringsTest {

    @Test
    public void countTrailingWhitespaces() {
        assertThat(Strings.countTrailingWhitespaces("a  ")).isEqualTo(0);
        assertThat(Strings.countTrailingWhitespaces(" a ")).isEqualTo(1);
        assertThat(Strings.countTrailingWhitespaces("  a")).isEqualTo(2);
        assertThat(Strings.countTrailingWhitespaces("   ")).isEqualTo(3);
    }

    @Test
    public void unindentBlock() {
        assertThat(Strings
                .unindentBlock("" +
                        "          A nice diagram\n" +
                        "          \n" +
                        "    +--------+   +-------+    +-------+\n" +
                        "    |        | --+ ditaa +--> |       |\n" +
                        "    |  Text  |   +-------+    |diagram|\n" +
                        "    |Document|   |!magic!|    |       |\n" +
                        "    |     {d}|   |       |    |       |\n" +
                        "    +---+----+   +-------+    +-------+\n" +
                        "        :                         ^\n" +
                        "        |       Lots of work      |\n" +
                        "        +-------------------------+"))
                .isEqualTo("" +
                        "      A nice diagram\n" +
                        "      \n" +
                        "+--------+   +-------+    +-------+\n" +
                        "|        | --+ ditaa +--> |       |\n" +
                        "|  Text  |   +-------+    |diagram|\n" +
                        "|Document|   |!magic!|    |       |\n" +
                        "|     {d}|   |       |    |       |\n" +
                        "+---+----+   +-------+    +-------+\n" +
                        "    :                         ^\n" +
                        "    |       Lots of work      |\n" +
                        "    +-------------------------+");
    }
}