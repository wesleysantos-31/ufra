#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <locale.h>

#define NOME_ARQUIVO "catalogo.txt"

typedef struct {
    char nome[100];
    char diretor[100];
    char genero[50];
    int ano;
    float nota;
} Filme;

void cadastrarFilme() {
    setlocale(LC_ALL, "portuguese");
    FILE *arquivo = fopen(NOME_ARQUIVO, "a");
    Filme filme;

    printf("+--------------------------------------+\n");
    printf("¦        CADASTRO DE NOVO FILME        ¦\n");
    printf("+--------------------------------------+\n");

    printf("Nome do filme: ");
    fgets(filme.nome, 100, stdin);
    filme.nome[strcspn(filme.nome, "\n")] = '\0';

    printf("Diretor: ");
    fgets(filme.diretor, 100, stdin);
    filme.diretor[strcspn(filme.diretor, "\n")] = '\0';

    printf("Gênero: ");
    fgets(filme.genero, 50, stdin);
    filme.genero[strcspn(filme.genero, "\n")] = '\0';

    printf("Ano: ");
    scanf("%d", &filme.ano);

    printf("Nota (0-10): ");
    scanf("%f", &filme.nota);
    getchar();

    fprintf(arquivo, "NOME: %s\n", filme.nome);
    fprintf(arquivo, "DIRETOR: %s\n", filme.diretor);
    fprintf(arquivo, "GÊNERO: %s\n", filme.genero);
    fprintf(arquivo, "ANO: %d\n", filme.ano);
    fprintf(arquivo, "NOTA: %.1f\n\n", filme.nota);

    fclose(arquivo);
    printf("\nFilme cadastrado com sucesso!\n");
}

void listarFilmes() {
    FILE *arquivo = fopen(NOME_ARQUIVO, "r");
    if (arquivo == NULL) {
        printf("Erro: Nenhum filme cadastrado.\n");
        return;
    }

    Filme filme;
    int contador = 1;

    printf("+--------------------------------------+\n");
    printf("¦           LISTA DE FILMES            ¦\n");
    printf("+--------------------------------------+\n\n");

    while (fscanf(arquivo, "NOME: %[^\n]\nDIRETOR: %[^\n]\nGÊNERO: %[^\n]\nANO: %d\nNOTA: %f\n\n",
                 filme.nome, filme.diretor, filme.genero, &filme.ano, &filme.nota) != EOF) {
        
        printf("Filme %d:\n", contador++);
        printf("   - Nome: %s\n", filme.nome);
        printf("   - Diretor: %s\n", filme.diretor);
        printf("   - Gênero: %s\n", filme.genero);
        printf("   - Ano: %d\n", filme.ano);
        printf("   - Nota: %.1f\n", filme.nota);
        printf("----------------------------------------\n");
    }

    fclose(arquivo);

    if (contador == 1) {
        printf("Nenhum filme cadastrado.\n");
    }
}

void excluirFilme() {
    char nomeExcluir[100];
    printf("+--------------------------------------+\n");
    printf("¦       EXCLUIR FILME DO CATÁLOGO      ¦\n");
    printf("+--------------------------------------+\n");
    printf("Digite o nome do filme a excluir: ");
    fgets(nomeExcluir, 100, stdin);
    nomeExcluir[strcspn(nomeExcluir, "\n")] = '\0';

    FILE *arquivo = fopen(NOME_ARQUIVO, "r");
    if (arquivo == NULL) {
        printf("Erro: Nenhum filme cadastrado.\n");
        return;
    }

    FILE *temp = fopen("temp.txt", "w");
    Filme filme;
    int encontrado = 0;

    while (fscanf(arquivo, "NOME: %[^\n]\nDIRETOR: %[^\n]\nGÊNERO: %[^\n]\nANO: %d\nNOTA: %f\n\n",
                 filme.nome, filme.diretor, filme.genero, &filme.ano, &filme.nota) != EOF) {

        if (strcmp(filme.nome, nomeExcluir) == 0) {
            encontrado = 1;
            printf("\nFilme \"%s\" removido.\n", filme.nome);
        } else {
            fprintf(temp, "NOME: %s\nDIRETOR: %s\nGÊNERO: %s\nANO: %d\nNOTA: %.1f\n\n",
                   filme.nome, filme.diretor, filme.genero, filme.ano, filme.nota);
        }
    }

    fclose(arquivo);
    fclose(temp);

    remove(NOME_ARQUIVO);
    rename("temp.txt", NOME_ARQUIVO);

    if (!encontrado) {
        printf("Filme não encontrado.\n");
    }
}

