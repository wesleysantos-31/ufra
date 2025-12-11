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
        return False
        
    def remover_inicio(self):
        if self.vazio(): return
        if self.pri == self.ult:
            self.pri = self.ult = None
            return
        self.pri = self.pri.prox
        self.pri.ante = None
        
    def print(self, para_frente=True):
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
        print("None") # Adicionado para clareza na saída

    def remover_valor(self, valor):
        if self.vazio(): return
        
        nodoAtual = self.pri
        while nodoAtual is not None:
            if nodoAtual.valor == valor:
                if self.pri == self.ult:
                    self.pri = self.ult = None
                elif nodoAtual == self.pri:
                    self.remover_inicio()
                elif nodoAtual == self.ult:
                    # Implementação de remover_final aqui para evitar chamada recursiva com print
                    self.ult = nodoAtual.ante
                    self.ult.prox = None
                else:
                    nodoAtual.ante.prox = nodoAtual.prox
                    nodoAtual.prox.ante = nodoAtual.ante
                # Bug corrigido: return deve estar aqui para parar após a primeira remoção
                return 
            nodoAtual = nodoAtual.prox
            
    def tamanho(self):
        contador = 0
        nodo_atual = self.pri
        while nodo_atual is not None:
            contador += 1
            nodo_atual = nodo_atual.prox
        return contador

    def ordenar(self):
        if self.vazio() or self.pri == self.ult:
            return
        
        valores = []
        nodo_atual = self.pri
        while nodo_atual is not None:
            valores.append(nodo_atual.valor)
            nodo_atual = nodo_atual.prox
        valores.sort()

        nodo_atual = self.pri
        i = 0
        while nodo_atual is not None:
            nodo_atual.valor = valores[i]
            i += 1
            nodo_atual = nodo_atual.prox

lista_dupla = Lista_Dupla()
lista_dupla.inserir_final(1)
lista_dupla.inserir_final(2)
lista_dupla.inserir_final(3)
lista_dupla.inserir_inicio(4)
lista_dupla.inserir_inicio(5)
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

lista_dupla.ordenar()
print("\nLista ordenada:")
lista_dupla.print(True)