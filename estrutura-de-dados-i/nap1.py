class Nodo:
    valor = 0
    prox = None
    ante = None

class Lista_Dupla:
    pri = None
    ult = None
    
    def novo_nodo(self, valor):
        novoNodo = Nodo()
        novoNodo.valor = valor
        if self.pri is None:
            self.pri = novoNodo
            self.ult = novoNodo
            return novoNodo, True
        return novoNodo, False
    
    def inserir_inicio(self, valor):
        novoNodo, primeiro = self.novo_nodo(valor)
        if primeiro == False:
            self.pri.ante = novoNodo
            novoNodo.prox = self.pri
            self.pri = novoNodo
    
    def inserir_final(self, valor):
        novoNodo, primeiro = self.novo_nodo(valor)
        if primeiro == False:
            self.ult.prox = novoNodo
            novoNodo.ante = self.ult
            self.ult = novoNodo
    
    def vazio(self):
        if self.pri is None:
            return True
        return False
        
    def remover_inicio(self):
        if self.vazio(): 
            print("Lista Vazia")
            return
        if self.pri == self.ult:
            self.pri = None
            self.ult = None
            return
        self.pri = self.pri.prox
        self.pri.ante = None
    
    def remover_final(self):
        if self.vazio(): 
            print("Lista Vazia")
            return
        if self.pri == self.ult:
            self.pri = None
            self.ult = None
            return
        self.ult = self.ult.ante
        self.ult.prox = None
    
    def print(self, para_frente=True):
        if self.vazio():
            print("Lista Vazia")
            return
        
        print("Lista atual:", end=' ')
        if para_frente:
            nodoAtual = self.pri
            while nodoAtual is not None:
                print(nodoAtual.valor, end=' <> ')
                nodoAtual = nodoAtual.prox
        else:
            nodoAtual = self.ult
            while nodoAtual is not None:
                print(nodoAtual.valor, end=' <> ')
                nodoAtual = nodoAtual.ante
        print("None")
    
    def inserir_apos_valor(self, valor_busca, novo_valor):
        """
        Insere um novo nodo com 'novo_valor' após o nodo que contém 
        'valor_busca'. Se 'valor_busca' não for encontrado, insere 
        'novo_valor' no final da lista.
        AGORA COM PRINTS PARA VISUALIZAÇÃO.
        """
        print(f"\n-> Tentando inserir {novo_valor} após {valor_busca}...")

        nodo_atual = self.pri
        
        while nodo_atual is not None:
            if nodo_atual.valor == valor_busca:
                # --- VALOR ENCONTRADO ---
                print(f"   - Valor {valor_busca} encontrado.")
                
                if nodo_atual == self.ult:
                    print(f"   - O valor encontrado é o último. Inserindo {novo_valor} no final.")
                    self.inserir_final(novo_valor)
                    return

                print(f"   - Inserindo {novo_valor} no meio da lista, após {nodo_atual.valor}.")
                novo_nodo = Nodo()
                novo_nodo.valor = novo_valor
                
                novo_nodo.prox = nodo_atual.prox
                novo_nodo.ante = nodo_atual
                nodo_atual.prox.ante = novo_nodo
                nodo_atual.prox = novo_nodo
                
                return

            nodo_atual = nodo_atual.prox
            
        # --- VALOR NÃO ENCONTRADO ---
        print(f"   - Valor {valor_busca} não foi encontrado na lista.")
        print(f"   - Inserindo {novo_valor} no final, conforme a regra.")
        self.inserir_final(novo_valor)

        # Criando uma instância da lista
minha_lista = Lista_Dupla()

# Inserindo valores iniciais
minha_lista.inserir_final(10)
minha_lista.inserir_final(20)
minha_lista.inserir_final(40)
minha_lista.inserir_final(50)

print("Lista original:")
minha_lista.print()

print("\n--- Teste 1: Inserir 30 após o 20 (caso de inserção no meio) ---")
minha_lista.inserir_apos_valor(20, 30)
minha_lista.print()

print("\n--- Teste 2: Inserir 60 após o 50 (caso de inserção no final) ---")
minha_lista.inserir_apos_valor(50, 60)
minha_lista.print()

print("\n--- Teste 3: Inserir 99 após o 100 (valor não encontrado) ---")
minha_lista.inserir_apos_valor(100, 99)
minha_lista.print()