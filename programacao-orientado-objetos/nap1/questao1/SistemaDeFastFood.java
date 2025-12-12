class Lanche {
    public String nome;
    public double preco;

    public void imprimirEtiqueta() {
    System.out.printf("Lanche: %s - R$ %.2f%n", nome, preco);
    }

}

class Sanduiche extends Lanche {
    public String ingredientes;

    public void adicionarMolhoExtra() {
        System.out.println("Adicionando molho extra no sanduíche " + nome + ".");
    }
}

class Bolo extends Lanche {
    public String saborCobertura;
    public String recheio;

    public void partirFatia() {
        System.out.println(
            "Cortando uma fatia do bolo de " + nome +
            " com cobertura de " + saborCobertura + "."
        );
    }
}

public class SistemaDeFastFood {
    public static void main(String[] args) {

        Sanduiche sanduiche = new Sanduiche();
        sanduiche.nome = "X-Burger";
        sanduiche.preco = 15.90;
        sanduiche.ingredientes = "Pão, carne, queijo";

        sanduiche.imprimirEtiqueta();
        sanduiche.adicionarMolhoExtra();

        System.out.println("--------------");

        Bolo bolo = new Bolo();
        bolo.nome = "Bolo de Aniversário";
        bolo.preco = 45.00;
        bolo.saborCobertura = "Chocolate";
        bolo.recheio = "Brigadeiro";

        bolo.imprimirEtiqueta();
        bolo.partirFatia();
    }
}
