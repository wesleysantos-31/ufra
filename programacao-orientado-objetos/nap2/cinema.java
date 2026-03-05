import java.sql.*;
import java.util.Scanner;

public class cinema {

    public static void main(String[] args) {

        Connection conn = null;
        Scanner scanner = new Scanner(System.in);

        try {
            // ================================
            // ABRINDO CONEXÃO COM O BANCO
            // ================================
            conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1/cinema", "root", "");
            System.out.println("Conexão estabelecida com o banco 'cinema'!");

            // Exibindo as características do cinema
            System.out.println("\n==========================================");
            System.out.println("=                                        =");
            System.out.println("=          ** CINEMA ATLAS **            =");
            System.out.println("=                                        =");
            System.out.println("==========================================");
            System.out.println("  Endereco: Av. Barao do Rio Branco");
            System.out.println("            n 250 - Castanhal/PA");
            System.out.println("  Fone: (91) 3344-7821");
            System.out.println("==========================================\n");

            // ================================
            // INSERINDO NO BANCO (ID FIXO)
            // ================================
            int idFixo = 1;
            String sqlInsert = "INSERT IGNORE INTO idcinema (idcinema) VALUES (?)";

            PreparedStatement pstmtInsert = conn.prepareStatement(sqlInsert);
            pstmtInsert.setInt(1, idFixo);

            // Executa a inserção
            pstmtInsert.executeUpdate();

            ResultSet rs = null;

            int opcaoPrincipal = -1;

            // ================================
            // MENU PRINCIPAL
            // ================================
            while (opcaoPrincipal != 0) {
                System.out.println("\n==================================");
                System.out.println("=======   MENU PRINCIPAL   =======");
                System.out.println("==================================");
                System.out.println("1 - Gerenciar Salas");
                System.out.println("2 - Gerenciar Filmes");
                System.out.println("3 - Gerenciar Sessões");
                System.out.println("4 - Gerenciar Ingressos");
                System.out.println("0 - Sair do sistema");
                System.out.print("Escolha uma opção: ");

                opcaoPrincipal = scanner.nextInt();
                scanner.nextLine();

                switch (opcaoPrincipal) {

                    // ==========================================
                    case 1: // MENU DE SALAS
                    // ==========================================
                    {
                        int opcaoSala = -1;
                        while (opcaoSala != 0) {
                            System.out.println("\n--- MENU: SALAS ---");
                            System.out.println("1 - Cadastrar sala");
                            System.out.println("2 - Listar salas");
                            System.out.println("3 - Atualizar sala");
                            System.out.println("4 - Excluir sala");
                            System.out.println("0 - Voltar");
                            System.out.print("Escolha: ");
                            opcaoSala = scanner.nextInt();
                            scanner.nextLine();

                            switch (opcaoSala) {
                                case 1: // CADASTRAR
                                    System.out.print("Número da sala: ");
                                    int numSala = scanner.nextInt();
                                    scanner.nextLine();
                                    System.out.print("Capacidade: ");
                                    int capacidade = scanner.nextInt();
                                    scanner.nextLine();
                                    System.out.print("Tipo (2D, 3D, VIP): ");
                                    String tipoSala = scanner.nextLine().toUpperCase();
                                    System.out.print("ID do cinema: ");
                                    int idCinemaSala = scanner.nextInt();
                                    scanner.nextLine();

                                    PreparedStatement psInsertSala = conn.prepareStatement(
                                            "INSERT INTO salas (numero_sala, capacidade, tipo, id_cinema) VALUES (?, ?, ?, ?)");
                                    psInsertSala.setInt(1, numSala);
                                    psInsertSala.setInt(2, capacidade);
                                    psInsertSala.setString(3, tipoSala);
                                    psInsertSala.setInt(4, idCinemaSala);
                                    psInsertSala.executeUpdate();
                                    System.out.println("✅ Sala cadastrada com sucesso!");
                                    break;

                                case 2: // LISTAR
                                    rs = conn.prepareStatement("SELECT * FROM salas").executeQuery();
                                    boolean temSala = false;
                                    while (rs.next()) {
                                        temSala = true;
                                        System.out.println("ID: " + rs.getInt("id_sala")
                                                + " | Nº Sala: " + rs.getInt("numero_sala")
                                                + " | Capacidade: " + rs.getInt("capacidade")
                                                + " | Tipo: " + rs.getString("tipo")
                                                + " | ID Cinema: " + rs.getInt("id_cinema"));
                                    }
                                    if (!temSala)
                                        System.out.println("Nenhuma sala cadastrada.");
                                    break;

                                case 3: // ATUALIZAR
                                    System.out.print("ID da sala a atualizar: ");
                                    int idSalaUp = scanner.nextInt();
                                    scanner.nextLine();
                                    System.out.print("Novo número da sala: ");
                                    int novoNumSala = scanner.nextInt();
                                    scanner.nextLine();
                                    System.out.print("Nova capacidade: ");
                                    int novaCapacidade = scanner.nextInt();
                                    scanner.nextLine();
                                    System.out.print("Novo tipo (2D, 3D, VIP): ");
                                    String novoTipoSala = scanner.nextLine().toUpperCase();
                                    System.out.print("Novo ID do cinema: ");
                                    int novoIdCinemaSala = scanner.nextInt();
                                    scanner.nextLine();

                                    PreparedStatement psUpdateSala = conn.prepareStatement(
                                            "UPDATE salas SET numero_sala = ?, capacidade = ?, tipo = ?, id_cinema = ? WHERE id_sala = ?");
                                    psUpdateSala.setInt(1, novoNumSala);
                                    psUpdateSala.setInt(2, novaCapacidade);
                                    psUpdateSala.setString(3, novoTipoSala);
                                    psUpdateSala.setInt(4, novoIdCinemaSala);
                                    psUpdateSala.setInt(5, idSalaUp);
                                    int linhasSala = psUpdateSala.executeUpdate();
                                    System.out.println(linhasSala > 0 ? "✅ Sala atualizada!" : "❌ ID não encontrado.");
                                    break;

                                case 4: // DELETAR
                                    System.out.print("ID da sala a excluir: ");
                                    int idSalaDel = scanner.nextInt();
                                    scanner.nextLine();

                                    PreparedStatement psDeleteSala = conn.prepareStatement(
                                            "DELETE FROM salas WHERE id_sala = ?");
                                    psDeleteSala.setInt(1, idSalaDel);
                                    int delSala = psDeleteSala.executeUpdate();
                                    System.out.println(delSala > 0 ? "✅ Sala excluída!" : "❌ ID não encontrado.");
                                    break;

                                case 0: // VOLTAR
                                    System.out.println("Voltando ao menu principal...");
                                    break;

                                default: // OPÇÃO INVÁLIDA
                                    System.out.println("❌ Opção inválida!");
                            }
                        }
                        break;
                    }

                    // ==========================================
                    case 2: // MENU DE FILMES
                    // ==========================================
                    {
                        int opcaoFilme = -1;
                        while (opcaoFilme != 0) {
                            System.out.println("\n--- MENU: FILMES ---");
                            System.out.println("1 - Cadastrar filme");
                            System.out.println("2 - Listar filmes");
                            System.out.println("3 - Atualizar filme");
                            System.out.println("4 - Excluir filme");
                            System.out.println("0 - Voltar");
                            System.out.print("Escolha: ");
                            opcaoFilme = scanner.nextInt();
                            scanner.nextLine();

                            switch (opcaoFilme) {
                                case 1: // CADASTRAR
                                    System.out.print("Título do filme: ");
                                    String titulo = scanner.nextLine();
                                    System.out.print("Gênero: ");
                                    String genero = scanner.nextLine();
                                    System.out.print("Classificação (Ex: Livre, 10, 12, 14, 16, 18): ");
                                    String classificacao = scanner.nextLine();
                                    System.out.print("Duração (minutos): ");
                                    int duracao = scanner.nextInt();
                                    scanner.nextLine();

                                    PreparedStatement psInsertFilme = conn.prepareStatement(
                                            "INSERT INTO filmes (titulo, genero, classificacao, duracao_minutos) VALUES (?, ?, ?, ?)");
                                    psInsertFilme.setString(1, titulo);
                                    psInsertFilme.setString(2, genero);
                                    psInsertFilme.setString(3, classificacao);
                                    psInsertFilme.setInt(4, duracao);
                                    psInsertFilme.executeUpdate();
                                    System.out.println("✅ Filme cadastrado com sucesso!");
                                    break;

                                case 2: // LISTAR
                                    rs = conn.prepareStatement("SELECT * FROM filmes").executeQuery();
                                    boolean temFilme = false;
                                    while (rs.next()) {
                                        temFilme = true;
                                        System.out.println("ID: " + rs.getInt("id_filme")
                                                + " | Título: " + rs.getString("titulo")
                                                + " | Gênero: " + rs.getString("genero")
                                                + " | Classificação: " + rs.getString("classificacao")
                                                + " | Duração: " + rs.getInt("duracao_minutos") + " min");
                                    }
                                    if (!temFilme)
                                        System.out.println("Nenhum filme cadastrado.");
                                    break;

                                case 3: // ATUALIZAR
                                    System.out.print("ID do filme a atualizar: ");
                                    int idFilmeUp = scanner.nextInt();
                                    scanner.nextLine();
                                    System.out.print("Novo título: ");
                                    String novoTitulo = scanner.nextLine();
                                    System.out.print("Novo gênero: ");
                                    String novoGenero = scanner.nextLine();
                                    System.out.print("Nova classificação: ");
                                    String novaClassificacao = scanner.nextLine();
                                    System.out.print("Nova duração (min): ");
                                    int novaDuracao = scanner.nextInt();
                                    scanner.nextLine();

                                    PreparedStatement psUpdateFilme = conn.prepareStatement(
                                            "UPDATE filmes SET titulo = ?, genero = ?, classificacao = ?, duracao_minutos = ? WHERE id_filme = ?");
                                    psUpdateFilme.setString(1, novoTitulo);
                                    psUpdateFilme.setString(2, novoGenero);
                                    psUpdateFilme.setString(3, novaClassificacao);
                                    psUpdateFilme.setInt(4, novaDuracao);
                                    psUpdateFilme.setInt(5, idFilmeUp);
                                    int linhasFilme = psUpdateFilme.executeUpdate();
                                    System.out
                                            .println(linhasFilme > 0 ? "✅ Filme atualizado!" : "❌ ID não encontrado.");
                                    break;

                                case 4: // DELETAR
                                    System.out.print("ID do filme a excluir: ");
                                    int idFilmeDel = scanner.nextInt();
                                    scanner.nextLine();

                                    PreparedStatement psDeleteFilme = conn.prepareStatement(
                                            "DELETE FROM filmes WHERE id_filme = ?");
                                    psDeleteFilme.setInt(1, idFilmeDel);
                                    int delFilme = psDeleteFilme.executeUpdate();
                                    System.out.println(delFilme > 0 ? "✅ Filme excluído!" : "❌ ID não encontrado.");
                                    break;

                                case 0: // VOLTAR
                                    System.out.println("Voltando ao menu principal...");
                                    break;

                                default: // OPÇÃO INVÁLIDA
                                    System.out.println("❌ Opção inválida!");
                            }
                        }
                        break;
                    }

                    // ==========================================
                    case 3: // MENU DE SESSÕES
                    // ==========================================
                    {
                        int opcaoSessao = -1;
                        while (opcaoSessao != 0) {
                            System.out.println("\n--- MENU: SESSÕES ---");
                            System.out.println("1 - Cadastrar sessão");
                            System.out.println("2 - Listar sessões");
                            System.out.println("3 - Atualizar sessão");
                            System.out.println("4 - Excluir sessão");
                            System.out.println("0 - Voltar");
                            System.out.print("Escolha: ");
                            opcaoSessao = scanner.nextInt();
                            scanner.nextLine();

                            switch (opcaoSessao) {
                                case 1: // CADASTRAR
                                    System.out.print("Data da sessão (DD/MM/AAAA): ");
                                    String dataSessao = scanner.nextLine().trim();
                                    // Converte DD/MM/AAAA para AAAA-MM-DD se necessário
                                    if (dataSessao.matches("\\d{2}/\\d{2}/\\d{4}")) {
                                        String[] partes = dataSessao.split("/");
                                        dataSessao = partes[2] + "-" + partes[1] + "-" + partes[0];
                                    }
                                    System.out.print("Horário (HH:MM): ");
                                    String horarioSessao = scanner.nextLine().trim();
                                    // Adiciona segundos se necessário (HH:MM → HH:MM:SS)
                                    if (horarioSessao.matches("\\d{2}:\\d{2}")) {
                                        horarioSessao = horarioSessao + ":00";
                                    }
                                    System.out.print("Valor do ingresso: ");
                                    double valorSessao = scanner.nextDouble();
                                    System.out.print("ID do filme: ");
                                    int filmeSessao = scanner.nextInt();
                                    System.out.print("ID da sala: ");
                                    int salaSessao = scanner.nextInt();
                                    scanner.nextLine();

                                    PreparedStatement psInsertSessao = conn.prepareStatement(
                                            "INSERT INTO sessoes (data_sessao, horario, valor_ingresso, id_filme, id_sala) VALUES (?, ?, ?, ?, ?)",
                                            Statement.RETURN_GENERATED_KEYS);
                                    psInsertSessao.setDate(1, Date.valueOf(dataSessao));
                                    psInsertSessao.setTime(2, Time.valueOf(horarioSessao));
                                    psInsertSessao.setDouble(3, valorSessao);
                                    psInsertSessao.setInt(4, filmeSessao);
                                    psInsertSessao.setInt(5, salaSessao);
                                    psInsertSessao.executeUpdate();

                                    // Pega o ID da sessão gerado automaticamente
                                    ResultSet rsIdSessao = psInsertSessao.getGeneratedKeys();
                                    if (rsIdSessao.next()) {
                                        int novoIdSessao = rsIdSessao.getInt(1);

                                        // Gera automaticamente 70 ingressos disponíveis para essa sessão
                                        PreparedStatement psGeraIngressos = conn.prepareStatement(
                                                "INSERT INTO ingressos (numero_assento, status, id_sessao) VALUES (?, 'disponivel', ?)");
                                        for (int assNum = 1; assNum <= 70; assNum++) {
                                            psGeraIngressos.setString(1, String.valueOf(assNum));
                                            psGeraIngressos.setInt(2, novoIdSessao);
                                            psGeraIngressos.addBatch();
                                        }
                                        psGeraIngressos.executeBatch();
                                        System.out.println("✅ Sessão cadastrada com sucesso! ID: " + novoIdSessao);
                                        System.out.println("   70 ingressos (assentos 1-70) gerados automaticamente.");
                                    }
                                    break;

                                case 2: // LISTAR
                                    rs = conn.prepareStatement("SELECT * FROM sessoes").executeQuery();
                                    boolean temSessao = false;
                                    while (rs.next()) {
                                        temSessao = true;
                                        System.out.println("ID: " + rs.getInt("id_sessao")
                                                + " | Data: " + rs.getDate("data_sessao")
                                                + " | Horário: " + rs.getTime("horario")
                                                + " | Valor: R$" + rs.getDouble("valor_ingresso")
                                                + " | ID Filme: " + rs.getInt("id_filme")
                                                + " | ID Sala: " + rs.getInt("id_sala"));
                                    }
                                    if (!temSessao)
                                        System.out.println("Nenhuma sessão cadastrada.");
                                    break;

                                case 3: // ATUALIZAR
                                    System.out.print("ID da sessão a atualizar: ");
                                    int idSessaoUp = scanner.nextInt();
                                    scanner.nextLine();
                                    System.out.print("Nova data (DD/MM/AAAA): ");
                                    String novaData = scanner.nextLine().trim();
                                    if (novaData.matches("\\d{2}/\\d{2}/\\d{4}")) {
                                        String[] partesUp = novaData.split("/");
                                        novaData = partesUp[2] + "-" + partesUp[1] + "-" + partesUp[0];
                                    }
                                    System.out.print("Novo horário (HH:MM): ");
                                    String novoHorario = scanner.nextLine().trim();
                                    if (novoHorario.matches("\\d{2}:\\d{2}")) {
                                        novoHorario = novoHorario + ":00";
                                    }
                                    System.out.print("Novo valor do ingresso: ");
                                    double novoValor = scanner.nextDouble();
                                    System.out.print("Novo ID do filme: ");
                                    int novoFilme = scanner.nextInt();
                                    System.out.print("Novo ID da sala: ");
                                    int novaSala = scanner.nextInt();
                                    scanner.nextLine();

                                    PreparedStatement psUpdateSessao = conn.prepareStatement(
                                            "UPDATE sessoes SET data_sessao = ?, horario = ?, valor_ingresso = ?, id_filme = ?, id_sala = ? WHERE id_sessao = ?");
                                    psUpdateSessao.setDate(1, Date.valueOf(novaData));
                                    psUpdateSessao.setTime(2, Time.valueOf(novoHorario));
                                    psUpdateSessao.setDouble(3, novoValor);
                                    psUpdateSessao.setInt(4, novoFilme);
                                    psUpdateSessao.setInt(5, novaSala);
                                    psUpdateSessao.setInt(6, idSessaoUp);
                                    int linhasSessao = psUpdateSessao.executeUpdate();
                                    System.out.println(
                                            linhasSessao > 0 ? "✅ Sessão atualizada!" : "❌ ID não encontrado.");
                                    break;

                                case 4: // DELETAR
                                    System.out.print("ID da sessão a excluir: ");
                                    int idSessaoDel = scanner.nextInt();
                                    scanner.nextLine();

                                    // Deleta primeiro os ingressos da sessão
                                    PreparedStatement psDeleteIngressosSessao = conn.prepareStatement(
                                            "DELETE FROM ingressos WHERE id_sessao = ?");
                                    psDeleteIngressosSessao.setInt(1, idSessaoDel);
                                    psDeleteIngressosSessao.executeUpdate();

                                    // Depois deleta a sessão
                                    PreparedStatement psDeleteSessao = conn.prepareStatement(
                                            "DELETE FROM sessoes WHERE id_sessao = ?");
                                    psDeleteSessao.setInt(1, idSessaoDel);
                                    int delSessao = psDeleteSessao.executeUpdate();
                                    System.out.println(delSessao > 0 ? "✅ Sessão e seus ingressos excluídos!"
                                            : "❌ ID não encontrado.");
                                    break;

                                case 0: // VOLTAR
                                    System.out.println("Voltando ao menu principal...");
                                    break;

                                default: // OPÇÃO INVÁLIDA
                                    System.out.println("❌ Opção inválida!");
                            }
                        }
                        break;
                    }

                    // ==========================================
                    case 4: // MENU DE VENDA DE INGRESSOS
                    // ==========================================
                    {
                        int opcaoIngresso = -1;
                        while (opcaoIngresso != 0) {
                            System.out.println("\n--- MENU: VENDA DE INGRESSOS ---");
                            System.out.println("1 - Ver ingressos disponíveis por sessão");
                            System.out.println("2 - Vender ingresso");
                            System.out.println("3 - Cancelar venda (devolver ingresso)");
                            System.out.println("0 - Voltar");
                            System.out.print("Escolha: ");
                            opcaoIngresso = scanner.nextInt();
                            scanner.nextLine();

                            switch (opcaoIngresso) {
                                case 1: // LISTAR DISPONÍVEIS
                                    System.out.print("Digite o ID da sessão: ");
                                    int idSessaoVer = scanner.nextInt();
                                    scanner.nextLine();

                                    PreparedStatement psDisponiveis = conn.prepareStatement(
                                            "SELECT * FROM ingressos WHERE id_sessao = ? AND status = 'disponivel' ORDER BY CAST(numero_assento AS UNSIGNED)");
                                    psDisponiveis.setInt(1, idSessaoVer);
                                    ResultSet rsDisp = psDisponiveis.executeQuery();

                                    boolean temDisponivel = false;
                                    int contDisp = 0;
                                    System.out.println("\n=== INGRESSOS DISPONIVEIS - Sessao " + idSessaoVer + " ===");
                                    System.out.println("------------------------------------------");
                                    while (rsDisp.next()) {
                                        temDisponivel = true;
                                        System.out.printf("%-6s", rsDisp.getString("numero_assento"));
                                        contDisp++;
                                        if (contDisp % 10 == 0) {
                                            System.out.println();
                                        }
                                    }
                                    if (contDisp % 10 != 0)
                                        System.out.println();
                                    System.out.println("------------------------------------------");
                                    if (!temDisponivel)
                                        System.out.println("Nenhum ingresso disponivel para esta sessao.");
                                    break;

                                case 2: // VENDER
                                    System.out.print("ID da sessão: ");
                                    int idSessaoVenda = scanner.nextInt();
                                    scanner.nextLine();

                                    // Mostra disponíveis antes de vender
                                    PreparedStatement psListaVenda = conn.prepareStatement(
                                            "SELECT * FROM ingressos WHERE id_sessao = ? AND status = 'disponivel' ORDER BY CAST(numero_assento AS UNSIGNED)");
                                    psListaVenda.setInt(1, idSessaoVenda);
                                    ResultSet rsVenda = psListaVenda.executeQuery();

                                    System.out.println("\nAssentos disponiveis:");
                                    System.out.println("------------------------------------------");
                                    boolean algumDisp = false;
                                    int contAssentos = 0;
                                    while (rsVenda.next()) {
                                        algumDisp = true;
                                        System.out.printf("%-6s", rsVenda.getString("numero_assento"));
                                        contAssentos++;
                                        if (contAssentos % 10 == 0) {
                                            System.out.println();
                                        }
                                    }
                                    if (contAssentos % 10 != 0)
                                        System.out.println();
                                    System.out.println("------------------------------------------");

                                    if (!algumDisp) {
                                        System.out.println("Nenhum ingresso disponivel para esta sessao.");
                                        break;
                                    }

                                    System.out.print("Numero do assento que deseja comprar: ");
                                    String assentoEscolhido = scanner.nextLine();

                                    PreparedStatement psVender = conn.prepareStatement(
                                            "UPDATE ingressos SET status = 'vendido' WHERE id_sessao = ? AND numero_assento = ? AND status = 'disponivel'");
                                    psVender.setInt(1, idSessaoVenda);
                                    psVender.setString(2, assentoEscolhido);
                                    int vendido = psVender.executeUpdate();

                                    if (vendido > 0) {
                                        System.out.println(
                                                "✅ Ingresso do assento " + assentoEscolhido + " vendido com sucesso!");
                                    } else {
                                        System.out.println("❌ Assento inválido ou já vendido.");
                                    }
                                    break;

                                case 3: // CANCELAR VENDA
                                    System.out.print("ID da sessão: ");
                                    int idSessaoCancelar = scanner.nextInt();
                                    scanner.nextLine();
                                    System.out.print("Número do assento a devolver: ");
                                    String assentoCancelar = scanner.nextLine();

                                    PreparedStatement psCancelar = conn.prepareStatement(
                                            "UPDATE ingressos SET status = 'disponivel' WHERE id_sessao = ? AND numero_assento = ? AND status = 'vendido'");
                                    psCancelar.setInt(1, idSessaoCancelar);
                                    psCancelar.setString(2, assentoCancelar);
                                    int cancelado = psCancelar.executeUpdate();

                                    if (cancelado > 0) {
                                        System.out.println("✅ Venda do assento " + assentoCancelar
                                                + " cancelada. Ingresso devolvido.");
                                    } else {
                                        System.out.println("❌ Assento inválido ou já disponível.");
                                    }
                                    break;

                                case 0:
                                    System.out.println("Voltando ao menu principal...");
                                    break;

                                default:
                                    System.out.println("❌ Opção inválida!");
                            }
                        }
                        break;
                    }

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