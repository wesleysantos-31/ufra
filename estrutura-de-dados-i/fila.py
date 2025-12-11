## FILAS
class Nodo:
    valor = 0
    proximo = None

class Fila:
    pri = None

    def adicionar(self, valor):
        novoNodo = Nodo()
        novoNodo.valor = valor
        if self.pri is None:
            self.pri = novoNodo
        else:
            nodoAtual = self.pri
            while nodoAtual.proximo is not None:
                nodoAtual = nodoAtual.proximo
            nodoAtual.proximo = novoNodo

    def remover(self):
        if self.pri is None:
            print("Fila vazia! nada pra remover")
        elif self.pri.proximo is None:
            self.pri = None
        else:
            self.pri = self.pri.proximo
    
    def print(self):
        nodoAtual = self.pri
        if nodoAtual is None:
            return
        while nodoAtual.proximo is not None:
            print("--------------------------")
            print("End:",nodoAtual)
            print("Valor:",nodoAtual.valor)
            print("Prox:",nodoAtual.proximo)
            print("--------------------------")
            nodoAtual = nodoAtual.proximo
        print("--------------------------")
        print("End:",nodoAtual)
        print("Valor:",nodoAtual.valor)
        print("Prox:",nodoAtual.proximo)
        print("--------------------------")

fila = Fila()
fila.adicionar(1)
fila.adicionar(2)
fila.adicionar(3)
fila.remover()
fila.print()

