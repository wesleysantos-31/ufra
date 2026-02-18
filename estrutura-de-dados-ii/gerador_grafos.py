import tkinter as tk
from tkinter import messagebox, filedialog, ttk
import networkx as nx
import matplotlib.pyplot as plt
from matplotlib.backends.backend_tkagg import FigureCanvasTkAgg

class GraphApp:
    def __init__(self, root):
        self.root = root
        self.root.title("Construtor de Grafos - Álgebra e Estrutura de Dados")
        self.root.geometry("1100x750")
        
        # Inicializa o grafo e limites
        self.is_directed = tk.BooleanVar(value=False)
        self.G = nx.Graph()
        self.MAX_VERTICES = 10
        self.MAX_ARESTAS = 20
        
        # --- Layout Principal ---
        # Frame lateral para controles (com scroll se necessário, mas simplificado aqui)
        self.sidebar = tk.Frame(root, width=300, bg="#f0f0f0", padx=10, pady=10)
        self.sidebar.pack(side=tk.LEFT, fill=tk.Y)
        self.sidebar.pack_propagate(False) # Mantém a largura fixa
        
        # Frame central para o desenho
        self.canvas_frame = tk.Frame(root, bg="white")
        self.canvas_frame.pack(side=tk.RIGHT, fill=tk.BOTH, expand=True)
        
        self.setup_ui()
        self.plot_graph()

    def setup_ui(self):
        """Configura os botões e campos da interface lateral"""
        tk.Label(self.sidebar, text="CONSTRUTOR DE GRAFOS", font=("Arial", 12, "bold"), bg="#f0f0f0").pack(pady=(0, 10))
        
        # --- Tipo de Grafo ---
        tk.Checkbutton(self.sidebar, text="Grafo Direcionado", variable=self.is_directed, 
                       command=self.toggle_graph_type, bg="#f0f0f0").pack(anchor="w")
        tk.Label(self.sidebar, text="(Mudar apaga o grafo atual!)", font=("Arial", 7), fg="red", bg="#f0f0f0").pack(anchor="w", pady=(0, 5))

        tk.Label(self.sidebar, text="-"*40, bg="#f0f0f0").pack(pady=5)
        
        # --- Seção de Vértices ---
        tk.Label(self.sidebar, text="1. Adicionar Vértice:", bg="#f0f0f0", font=("Arial", 10, "bold")).pack(anchor="w")
        self.node_entry = tk.Entry(self.sidebar)
        self.node_entry.pack(fill=tk.X, pady=2)
        tk.Button(self.sidebar, text="Adicionar Vértice", command=self.add_node, bg="#e1e1e1").pack(fill=tk.X, pady=2)
        
        tk.Label(self.sidebar, text="-"*40, bg="#f0f0f0").pack(pady=5)
        
        # --- Seção de Arestas ---
        tk.Label(self.sidebar, text="2. Adicionar Aresta:", bg="#f0f0f0", font=("Arial", 10, "bold")).pack(anchor="w")
        
        frame_edge = tk.Frame(self.sidebar, bg="#f0f0f0")
        frame_edge.pack(fill=tk.X)
        
        tk.Label(frame_edge, text="Origem:", bg="#f0f0f0", font=("Arial", 8)).grid(row=0, column=0, sticky="w")
        self.edge_u_entry = tk.Entry(frame_edge, width=10)
        self.edge_u_entry.grid(row=0, column=1, padx=2)
        
        tk.Label(frame_edge, text="Destino:", bg="#f0f0f0", font=("Arial", 8)).grid(row=1, column=0, sticky="w")
        self.edge_v_entry = tk.Entry(frame_edge, width=10)
        self.edge_v_entry.grid(row=1, column=1, padx=2)
        
        tk.Label(frame_edge, text="Peso:", bg="#f0f0f0", font=("Arial", 8)).grid(row=2, column=0, sticky="w")
        self.edge_w_entry = tk.Entry(frame_edge, width=10)
        self.edge_w_entry.insert(0, "1.0")
        self.edge_w_entry.grid(row=2, column=1, padx=2)

        tk.Button(self.sidebar, text="Conectar", command=self.add_edge, bg="#e1e1e1").pack(fill=tk.X, pady=5)
        
        tk.Label(self.sidebar, text="-"*40, bg="#f0f0f0").pack(pady=5)

        # --- Algoritmos ---
        tk.Label(self.sidebar, text="3. Algoritmos:", bg="#f0f0f0", font=("Arial", 10, "bold")).pack(anchor="w")
        
        # Caminho Mais Curto
        tk.Label(self.sidebar, text="Caminho Mais Curto:", bg="#f0f0f0", font=("Arial", 9, "underline")).pack(anchor="w", pady=(5,0))
        frame_path = tk.Frame(self.sidebar, bg="#f0f0f0")
        frame_path.pack(fill=tk.X)
        tk.Label(frame_path, text="De:", bg="#f0f0f0").pack(side=tk.LEFT)
        self.path_start = tk.Entry(frame_path, width=5)
        self.path_start.pack(side=tk.LEFT, padx=2)
        tk.Label(frame_path, text="Para:", bg="#f0f0f0").pack(side=tk.LEFT)
        self.path_end = tk.Entry(frame_path, width=5)
        self.path_end.pack(side=tk.LEFT, padx=2)
        tk.Button(self.sidebar, text="Calcular", command=self.calc_shortest_path, bg="#d9edf7").pack(fill=tk.X, pady=2)

        # Busca
        tk.Label(self.sidebar, text="Busca (BFS/DFS):", bg="#f0f0f0", font=("Arial", 9, "underline")).pack(anchor="w", pady=(5,0))
        frame_search = tk.Frame(self.sidebar, bg="#f0f0f0")
        frame_search.pack(fill=tk.X)
        tk.Label(frame_search, text="Início:", bg="#f0f0f0").pack(side=tk.LEFT)
        self.search_start = tk.Entry(frame_search, width=5)
        self.search_start.pack(side=tk.LEFT, padx=5)
        
        self.search_type = tk.StringVar(value="BFS")
        ttk.Combobox(frame_search, textvariable=self.search_type, values=["BFS", "DFS"], width=5, state="readonly").pack(side=tk.LEFT)
        
        tk.Button(self.sidebar, text="Executar Busca", command=self.perform_search, bg="#d9edf7").pack(fill=tk.X, pady=2)
        
        tk.Label(self.sidebar, text="-"*40, bg="#f0f0f0").pack(pady=5)
        
        # --- Visualização ---
        tk.Label(self.sidebar, text="4. Visualização:", bg="#f0f0f0", font=("Arial", 10, "bold")).pack(anchor="w")
        
        tk.Label(self.sidebar, text="Espaçamento:", bg="#f0f0f0").pack(anchor="w")
        
        self.spacing_var = tk.DoubleVar(value=1.5)
        self.spacing_slider = tk.Scale(self.sidebar, from_=0.1, to=5.0, resolution=0.1, 
                                     orient=tk.HORIZONTAL, variable=self.spacing_var, 
                                     command=lambda x: self.plot_graph(), bg="#f0f0f0")
        self.spacing_slider.pack(fill=tk.X)
        
        tk.Label(self.sidebar, text="-"*40, bg="#f0f0f0").pack(pady=5)
        
        # --- Estatísticas e Ações ---
        self.stats_label = tk.Label(self.sidebar, text="V: 0 | A: 0", bg="#f0f0f0", fg="blue", font=("Arial", 10, "bold"))
        self.stats_label.pack(pady=5)
        
        tk.Button(self.sidebar, text="Limpar Tudo", command=self.clear_graph, bg="#ffcccc").pack(fill=tk.X, pady=2)
        tk.Button(self.sidebar, text="Exportar Imagem", command=self.save_image, bg="#ccffcc").pack(fill=tk.X, pady=2)

    def toggle_graph_type(self):
        """Reinicia o grafo com o novo tipo selecionado"""
        if messagebox.askyesno("Confirmar", "Mudar o tipo apagará o grafo atual. Continuar?"):
            self.G.clear()
            if self.is_directed.get():
                self.G = nx.DiGraph()
            else:
                self.G = nx.Graph()
            self.plot_graph()
        else:
            # Reverte a seleção se o usuário cancelar
            self.is_directed.set(not self.is_directed.get())

    def plot_graph(self, path_nodes=None, search_nodes=None):
        """Renderiza o grafo no frame do canvas"""
        for widget in self.canvas_frame.winfo_children():
            widget.destroy()
            
        fig, ax = plt.subplots(figsize=(6, 5))
        
        if self.G.number_of_nodes() > 0:
            k_val = self.spacing_var.get()
            pos = nx.spring_layout(self.G, k=k_val, seed=42)
            
            node_colors = ['skyblue'] * self.G.number_of_nodes()
            edge_colors = ['gray'] * self.G.number_of_edges()
            
            if path_nodes:
                # Mapeia nós para índices para colorir
                node_list = list(self.G.nodes())
                node_colors = ['#ff9999' if n in path_nodes else 'skyblue' for n in node_list]
                
                # Destacar arestas do caminho
                edge_list = list(self.G.edges())
                path_edges = list(zip(path_nodes, path_nodes[1:]))
                if not self.is_directed.get():
                     # Para não direcionado, a ordem na tupla pode variar
                     path_edges_set = {frozenset(e) for e in path_edges}
                     edge_colors = ['red' if frozenset(e) in path_edges_set else 'gray' for e in edge_list]
                else:
                    edge_colors = ['red' if e in path_edges else 'gray' for e in edge_list]

            elif search_nodes:
                 node_list = list(self.G.nodes())
                 node_colors = ['#99ff99' if n in search_nodes else 'skyblue' for n in node_list]

            # Desenha nós e arestas
            nx.draw(self.G, pos, with_labels=True, node_color=node_colors, 
                    node_size=800, edge_color=edge_colors, font_size=12, 
                    font_weight='bold', width=2, ax=ax, arrows=self.is_directed.get())
            
            # Desenha pesos das arestas
            edge_labels = nx.get_edge_attributes(self.G, 'weight')
            if edge_labels:
                nx.draw_networkx_edge_labels(self.G, pos, edge_labels=edge_labels)
                
            title = "Grafo Direcionado" if self.is_directed.get() else "Grafo Não Direcionado"
            ax.set_title(title)
        else:
            ax.text(0.5, 0.5, "Adicione vértices para começar", 
                    horizontalalignment='center', verticalalignment='center')
            ax.axis('off')

        canvas = FigureCanvasTkAgg(fig, master=self.canvas_frame)
        canvas.draw()
        canvas.get_tk_widget().pack(fill=tk.BOTH, expand=True)
        plt.close(fig)
        
        # Atualiza contadores
        self.stats_label.config(text=f"V: {self.G.number_of_nodes()} | A: {self.G.number_of_edges()}")

    def add_node(self):
        nome = self.node_entry.get().strip()
        if not nome:
            messagebox.showwarning("Erro", "Digite um nome para o vértice.")
            return
        if self.G.number_of_nodes() >= self.MAX_VERTICES:
            messagebox.showerror("Limite", f"Limite de {self.MAX_VERTICES} vértices atingido!")
            return
        
        self.G.add_node(nome)
        self.node_entry.delete(0, tk.END)
        self.plot_graph()

    def add_edge(self):
        u = self.edge_u_entry.get().strip()
        v = self.edge_v_entry.get().strip()
        try:
            w = float(self.edge_w_entry.get().strip())
        except ValueError:
            messagebox.showerror("Erro", "O peso deve ser um número.")
            return
        
        if u not in self.G.nodes() or v not in self.G.nodes():
            messagebox.showwarning("Erro", "Um ou ambos os vértices não existem.")
            return
        if self.G.number_of_edges() >= self.MAX_ARESTAS:
            messagebox.showerror("Limite", f"Limite de {self.MAX_ARESTAS} arestas atingido!")
            return
            
        self.G.add_edge(u, v, weight=w)
        self.edge_u_entry.delete(0, tk.END)
        self.edge_v_entry.delete(0, tk.END)
        self.edge_w_entry.delete(0, tk.END)
        self.edge_w_entry.insert(0, "1.0")
        self.plot_graph()

    def calc_shortest_path(self):
        start = self.path_start.get().strip()
        end = self.path_end.get().strip()
        
        if start not in self.G or end not in self.G:
            messagebox.showwarning("Erro", "Vértices de início ou fim inválidos.")
            return
            
        try:
            path = nx.shortest_path(self.G, source=start, target=end, weight='weight')
            length = nx.shortest_path_length(self.G, source=start, target=end, weight='weight')
            messagebox.showinfo("Caminho Mínimo", f"Caminho: {' -> '.join(path)}\nCusto Total: {length}")
            self.plot_graph(path_nodes=path)
        except nx.NetworkXNoPath:
            messagebox.showinfo("Resultado", "Não existe caminho entre estes vértices.")
        except Exception as e:
            messagebox.showerror("Erro", str(e))

    def perform_search(self):
        start = self.search_start.get().strip()
        algo = self.search_type.get()
        
        if start not in self.G:
            messagebox.showwarning("Erro", "Vértice de início inválido.")
            return
            
        try:
            if algo == "BFS":
                edges = list(nx.bfs_edges(self.G, source=start))
                nodes = [start] + [v for u, v in edges]
            else: # DFS
                edges = list(nx.dfs_edges(self.G, source=start))
                nodes = [start] + [v for u, v in edges]
            
            msg = f"Ordem de Visita ({algo}):\n{' -> '.join(nodes)}"
            messagebox.showinfo(f"Busca {algo}", msg)
            self.plot_graph(search_nodes=nodes)
        except Exception as e:
            messagebox.showerror("Erro", str(e))

    def clear_graph(self):
        if messagebox.askyesno("Limpar", "Deseja apagar todo o grafo?"):
            self.G.clear()
            self.plot_graph()

    def save_image(self):
        if self.G.number_of_nodes() == 0:
            messagebox.showwarning("Vazio", "Não há nada para salvar.")
            return
            
        file_path = filedialog.asksaveasfilename(defaultextension=".png", 
                                                filetypes=[("PNG files", "*.png"), ("All files", "*.*")])
        if file_path:
            plt.figure(figsize=(8, 6))
            pos = nx.spring_layout(self.G, seed=42)
            nx.draw(self.G, pos, with_labels=True, node_color='skyblue', node_size=800, arrows=self.is_directed.get())
            # Adiciona labels se houver pesos
            edge_labels = nx.get_edge_attributes(self.G, 'weight')
            if edge_labels:
                nx.draw_networkx_edge_labels(self.G, pos, edge_labels=edge_labels)
                
            plt.savefig(file_path)
            plt.close()
            messagebox.showinfo("Sucesso", f"Imagem salva em: {file_path}")

if __name__ == "__main__":
    root = tk.Tk()
    app = GraphApp(root)
    root.mainloop()