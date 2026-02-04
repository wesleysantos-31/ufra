import tkinter as tk
from tkinter import messagebox, filedialog
import networkx as nx
import matplotlib.pyplot as plt
from matplotlib.backends.backend_tkagg import FigureCanvasTkAgg

class GraphApp:
    def __init__(self, root):
        self.root = root
        self.root.title("Construtor de Grafos - Álgebra e Estrutura de Dados")
        self.root.geometry("1000x700")
        
        # Inicializa o grafo e limites
        self.G = nx.Graph()
        self.MAX_VERTICES = 10
        self.MAX_ARESTAS = 20
        
        # --- Layout Principal ---
        # Frame lateral para controles
        self.sidebar = tk.Frame(root, width=250, bg="#f0f0f0", padx=10, pady=10)
        self.sidebar.pack(side=tk.LEFT, fill=tk.Y)
        
        # Frame central para o desenho
        self.canvas_frame = tk.Frame(root, bg="white")
        self.canvas_frame.pack(side=tk.RIGHT, fill=tk.BOTH, expand=True)
        
        self.setup_ui()
        self.plot_graph()

    def setup_ui(self):
        """Configura os botões e campos da interface lateral"""
        tk.Label(self.sidebar, text="CONSTRUTOR DE GRAFOS", font=("Arial", 12, "bold"), bg="#f0f0f0").pack(pady=10)
        
        # Seção de Vértices
        tk.Label(self.sidebar, text="Adicionar Vértice:", bg="#f0f0f0").pack(anchor="w")
        self.node_entry = tk.Entry(self.sidebar)
        self.node_entry.pack(fill=tk.X, pady=5)
        tk.Button(self.sidebar, text="Adicionar Vértice", command=self.add_node, bg="#e1e1e1").pack(fill=tk.X, pady=5)
        
        tk.Label(self.sidebar, text="-"*30, bg="#f0f0f0").pack(pady=10)
        
        # Seção de Arestas
        tk.Label(self.sidebar, text="Adicionar Aresta:", bg="#f0f0f0").pack(anchor="w")
        tk.Label(self.sidebar, text="Origem:", bg="#f0f0f0", font=("Arial", 8)).pack(anchor="w")
        self.edge_u_entry = tk.Entry(self.sidebar)
        self.edge_u_entry.pack(fill=tk.X, pady=2)
        
        tk.Label(self.sidebar, text="Destino:", bg="#f0f0f0", font=("Arial", 8)).pack(anchor="w")
        self.edge_v_entry = tk.Entry(self.sidebar)
        self.edge_v_entry.pack(fill=tk.X, pady=2)
        
        tk.Button(self.sidebar, text="Conectar Vértices", command=self.add_edge, bg="#e1e1e1").pack(fill=tk.X, pady=5)
        
        tk.Label(self.sidebar, text="-"*30, bg="#f0f0f0").pack(pady=10)
        
        # Estatísticas
        self.stats_label = tk.Label(self.sidebar, text="Vértices: 0 | Arestas: 0", bg="#f0f0f0", fg="blue")
        self.stats_label.pack(pady=5)
        
        # Ações Finais
        tk.Button(self.sidebar, text="Limpar Tudo", command=self.clear_graph, bg="#ffcccc").pack(fill=tk.X, pady=5)
        tk.Button(self.sidebar, text="Exportar Imagem (PNG)", command=self.save_image, bg="#ccffcc").pack(fill=tk.X, pady=5)
        
        tk.Label(self.sidebar, text="Limites: 10 Vértices / 20 Arestas", font=("Arial", 7), bg="#f0f0f0").pack(side=tk.BOTTOM)

    def plot_graph(self):
        """Renderiza o grafo no frame do canvas"""
        for widget in self.canvas_frame.winfo_children():
            widget.destroy()
            
        fig, ax = plt.subplots(figsize=(6, 5))
        
        if self.G.number_of_nodes() > 0:
            pos = nx.spring_layout(self.G, seed=42)
            nx.draw(self.G, pos, with_labels=True, node_color='skyblue', 
                    node_size=800, edge_color='gray', font_size=12, 
                    font_weight='bold', width=2, ax=ax)
            ax.set_title(f"Visualização do Grafo")
        else:
            ax.text(0.5, 0.5, "Adicione vértices para começar", 
                    horizontalalignment='center', verticalalignment='center')
            ax.axis('off')

        canvas = FigureCanvasTkAgg(fig, master=self.canvas_frame)
        canvas.draw()
        canvas.get_tk_widget().pack(fill=tk.BOTH, expand=True)
        plt.close(fig)
        
        # Atualiza contadores
        self.stats_label.config(text=f"Vértices: {self.G.number_of_nodes()} | Arestas: {self.G.number_of_edges()}")

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
        
        if u not in self.G.nodes() or v not in self.G.nodes():
            messagebox.showwarning("Erro", "Um ou ambos os vértices não existem.")
            return
        if self.G.number_of_edges() >= self.MAX_ARESTAS:
            messagebox.showerror("Limite", f"Limite de {self.MAX_ARESTAS} arestas atingido!")
            return
            
        self.G.add_edge(u, v)
        self.edge_u_entry.delete(0, tk.END)
        self.edge_v_entry.delete(0, tk.END)
        self.plot_graph()

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
            nx.draw(self.G, pos, with_labels=True, node_color='skyblue', node_size=800)
            plt.savefig(file_path)
            plt.close()
            messagebox.showinfo("Sucesso", f"Imagem salva em: {file_path}")

if __name__ == "__main__":
    root = tk.Tk()
    app = GraphApp(root)
    root.mainloop()