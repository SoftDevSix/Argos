public class Test2 {
    private String brand;
    private String model;
    private int yearOfFactory;

    public Test2(String brand, String model, int yearOfFactory) {
        this.brand = brand;
        this.model = model;
        this.yearOfFactory = yearOfFactory;
    }

    public void accelerate() {
        System.out.println("The car is accelerating.");
    }
}