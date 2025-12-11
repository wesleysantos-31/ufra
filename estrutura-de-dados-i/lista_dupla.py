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
            print("Lista Vazia")
            return True
        
    def remover_inicio(self):
        if self.vazio(): return
        if self.pri == self.ult:
            self.pri = None
            self.ult = None
            return
        self.pri = self.pri.prox
        self.pri.ante = None
    
    def remover_final(self):
        if self.vazio(): return
        if self.pri == self.ult:
            self.pri = None
            self.ult = None
            return
        self.ult = self.ult.ante
        self.ult.prox = None
    
    def print(self, para_frente):
        if self.vazio(): return
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
    
    def inserir_apos_valor(self, valor_busca, novo_valor):
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

    def remover_valor(self, valor):
        if self.vazio(): return
        
        nodoAtual = self.pri
        while nodoAtual is not None:
            if nodoAtual.valor == valor:
                
                if self.pri == self.ult:
                    self.pri = None
                    self.ult = None
            
                elif nodoAtual == self.pri:
                    self.pri = nodoAtual.prox
                    self.pri.ante = None
            
                elif nodoAtual == self.ult:
                    self.ult = nodoAtual.ante
                    self.ult.prox = None
            
                else:
                    nodoAtual.ante.prox = nodoAtual.prox
                    nodoAtual.prox.ante = nodoAtual.ante
                return 
            nodoAtual = nodoAtual.prox
    
    def trocar_valores(self, valor_busca1, valor_busca2):
        if self.vazio(): return
        
        nodo1 = None
        nodo2 = None
        
        nodoAtual = self.pri
        while nodoAtual is not None:
            if nodoAtual.valor == valor_busca1:
                nodo1 = nodoAtual
            if nodoAtual.valor == valor_busca2:
                nodo2 = nodoAtual
            nodoAtual = nodoAtual.prox
        
        if nodo1 is not None and nodo2 is not None:
            nodo1.valor, nodo2.valor = nodo2.valor, nodo1.valor
        return
    
    def tamanho(self):
        # 1. Inicia um contador para armazenar o tamanho
        contador = 0
        
        # 2. Cria uma variável temporária para percorrer a lista, começando pelo primeiro nodo
        nodo_atual = self.pri
        
        # 3. Percorre a lista até o final (enquanto o nodo atual não for Nulo)
        while nodo_atual is not None:
            # 4. Para cada nodo visitado, incrementa o contador
            contador += 1
            # 5. Avança para o próximo nodo da lista
            nodo_atual = nodo_atual.prox
            
        # 6. Após o fim do laço, retorna o total contado
        return contador
    
    def prioridade(self, valor):
        # 1. ENCONTRAR O NODO
        nodo_alvo = self.pri
        while nodo_alvo is not None and nodo_alvo.valor != valor:
            nodo_alvo = nodo_alvo.prox

        # Se não encontrou o nodo ou se ele já é o primeiro, não faz nada.
        if nodo_alvo is None or nodo_alvo == self.pri:
            return

        # 2. DESCONECTAR O NODO DA POSIÇÃO ATUAL
        # Conecta o vizinho anterior com o vizinho posterior
        nodo_alvo.ante.prox = nodo_alvo.prox
        
        # Se o nodo_alvo não for o último, atualiza o ponteiro 'ante' do seu vizinho
        if nodo_alvo.prox is not None:
            nodo_alvo.prox.ante = nodo_alvo.ante
        else:
            # Se era o último, o novo último passa a ser o seu anterior
            self.ult = nodo_alvo.ante

        # 3. RECONECTAR O NODO NO INÍCIO
        nodo_alvo.prox = self.pri  # O próximo do nodo alvo agora é o antigo primeiro
        nodo_alvo.ante = None      # O anterior do nodo alvo é None, pois ele é o novo primeiro
        self.pri.ante = nodo_alvo  # O anterior do antigo primeiro agora é o nodo alvo
        self.pri = nodo_alvo       # O ponteiro 'pri' da lista agora aponta para o nodo alvo
        return
    
    def ordenar(self):
        # Se a lista tem 0 ou 1 elemento, já está ordenada.
        if self.vazio() or self.pri == self.ult:
            return

        # 1. Extrai todos os valores para uma lista Python
        valores = []
        nodo_atual = self.pri
        while nodo_atual is not None:
            valores.append(nodo_atual.valor)
            nodo_atual = nodo_atual.prox

        # 2. Ordena a lista de valores usando o sort() do Python
        valores.sort()

        # 3. Atualiza os valores na lista ligada com os valores ordenados
        nodo_atual = self.pri
        i = 0
        while nodo_atual is not None:
            nodo_atual.valor = valores[i]
            i += 1
            nodo_atual = nodo_atual.prox
        return
    
    def cortar(self):
        if self.vazio(): return None, None

        count = 0
        nodoAtual = self.pri
        while nodoAtual:
            count += 1
            nodoAtual = nodoAtual.prox

        meio = count // 2
        if count % 2 != 0:
            meio += 1 

        nodoAtual = self.pri
        for _ in range(meio - 1):
            nodoAtual = nodoAtual.prox

        segundaMetade = Lista_Dupla()
        segundaMetade.pri = nodoAtual.prox
        if segundaMetade.pri:
            segundaMetade.pri.ante = None

        segundaMetade.ult = self.ult

        primeiraMetade = Lista_Dupla()
        primeiraMetade.pri = self.pri
        primeiraMetade.ult = nodoAtual
        primeiraMetade.ult.prox = None

        return primeiraMetade, segundaMetade
    
lista_dupla = Lista_Dupla()
lista_dupla.inserir_final(5)
lista_dupla.inserir_final(3)
lista_dupla.inserir_final(4)
lista_dupla.inserir_final(6)
lista_dupla.inserir_inicio(1)
lista_dupla.inserir_inicio(1)
lista_dupla.remover_inicio()

print("Lista de trás para frente:")
lista_dupla.print(False)

print("\nLista criada:")
lista_dupla.print(True)

print("\nTamanho da lista:")
print(lista_dupla.tamanho())

lista_dupla.remover_valor(4)
print("\nApós remover o 4:")
lista_dupla.print(True)

# --- Implementando a chamada para prioridade() ---
print("\nDando prioridade ao valor 3:")
lista_dupla.prioridade(3)
lista_dupla.print(True)

lista_dupla.trocar_valores(3, 5)
print("\nApós trocar 3 e 5:")
lista_dupla.print(True)

# --- Implementando a chamada para cortar() ---
print("\n--- Cortando a lista... ---")
primeira_parte, segunda_parte = lista_dupla.cortar()

print("Primeira parte da lista cortada:")
primeira_parte.print(True)

print("\nSegunda parte da lista cortada:")
segunda_parte.print(True)

# A lista_dupla original fica vazia após o corte!
print("\nEstado final da lista original:")
lista_dupla.print(True)

lista_dupla.ordenar()
lista_dupla.print(True)
