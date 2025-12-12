class Tarefa {
    public String titulo;
    public String prazo;

    public void concluir() {
        System.out.println("Tarefa " + titulo + " concluída.");
    }
}

class TarefaPrazoCurto extends Tarefa {
    public int prioridade;

    public void enviarAlerta() {
        System.out.println("ALERTA: Prioridade " + prioridade + " para a tarefa " + titulo + "!");
    }
}

class TarefaPrazoLongo extends Tarefa {
    public int diasEstimados;

    public void planejarCronograma() {
        System.out.println("Cronograma criado para " + diasEstimados + " dias.");
    }
}

public class Principal {
    public static void main(String[] args) {
        
        System.out.println("=== TAREFA DE PRAZO CURTO ===");
        TarefaPrazoCurto urgente = new TarefaPrazoCurto();
        urgente.titulo = "Corrigir Bug Crítico";
        urgente.prazo = "Imediato";
        urgente.prioridade = 1;
        
        urgente.enviarAlerta();
        urgente.concluir();

        System.out.println("\n=== TAREFA DE PRAZO LONGO ===");
        TarefaPrazoLongo projeto = new TarefaPrazoLongo();
        projeto.titulo = "Migração de Banco de Dados";
        projeto.prazo = "Dezembro 2025";
        projeto.diasEstimados = 120;
        
        projeto.planejarCronograma();
        projeto.concluir();
    }
}
