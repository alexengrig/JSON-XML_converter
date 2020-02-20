import converter.Main;
import org.hyperskill.hstest.v6.stage.BaseStageTest;
import org.hyperskill.hstest.v6.testcase.CheckResult;
import org.hyperskill.hstest.v6.testcase.TestCase;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class Clue {
    String answer;
    String input;
    boolean showAnswer;

    Clue(String answer, String input, boolean showAnswer) {
        this.answer = answer.replaceAll("\\s+", "");;
        this.input = input.replaceAll("\\s+", "");
        this.showAnswer = showAnswer;
    }

    String getFeedback(String userOutput) {
        if (!showAnswer) {
            return "";
        }
        return
            "Test: " + input + "\n" +
            "Answer: " + answer + "\n" +
            "Your output: " + userOutput;
    }
}

public class ConverterTest extends BaseStageTest<Clue> {
    public ConverterTest() throws Exception {
        super(Main.class);
    }

    static Map<String, String> allTests = Map.of(
        "<host>127.0.0.1</host>", "{\"host\":\"127.0.0.1\"}",
        "{\"host\":\"127.0.0.1\"}", "<host>127.0.0.1</host>",
        "<pizza>slice</pizza>", "{\"pizza\":\"slice\"}",
        "{\"pizza\":\"slice\"}", "<pizza>slice</pizza>",
        "<success/>", "{\"success\":null}",
        "{\"success\":null}", "<success/>",
        "{\"jdk\":\"1.8.9\"}", "<jdk>1.8.9</jdk>",
        "<jdk>1.8.9</jdk>", "{\"jdk\":\"1.8.9\"}",
        "<qwerty/>", "{\"qwerty\":null}",
        "{\"qwerty\":null}", "<qwerty/>"
    );

    @Override
    public List<TestCase<Clue>> generate() {

        List<TestCase<Clue>> tests = new ArrayList<>();

        allTests.forEach((input, answer) -> {
            TestCase<Clue> test = new TestCase<>();
            test.setInput(input);
            test.setAttach(new Clue(answer, input, false));
            tests.add(test);
        });

        int i = 0;
        for(TestCase<Clue> test : tests) {

            test.getAttach().showAnswer = true;

            i++;
            if (i == 6){
                break;
            }
        }

        return tests;
    }

    @Override
    public CheckResult check(String reply, Clue clue) {
        String userAnswer = reply.replaceAll("\\s+", "");

        if (userAnswer.equals(clue.answer)) {
            return CheckResult.TRUE;
        }

        return new CheckResult(false, clue.getFeedback(userAnswer));
    }

}
