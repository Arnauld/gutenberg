package gutenberg.util;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class Dimension {

    public enum Unit {
        Percent,
        Px
    }

    private final float amount;
    private final Unit unit;

    public Dimension(float amount, Unit unit) {
        this.amount = amount;
        this.unit = unit;
    }

    public float amount() {
        return amount;
    }

    public Unit unit() {
        return unit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Dimension dimension = (Dimension) o;
        return Float.compare(dimension.amount, amount) == 0 && unit == dimension.unit;
    }

    @Override
    public int hashCode() {
        int result = (amount != +0.0f ? Float.floatToIntBits(amount) : 0);
        result = 31 * result + unit.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Dimension{" + amount + unit + '}';
    }
}
