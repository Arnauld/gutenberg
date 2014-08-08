package gutenberg.pygments;

import static gutenberg.pygments.Style.style;
import static gutenberg.pygments.Token.*;
import static gutenberg.pygments.Token.Error;
import static gutenberg.pygments.Token.Number;
import static gutenberg.pygments.Token.String;
import static gutenberg.util.RGB.rgb;

/**
 * @author <a href="define(http,//twitter.com/aloyer">@aloyer</a>
 */
public class MonokaiStyle extends StyleSheet {

    public MonokaiStyle() {
        backgroundColor(rgb("#272822"));
        highlightColor(rgb("#49483e"));
        initStyles();
    }

    protected void initStyles() {
        define(Token);
        //
        define(Text, fg("#f8f8f2"));
        define(Whitespace);
        define(Error, fg("#960050").bg("#1e0010"));
        define(Other);
        //
        define(Comment, fg("#75715e"));
        define(CommentMultiline);
        define(CommentPreproc);
        define(CommentSingle);
        define(CommentSpecial);
        //
        define(Keyword, fg("#66d9ef"));
        define(KeywordConstant);
        define(KeywordDeclaration);
        define(KeywordNamespace, fg("#f92672"));
        define(KeywordPseudo);
        define(KeywordReserved);
        define(KeywordType);
        //
        define(Operator, fg("#f92672"));
        define(OperatorWord);
        //
        define(Punctuation, fg("#f8f8f2"));
        //
        define(Name, fg("#f8f8f2"));
        define(NameAttribute, fg("#a6e22e"));
        define(NameBuiltin);
        define(NameBuiltinPseudo);
        define(NameClass, fg("#a6e22e"));
        define(NameConstant, fg("#66d9ef"));
        define(NameDecorator, fg("#a6e22e"));
        define(NameEntity);
        define(NameException, fg("#a6e22e"));
        define(NameFunction, fg("#a6e22e"));
        define(NameProperty);
        define(NameLabel);
        define(NameNamespace);
        define(NameOther, fg("#a6e22e"));
        define(NameTag, fg("#f92672"));
        define(NameVariable);
        define(NameVariableClass);
        define(NameVariableGlobal);
        define(NameVariableInstance);
        //
        define(Number, fg("#ae81ff"));
        define(NumberFloat);
        define(NumberHex);
        define(NumberInteger);
        define(NumberIntegerLong);
        define(NumberOct);
        //
        define(Literal, fg("#ae81ff"));
        define(LiteralDate, fg("#e6db74"));
        //
        define(String, fg("#e6db74"));
        define(StringBacktick);
        define(StringChar);
        define(StringDoc);
        define(StringDouble);
        define(StringEscape);
        define(StringHeredoc);
        define(StringInterpol);
        define(StringOther);
        define(StringRegex);
        define(StringSingle);
        define(StringSymbol);
        //
        define(Generic);
        define(GenericDeleted, fg("#f92672"));
        define(GenericEmph, style().italic());
        define(GenericError);
        define(GenericHeading);
        define(GenericInserted, fg("#a6e22e"));
        define(GenericOutput);
        define(GenericPrompt);
        define(GenericStrong, style().bold());
        define(GenericSubheading, fg("#75715e"));
        define(GenericTraceback);
    }
}
