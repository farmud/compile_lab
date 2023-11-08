package entity;

import java.util.Arrays;
import java.util.List;

// 项目集
public class ProgramSet {
    private int id = -1;         //项目集的编号
    private List<GrammarRule> grammarRules;

    public ProgramSet() {
    }
    public ProgramSet(List<GrammarRule> grammarRules) {
        this.grammarRules = grammarRules;
    }
    public ProgramSet(int id, List<GrammarRule> grammarRules) {
        this.id = id;
        this.grammarRules = grammarRules;
    }




    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<GrammarRule> getGrammarRules() {
        return grammarRules;
    }

    public void setGrammarRule(List<GrammarRule> grammarRules) {
        this.grammarRules = grammarRules;
    }
    public void setGrammarRule(GrammarRule[] grammarRule){
        grammarRules.addAll(Arrays.asList(grammarRule));
    }


    // 查找returnGo在programSets中的位置，返回编号
    public int getIndex(List<ProgramSet> programSets, List<GrammarRule> returnGo){
        // @programSets: 项目集的集合
        // @returnGo: 待查找的项目集


        for (ProgramSet programSet : programSets) {
            if (GrammarRule.judgeEqualPro( programSet.getGrammarRules(),(returnGo))) {
                return programSet.getId();
            }
        }
        return -1;
    }


}
