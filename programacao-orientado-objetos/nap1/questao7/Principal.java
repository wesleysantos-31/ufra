class Veiculo {
    public String placa;
    public int ano;

    public void cadastrar() {
        System.out.println("Veículo placa " + placa + " cadastrado no sistema.");
    }
}

class Caminhao extends Veiculo {
    public int eixos;

    public void pesarCarga() {
        System.out.println("Pesando caminhão de " + eixos + " eixos.");
    }
}

class MotoEntrega extends Veiculo {
    public double capacidadeBau;

    public void fazerEntrega() {
        System.out.println("Moto saindo para entrega. Capacidade: " + capacidadeBau + " litros.");
    }
}

public class Principal {
    public static void main(String[] args) {
        
        System.out.println("=== CAMINHÃO ===");
        Caminhao caminhao = new Caminhao();
        caminhao.placa = "ABC-1234";
        caminhao.ano = 2021;
        caminhao.eixos = 6;
        
        caminhao.cadastrar();
        caminhao.pesarCarga();

        System.out.println("\n==== MOTO ====");
        MotoEntrega moto = new MotoEntrega();
        moto.placa = "XYZ-9876";
        moto.ano = 2024;
        moto.capacidadeBau = 45.5;
        
        moto.cadastrar();
        moto.fazerEntrega();
    }
}
