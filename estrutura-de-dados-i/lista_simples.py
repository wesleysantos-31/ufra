class Nodo:
    valor = 0
    prox = None
    
class Lista_Simples:
    pri = None
    ult = None
     
    def inserir_inicio(self, valor):
        novoNodo = Nodo()
        novoNodo.valor = valor
        if self.pri == None:
            self.pri = novoNodo
            self.ult = novoNodo
        else:
            novoNodo.prox = self.pri
            self.pri = novoNodo
    
    def inserir_final(self, valor):
        novoNodo = Nodo()
        novoNodo.valor = valor
        if self.pri == None:
            self.pri = novoNodo
            self.ult = novoNodo
        else:
            self.ult.prox = novoNodo
            self.ult = novoNodo
    
    def inserir_apos(self, valor_buscado, novo_valor):
        novoNodo = Nodo()
        novoNodo.valor = novo_valor
        if self.pri == None:
            self.pri = novoNodo
            self.ult = novoNodo
            return
        
        nodoAtual = self.pri
        while nodoAtual is not None:
            if nodoAtual == self.ult:
                self.inserir_final(novo_valor)
                return 
            if nodoAtual.valor ==  valor_buscado:
                novoNodo.prox = nodoAtual.prox
                nodoAtual.prox = novoNodo
                return
            nodoAtual = nodoAtual.prox
            
    def remover_inicio(self):
        if self.pri is None:
            print("Lista Vazia!")
            return
        if self.pri == self.ult:
            self.pri = None
            self.ult = None
            return
        self.pri = self.pri.prox
    
    def remover_final(self):
        if self.pri is None:
            print("Lista Vazia!")
            return
        if self.pri == self.ult:
            self.pri = None
            self.ult = None
            return
        nodoAtual = self.pri
        while nodoAtual.prox is not self.ult:
            nodoAtual = nodoAtual.prox
        self.ult = nodoAtual
        nodoAtual.prox = None
    
    def remover_valor(self, valor):
        if self.pri is None:
            print("Lista Vazia!")
            return
        if self.pri == self.ult and self.pri.valor == valor:
            self.pri = None
            self.ult = None
            return
        nodoAtual = self.pri
        while nodoAtual.prox.valor is not valor:
            nodoAtual = nodoAtual.prox
            if nodoAtual == self.ult:
                nodoAtual = None
                break
        if nodoAtual is None:
            print("Valor nao encontrado!")
            return
        nodoAtual.prox = nodoAtual.prox.prox
                
    def print(self):
        nodoAtual = self.pri
        while nodoAtual is not None:
            print(nodoAtual.valor, sep=' ')
            nodoAtual = nodoAtual.prox
    
            
lista = Lista_Simples()
lista.inserir_final(1)
lista.inserir_final(2)
lista.inserir_final(3)
lista.inserir_final(4)
lista.inserir_final(5)
lista.remover_valor(3)
lista.remover_valor(6)
lista.print()
        