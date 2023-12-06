package jogadores;

import trabalho2.Coordenada;
import trabalho2.Tabuleiro;
import trabalho2.navios.Navio;

public class Jogador {
    public Navio[] frota;
    private int naviosNaFrota;
    public Tabuleiro tabuleiro;
    int linhas;
    int colunas;

    public Jogador() {
        this.frota = new Navio[5];
        this.linhas = 15;
        this.colunas = 20;
        this.naviosNaFrota = 0;
        this.tabuleiro = new Tabuleiro(linhas, colunas);

    }

    public void atirar(Jogador alvo, Coordenada coordenada) {
        alvo.tabuleiro.processaTiro(coordenada);
    }


    public Tabuleiro getTabuleiro() {
        return tabuleiro;
    }

    public int getPedacosRestantesNavios() {
        return tabuleiro.getPedacosRestantes();
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

    public void posicionaNavios(Navio[] frota) {
        // Para cada um dos navios da frota
        for (int indiceNavio = 0; indiceNavio < frota.length; indiceNavio++) {
            Navio navio = frota[indiceNavio];
            if (navio == null) {
                break;
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
    }

    private void posicionaNavio(Navio navio) {
        if (navio == null) {
            throw new IllegalArgumentException("Navio nulo!");
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

}

