//CRIANDO UMA CLASSE CONTA COM ATRIBUTOS E MÉTODOS

class Conta
{
    String titular;
    double saldo;
    double limite;
    int numero;

    void imprimirTitular()
    {
        System.out.println("O titular da conta é: " + titular);
    }

    void imprimirSaldo()
    {
        System.out.println("O saldo da conta é: " + saldo);
    }

    void imprimirLimite()
    {
        System.out.println("O limite da conta é: " + limite);
    }

    void imprimirNumero()
    {
        System.out.println("O número da conta é: " + numero);
    }
}

//CRIANDO UMA CLASSE SEPARADOR COM UM MÉTODO PARA IMPRIMIR UMA LINHA DE SEPARAÇÃO
class Separador
{
    void imprimirLinha()
    {
        System.out.println("\n=======================================================\n");
    }
}

public class UtilizandoClasseObjetoMetodos
{
    public static void main(String[] args)
    {
        // Criando um objeto do tipo Conta
        Conta c1 = new Conta();
        Separador separador = new Separador();
        
        separador.imprimirLinha();
        c1.titular = "Wesley Santos da Silva Brito";
        c1.numero = 3108000;
        c1.limite = 1000.0;
        c1.saldo = 3000.0;

        c1.imprimirTitular();
        c1.imprimirNumero();
        c1.imprimirSaldo();
        separador.imprimirLinha();

        // Criando um segundo objeto do tipo Conta
        Conta c2 = new Conta();

        c2.titular = "Maria Eduarda Souza";
        c2.numero = 3108001;
        c2.limite = 2000.0;
        c2.saldo = 3500.0;

        c2.imprimirTitular();
        c2.imprimirNumero();
        c2.imprimirSaldo();
        separador.imprimirLinha();

        // Criando um terceiro objeto do tipo Conta
        Conta c3 = new Conta();

        c3.titular = "João Pedro Lima";
        c3.numero = 3108002;
        c3.limite = 2000.0;
        c3.saldo = 4000.0;

        c3.imprimirTitular();
        c3.imprimirNumero();
        c3.imprimirSaldo();
        separador.imprimirLinha();
    }
}