public class Calculos
{
    public static void main(String[] args)
    {
        int num1 = 20;
        int num2 = 10;
        int soma = 0;
        int sub = 0;
        int mult = 0;
        int div = 0;

        soma = num1 + num2;
        sub = num1 - num2;
        mult = num1 * num2;
        div = num1 / num2;

        System.out.println("========== CÁLCULOS ==========");

        System.out.println("\n1º NÚMERO: " + num1);
        System.out.println("2º NÚMERO: " + num2);

        System.out.println("\n========= RESULTADOS =========");

        System.out.println("\nSOMA: " + soma);
        System.out.println("SUBTRAÇÃO: " + sub);
        System.out.println("MULTIPLICAÇÃO: " + mult);
        System.out.println("DIVISÃO: " + div);
    }
}