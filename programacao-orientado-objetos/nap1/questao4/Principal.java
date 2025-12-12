class Dispositivo {
    String marca, tipo;
    boolean status;

    void tipoDispositivo() {
        System.out.println(tipo);
    }
    void imprimirMarca() {
        System.out.print("|                                           |\n| MARCA: " + marca);

    }
    void ligar() {
        System.out.println("\n| Dispositivo ligado!                       |");
    }
}

class SmartTV extends Dispositivo {
    int polegadas;

    void polegadas() {
        System.out.print("\n| POLEGADAS: " + polegadas);
    }
    void escolherCanal() {
        System.out.println("| Canal alterado na TV de " + polegadas + " polegadas.     |");
    }
}

class SmartLampada extends Dispositivo {
    String corLuz;

    void mudarCor() {
        System.out.println("| A lâmpada da marca " + marca + " mudou para a cor |\n| " + corLuz);
    }
}

class Separador {
    void linha() {
        System.out.println("=============================================");
    }
}

//CLASSE PRINCIPAL
public class Principal {
    public static void main(String[] args) {

        Dispositivo dispositivo = new Dispositivo();
        SmartTV tv = new SmartTV();
        SmartLampada lampada = new SmartLampada();
        Separador separador = new Separador();

        dispositivo.tipo = "|           DISPOSITIVO PRINCIPAL           |";
        dispositivo.marca = "Intelbras" + "                          |";
        dispositivo.status = true;
        separador.linha();
        dispositivo.tipoDispositivo();
        separador.linha();
        dispositivo.imprimirMarca();
        dispositivo.ligar();
        System.out.println("|                                           |");

        tv.tipo = "|                 SMART TV                  |";
        tv.marca = "Samsung" + "                            |";
        tv.polegadas = 40;
        tv.status = true;
        separador.linha();
        tv.tipoDispositivo();
        separador.linha();
        tv.imprimirMarca();
        tv.polegadas();
        System.out.print("                             |\n|                                           |");
        tv.ligar();
        tv.escolherCanal();
        System.out.println("|                                           |");

        lampada.tipo = "|               SMART LÂMPADA               |";
        lampada.marca = "Elgin"; //+ "                              |";
        lampada.corLuz = "Branca" + "                                    |";
        lampada.status = true;
        separador.linha();
        lampada.tipoDispositivo();
        separador.linha();
        lampada.imprimirMarca();
        System.out.print("                              |\n|                                           |");
        lampada.ligar();
        lampada.mudarCor();
        System.out.println("|                                           |");
        separador.linha();
    }
}
