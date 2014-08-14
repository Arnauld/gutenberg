package gutenberg.util;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DimensionParserTest {

    private DimensionParser dimensionParser;

    @Before
    public void setUp() {
        dimensionParser = new DimensionParser();
    }

    @Test
    public void parse_valid_inputs() throws DimensionFormatException {
        assertThat(dimensionParser.parse("123px")).isEqualTo(new Dimension(123, Dimension.Unit.Px));
        assertThat(dimensionParser.parse("7px")).isEqualTo(new Dimension(7, Dimension.Unit.Px));
        assertThat(dimensionParser.parse("27%")).isEqualTo(new Dimension(27, Dimension.Unit.Percent));
        assertThat(dimensionParser.parse("50%")).isEqualTo(new Dimension(50, Dimension.Unit.Percent));
    }
}