package entity;

import java.util.List;

public class GrammarRule {
    private ValueType left;     //语法规则的左部
    private List<ValuesAndTerminals> right;   //语法规则的右部

    private int index;          //当前点的位置

    public GrammarRule(){
        index = 0;
    }

    public GrammarRule(ValueType left, List<ValuesAndTerminals> right, int index) {
        this.left = left;
        this.right = right;
        this.index = index;
    }

    public ValueType getLeft() {
        return left;
    }

    public void setLeft(ValueType left) {
        this.left = left;
    }

    public List<ValuesAndTerminals> getRight() {
        return right;
    }

    public void setRight(List<ValuesAndTerminals> right) {
        this.right = right;
    }

    public int getIndex(){
        return index;
    }
    public void setIndex(int index){
        this.index = index;
    }



    // 获得当前点后面的值
    public ValuesAndTerminals getRecentValueAndTerminal(){
        return right.get(index);
    }



    // 判断GrammarRule List中是否已经存在待存入的该GrammarRule，当左部和右部都相同时，认为两个GrammarRule相同
    public boolean haveNotExistedIn(List<GrammarRule> grammarRules){
        for(GrammarRule grammarRule : grammarRules){
            if(grammarRule.getLeft().equals(this.getLeft()) && grammarRule.getRight().equals(this.getRight())){
                return false;
            }
        }
        return true;
    }

    // 判断GrammarRule List中是否已经存在待存入的该GrammarRule，当左部和右部都相同,且点的位置也相同时，认为两个GrammarRule相同
    public boolean haveNotExistedInPro(List<GrammarRule> grammarRules){
        for(GrammarRule grammarRule : grammarRules){
            if(grammarRule.getLeft().equals(this.getLeft()) && grammarRule.getRight().equals(this.getRight()) && grammarRule.getIndex() == this.getIndex()){
                return false;
            }
        }
        return true;
    }


    // 判断两个GrammarRule是否相同，严格
    public boolean judgeEqual(GrammarRule grammarRule){
        return grammarRule.getLeft().equals(this.getLeft()) && grammarRule.getRight().equals(this.getRight()) && grammarRule.getIndex() == this.getIndex();
    }

    // 判断两个GrammarRule是否相同，不严格，不考虑点的位置
    public boolean judgeEqualSimple(GrammarRule grammarRule){
        return grammarRule.getLeft().equals(this.getLeft()) && grammarRule.getRight().equals(this.getRight());
    }

    // 判断两个GrammarRule List是否相同
    public static boolean judgeEqual(List<GrammarRule> gm1s, List<GrammarRule> gm2s){
        for(GrammarRule gm1 : gm1s){
            if(gm1.haveNotExistedIn(gm2s))
                return false;
        }
        for(GrammarRule gm2 : gm2s){
            if(gm2.haveNotExistedIn(gm1s))
                return false;
        }
        return true;
    }

    // 判断两个GrammarRule List是否严格相同，包括index
    public static boolean judgeEqualPro(List<GrammarRule> gm1s, List<GrammarRule> gm2s){
        for(GrammarRule gm1 : gm1s){
            if(gm1.haveNotExistedInPro(gm2s))
                return false;
        }
        for(GrammarRule gm2 : gm2s){
            if(gm2.haveNotExistedInPro(gm1s))
                return false;
        }
        return true;
    }
}
