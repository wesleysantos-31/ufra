import java.util.Scanner;

public class Reajustesalario {

    public static void main(String[] args) {
                
        double total, porcento;
        
        Scanner entrada = new Scanner(System.in);
        
        System.out.println("--DIGITE SEU SALARIO ATUAL--");
        System.out.print("RS ");
        double salario = entrada.nextDouble();
        System.out.println(" ");
        
        porcento = salario * 0.15;
        
        String resultado = String.format(" %.2f", porcento);
        System.out.println("OS 15% DE RS "+ salario+ " E RS "+ resultado);
        System.out.println(" ");
        
        total = salario + porcento;
        
        System.out.println("--SALARIO REAJUSTADO COM OS 15%--");
        String reajuste = String.format(" %.2f", total);
        System.out.print("RS "+ reajuste);
       
    }
}
