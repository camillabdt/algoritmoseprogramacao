package jogadores;

import trabalho2.Coordenada;
import trabalho2.Tabuleiro;
import trabalho2.navios.Navio;

public class Jogador {
    private int[] tamanhosDeNaviosDisponiveis;
    public Navio[] frota;
    private int naviosNaFrota;
    public Tabuleiro tabuleiro;
    int linhas;
    int colunas;

    int tirosDados;


    public Jogador(int[] qtdsMaximaDeNavio) {
        this.frota = new Navio[5];
        this.linhas = 15;
        this.colunas = 20;
        this.naviosNaFrota = 0;
        this.tirosDados = 0;
        this.tabuleiro = new Tabuleiro(linhas, colunas);
        this.tamanhosDeNaviosDisponiveis = qtdsMaximaDeNavio;
    }

    public boolean isTamanhoDisponivel(int tamanho) {
        if (tamanhosDeNaviosDisponiveis[tamanho] > 0) {
            tamanhosDeNaviosDisponiveis[tamanho]--;
            return true;
        }
        return false;
    }

    public boolean atirar(Jogador alvo, Coordenada coordenada) {
        boolean repetido = !alvo.tabuleiro.processaTiro(coordenada);
        return repetido;

    }

    public Tabuleiro getTabuleiro() {
        return tabuleiro;
    }

    public int getnaviosRestantes() {
        int naviosRestantes = 0;
        for (int i = 0; i < frota.length; i++) {
            Navio navio = frota[i]; // estou guardando o navio da frota numa variavel navio, pra facilitar.
            if (navio != null) { //existe um navio nessa posição da frota, pode estar afundado ou não.
                if (!navio.isAfundado()) {
                    naviosRestantes = naviosRestantes + 1;
                }
            }
        }
        return naviosRestantes;

    }


    public boolean adicionarNavio(Navio navio) {
        if (naviosNaFrota < frota.length) {
            frota[naviosNaFrota] = navio;
            naviosNaFrota = naviosNaFrota + 1;
            posicionaNavio(navio);
            return true;
        } else {
            System.out.println("O navio não foi adicionado, pois o limite de " + frota.length + "navios foi atingido.");
            return false;
        }
    }

    public void imprimirTabuleiro() {
        System.out.println("########### Tabuleiro: ");
        tabuleiro.imprimirTabuleiro();
    }

    public void posicionaNavios(Navio[] frota) { // posiciona frota de navios
        // Para cada um dos navios da frota
        for (int indiceNavio = 0; indiceNavio < frota.length; indiceNavio++) {
            Navio navio = frota[indiceNavio];
            if (navio != null) {
                posicionaNavio(navio);
                // serve para o computador (txt)
            }
        }
    }

    private void posicionaNavio(Navio navio) { // posiciona navio por navio
        if (navio == null) {
            System.out.println("Erro ao posicionar o navio: O navio está nulo!");
            // serve para dar msg de erro.
            return;
        }
        if (navio.getCoordenadasIniciais().getX() == navio.getCoordenadasFinais().getX()) {
            int x = navio.getCoordenadasIniciais().getX();
            // Navio na horizontal
            for (int y = navio.getCoordenadasIniciais().getY(); y <= navio.getCoordenadasFinais().getY(); y++) {
                // Verifica se os índices estão dentro dos limites da matriz
                if (x < tabuleiro.getTabuleiro().length && y < tabuleiro.getTabuleiro().length) {
                    tabuleiro.adicionaPedacoNavio(x, y);
                }
            }
        } else if (navio.getCoordenadasIniciais().getY() == navio.getCoordenadasFinais().getY()) {
            int y = navio.getCoordenadasIniciais().getY();
            // Navio na vertical
            for (int x = navio.getCoordenadasIniciais().getX(); x <= navio.getCoordenadasFinais().getX(); x++) {
                // Verifica se os índices estão dentro dos limites da matriz
                if (x < tabuleiro.getTabuleiro().length && y < tabuleiro.getTabuleiro().length) {
                    tabuleiro.adicionaPedacoNavio(x, y);
                }
            }
        } else {
            throw new IllegalArgumentException("Navio com coordenadas inválidas.");
        }
    }

    public int getTirosDados() {
        return tirosDados;
    }

    public void setTirosDados(int tirosDados) {
        this.tirosDados = tirosDados;
    }
}

