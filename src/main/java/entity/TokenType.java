package entity;

// 储存所有的token类型（终结符）,共26个
public enum TokenType {

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
    LEFT_PAREN,     // (
    RIGHT_PAREN,    // )
    LEFT_BRACE,     // {
    RIGHT_BRACE,    // }
    SEMICOLON,      // ;
    COMMA,          // ,
    FUNCTION,
    EOF            // end of file

//    a,
//    b,
//    EOF
}