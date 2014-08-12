package gutenberg.util;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class Strings {
    public static String unindentBlock(String block) {
        int nbSp = -1;
        String[] linesArray = block.split("[\r]?\n");
        for (String aLinesArray1 : linesArray) {
            int cnSp = countTrailingWhitespaces(aLinesArray1);
            if (nbSp == -1)
                nbSp = cnSp;
            else
                nbSp = Math.min(nbSp, cnSp);
        }

        if (nbSp <= 0) {
            return block;
        }

        StringBuilder b = new StringBuilder();
        for (String aLinesArray : linesArray) {
            if(b.length()>0)
                b.append("\n");
            b.append(aLinesArray.substring(nbSp));
        }
        return b.toString();
    }

    public static int countTrailingWhitespaces(String s) {
        for (int st = 0; st < s.length(); st++) {
            if (s.charAt(st) > ' ') {
                return st;
            }
        }
        return s.length();
    }
}
