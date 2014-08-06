package gutenberg.pygments;

/**
 * @author <a href="http://twittercom/aloyer">@aloyer</a>
 */
@SuppressWarnings("UnusedDeclaration")
public enum Token {
    Token(""),
    //
    Text(""),
    Whitespace("w"),
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
    String("s"),
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
    Number("m"),
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
    Generic("g"),
    GenericDeleted(Generic, "gd"),
    GenericEmph(Generic, "ge"),
    GenericError(Generic, "gr"),
    GenericHeading(Generic, "gh"),
    GenericInserted(Generic, "gi"),
    GenericOutput(Generic, "go"),
    GenericPrompt(Generic, "gp"),
    GenericStrong(Generic, "gs"),
    GenericSubheading(Generic, "gu"),
    GenericTraceback(Generic, "gt");

    private final Token parentToken;
    private final String shortName;

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
                    return Text;
            }
        }
        return parentToken;
    }

    public String shortName() {
        return shortName;
    }
}
