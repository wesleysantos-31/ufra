# Relatório de Atividades - Configuração do Ambiente e Análise

## Resumo
Este relatório detalha a configuração do ambiente para execução do `gerador_grafos.py` e fornece uma análise da estrutura do código.

## Estrutura do Código (`gerador_grafos.py`)
O código é uma aplicação desktop construída em Python que permite a criação visual de grafos.

### Tecnologias Utilizadas
- **Tkinter**: Biblioteca padrão do Python para criação da Interface Gráfica (GUI). Gerencia janelas, botões e campos de entrada.
- **NetworkX**: Biblioteca poderosa para criação, manipulação e estudo da estrutura de redes complexas. É usada aqui para manter a lógica do grafo (nós e arestas).
- **Matplotlib**: Biblioteca de plotagem usada para desenhar o grafo visualmente dentro da interface Tkinter (via `FigureCanvasTkAgg`).

### Lógica de Implementação
O código é encapsulado na classe `GraphApp`:
1. **Inicialização (`__init__`)**: Configura a janela principal, inicializa um grafo vazio (`nx.Graph`) e define as áreas de layout (barra lateral e área de desenho).
2. **Setup da Interface (`setup_ui`)**: Cria os controles para interagir com o usuário:
   - Entradas para adicionar vértices (nós).
   - Entradas para adicionar arestas (ligações entre nós).
   - Botões de ação (Adicionar, Limpar, Salvar).
   - Mostrador de estatísticas.
3. **Renderização (`plot_graph`)**:
   - Limpa a área de desenho atual.
   - Usa `nx.draw` com o layout `spring_layout` (que tenta posicionar nós de forma esteticamente agradável) para gerar a imagem.
   - Integra a figura do Matplotlib na janela do Tkinter.
4. **Interatividade**:
   - Métodos como `add_node` e `add_edge` validam a entrada do usuário (ex: verifica limites de 10 vértices/20 arestas) antes de atualizar o modelo do grafo e redesenhar.

## Diagnóstico do Ambiente
Antes da execução, identificamos:
- **Python**: Versão 3.13.5 instalada.
- **Pip**: Ausente na instalação inicial.
- **Dependências**: Faltavam `networkx` e `matplotlib`.

## Ações Realizadas

### 1. Instalação do Pip
Instalado via módulo do Python:
```bash
python -m ensurepip --default-pip
```

### 2. Instalação de Bibliotecas
Dependências instaladas:
```bash
python -m pip install networkx matplotlib
```

## Validação
O script foi executado com sucesso:
```bash
python gerador_grafos.py
```
A aplicação abriu corretamente, permitindo a inserção de nós e arestas conforme programado.
