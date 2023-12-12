package trabalho2;

import java.util.logging.Logger;

public class Tabuleiro {
    char[][] tabuleiro;
    int pedacosNaviosRestantes;

    public Tabuleiro(int linhas, int colunas) {
        tabuleiro = new char[linhas][colunas];
        pedacosNaviosRestantes = 0;
        // Inicializa o tabuleiro vazio (sem navios)
        inicializaTabuleiro(linhas, colunas);
    }

    private void inicializaTabuleiro(int linhas, int colunas) {
        for (int linha = 0; linha < linhas; linha++) {
            for (int coluna = 0; coluna < colunas; coluna++) {
                //Coordenadas não utilizadas em nenhuma jogadas representadas por espaços em branco
                tabuleiro[linha][coluna] = ' ';
            }
        }
    }
    public void imprimirTabuleiro() {
        System.out.print("   ");
        for (int i = 65; i < 85; i++) {
            System.out.print(" " +(char) i + " ");
        }
        System.out.println();
        for (int i = 0; i < tabuleiro.length; i++) {

           if (i < 10) {
               System.out.print( "0" +i + " ");
           }else {
               System.out.print(i + " ");
           }
            for (int j = 0; j < tabuleiro[i].length; j++) {
                System.out.print("[" + tabuleiro[i][j] + "]");
            }
            System.out.println(); // Quebra a linha para mostrar a próxima
        }
    }

    public int retornaPosicao(char letra) throws IllegalArgumentException {
        if (Character.isLetter(letra)) {
            int posicao = Character.toLowerCase(letra) - 'a'; // descobre a posição da letra e diminui da primeira posição que contém letras (a = 97)
            if (posicao < 0 || posicao >= tabuleiro[0].length) {
                // Dispara a exeção quando o valor informado nao estiver dentro dos limites do tabuleiro
                throw new IllegalArgumentException("A posição informada (" + posicao + ") está fora dos limites do tabuleiro" + tabuleiro.length + "x" + tabuleiro[0].length + "!");
            }
            return posicao;
        } else {
            // Dispara a exeção quando o valor informado para a posição da coluna não for uma letra
            throw new IllegalArgumentException("O caractere  [" + letra + "] não é uma letra!");
        }
    }

    // Método auxiliar para processar as coordenadas
    public Coordenada processarCoordenadas(String coordenada) {
        coordenada = coordenada.trim(); // remove espaços em branco
        try {
            char linhaLetra = coordenada.charAt(0);
            int linha = retornaPosicao(linhaLetra);
            int coluna = Integer.parseInt(coordenada.substring(1));
            return new Coordenada(coluna, linha);
        } catch (NumberFormatException e) { // gera exceção quando o valor da coordenada Y não for numérico
            System.out.println("O valor para as coordenadas é inválido: " + coordenada);
            return null;
        }
    }

    public boolean processaTiro(Coordenada coordenada) {
        try {
            if (tabuleiro[coordenada.getX()][coordenada.getY()] == 'N') {
                System.err.println(" Um navio foi atingido");
                tabuleiro[coordenada.getX()][coordenada.getY()] = 'H';
                pedacosNaviosRestantes = pedacosNaviosRestantes - 1;
                return true;
            } else if (tabuleiro[coordenada.getX()][coordenada.getY()] == ' ') {
                System.out.println("O tiro acertou a água.");
                tabuleiro[coordenada.getX()][coordenada.getY()] = 'M';
                return true;
            } else if (tabuleiro[coordenada.getX()][coordenada.getY()] == 'M' || tabuleiro[coordenada.getX()][coordenada.getY()] == 'H') {
                System.out.println("Jogada repetida. Tente novamente.");
                return false;
            } else {
                throw new IllegalArgumentException("O tabuleiro possui valores inesperados. Por favor, verifique o arquivo de configuração e reinicie o jogo.");
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Um tiro foi dado em uma posição inválida: " + coordenada.getString());
            return false;
        }
    }

    public char[][] getTabuleiro() {
        return this.tabuleiro;
    }

    public void adicionaPedacoNavio(int i, int j) {
        this.tabuleiro[i][j] = 'N';
        this.pedacosNaviosRestantes = this.pedacosNaviosRestantes + 1;
    }

    public boolean restamNavios() {
        if (this.pedacosNaviosRestantes > 0) {
            return true;
        }
        // System.out.println("Temos um vencedor!");
        return false;
    }

    public int getPedacosRestantes() {
        return this.pedacosNaviosRestantes;
    }

    public boolean isCoordenadaDisponivel(Coordenada coordenada) {
        if (isCoordenadaNoTabuleiro(coordenada)) {
            if (tabuleiro[coordenada.getX()][coordenada.getY()] == ' ') {
                return true;
            } else {
                return false;
            }
        } else {
            System.out.println("Essa coordenada não está no tabuleiro");
            return false;
        }
    }

    public boolean isCoordenadaNoTabuleiro(Coordenada coordenada) {
        boolean xValido = false;
        boolean yValido = false;

        if (coordenada.getX() >= 0 && coordenada.getX() < tabuleiro.length) { // verifica o numero de linhas
            xValido = true;
        }

        if (coordenada.getY() >= 0 && coordenada.getY() < tabuleiro[0].length) { // verifica o numero de coluna
            yValido = true;
        }

        return xValido && yValido; // isso evita um if, Ela retorna verdadeiro se os dois forem true.
    }
}
