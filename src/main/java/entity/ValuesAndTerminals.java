package entity;

// 存储所有的文法符号,共43种
public enum ValuesAndTerminals {

    // 以下为非终结符
    init,
    program,
    declaration,

    function,
    declaration_list,
    identifier,
    statement,
    assignment,
    expression,
    judge,
    loop,
    selection,

//
//
//    // 以下为终结符
    IDENTIFIER,     // 标识符
    INTEGER,        // 整型
    STRING,         // 字符串
    IF,             // if
    ELSE,           // else
    WHILE,          // while
    RETURN,         // return
    TYPE,           // int, float, double, char, void
    EQUAL,          // ==
    ASSIGN,         // =
    NOT_EQUAL,      // !=
    LESS_EQUAL,     // <=
    LESS,           // <
    GREATER_EQUAL,  // >=
    GREATER,        // >
    PLUS,           // +
    MINUS,          // -
    MULTIPLY,       // *
    DIVIDE,         // /
    LEFT_PAREN,     // (
    RIGHT_PAREN,    // )
    LEFT_BRACE,     // {
    RIGHT_BRACE,    // }
    SEMICOLON,      // ;
    COMMA,          // ,
    FUNCTION,
    EOF;           // end of file
    public static boolean isTerminal(ValuesAndTerminals valuesAndTerminals){
        return valuesAndTerminals.compareTo(IDENTIFIER) >= 0;
    }
    public static boolean isValue(ValuesAndTerminals valuesAndTerminals){
        return valuesAndTerminals.compareTo(IDENTIFIER) < 0;
    }



//    Sp,
//    S,
//    B,
//
//    a,
//    b,
//    eof;
//
//    public static boolean isTerminal(ValuesAndTerminals valuesAndTerminals){
//        return valuesAndTerminals.compareTo(a) >= 0;
//    }
//    public static boolean isValue(ValuesAndTerminals valuesAndTerminals){
//        return valuesAndTerminals.compareTo(a) < 0;
//    }
}
