import java.util.Scanner;

public class Test {
    public static void main(String[] args) {
        GrammarAnalysis grammarAnalysis = new GrammarAnalysis();

            System.out.println("输入要测试的功能");
            System.out.println("1. 函数声明");
            System.out.println("2. 赋值语句");
            System.out.println("3. if语句");
            System.out.println("4. while语句");
            System.out.println("5. 变量声明");
            System.out.println("6. 退出");
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();

            switch (Integer.parseInt(input)){
                case 1:
                    grammarAnalysis.readTokenStack("src/main/resources/function.txt");
                    break;
                case 2:
                    grammarAnalysis.readTokenStack("src/main/resources/assignment.txt");
                    break;
                case 3:
                    grammarAnalysis.readTokenStack("src/main/resources/if.txt");
                    break;
                case 4:
                    grammarAnalysis.readTokenStack("src/main/resources/while.txt");
                    break;
                case 5:
                    grammarAnalysis.readTokenStack("src/main/resources/variable.txt");
                    break;
                case 6:
                    return;
                default:
                    System.out.println("输入错误，请重新输入");

            }
            grammarAnalysis.SLR1Analysis();

    }
}
