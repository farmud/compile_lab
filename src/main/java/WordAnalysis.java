import entity.Token;
import entity.TokenType;
import util.FileUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

    public class WordAnalysis {



        private String input;       // 输入字符串
        private int position;       // 当前字符的位置
        private Map<String, TokenType> keywords;        // 关键字

        public WordAnalysis(String input) {
            this.input = input;
            this.position = 0;
            this.keywords = new HashMap<>();
            keywords.put("if", TokenType.IF);
            keywords.put("else", TokenType.ELSE);
            keywords.put("while", TokenType.WHILE);
            keywords.put("return", TokenType.RETURN);
            keywords.put("int", TokenType.TYPE);
            keywords.put("double", TokenType.TYPE);
            keywords.put("char", TokenType.TYPE);
            keywords.put("void", TokenType.TYPE);
            keywords.put("function", TokenType.FUNCTION);
        }

        // 获取下一个 token
        public Token getNextToken() {
            while (position < input.length()) {
                char currentChar = input.charAt(position);

                // 跳过空白字符
                if (Character.isWhitespace(currentChar)) {
                    position++;
                    continue;
                }

                // 第一个字符是字母：可能是关键字或者标识符
                if (Character.isLetter(currentChar)) {
                    return processIdentifier();
                }

                // 第一个字符是数字：可能是整数
                if (Character.isDigit(currentChar)) {
                    return processNumber();
                }

                // 第一个字符是双引号：可能是字符串
                if (currentChar == '"') {
                    return processString();
                }

                // 第一个字符是运算符
                if (isOperator(currentChar)) {
                    return processOperator();
                }

                // 第一个字符是分隔符
                if (isSeparator(currentChar)) {
                    return processSeparator();
                }

                // 无法识别的字符
                throw new RuntimeException("Invalid character: " + currentChar);
            }

            // 退出循环，说明已经到达输入的末尾
            return new Token(TokenType.EOF, "");
        }

        // 处理标识符和关键字
        private Token processIdentifier() {

            // 使用 StringBuilder 保存标识符，提高效率
            StringBuilder sb = new StringBuilder();

            // 读取所有字母和数字以及下划线
            while (position < input.length() && (Character.isLetterOrDigit(input.charAt(position)) || input.charAt(position) == '_')) {
                sb.append(input.charAt(position));
                position++;
            }
            String identifier = sb.toString();

            // 如果是关键字，返回关键字
            if (keywords.containsKey(identifier)) {
                return new Token(keywords.get(identifier), identifier);
            }

            // 否则返回标识符
            return new Token(TokenType.IDENTIFIER, identifier);
        }

        // 处理数字
        private Token processNumber() {
            StringBuilder sb = new StringBuilder();
            // 读取整数部分
            while (position < input.length() && Character.isDigit(input.charAt(position))) {
                sb.append(input.charAt(position));
                position++;
            }
            return new Token(TokenType.INTEGER, sb.toString());
        }


        // 处理字符串
        private Token processString() {
            StringBuilder sb = new StringBuilder();
            position++;
            while (position < input.length() && input.charAt(position) != '"') {
                sb.append(input.charAt(position));
                position++;
            }
            position++;    // 跳过最后一个双引号
            return new Token(TokenType.STRING, sb.toString());
        }


        // 处理运算符
        private Token processOperator() {
            char currentChar = input.charAt(position);

            // 如果是等号，判断下一个字符是否也是等号，如果是，返回等于号，否则返回赋值号
            if (currentChar == '=') {
                if (position + 1 < input.length() && input.charAt(position + 1) == '=') {
                    position += 2;
                    return new Token(TokenType.EQUAL, "==");
                }
                position++;
                return new Token(TokenType.ASSIGN, "=");
            }

            // 如果是感叹号，判断下一个字符是否也是等号，如果是，返回不等号，否则抛出异常
            if (currentChar == '!') {
                if (position + 1 < input.length() && input.charAt(position + 1) == '=') {
                    position += 2;
                    return new Token(TokenType.NOT_EQUAL, "!=");
                }
                throw new RuntimeException("Invalid operator: " + currentChar);
            }

            // 如果是小于号，判断下一个字符是否也是等号，如果是，返回小于等于号，否则返回小于号
            if (currentChar == '<') {
                if (position + 1 < input.length() && input.charAt(position + 1) == '=') {
                    position += 2;
                    return new Token(TokenType.LESS_EQUAL, "<=");
                }
                position++;
                return new Token(TokenType.LESS, "<");
            }

            // 如果是大于号，判断下一个字符是否也是等号，如果是，返回大于等于号，否则返回大于号
            if (currentChar == '>') {
                if (position + 1 < input.length() && input.charAt(position + 1) == '=') {
                    position += 2;
                    return new Token(TokenType.GREATER_EQUAL, ">=");
                }
                position++;
                return new Token(TokenType.GREATER, ">");
            }


//            if (currentChar == '+') {
//                position++;
//                return new Token(TokenType.PLUS, "+");
//            }
//
//
//            if (currentChar == '-') {
//                position++;
//                return new Token(TokenType.MINUS, "-");
//            }
//
//
//            if (currentChar == '*') {
//                position++;
//                return new Token(TokenType.MULTIPLY, "*");
//            }
//
//
//            if (currentChar == '/') {
//                position++;
//                return new Token(TokenType.DIVIDE, "/");
//            }

            // 无法识别的运算符
            throw new RuntimeException("Invalid operator: " + currentChar);
        }


        // 处理分隔符
        private Token processSeparator() {
            char currentChar = input.charAt(position);
            if(currentChar == ','){
                position++;
                return new Token(TokenType.COMMA, ",");
            }
            if (currentChar == '(') {
                position++;
                return new Token(TokenType.LEFT_PAREN, "(");
            }
            if (currentChar == ')') {
                position++;
                return new Token(TokenType.RIGHT_PAREN, ")");
            }
            if (currentChar == '{') {
                position++;
                return new Token(TokenType.LEFT_BRACE, "{");
            }
            if (currentChar == '}') {
                position++;
                return new Token(TokenType.RIGHT_BRACE, "}");
            }
            if (currentChar == ';') {
                position++;
                return new Token(TokenType.SEMICOLON, ";");
            }


            throw new RuntimeException("Invalid separator: " + currentChar);
        }

        private boolean isOperator(char c) {
            return "+-*/=!<>".indexOf(c) != -1;
        }

        private boolean isSeparator(char c) {
            return "(){};,".indexOf(c) != -1;
        }

        public static void main(String[] args) {
            StringBuffer buffer = new StringBuffer();
            try {
                FileUtil.readToBuffer(buffer, "src/main/resources/function.txt");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            WordAnalysis lexer = new WordAnalysis(buffer.toString());
            Token token;
            System.out.println("==========================================");
            System.out.println("词法分析结果：");
            do {
                token = lexer.getNextToken();
                System.out.println(token);
            } while (token.getType() != TokenType.EOF);

            System.out.println("词法分析结束！");
            System.out.println("==========================================");
        }

        }


