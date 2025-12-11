import java.util.Scanner;

public class EntradaDeDados
{
    public static void main(String[] args)
    {
        int num;
        Scanner ler =  new Scanner(System.in);
        System.out.println("Digite um número: ");
        num = ler.nextInt();
        ler.close();

        System.out.println("O número digitado foi: " + num);
    }  
}
