import java.util.Scanner;

public class PrestacoesDeProdutos
{
    public static void main(String[] args)
    {
        int numPrestacoes = 5;
        double valorProduto = 0;
        double valorDesconto = 15.0 / 100.0;
        double valorPrestacao = 0;

        Scanner ler = new Scanner(System.in);

        System.out.println("\n===== CÁLCULO DE PRESTAÇÕES DE PRODUTOS =====");

        System.out.println("\nDigite o valor do produto: ");
        valorProduto = ler.nextDouble();
        ler.close();

        if (valorProduto >= 200)
        {
            System.out.println("\n=== PRODUTO COM 15% DE DESCONTO! ===");

            valorProduto = valorProduto - (valorProduto * valorDesconto);

            valorPrestacao = valorProduto / numPrestacoes;

            System.out.println("\nValor do produto com desconto: R$ " + String.format("%.2f", valorProduto));
            System.out.println("Valor de cada prestação (5x): R$ " + String.format("%.2f", valorPrestacao));
        }
        else
        {
            valorPrestacao = valorProduto / numPrestacoes;

            System.out.println("\nValor do produto: R$ " + String.format("%.2f", valorProduto));
            System.out.println("Valor de cada prestação (5x): R$ " + String.format("%.2f", valorPrestacao));
        }
        System.out.println("\n=============================================\n");
    }

}
