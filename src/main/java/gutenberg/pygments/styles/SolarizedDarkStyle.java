package gutenberg.pygments.styles;

import gutenberg.pygments.StyleSheet;
import gutenberg.util.RGB;

import static gutenberg.pygments.Token.*;
import static gutenberg.util.RGB.rgb;
import static gutenberg.util.Style.style;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class SolarizedDarkStyle extends StyleSheet {

    private static final RGB BASE03 = RGB.rgb("#002B36");
    private static final RGB BASE02 = RGB.rgb("#073642");
    private static final RGB BASE01 = RGB.rgb("#586E75");
    private static final RGB BASE00 = RGB.rgb("#657B83");
    private static final RGB BASE0 = RGB.rgb("#839496");
    private static final RGB BASE1 = RGB.rgb("#93A1A1");
    private static final RGB BASE2 = RGB.rgb("#EEE8D5");
    private static final RGB BASE3 = RGB.rgb("#FDF6E3");
    private static final RGB YELLOW = RGB.rgb("#B58900");
    private static final RGB ORANGE = RGB.rgb("#CB4B16");
    private static final RGB RED = RGB.rgb("#DC322F");
    private static final RGB MAGENTA = RGB.rgb("#D33682");
    private static final RGB VIOLET = RGB.rgb("#6C71C4");
    private static final RGB BLUE = RGB.rgb("#268BD2");
    private static final RGB CYAN = RGB.rgb("#2AA198");
    private static final RGB GREEN = RGB.rgb("#859900");

    public SolarizedDarkStyle() {
        backgroundColor(BASE03);
        highlightColor(rgb("#49483e"));
        initStyles();
    }

    protected void initStyles() {
        define(Token, fg(BASE1));
        //
        define(Text);
        define(Whitespace);
        define(Error);
        define(Other, fg(ORANGE));
        //
        define(Comment, fg(BASE01).italic());
        define(CommentMultiline);
        define(CommentPreproc, fg(GREEN));
        define(CommentSingle);
        define(CommentSpecial, fg(GREEN));
        //
        define(Keyword, fg(GREEN));
        define(KeywordConstant, fg(ORANGE));
        define(KeywordDeclaration, fg(BLUE));
        define(KeywordNamespace);
        define(KeywordPseudo);
        define(KeywordReserved, fg(BLUE));
        define(KeywordType, fg(RED));
        //
        define(Operator, fg(GREEN));
        define(OperatorWord);
        //
        define(Punctuation);
        //
        define(Name);
        define(NameAttribute, fg(BASE1));
        define(NameBuiltin, fg(YELLOW));
        define(NameBuiltinPseudo, fg(BLUE));
        define(NameClass, fg(BLUE).bold());
        define(NameConstant, fg(ORANGE));
        define(NameDecorator, fg(BLUE));
        define(NameEntity, fg(ORANGE).bold());
        define(NameException, fg(ORANGE));
        define(NameFunction, fg(BLUE));
        define(NameProperty);
        define(NameLabel);
        define(NameNamespace);
        define(NameOther);
        define(NameTag, fg(BLUE).bold());
        define(NameVariable, fg(BLUE));
        define(NameVariableClass);
        define(NameVariableGlobal);
        define(NameVariableInstance);
        //
        define(Number, fg(CYAN));
        define(NumberFloat);
        define(NumberHex);
        define(NumberInteger);
        define(NumberIntegerLong);
        define(NumberOct);
        //
        define(Literal);
        define(LiteralDate);
        //
        define(String, fg(CYAN));
        define(StringBacktick, fg(BASE01));
        define(StringChar, fg(CYAN));
        define(StringDoc, fg(BASE1));
        define(StringDouble);
        define(StringEscape, fg(ORANGE));
        define(StringHeredoc, fg(BASE1));
        define(StringInterpol);
        define(StringOther);
        define(StringRegex, fg(RED));
        define(StringSingle);
        define(StringSymbol, fg(CYAN));
        //
        define(Generic);
        define(GenericDeleted, fg(CYAN));
        define(GenericEmph, style().italic());
        define(GenericError, fg(RED));
        define(GenericHeading, fg(ORANGE));
        define(GenericInserted, fg(GREEN));
        define(GenericOutput);
        define(GenericPrompt);
        define(GenericStrong, style().bold());
        define(GenericSubheading, fg(ORANGE));
        define(GenericTraceback);
    }
}
