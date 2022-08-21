import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.concat;
import static util.SequenceGenerator.generate;

public class Application {
    public static void main(String[] args) throws InterruptedException {
        new SequenceConsolePrinter(
                concat(
                        generate(1, 1, 10).stream(),
                        generate(9, -1,  9).stream()
                ).collect(toList())
        ).run();
    }
}