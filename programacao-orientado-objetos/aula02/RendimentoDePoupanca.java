import java.util.Scanner;

public class RendimentoDePoupanca
{
    public static void main(String[] args)
    {
        double poupanca = 0;
        double juroFixo = 0.89;
        double deposito = 0;

        Scanner ler = new Scanner(System.in);

        System.out.println("\n===== CONTA POUPANÇA =====");

        System.out.println("\nDigite o valor do depósito: ");
        deposito = ler.nextDouble();
        ler.close();

        poupanca = deposito * (juroFixo / 100) + deposito;

        System.out.println("\n===== RENDIMENTO =====");

        System.out.println("\nValor do depósito após um mês: R$ " + String.format("%.2f", poupanca));

        System.out.println("\n===========================\n");
    }
}
