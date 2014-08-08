package gutenberg.pygments;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.text.StrBuilder;

import java.util.Collections;
import java.util.List;

/**
 * @author <a href="http://twittercom/aloyer">@aloyer</a>
 */
@SuppressWarnings("UnusedDeclaration")
public enum Token {
    Token(""),
    //
    Text(""),
    Whitespace(Text, "w"),
    Escape("esc"),
    Error("err"),
    Other("x"),
    //
    Keyword("k"),
    KeywordConstant(Keyword, "kc"),
    KeywordDeclaration(Keyword, "kd"),
    KeywordNamespace(Keyword, "kn"),
    KeywordPseudo(Keyword, "kp"),
    KeywordReserved(Keyword, "kr"),
    KeywordType(Keyword, "kt"),
    //
    Name("n"),
    NameAttribute(Name, "na"),
    NameBuiltin(Name, "nb"),
    NameBuiltinPseudo(Name, "bp"),
    NameClass(Name, "nc"),
    NameConstant(Name, "no"),
    NameDecorator(Name, "nd"),
    NameEntity(Name, "ni"),
    NameException(Name, "ne"),
    NameFunction(Name, "nf"),
    NameProperty(Name, "py"),
    NameLabel(Name, "nl"),
    NameNamespace(Name, "nn"),
    NameOther(Name, "nx"),
    NameTag(Name, "nt"),
    NameVariable(Name, "nv"),
    NameVariableClass(NameVariable, "vc"),
    NameVariableGlobal(NameVariable, "vg"),
    NameVariableInstance(NameVariable, "vi"),
    //
    Literal("l"),
    LiteralDate(Literal, "ld"),
    //
    String(Literal, "s"),
    StringBacktick(String, "sb"),
    StringChar(String, "sc"),
    StringDoc(String, "sd"),
    StringDouble(String, "s2"),
    StringEscape(String, "se"),
    StringHeredoc(String, "sh"),
    StringInterpol(String, "si"),
    StringOther(String, "sx"),
    StringRegex(String, "sr"),
    StringSingle(String, "s1"),
    StringSymbol(String, "ss"),
    //
    Number(Literal, "m"),
    NumberBin(Number, "mb"),
    NumberFloat(Number, "mf"),
    NumberHex(Number, "mh"),
    NumberInteger(Number, "mi"),
    NumberIntegerLong(NumberInteger, "il"),
    NumberOct(Number, "mo"),
    //
    Operator("o"),
    OperatorWord(Operator, "ow"),
    //
    Punctuation("p"),
    //
    Comment("c"),
    CommentMultiline(Comment, "cm"),
    CommentPreproc(Comment, "cp"),
    CommentSingle(Comment, "c1"),
    CommentSpecial(Comment, "cs"),
    //

    /**
     * Generic tokens are for special lexers like the DiffLexer that doesn’t
     * really highlight a programming language but a patch file.
     * A generic, unstyled token. Normally you don’t use this token type.
     */
    Generic("g"),

    /**
     * Marks the token value as deleted.
     *
     * @see #Generic
     */
    GenericDeleted(Generic, "gd"),

    /**
     * Marks the token value as emphasized.
     *
     * @see #Generic
     */
    GenericEmph(Generic, "ge"),
    /**
     * Marks the token value as an error message.
     *
     * @see #Generic
     */
    GenericError(Generic, "gr"),
    /**
     * Marks the token value as headline.
     *
     * @see #Generic
     */
    GenericHeading(Generic, "gh"),
    /**
     * Marks the token value as inserted.
     *
     * @see #Generic
     */
    GenericInserted(Generic, "gi"),
    /**
     * Marks the token value as program output (e.g. for python cli lexer).
     *
     * @see #Generic
     */
    GenericOutput(Generic, "go"),
    /**
     * Marks the token value as command prompt (e.g. bash lexer).
     *
     * @see #Generic
     */
    GenericPrompt(Generic, "gp"),
    /**
     * Marks the token value as bold (e.g. for rst lexer).
     *
     * @see #Generic
     */
    GenericStrong(Generic, "gs"),
    /**
     * Marks the token value as subheadline.
     *
     * @see #Generic
     */
    GenericSubheading(Generic, "gu"),
    /**
     * Marks the token value as a part of an error traceback.
     *
     * @see #Generic
     */
    GenericTraceback(Generic, "gt");

    private final Token parentToken;
    private final String shortName;
    private String repr;

    private Token(String shortName) {
        this(null, shortName);
    }

    private Token(Token parentToken, String shortName) {
        this.parentToken = parentToken;
        this.shortName = shortName;
    }

    public Token parent() {
        if (parentToken == null) {
            switch (this) {
                case Keyword:
                case Name:
                case Literal:
                case String:
                case Number:
                case Operator:
                case Punctuation:
                case Comment:
                case Generic:
                    return Token;
            }
        }
        return parentToken;
    }

    public String shortName() {
        return shortName;
    }

    public static Optional<Token> findTokenByRepr(String repr) {
        for (Token token : values()) {
            if (token.repr().equals(repr))
                return Optional.of(token);
        }
        return Optional.absent();
    }

    public List<Token> path() {
        List<Token> chain = Lists.newArrayListWithCapacity(3);
        Token token = this;
        while (token != null) {
            chain.add(token);
            token = token.parent();
        }
        Collections.reverse(chain);
        return chain;
    }

    public String repr() {
        if (repr == null) {
            StrBuilder b = new StrBuilder();
            Token token = this;
            while (token != null) {
                if (!b.startsWith(token.name())) {
                    b.insert(0, token.name());
                }
                token = token.parent();
            }
            repr = b.toString();
        }
        return repr;
    }
}
