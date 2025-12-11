class Nodo:
    valor = 0
    proximo = None
    
class Pilha:
    primeiro = None
    def adicionar(self, valor):
        novoNodo = Nodo()
        novoNodo.valor = valor
        if self.primeiro is None:
            self.primeiro = novoNodo
        else:
            nodoAtual = self.primeiro
            while nodoAtual.proximo is not None:
                nodoAtual = nodoAtual.proximo
            nodoAtual.proximo = novoNodo
    
    def remover(self):
        if self.primeiro is None:
            print("Pilha vazia")
        elif self.primeiro.proximo is None:
            self.primeiro = None
        else:
            nodoAtual = self.primeiro
            while nodoAtual.proximo.proximo is not None:
                nodoAtual = nodoAtual.proximo
            nodoAtual.proximo = None
            

pilha = Pilha()
pilha.adicionar(1)
pilha.adicionar(2)
pilha.adicionar(3)
pilha.remover()
    