import entity.*;
import org.junit.Test;

import java.util.*;
import java.util.zip.GZIPOutputStream;

public class GrammarData {

    // 测试action表和goto表
//    public static void main(String[] args) {
//        GrammarData grammarData = new GrammarData();
//        ActionData[][] actionTable = grammarData.getActionTable();
//        int[][] gotoTable = grammarData.getGotoTable();
//        for (int i = 0; i < actionTable.length; i++) {
//            for (int j = 0; j < actionTable[i].length; j++) {
//                System.out.print(actionTable[i][j].getAction().name() + " " + actionTable[i][j].getNumber() + " ");
//            }
//            System.out.println();
//        }
//        System.out.println("-------------------------------------------------");
//        for (int i = 0; i < gotoTable.length; i++) {
//            for (int j = 0; j < gotoTable[i].length; j++) {
//                System.out.print(gotoTable[i][j] + " ");
//            }
//            System.out.println();
//        }
//    }

    private GrammarRule[] grammarRules;               // 存储所有的文法规则
    private Map<ValueType, List<TokenType>> followSet;   // map的key为变量，value为变量对应的follow集合

    private List<ProgramSet> programSetList;          // 存储项目集规范族


    ActionData[][] actionTable;        // 存储action表,行为状态数，列为终结符在tokenType中的位置
    int[][] gotoTable;         // 存储goto表，行为状态数，列为非终结符在valueType中的位置

    public GrammarData() {
        initGrammar();
        initFollowSet();
        initSLR1Table();
    }