void alterarFilme() {
    FILE *arquivo = fopen(NOME_ARQUIVO, "r");
    if (arquivo == NULL) {
        printf("Erro: Nenhum filme cadastrado.\n");
        return;
    }

    FILE *temp = fopen("temp.txt", "w");
    if (temp == NULL) {
        printf("Erro ao criar arquivo temporário.\n");
        fclose(arquivo);
        return;
    }

    Filme filme;
    char titulo[100];
    int encontrado = 0;

    printf("+--------------------------------------+\n");
    printf("¦        ALTERAR DADOS DO FILME        ¦\n");
    printf("+--------------------------------------+\n");
    printf("Digite o título: ");
    fgets(titulo, sizeof(titulo), stdin);
    titulo[strcspn(titulo, "\n")] = '\0';

    while (fscanf(arquivo, "NOME: %[^\n]\nDIRETOR: %[^\n]\nGÊNERO: %[^\n]\nANO: %d\nNOTA: %f\n\n",
                  filme.nome, filme.diretor, filme.genero, &filme.ano, &filme.nota) != EOF) {

        if (strcmp(filme.nome, titulo) == 0) {
            encontrado = 1;
            printf("\nDigite os novos dados:\n");

            printf("Nome: ");
            fgets(filme.nome, sizeof(filme.nome), stdin);
            filme.nome[strcspn(filme.nome, "\n")] = '\0';

            printf("Diretor: ");
            fgets(filme.diretor, sizeof(filme.diretor), stdin);
            filme.diretor[strcspn(filme.diretor, "\n")] = '\0';

            printf("Gênero: ");
            fgets(filme.genero, sizeof(filme.genero), stdin);
            filme.genero[strcspn(filme.genero, "\n")] = '\0';

            printf("Ano: ");
            scanf("%d", &filme.ano);
            getchar();

            printf("Nota: ");
            scanf("%f", &filme.nota);
            getchar();
        }

        fprintf(temp, "NOME: %s\nDIRETOR: %s\nGÊNERO: %s\nANO: %d\nNOTA: %.1f\n\n",
                filme.nome, filme.diretor, filme.genero, filme.ano, filme.nota);
    }

    fclose(arquivo);
    fclose(temp);

    remove(NOME_ARQUIVO);
    rename("temp.txt", NOME_ARQUIVO);

    if (encontrado) {
        printf("\nFilme atualizado com sucesso!\n");
    } else {
        printf("Filme não encontrado no catálogo.\n");
    }
}

int main() {
    setlocale(LC_ALL, "portuguese");
    int opcao;

    do {
        printf("\n+--------------------------------------+\n");
        printf("¦          CATÁLOGO DE FILMES          ¦\n");
        printf("+--------------------------------------+\n");
        printf("1. Cadastrar filme\n");
        printf("2. Listar filmes\n");
        printf("3. Excluir filme\n");
        printf("4. Alterar filme\n");
        printf("5. Sair\n");
        printf("----------------------------------------\n");
        printf("Escolha: ");
        scanf("%d", &opcao);
        getchar();
        printf("\n");

        switch (opcao) {
            case 1:
                cadastrarFilme();
                break;
            case 2:
                listarFilmes();
                break;
            case 3:
                excluirFilme();
                break;
            case 4:
                alterarFilme();
                break;
            case 5:
                printf("Encerrando o programa...\n");
                break;
            default:
                printf("Opção inválida. Tente novamente.\n");
        }
    } while (opcao != 5);

    return 0;
}


