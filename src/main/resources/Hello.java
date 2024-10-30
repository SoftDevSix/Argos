public class Hello {
    int a;
    int b = 1;
    int c = 1;

    int d = 1;
    int r = 1;
    int ew = 1;

    // Método que no utiliza ninguna variable de instancia
    public void unusedMethod() {
        int apoqwe;
    }

    // Método que usa algunas variables
    public void usedMethod() {
        int sum = a + b; // Utiliza a y b
        System.out.println("Sum: " + sum);
    }
}
