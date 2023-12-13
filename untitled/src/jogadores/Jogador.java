package jogadores;

import trabalho2.Coordenada;
import trabalho2.Tabuleiro;
import trabalho2.navios.Navio;

import java.util.Random;

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
        this.tabuleiro = new Tabuleiro(colunas, linhas);
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

    public int getNaviosRestantes() {
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
        tabuleiro.imprimirTabuleiro();
    }

    public void posicionaNavios(Navio[] frota) { // posiciona frota de navios
        // Para cada um dos navios da frota
        for (int indiceNavio = 0; indiceNavio < frota.length; indiceNavio++) {
            Navio navio = frota[indiceNavio];
            if (navio != null) {
                // serve para o computador (txt)
                posicionaNavio(navio);
            }
        }
    }

    private void posicionaNavio(Navio navio) { // posiciona navio por navio
        if (navio == null) {
            System.err.println("Erro ao posicionar o navio: O navio está nulo!");
            // serve para dar msg de erro.
            return;
        }
        if (navio.getCoordenadasIniciais().getColuna() == navio.getCoordenadasFinais().getColuna()) {
            int coluna = navio.getCoordenadasIniciais().getColuna();
            // Navio na horizontal
            for (int linha = navio.getCoordenadasIniciais().getLinha(); linha <= navio.getCoordenadasFinais().getLinha(); linha++) {
                // Verifica se os índices estão dentro dos limites da matriz
                if (coluna < tabuleiro.getTabuleiro().length && linha < tabuleiro.getTabuleiro().length) {
                    tabuleiro.adicionaPedacoNavio(coluna, linha);
                }
            }
        } else if (navio.getCoordenadasIniciais().getLinha() == navio.getCoordenadasFinais().getLinha()) {
            int linha = navio.getCoordenadasIniciais().getLinha();
            // Navio na vertical
            for (int coluna = navio.getCoordenadasIniciais().getColuna(); coluna <= navio.getCoordenadasFinais().getColuna(); coluna++) {
                // Verifica se os índices estão dentro dos limites da matriz
                if (coluna < tabuleiro.getTabuleiro().length && linha < tabuleiro.getTabuleiro()[0].length) {
                    tabuleiro.adicionaPedacoNavio(coluna, linha);
                } else {
                    System.err.println("Coordenadas fora dos limites do tabuleiro: (linha:" + linha + ", coluna:" + coluna + ")");
                }
            }
        } else {
            throw new IllegalArgumentException("Navio com coordenadas inválidas.");
        }
    }

    public int getTirosDados() {
        return tirosDados;
    }

    public void printTamanhosDeNaviosDisponiveis() {
        System.out.println("Os navios disponíveis e seus tamanhos são os seguintes:");

        for (int i = 0; i < tamanhosDeNaviosDisponiveis.length; i++) {
            if (tamanhosDeNaviosDisponiveis[i] != 0) {
                System.out.println("Navios de tamanho [" + i + "] disponíveis: " + tamanhosDeNaviosDisponiveis[i]);
            }
        }
    }

    public int getProximoTamanhoNavioDisponivel() { // retorna um navio de tamanho valido

        // para garantir que tanto navio de tamanho menor quanto de tamanho maior seja sorteado, é tirado o par ou impar a seguir
        Random parImpar = new Random();

        if (parImpar.nextInt() % 2 == 0) { // se o numero aleatorio for par, percorre do inicio ao fim
            for (int i = 0; i < tamanhosDeNaviosDisponiveis.length; i++) {
                if (tamanhosDeNaviosDisponiveis[i] != 0) {
                    return i;
                }
            }
            // se o numero aleatorio for impar, percorre do fim para o inicio
        } else {
            for (int i = tamanhosDeNaviosDisponiveis.length - 1; i >= 0; i--) {
                if (tamanhosDeNaviosDisponiveis[i] != 0) {
                    return i;
                }
            }
        }


        return 0;
    }

    public void setTirosDados(int tirosDados) {
        this.tirosDados = tirosDados;
    }
}

