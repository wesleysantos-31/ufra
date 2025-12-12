class Ingresso {
    public String nomeEvento;
    public double valor;

    public void mostrarResumo() {
        System.out.println("Evento: " + nomeEvento + " | Valor: R$ " + valor);
    }
}

class IngressoVIP extends Ingresso {
    public String camarote;

    public void acessarAreaVip() {
        System.out.println("Acesso liberado ao camarote " + camarote +
                           " do evento " + nomeEvento + ".");
    }
}

class IngressoMeia extends Ingresso {
    public String documentoEstudante;

    public void validarCarteirinha() {
        System.out.println("Carteirinha " + documentoEstudante +
                           " validada para o evento.");
    }
}

public class EventosEIngressos {
    public static void main(String[] args) {

        // Ingresso VIP
        IngressoVIP vip = new IngressoVIP();
        vip.nomeEvento = "Show de Rock";
        vip.valor = 250.00;
        vip.camarote = "Camarote Gold";

        vip.mostrarResumo();
        vip.acessarAreaVip();

        System.out.println("--------------");

        // Ingresso Meia
        IngressoMeia meia = new IngressoMeia();
        meia.nomeEvento = "Show de Rock";
        meia.valor = 125.00;
        meia.documentoEstudante = "EST-2025-123";

        meia.mostrarResumo();
        meia.validarCarteirinha();
    }
}
