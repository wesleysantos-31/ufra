import java.sql.*;
import java.util.Scanner;

public class cinema {

    public static void main(String[] args) {

        Connection conn = null;
        Scanner scanner = new Scanner(System.in);

        try {
            // ================================
            // 1. ABRINDO CONEXÃO COM O BANCO
            // ================================
            conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1/cinema", "root", "");
            System.out.println("Conexão estabelecida com o banco 'cinema'!");

            // Exibindo as características do cinema
            System.out.println("==================================");
            System.out.println("======    CINEMA ATLAS    ======");
            System.out.println("==================================");
            System.out.println(" Endereço: Avenida Barão do Rio Branco, n° 250");
            System.out.println(" Castanhal-Pará");
            System.out.println(" Fone: (91) 3344-7821");
            System.out.println("==================================\n");

            // ================================
            // 2. INSERINDO NO BANCO (AUTO_INCREMENT)
            // ================================
            // Passamos NULL para que o banco gere o número automaticamente
            String sqlInsert = "INSERT INTO idcinema (idcinema) VALUES (NULL)";

            // Preparamos o comando avisando que queremos saber qual ID foi gerado
            PreparedStatement pstmtInsert = conn.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS);

            // Executa a inserção
            pstmtInsert.executeUpdate();

            // ================================
            // 3. MOSTRANDO O ID GERADO
            // ================================
            ResultSet rs = pstmtInsert.getGeneratedKeys();
            if (rs.next()) {
                int idGerado = rs.getInt(1);
                System.out.println("Cinema incluído com sucesso! O banco gerou o ID: " + idGerado);
            }

            int opcao = -1;

            // ================================
            // 2. MENU
            // ================================
            while (opcao != 0) {
                System.out.println("\n==================================");
                System.out.println("====== SISTEMA DE INGRESSOS ======");
                System.out.println("==================================");
                System.out.println("1 - Cadastrar novo ingresso");
                System.out.println("2 - Listar todos os ingressos");
                System.out.println("3 - Atualizar um ingresso");
                System.out.println("4 - Excluir um ingresso");
                System.out.println("0 - Sair do sistema");
                System.out.print("Escolha uma opção: ");

                opcao = scanner.nextInt();
                scanner.nextLine(); // Limpa o buffer do teclado

                switch (opcao) {
                    case 1:
                        // ===================== CREATE =======================
                        System.out.println("\n--- CADASTRAR INGRESSO ---");
                        System.out.print("Digite o número do assento (Obs: de 1 a 50): ");
                        String assento = scanner.nextLine();
                        System.out.print("Digite o status (disponível ou vendido): ");
                        String status = scanner.nextLine();
                        System.out.print("Digite o ID da Sessão (Ex: 1, 2, 3...): ");
                        int idSessao = scanner.nextInt();

                        String sqlInsertIngresso = "INSERT INTO ingressos (numero_assento, status, id_sessao) VALUES (?, ?, ?)";
                        PreparedStatement pstmtInsertIngresso = conn.prepareStatement(sqlInsertIngresso);
                        pstmtInsertIngresso.setString(1, assento);
                        pstmtInsertIngresso.setString(2, status);
                        pstmtInsertIngresso.setInt(3, idSessao);

                        pstmtInsertIngresso.executeUpdate();
                        System.out.println("✅ Ingresso cadastrado com sucesso!");
                        break;

                    case 2:
                        // ====================== READ =========================
                        System.out.println("\n--- LISTA DE INGRESSOS ---");
                        String sqlSelect = "SELECT * FROM ingressos";
                        PreparedStatement pstmtSelect = conn.prepareStatement(sqlSelect);
                        rs = pstmtSelect.executeQuery();

                        boolean temIngressos = false;
                        while (rs.next()) {
                            temIngressos = true;
                            System.out.println("ID: " + rs.getInt("id_ingresso") +
                                    " | Assento: " + rs.getString("numero_assento") +
                                    " | Status: " + rs.getString("status") +
                                    " | ID Sessão: " + rs.getInt("id_sessao"));
                        }

                        if (!temIngressos) {
                            System.out.println("Nenhum ingresso cadastrado no momento.");
                        }
                        break;

                    case 3:
                        // ===================== UPDATE ========================
                        System.out.println("\n--- ATUALIZAR INGRESSO ---");
                        System.out.print("Digite o ID do ingresso que deseja alterar: ");
                        int idAtualizar = scanner.nextInt();
                        scanner.nextLine(); // Limpa o buffer

                        System.out.print("Digite o NOVO número do assento: ");
                        String novoAssento = scanner.nextLine();
                        System.out.print("Digite o NOVO status (disponível ou vendido): ");
                        String novoStatus = scanner.nextLine();
                        System.out.print("Digite o NOVO ID da Sessão: ");
                        int novaSessao = scanner.nextInt();

                        String sqlUpdate = "UPDATE ingressos SET numero_assento = ?, status = ?, id_sessao = ? WHERE id_ingresso = ?";
                        PreparedStatement pstmtUpdate = conn.prepareStatement(sqlUpdate);
                        pstmtUpdate.setString(1, novoAssento);
                        pstmtUpdate.setString(2, novoStatus);
                        pstmtUpdate.setInt(3, novaSessao);
                        pstmtUpdate.setInt(4, idAtualizar);

                        int linhasAfetadas = pstmtUpdate.executeUpdate();
                        if (linhasAfetadas > 0) {
                            System.out.println("✅ Ingresso atualizado com sucesso!");
                        } else {
                            System.out.println("❌ Ingresso não encontrado (ID inválido).");
                        }
                        break;

                    case 4:
                        // ===================== DELETE ========================
                        System.out.println("\n--- EXCLUIR INGRESSO ---");
                        System.out.print("Digite o ID do ingresso que deseja excluir: ");
                        int idExcluir = scanner.nextInt();

                        String sqlDelete = "DELETE FROM ingressos WHERE id_ingresso = ?";
                        PreparedStatement pstmtDelete = conn.prepareStatement(sqlDelete);
                        pstmtDelete.setInt(1, idExcluir);

                        int linhasDeletadas = pstmtDelete.executeUpdate();
                        if (linhasDeletadas > 0) {
                            System.out.println("✅ Ingresso deletado com sucesso!");
                        } else {
                            System.out.println("❌ Ingresso não encontrado (ID inválido).");
                        }
                        break;
                    
                        
                    //SESSÕES
                    case 5:
                        // ===================== CREATE =======================
                        System.out.println("\n--- CADASTRAR SESSÃO ---");

                        System.out.print("Digite a data da sessao: ");
                        String dataSessao = scanner.nextLine();

                        System.out.print("Digite o horário: ");
                        String horarioSessao = scanner.nextLine();

                        System.out.print("Digite o valor do ingresso: ");
                        double valorSessao = scanner.nextDouble();

                        System.out.print("Digite o ID do filme: ");
                        int filmeSessao = scanner.nextInt();

                        System.out.print("Digite o ID da sala: ");
                        int salaSessao = scanner.nextInt();

                        String sqlInsertSessao = "INSERT INTO sessoes (data, horario, valor_ingresso, id_filme, id_sala) VALUES (?, ?, ?, ?, ?)";
                        PreparedStatement pstmtInsertSessao = conn.prepareStatement(sqlInsertSessao);

                        pstmtInsertSessao.setDate(1, Date.valueOf(dataSessao));
                        pstmtInsertSessao.setTime(2, Time.valueOf(horarioSessao));
                        pstmtInsertSessao.setDouble(3, valorSessao);
                        pstmtInsertSessao.setInt(4, filmeSessao);
                        pstmtInsertSessao.setInt(5, salaSessao);

                        pstmtInsertSessao.executeUpdate();
                        System.out.println("Sessão cadastrada com sucesso!");
                        break;

                    case 6:
                        // ====================== READ =========================
                        System.out.println("\n--- LISTA DE SESSÕES ---");

                        String sqlSelectSessao = "SELECT * FROM sessoes";
                        PreparedStatement pstmtSelectSessao = conn.prepareStatement(sqlSelectSessao);
                        ResultSet rsSessao = pstmtSelectSessao.executeQuery();

                        boolean temSessao = false;

                        while (rsSessao.next()) {
                            temSessao = true;
                            System.out.println("ID: " + rsSessao.getInt("id_sessao") +
                                    " | Data: " + rsSessao.getDate("data") +
                                    " | Horário: " + rsSessao.getTime("horario") +
                                    " | Valor: " + rsSessao.getDouble("valor_ingresso") +
                                    " | ID Filme: " + rsSessao.getInt("id_filme") +
                                    " | ID Sala: " + rsSessao.getInt("id_sala"));
                        }

                        if (!temSessao) {
                            System.out.println("Nenhuma sessão cadastrada.");
                        }
                        break;

                    case 7:
                        // ===================== UPDATE ========================
                        System.out.println("\n--- ATUALIZAR SESSÃO ---");

                        System.out.print("Digite o ID da sessão que deseja alterar: ");
                        int idAtualizarSessao = scanner.nextInt();
                        scanner.nextLine();

                        System.out.print("Digite a NOVA data: ");
                        String novaDataSessao = scanner.nextLine();

                        System.out.print("Digite o NOVO horário: ");
                        String novoHorarioSessao = scanner.nextLine();

                        System.out.print("Digite o NOVO valor do ingresso: ");
                        double novoValorSessao = scanner.nextDouble();

                        System.out.print("Digite o NOVO ID do filme: ");
                        int novoFilmeSessao = scanner.nextInt();

                        System.out.print("Digite o NOVO ID da sala: ");
                        int novaSalaSessao = scanner.nextInt();

                        String sqlUpdateSessao = "UPDATE sessoes SET data = ?, horario = ?, valor_ingresso = ?, id_filme = ?, id_sala = ? WHERE id_sessao = ?";
                        PreparedStatement pstmtUpdateSessao = conn.prepareStatement(sqlUpdateSessao);

                        pstmtUpdateSessao.setDate(1, Date.valueOf(novaDataSessao));
                        pstmtUpdateSessao.setTime(2, Time.valueOf(novoHorarioSessao));
                        pstmtUpdateSessao.setDouble(3, novoValorSessao);
                        pstmtUpdateSessao.setInt(4, novoFilmeSessao);
                        pstmtUpdateSessao.setInt(5, novaSalaSessao);
                        pstmtUpdateSessao.setInt(6, idAtualizarSessao);

                        int linhasSessao = pstmtUpdateSessao.executeUpdate();

                        if (linhasSessao > 0) {
                            System.out.println("Sessão atualizada com sucesso!");
                        } else {
                            System.out.println(" Sessão não encontrada (ID inválido).");
                        }
                        break;

                    case 8:
                                              // DELETE
                        System.out.println("\n EXCLUIR SESSÃO ");

                        System.out.print("Digite o ID da sessão que deseja excluir: ");
                        int idExcluirSessao = scanner.nextInt();

                        String sqlDeleteSessao = "DELETE FROM sessoes WHERE id_sessao = ?";
                        PreparedStatement pstmtDeleteSessao = conn.prepareStatement(sqlDeleteSessao);
                        pstmtDeleteSessao.setInt(1, idExcluirSessao);

                        int linhasDeletadasSessao = pstmtDeleteSessao.executeUpdate();

                        if (linhasDeletadasSessao > 0) {
                            System.out.println("Sessão deletada com sucesso!");
                        } else {
                            System.out.println("Sessão não encontrada (ID inválido).");
                        }
                        break;

                    case 0:
                        System.out.println("\nEncerrando o sistema... Até logo!");
                        break;

                    default:
                        System.out.println("\n❌ Opção inválida! Tente novamente.");
                        break;
                }
            }

        } catch (SQLException e) {
            System.out.println(
                    "Erro ao acessar o banco de dados! Verifique se o XAMPP está rodando e se o banco foi criado.");
            e.printStackTrace();

        } finally {
            // ================================
            // 3. FECHANDO A CONEXÃO
            // ================================
            try {
                if (scanner != null) {
                    scanner.close();
                }
                if (conn != null) {
                    conn.close();
                    System.out.println("Conexão com o banco de dados encerrada.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}