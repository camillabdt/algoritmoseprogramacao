package trabalho2.navios;

import trabalho2.Coordenada;

public class Navio {
    int tamanho;
    Coordenada coordenadasIniciais; // duas posições: x e y
    Coordenada coordenadasFinais; // duas posições: x e y
    int pedacosRestantes;

    public Navio(Coordenada coordenadasIniciais, Coordenada coordenadasFinais) {
        this.coordenadasIniciais = coordenadasIniciais;
        this.coordenadasFinais = coordenadasFinais;
        this.tamanho = coordenadasFinais.getX() - coordenadasIniciais.getX() + coordenadasFinais.getY() - coordenadasIniciais.getY() + 1;
        this.pedacosRestantes = tamanho; // quando um navio é criado, o numero de pedaços não atingidos é igual ao seu tamanho
/*System.out.println("Novo navio, coordenadas:" +
                " (" + coordenadasIniciais.getX() + "," + coordenadasIniciais.getY() + ") à "
                + " (" + coordenadasFinais.getX() + "," + coordenadasFinais.getY() + ")"
                + " - Tamanho: " + tamanho);*/
    }

    private int sofrerDano() {
        this.pedacosRestantes = pedacosRestantes - 1;
        return pedacosRestantes;
    }

    public int getTamanho() {
        return tamanho;
    }

    public void setTamanho(int tamanho) {
        this.tamanho = tamanho;
    }

    public Coordenada getCoordenadasIniciais() {
        return coordenadasIniciais;
    }

    public void setCoordenadasIniciais(Coordenada coordenadasIniciais) {
        this.coordenadasIniciais = coordenadasIniciais;
    }

    public Coordenada getCoordenadasFinais() {
        return coordenadasFinais;
    }

    public void setCoordenadasFinais(Coordenada coordenadasFinais) {
        this.coordenadasFinais = coordenadasFinais;
    }

    public int getPedacosRestantes() {
        return pedacosRestantes;
    }

    public void setPedacosRestantes(int pedacosRestantes) {
        this.pedacosRestantes = pedacosRestantes;
    }

    public boolean isAfundado() {
       if (pedacosRestantes ==0){
           return true;
       } else {
           return false;
       }
    }
}

