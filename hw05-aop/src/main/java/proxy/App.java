package proxy;

public class App {
    public static void main(String[] args) {
        Calculator calculator = Arbiter.create(new CalculatorImpl());
        calculator.calculation(1);
        calculator.calculation(1, 2);
        calculator.calculation(1, 2, "3");
    }
}
