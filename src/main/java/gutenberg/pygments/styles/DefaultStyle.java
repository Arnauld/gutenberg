package gutenberg.pygments.styles;

import gutenberg.pygments.StyleSheet;

import static gutenberg.pygments.Style.style;
import static gutenberg.pygments.Token.*;
import static gutenberg.util.RGB.rgb;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class DefaultStyle extends StyleSheet {

    public DefaultStyle() {
        backgroundColor(rgb("#f8f8f8"));
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
        define(Comment, fg("#408080").italic());
        define(CommentMultiline);
        define(CommentPreproc, fg("#BC7A00").noitalic());
        define(CommentSingle);
        define(CommentSpecial);
        //
        define(Keyword, fg("#008000").bold());
        define(KeywordConstant);
        define(KeywordDeclaration);
        define(KeywordNamespace);
        define(KeywordPseudo, style().nobold());
        define(KeywordReserved);
        define(KeywordType, fg("#B00040").nobold());
        //
        define(Operator, fg("#666666"));
        define(OperatorWord, fg("#AA22FF").bold());
        //
        define(Punctuation);
        //
        define(Name);
        define(NameAttribute, fg("#7D9029"));
        define(NameBuiltin, fg("#008000"));
        define(NameBuiltinPseudo);
        define(NameClass, fg("#0000FF").bold());
        define(NameConstant, fg("#880000"));
        define(NameDecorator, fg("#AA22FF"));
        define(NameEntity, fg("#999999").bold());
        define(NameException, fg("#D2413A").bold());
        define(NameFunction, fg("#0000FF"));
        define(NameProperty);
        define(NameLabel, fg("#A0A000"));
        define(NameNamespace, fg("#0000FF").bold());
        define(NameOther);
        define(NameTag, fg("#008000").bold());
        define(NameVariable, fg("#19177C"));
        define(NameVariableClass);
        define(NameVariableGlobal);
        define(NameVariableInstance);
        //
        define(Number, fg("#666666"));
        define(NumberFloat);
        define(NumberHex);
        define(NumberInteger);
        define(NumberIntegerLong);
        define(NumberOct);
        //
        define(Literal);
        define(LiteralDate);
        //
        define(String, fg("#BA2121"));
        define(StringBacktick);
        define(StringChar);
        define(StringDoc);
        define(StringDouble);
        define(StringEscape);
        define(StringHeredoc);
        define(StringInterpol, fg("#BB6688").bold());
        define(StringOther, fg("#008000"));
        define(StringRegex, fg("#BB6688"));
        define(StringSingle);
        define(StringSymbol);
        //
        define(Generic);
        define(GenericDeleted, fg("#A00000"));
        define(GenericEmph, style().italic());
        define(GenericError, fg("#FF0000"));
        define(GenericHeading, fg("#000080").bold());
        define(GenericInserted, fg("#00A000"));
        define(GenericOutput, fg("#888"));
        define(GenericPrompt, fg("#000080").bold());
        define(GenericStrong, style().bold());
        define(GenericSubheading, fg("#800080").bold());
        define(GenericTraceback, fg("#04D"));
    }
}
