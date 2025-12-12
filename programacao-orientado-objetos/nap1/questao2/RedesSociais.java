class Postagem {
    public String autor;
    public String link;

    public void compartilhar() {
        System.out.println("Postagem de " + autor + " foi compartilhada.");
    }
}

class PostagemTexto extends Postagem {
    public String conteudo;

    public void lerEmVozAlta() {
        System.out.println("Lendo: " + conteudo + ".");
    }
}

class PostagemVideo extends Postagem {
    public int duracao;
    public String formato;

    public void darPlay() {
        System.out.println("O vídeo de " + duracao + " segundos começou a tocar.");
    }
}

public class RedesSociais {
    public static void main(String[] args) {

        // Postagem de texto
        PostagemTexto texto = new PostagemTexto();
        texto.autor = "Ana";
        texto.link = "https://exemplo.com/post/123";
        texto.conteudo = "Olá, mundo!";
        texto.compartilhar();
        texto.lerEmVozAlta();

        System.out.println("--------------");

        // Postagem de vídeo
        PostagemVideo video = new PostagemVideo();
        video.autor = "Bruno";
        video.link = "https://exemplo.com/video/999";
        video.duracao = 30;
        video.formato = "MP4";
        video.compartilhar();
        video.darPlay();
    }
}
