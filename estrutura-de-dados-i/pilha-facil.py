class Nodo:
    valor = 0
    proximo = None

class Pilha:
    topo = None
    def adicionar(self,valor):
        novoNodo = Nodo()
        novoNodo.valor = valor
        if self.topo is None:
            self.topo = novoNodo
        else:
            novoNodo.proximo = self.topo
            self.topo = novoNodo
    
    def remover(self):
        if self.topo is None:
            print("Pilha vazia!")
        else:
            self.topo = self.topo.proximo
    
    def print(self):
        nodoAtual = self.topo
        while nodoAtual is not None:
            print('end:',nodoAtual)
            print('valor:',nodoAtual.valor, 'prox:',nodoAtual.proximo)
            nodoAtual = nodoAtual.proximo

pilha = Pilha()
pilha.adicionar(1)
pilha.adicionar(2)
pilha.adicionar(3)
pilha.remover()
pilha.print()
    