    private void initGrammar() {
        // 初始化文法, 用于构造闭包,左边一定是非终结符, 右边是终结符或者非终结符
        grammarRules = new GrammarRule[24];

        // 0号产生式
        grammarRules[0] = new GrammarRule(ValueType.init, new ArrayList<>(List.of(ValuesAndTerminals.program)), 0);

        grammarRules[1] = new GrammarRule(ValueType.program, new ArrayList<>(List.of(ValuesAndTerminals.declaration)), 0);
        grammarRules[2] = new GrammarRule(ValueType.program, new ArrayList<>(List.of(ValuesAndTerminals.assignment)), 0);
        grammarRules[3] = new GrammarRule(ValueType.program, new ArrayList<>(List.of(ValuesAndTerminals.statement)), 0);
        grammarRules[4] = new GrammarRule(ValueType.program, new ArrayList<>(List.of(ValuesAndTerminals.function)), 0);
        grammarRules[5] = new GrammarRule(ValueType.declaration, new ArrayList<>(List.of(ValuesAndTerminals.TYPE, ValuesAndTerminals.identifier)), 0);
        grammarRules[6] = new GrammarRule(ValueType.identifier, new ArrayList<>(List.of(ValuesAndTerminals.IDENTIFIER)), 0);
        grammarRules[7] = new GrammarRule(ValueType.statement, new ArrayList<>(List.of(ValuesAndTerminals.assignment)), 0);
        grammarRules[8] = new GrammarRule(ValueType.statement, new ArrayList<>(List.of(ValuesAndTerminals.loop)), 0);
        grammarRules[9] = new GrammarRule(ValueType.statement, new ArrayList<>(List.of(ValuesAndTerminals.selection)), 0);
        grammarRules[10] = new GrammarRule(ValueType.assignment, new ArrayList<>(List.of(ValuesAndTerminals.identifier, ValuesAndTerminals.ASSIGN, ValuesAndTerminals.INTEGER)), 0);
        grammarRules[23] = new GrammarRule(ValueType.assignment, new ArrayList<>(List.of(ValuesAndTerminals.identifier, ValuesAndTerminals.ASSIGN, ValuesAndTerminals.identifier)), 0);
        grammarRules[11] = new GrammarRule(ValueType.judge, new ArrayList<>(List.of(ValuesAndTerminals.identifier, ValuesAndTerminals.EQUAL, ValuesAndTerminals.identifier)), 0);
        grammarRules[12] = new GrammarRule(ValueType.judge, new ArrayList<>(List.of(ValuesAndTerminals.identifier, ValuesAndTerminals.NOT_EQUAL, ValuesAndTerminals.identifier)), 0);
        grammarRules[13] = new GrammarRule(ValueType.judge, new ArrayList<>(List.of(ValuesAndTerminals.identifier, ValuesAndTerminals.LESS_EQUAL, ValuesAndTerminals.identifier)), 0);
        grammarRules[14] = new GrammarRule(ValueType.judge, new ArrayList<>(List.of(ValuesAndTerminals.identifier, ValuesAndTerminals.LESS, ValuesAndTerminals.identifier)), 0);
        grammarRules[15] = new GrammarRule(ValueType.judge, new ArrayList<>(List.of(ValuesAndTerminals.identifier, ValuesAndTerminals.GREATER_EQUAL, ValuesAndTerminals.identifier)), 0);
        grammarRules[16] = new GrammarRule(ValueType.judge, new ArrayList<>(List.of(ValuesAndTerminals.identifier, ValuesAndTerminals.GREATER, ValuesAndTerminals.identifier)), 0);
        grammarRules[17] = new GrammarRule(ValueType.loop, new ArrayList<>(List.of(ValuesAndTerminals.WHILE, ValuesAndTerminals.LEFT_PAREN, ValuesAndTerminals.judge, ValuesAndTerminals.RIGHT_PAREN, ValuesAndTerminals.statement)), 0);
        grammarRules[18] = new GrammarRule(ValueType.selection, new ArrayList<>(List.of(ValuesAndTerminals.IF, ValuesAndTerminals.LEFT_PAREN, ValuesAndTerminals.judge, ValuesAndTerminals.RIGHT_PAREN, ValuesAndTerminals.statement)), 0);
        grammarRules[19] = new GrammarRule(ValueType.selection, new ArrayList<>(List.of(ValuesAndTerminals.IF, ValuesAndTerminals.LEFT_PAREN, ValuesAndTerminals.judge, ValuesAndTerminals.RIGHT_PAREN, ValuesAndTerminals.statement, ValuesAndTerminals.ELSE, ValuesAndTerminals.statement)), 0);
        grammarRules[20] = new GrammarRule(ValueType.function, new ArrayList<>(List.of(ValuesAndTerminals.FUNCTION, ValuesAndTerminals.TYPE, ValuesAndTerminals.identifier,
                ValuesAndTerminals.LEFT_PAREN, ValuesAndTerminals.declaration_list, ValuesAndTerminals.RIGHT_PAREN, ValuesAndTerminals.LEFT_BRACE,
                ValuesAndTerminals.statement, ValuesAndTerminals.RIGHT_BRACE)), 0);
        grammarRules[21] = new GrammarRule(ValueType.declaration_list, new ArrayList<>(List.of(ValuesAndTerminals.declaration, ValuesAndTerminals.COMMA,
                ValuesAndTerminals.declaration_list)), 0);
        grammarRules[22] = new GrammarRule(ValueType.declaration_list, new ArrayList<>(List.of(ValuesAndTerminals.declaration)), 0);

    }



