package entity;

// 封装了 Action 的类型和文本值
public class ActionData {
    public enum Action {
        STATE, RETURN, ERROR, END
    }
    Action action;      // 动作类型
    int number;         // 动作对应的数字

    public ActionData(Action action, int number){
        this.action = action;
        this.number = number;
    }
    public ActionData(){
        action = Action.ERROR;
        number = -1;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String toString(){
        return "action:"+action.name() + " state:" + number+"\n";
    }
}
