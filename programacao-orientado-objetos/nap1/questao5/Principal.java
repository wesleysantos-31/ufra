class ProdutoLiterario {
    public String titulo;
    public String autor;

    public void exibirDados() {
        System.out.println("Título: " + titulo);
        System.out.println("Autor: " + autor);
    }
}

class LivroFisico extends ProdutoLiterario {
    public double peso;
    public String tipoCapa;

    public void calcularFrete() {
        System.out.println("Calculando frete para o livro de " + peso + "g.");
    }
}

class Ebook extends ProdutoLiterario {
    public double tamanhoArquivo;

    public void baixar() {
        System.out.println("Iniciando download do livro " + titulo + " (" + tamanhoArquivo + " MB).");
    }
}

public class Principal {
    public static void main(String[] args) {
        
        System.out.println("=== TESTE LIVRO FÍSICO ===");
        LivroFisico meuLivro = new LivroFisico();
        meuLivro.titulo = "O Senhor dos Anéis";
        meuLivro.autor = "J.R.R. Tolkien";
        meuLivro.peso = 1200.50;
        meuLivro.tipoCapa = "Dura";

        meuLivro.exibirDados();
        meuLivro.calcularFrete();

        System.out.println("\n=== TESTE EBOOK ===");
        Ebook meuEbook = new Ebook();
        meuEbook.titulo = "Entendendo Algoritmos";
        meuEbook.autor = "Aditya Y. Bhargava";
        meuEbook.tamanhoArquivo = 15.4;

        meuEbook.exibirDados();
        meuEbook.baixar();
    }
}