    private void initFollowSet() {
        followSet = new HashMap<>();
        followSet.put(ValueType.init, new ArrayList<TokenType>(List.of(TokenType.EOF)));
        followSet.put(ValueType.program, new ArrayList<TokenType>(List.of(TokenType.EOF)));
        followSet.put(ValueType.declaration, new ArrayList<TokenType>(List.of(TokenType.EOF, TokenType.COMMA,TokenType.RIGHT_PAREN)));
        followSet.put(ValueType.identifier, new ArrayList<TokenType>(List.of(TokenType.EOF, TokenType.ASSIGN, TokenType.LEFT_PAREN,TokenType.RIGHT_PAREN, TokenType.COMMA,TokenType.EQUAL,
                TokenType.NOT_EQUAL,TokenType.LESS_EQUAL,TokenType.GREATER_EQUAL,TokenType.GREATER,TokenType.LESS, TokenType.ELSE)));
        followSet.put(ValueType.statement, new ArrayList<TokenType>(List.of(TokenType.EOF,TokenType.RIGHT_BRACE, TokenType.ELSE)));
        followSet.put(ValueType.assignment, new ArrayList<TokenType>(List.of(TokenType.EOF, TokenType.ELSE, TokenType.RIGHT_BRACE)));
        followSet.put(ValueType.judge, new ArrayList<TokenType>(List.of(TokenType.EOF, TokenType.RIGHT_PAREN)));
        followSet.put(ValueType.loop, new ArrayList<TokenType>(List.of(TokenType.EOF, TokenType.ELSE, TokenType.RIGHT_BRACE)));
        followSet.put(ValueType.selection, new ArrayList<TokenType>(List.of(TokenType.EOF, TokenType.ELSE, TokenType.RIGHT_BRACE)));
        followSet.put(ValueType.function, new ArrayList<TokenType>(List.of(TokenType.EOF)));
        followSet.put(ValueType.declaration_list, new ArrayList<TokenType>(List.of(TokenType.RIGHT_PAREN)));
    }


    // 获得SLR(1)分析表，即goto表和action表
    private void initSLR1Table() {
        this.programSetList = getProgramSetList();                  // 获取项目集规范族

        setProgramSetId();                                          // 给项目集规范族中的项目集编号
        int n = programSetList.size();                              // 项目集规范族的大小

        actionTable = new ActionData[n][TokenType.values().length];
        // 初始化action表
        for (int i = 0; i < n; i++) {
            for (TokenType tokenType : TokenType.values()) {
                // 默认状态为ERROR，默认值为-1
                actionTable[i][tokenType.ordinal()] = new ActionData();
            }
        }
        gotoTable = new int[n][ValueType.values().length];
        // 初始化goto表
        for (int i = 0; i < n; i++) {
            for (ValueType valueType : ValueType.values()) {
                // 默认值为-1
                gotoTable[i][valueType.ordinal()] = -1;
            }
        }


        for (int i = 0; i < n; i++) {
            // 遍历项目集规范族中的每一个项目集,针对项目集中的每一个文法规则:
            for (GrammarRule grammarRule : programSetList.get(i).getGrammarRules()) {

                // 如果点在最后
                if (grammarRule.getIndex() == grammarRule.getRight().size()) {
                    if (grammarRule.getLeft().equals(ValueType.init)) {
                        // 如果该文法规则是0号文法规则,就把#加入action表
                        actionTable[i][TokenType.EOF.ordinal()] = new ActionData(ActionData.Action.END, 0);
                    } else {
                        // 否则进行规约操作
                        int j = getJ(grammarRule);      // j为该文法规则在文法规则数组中的位置
//                        ValuesAndTerminals terminal = grammarRule.getRight().get(grammarRule.getIndex() - 1);   // 获取点前面的终结符
//                        if(ValuesAndTerminals.isValue(terminal))
//                            continue;

                        // 如果follow集中包含该终结符，就把规约操作加入action表



                        // SLR(1)文法
//                        if (j != -1 && terminalInFollowSet(grammarRule.getLeft(), TokenType.valueOf(terminal.name()))) {
//
//                            System.out.println("规约了");
//                            actionTable[i][TokenType.valueOf( terminal.name()).ordinal()] = new ActionData(ActionData.Action.RETURN, j);
//                        }


                        // SLR(1)文法
                        //LR(0)文法
                        if(j!=-1){
                            for( int t = 0 ; t<actionTable[i].length;t++) {
                                // 如果follow集中包含该终结符，就把规约操作加入action表
                                if (terminalInFollowSet(grammarRule.getLeft(), TokenType.values()[t])) {
                                    actionTable[i][t] = new ActionData(ActionData.Action.RETURN, j);
                                }
                            }

                        }
                    }
                } else {


                    // 如果点不在最后，就进行移进操作


                    ValuesAndTerminals afterPoint = grammarRule.getRecentValueAndTerminal(); // 获取点后面的文法符号
                    List<GrammarRule> myGo = Go(programSetList.get(i).getGrammarRules(), afterPoint);
                    int j = programSetList.get(i).getIndex(programSetList, myGo);   // 获取转移后的项目集在项目集规范族中的位置j

                    // 如果点后面是终结符,就把该终结符加入action表
                    if (ValuesAndTerminals.isTerminal(afterPoint) && j != -1) {
                        TokenType tokenType = TokenType.valueOf(afterPoint.name());
                        actionTable[i][tokenType.ordinal()] = new ActionData(ActionData.Action.STATE, j);
                    }

                    // 如果点后面是变量,就把该变量加入goto表
                    else if (ValuesAndTerminals.isValue(afterPoint) && j != -1) {
                        ValueType valueType = ValueType.valueOf(afterPoint.name());
                        gotoTable[i][valueType.ordinal()] = j;
                    }

                }
            }
        }
    }





