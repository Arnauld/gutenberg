package gutenberg.pygments.styles;

import gutenberg.pygments.StyleSheet;

import static gutenberg.util.Style.style;
import static gutenberg.pygments.Token.*;
import static gutenberg.util.RGB.rgb;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class FriendlyStyle extends StyleSheet {

    public FriendlyStyle() {
        backgroundColor(rgb("#f0f0f0"));
        highlightColor(rgb("#49483e"));
        initStyles();
    }

    protected void initStyles() {
        define(Token);
        //
        define(Text, fg("#000000"));
        define(Whitespace, fg("#bbbbbb"));
        define(Error, fg("#ff0000"));
        define(Other);
        //
        define(Comment, fg("#60a0b0").italic());
        define(CommentMultiline);
        define(CommentPreproc, fg("#007020").noitalic());
        define(CommentSingle);
        define(CommentSpecial, bg("#fff0f0").noitalic());
        //
        define(Keyword, fg("#007020").bold());
        define(KeywordConstant);
        define(KeywordDeclaration);
        define(KeywordNamespace);
        define(KeywordPseudo, style().nobold());
        define(KeywordReserved);
        define(KeywordType, fg("#902000").nobold());
        //
        define(Operator, fg("#666666"));
        define(OperatorWord, fg("#007020").bold());
        //
        define(Punctuation);
        //
        define(Name);
        define(NameAttribute, fg("#4070a0"));
        define(NameBuiltin, fg("#007020"));
        define(NameBuiltinPseudo);
        define(NameClass, fg("#0e84b5").bold());
        define(NameConstant, fg("#60add5"));
        define(NameDecorator, fg("#555555").bold());
        define(NameEntity, fg("#d55537").bold());
        define(NameException, fg("#007020"));
        define(NameFunction, fg("#06287e"));
        define(NameProperty);
        define(NameLabel, fg("#002070").bold());
        define(NameNamespace, fg("#0e84b5").bold());
        define(NameOther);
        define(NameTag, fg("#062873").bold());
        define(NameVariable, fg("#bb60d5"));
        define(NameVariableClass);
        define(NameVariableGlobal);
        define(NameVariableInstance);
        //
        define(Number, fg("#40a070"));
        define(NumberFloat);
        define(NumberHex);
        define(NumberInteger);
        define(NumberIntegerLong);
        define(NumberOct);
        //
        define(Literal);
        define(LiteralDate);
        //
        define(String, fg("#4070a0"));
        define(StringBacktick);
        define(StringChar);
        define(StringDoc, style().italic());
        define(StringDouble);
        define(StringEscape, fg("#4070a0").bold());
        define(StringHeredoc);
        define(StringInterpol, fg("#70a0d0").italic());
        define(StringOther, fg("#c65d09"));
        define(StringRegex, fg("#235388"));
        define(StringSingle);
        define(StringSymbol, fg("#517918"));
        //
        define(Generic);
        define(GenericDeleted, fg("#A00000"));
        define(GenericEmph, style().italic());
        define(GenericError, fg("#FF0000"));
        define(GenericHeading, fg("#000080").bold());
        define(GenericInserted, fg("#00A000"));
        define(GenericOutput, fg("#888"));
        define(GenericPrompt, fg("#c65d09").bold());
        define(GenericStrong, style().bold());
        define(GenericSubheading, fg("#800080").bold());
        define(GenericTraceback, fg("#04D"));
    }
}
