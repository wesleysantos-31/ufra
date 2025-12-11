import java.util.Scanner;

public class ConversaoDeMoeda
{
    public static void main(String[] args)
    {
        double CotacaoDolar, ValorReal = 0, ValorDisponivel;

        Scanner ler = new Scanner(System.in);

        System.out.println("\n====== CONVERSOR DE MOEDA - DÓLAR PARA REAL ======");

        System.out.println("\nDigite o valor do dólar hoje: ");
        CotacaoDolar = ler.nextDouble();
        System.out.println("Digite o valor em dólar que você possui: ");
        ValorDisponivel = ler.nextDouble();
        ler.close();

        ValorReal = CotacaoDolar * ValorDisponivel;

        System.out.println("\n====== VALOR CONVERTIDO ======");

        System.out.println("\nCOTAÇÃO DO DÓLAR: R$ " + String.format("%.2f", CotacaoDolar));
        System.out.println("VALOR DISPONÍVEL EM DÓLAR: U$ " + String.format("%.2f", ValorDisponivel));
        System.out.println("\nVALOR CONVERTIDO EM REAL: R$ " + String.format("%.2f", ValorReal));
        System.out.println("\n========================================");
    }
}