    // 给项目集规范族中的项目集编号，除了0号项目集导出的项目集
    private void setProgramSetId(){
        for(int i = 0 ; i < programSetList.size() ; i++){
            if(programSetList.get(i).getId()<0)
                programSetList.get(i).setId(i);
        }

    }


     //获取项目集规范族
    private List<ProgramSet> getProgramSetList() {
        List<ProgramSet> programSetList = new ArrayList<>();

        // 初始化第一个项目集
        ProgramSet programSet = new ProgramSet();

        GrammarRule gr = new GrammarRule();
        gr.setLeft(ValueType.init);
        gr.setRight(new ArrayList<>(List.of(ValuesAndTerminals.program)));
        programSet.setGrammarRule(getClosure(new ArrayList<>(List.of(gr))));
        programSet.setId(0);
        programSetList.add(programSet);


        // 从第一个项目集开始，遍历所有的项目集，直到没有新的项目集产生


        // !!!!!这里困了好久，主要目的是为了规约中与行号对应
        // 获得第一个项目集I0，然后获得与他直接相连的项目集编号
        List <GrammarRule> I0 =  getClosure(programSet.getGrammarRules());

        for(int i = 1 ; i <= I0.size() ; i++){
            GrammarRule gmr = I0.get(i-1);

            GrammarRule first = new GrammarRule(gmr.getLeft(), gmr.getRight(), gmr.getIndex()+1);
            List<GrammarRule> closure = getClosure(new ArrayList<>(List.of(first)));

            ProgramSet programSet1 = new ProgramSet(i,closure);

            programSetList.add(programSet1);


        }







        // 这里使用到了辅助函数进行递归,返回生成的项目集规范族
        return getProgramSetListHelper(programSetList);



    }




     //获取项目集规范族的辅助函数,用于实现递归
     private List<ProgramSet> getProgramSetListHelper(List<ProgramSet> programSetList) {

        // 用于临时存储项目集规范族
        List<ProgramSet> temp = new ArrayList<>();


        for (ProgramSet programSet : programSetList) {
            //对每个项目集遍历：


            for (ValuesAndTerminals valuesAndTerminals : ValuesAndTerminals.values()) {
                // 对每一个文法符号遍历

                List<GrammarRule> grammarRules = Go(programSet.getGrammarRules(), valuesAndTerminals);
                if (!grammarRules.isEmpty() && !inSet(programSetList, grammarRules) && !inSet(temp, grammarRules)) {

                    // 如果Go集不为空，且Go集不在项目集规范族中，就把该项目集加入项目集规范族

                    ProgramSet newProgramSet = new ProgramSet(grammarRules);
                    temp.add(newProgramSet);
                }
            }

        }
        if (temp.isEmpty()) {
            // 如果项目集规范族没有增加新的项目集，就返回项目集规范族
            return programSetList;
        } else {
            programSetList.addAll(temp);

            // 如果项目集规范族增加了新的项目集，就递归调用
            return getProgramSetListHelper(programSetList);
        }
    }

