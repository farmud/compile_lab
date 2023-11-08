import entity.*;
import util.FileUtil;

import java.io.IOException;
import java.util.Deque;
import java.util.LinkedList;

// 语法分析
public class GrammarAnalysis {


    private Deque<Token> tokenStack ;       // 剩余的输入符号栈
    private Deque<Integer> stateStack;      // 状态栈

    private Deque<ValuesAndTerminals> valueStack;     // 已经识别出的符号栈


    public GrammarAnalysis() {
        tokenStack = new LinkedList<Token>();
        stateStack = new LinkedList<Integer>();
        valueStack = new LinkedList<ValuesAndTerminals>();

        stateStack.push(0);
    }



    // 调用词法分析器，将词法分析结果存入tokenStack
    public Deque<Token> readTokenStack(String path){
        // @param path: 词法分析结果的路径

        // 用于反序栈
        Deque<Token> temp = new LinkedList<Token>();
        // 将文件载入内存
        StringBuffer buffer = new StringBuffer();
        try {
            FileUtil.readToBuffer(buffer, path);
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
            temp.push(token);

        } while (token.getType() != TokenType.EOF);

        System.out.println("词法分析结束！");
        System.out.println("==========================================");
        // 反序
        while(!temp.isEmpty()){
            tokenStack.push(temp.pop());
        }
        return tokenStack;
    }

    // 语法分析
    public void SLR1Analysis(){
        GrammarData grammarData = new GrammarData();
        ActionData [][] actionTable = grammarData.getActionTable();
        int [][] gotoTable = grammarData.getGotoTable();

        System.out.println("语法分析的规约过程：");

//        // 树节点的栈，用于结果展示
//        Deque<TreeNode> treeNodeStack = new LinkedList<TreeNode>();
        // 保存打印规约结果的栈
        Deque<ValuesAndTerminals> printStack = new LinkedList<ValuesAndTerminals>();

        // 剩余符号栈非空时
        while(!tokenStack.isEmpty()){

            TokenType topTerminal = tokenStack.peek().getType();        // 剩余符号栈的栈顶符号
            int topState = stateStack.peek();                           // 状态栈的栈顶状态码





            ActionData presentAction = actionTable[topState][topTerminal.ordinal()];   // 当前动作

            if(presentAction.getAction().equals(ActionData.Action.STATE)){
                // 如果当前动作是移进
                valueStack.push(ValuesAndTerminals.valueOf(topTerminal.name()));
                stateStack.push(presentAction.getNumber());
                tokenStack.pop();
            }

            else if(presentAction.getAction().equals(ActionData.Action.RETURN)){
                // 如果当前动作是归约
                GrammarRule grammarRule = grammarData.getGrammarRuleById(presentAction.getNumber());   // 当前归约的语法规则
                int rightLength = grammarRule.getRight().size();                                        // 当前归约的语法规则右部的长度

                // 将栈顶的rightLength个符号出栈
                for(int i = 0; i < rightLength; i++){
                    stateStack.pop();
                    ValuesAndTerminals top =  valueStack.pop();

//                    TreeNode node = new TreeNode(top.name());
//                    treeNodeStack.push(node);
                    printStack.push(top);

                }
//                // 这些同时出栈的符号互相为兄弟
//
//                TreeNode firstChild = treeNodeStack.peek();
//                for(int i = 0; i < rightLength; i++){
//                    TreeNode node = treeNodeStack.pop();
//                    node.setNextBro(treeNodeStack.peek());
//                }





                // 将归约后的非终结符入栈
                stateStack.push(gotoTable[stateStack.peek()][grammarRule.getLeft().ordinal()]);

                ValuesAndTerminals fatherValue = ValuesAndTerminals.valueOf(grammarRule.getLeft().name());
                valueStack.push(fatherValue);
                System.out.print("将");
                for(int i = 0; i < rightLength; i++){
                    System.out.print(printStack.pop().name()+" ");
                }
                System.out.println("归约为 "+fatherValue.name());

//                TreeNode fatherNode = new TreeNode(fatherValue.name());
//                fatherNode.setFirstChild(firstChild);
//                treeNodeStack.push(fatherNode);


            }

            else if(presentAction.getAction().equals(ActionData.Action.END)){
                // 如果当前动作是接受
                System.out.println("语法分析成功！");
                System.out.println("==========================================");
                break;
            }

            else throw new RuntimeException("语法分析失败！");


        }

    }

}