    //判断是否在项目集规范族中,为Helper函数的辅助函数
    private boolean inSet(List<ProgramSet> programSetList, List<GrammarRule> grammarRules){
        for (ProgramSet programSet : programSetList) {
            if (GrammarRule.judgeEqualPro(grammarRules, programSet.getGrammarRules())) {
                return true;
            }
        }
        return false;
    }



    //输入一个项目集，返回该项目集的闭包
    private List<GrammarRule> getClosure(List<GrammarRule> grammarRules) {
        // @param grammarRules 项目集，这里只传入了项目集中的文法规则


        // 用于临时存储闭包
        List<GrammarRule> temp = new ArrayList<>();

        for (GrammarRule grammarRule : grammarRules) {
            // 遍历项目集中的每一个文法规则
            if (grammarRule.getIndex() < grammarRule.getRight().size()) {
                // 如果该文法规则的点不在最后，就把点后面的文法符号加入闭包
                ValuesAndTerminals valuesAndTerminals = grammarRule.getRecentValueAndTerminal();

                if (ValuesAndTerminals.isValue(valuesAndTerminals)) {
                    // 如果点后面的文法符号是非终结符，就把该非终结符的所有产生式加入闭包

                    for (GrammarRule gr : this.grammarRules) {
                        if (gr.getLeft().name().equals(valuesAndTerminals.name())) {
                            if(gr.haveNotExistedInPro(grammarRules) && gr.haveNotExistedInPro(temp))
                                temp.add(gr);
                        }
                    }
                }
            }
        }
        if (temp.isEmpty()) {
            // 如果闭包没有增加新的文法规则，就返回闭包
            return grammarRules;
        } else {
            // 如果闭包增加了新的文法规则，就递归调用

            grammarRules.addAll(temp);
            return getClosure(grammarRules);
        }
    }


    // 确定某一状态后遇到某个文法符号应该转移到哪个状态
    private List<GrammarRule> Go(List<GrammarRule> grammarRules, ValuesAndTerminals valuesAndTerminals) {
        //@param grammarRules 项目集，这里只传入了项目集中的文法规则
        //@param valuesAndTerminals 文法符号


        List<GrammarRule> res = new ArrayList<>();

        for (GrammarRule grammarRule : grammarRules) {
            // 遍历项目集中的每一个文法规则

            if (grammarRule.getIndex() < grammarRule.getRight().size()) {
                // 如果该文法规则的点不在最后，就把点后面的文法符号加入闭包

                if (grammarRule.getRecentValueAndTerminal().equals(valuesAndTerminals)) {
                    // 如果该文法规则的点后面的文法符号和输入的文法符号相同，就把该文法规则的点向后移动一位

                    GrammarRule temp = new GrammarRule(grammarRule.getLeft(), grammarRule.getRight(), grammarRule.getIndex()+1);
                    res.add(temp);
                }
            }
        }
        return getClosure(res);
    }

    private boolean terminalInFollowSet(ValueType valueType, TokenType tokenType) {
        // @param valueType 变量
        // @param tokenType 终结符


        List<TokenType> grammarRules = this.getFollowSet().get(valueType);
        if(grammarRules == null){
            return false;
        }
        for(TokenType grammarRule : grammarRules){
            if(grammarRule.name().equals(tokenType.name())){
                return true;
            }
        }
        return false;
    }



    private int getJ(GrammarRule grammarRule) {
        for (int j = 0; j < this.grammarRules.length; j++) {
            if (this.grammarRules[j].judgeEqualSimple(grammarRule)) {
                return j;

            }
        }
        return -1;
    }

    public GrammarRule[] getGrammarRules() {
        return grammarRules;
    }

    public Map<ValueType, List<TokenType>> getFollowSet() {
        return followSet;
    }

    public ActionData[][] getActionTable() {
        return actionTable;
    }

    public int[][] getGotoTable() {
        return gotoTable;
    }

    public GrammarRule getGrammarRuleById(int number) {
        return grammarRules[number];
    }

    // 判断某个终结符是否在某个变量的follow集中


}
