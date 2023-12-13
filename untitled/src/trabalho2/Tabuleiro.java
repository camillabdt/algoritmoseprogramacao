package trabalho2;

public class Tabuleiro {
    char[][] tabuleiro;
    int pedacosNaviosRestantes;


    public Tabuleiro(int colunas, int linhas) {
        System.out.println("Foi criado um tabuleiro com " + colunas + " colunas (letras de " + numeroParaLetra(0) + " a " + numeroParaLetra(colunas - 1) + ") e " + linhas + " linhas, numeradas de 0 a " + (linhas - 1));
        tabuleiro = new char[colunas][linhas];
        pedacosNaviosRestantes = 0;
        // Inicializa o tabuleiro vazio (sem navios)
        inicializaTabuleiro(colunas, linhas);

    }

    private void inicializaTabuleiro(int totalColunas, int totalLinhas) {
        for (int posicaoLinha = 0; posicaoLinha < totalLinhas; posicaoLinha++) {
            for (int posicaoColuna = 0; posicaoColuna < totalColunas; posicaoColuna++) {
                //Coordenadas não utilizadas em nenhuma jogadas representadas por espaços em branco
                tabuleiro[posicaoColuna][posicaoLinha] = ' ';
            }
        }
    }

    public void imprimirTabuleiro() {
        System.out.print("   ");
        int totalColunas = tabuleiro.length; // quantidade de colunas do tabuleiro
        int totalLinhas = tabuleiro[0].length; // quantidade de linhas do tabuleiro

        for (int i = 0; i < totalColunas; i++) {
            System.out.print(" " + numeroParaLetra(i) + " ");
        }
        System.out.println();
        for (int linha = 0; linha < totalLinhas; linha++) {
            if (linha < 10) {
                System.out.print("0" + linha + " ");
            } else {
                System.out.print(linha + " ");
            }
            for (int coluna = 0; coluna < totalColunas; coluna++) {
                if (tabuleiro[coluna][linha] == 'N' || tabuleiro[coluna][linha] == 'H') {
                    mensagemVermelha("[" + tabuleiro[coluna][linha] + "]", false);
                } else {
                    System.out.print("[" + tabuleiro[coluna][linha] + "]");
                }
            }
            System.out.println(); // Quebra a linha para mostrar a próxima
        }
    }

    public int retornaPosicao(char letra) throws IllegalArgumentException {
        if (Character.isLetter(letra)) {
            int posicao = letraParaNumero(letra); // descobre a posição da letra e diminui da primeira posição que contém letras (a = 97)
            if (posicao < 0 || posicao >= tabuleiro.length) {
                // Dispara a exeção quando o valor informado nao estiver dentro dos limites do tabuleiro
                throw new IllegalArgumentException("A coordenada informada (" + letra + ") está fora dos limites do tabuleiro (limite atual: " + numeroParaLetra(tabuleiro.length - 1) + ")");
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
            char colunaLetra = coordenada.charAt(0);
            int coluna = retornaPosicao(colunaLetra);
            int linha = Integer.parseInt(coordenada.substring(1));
            return new Coordenada(coluna, linha);
        } catch (NumberFormatException e) { // gera exceção quando o valor da coordenada Y não for numérico
            System.err.println("O valor para as coordenadas é inválido: " + coordenada);
            return null;
        }
    }

    public boolean processaTiro(Coordenada coordenada) {
        try {
            if (tabuleiro[coordenada.getColuna()][coordenada.getLinha()] == 'N') {
                mensagemVermelha("Um navio foi atingido", true);
                tabuleiro[coordenada.getColuna()][coordenada.getLinha()] = 'H';
                pedacosNaviosRestantes = pedacosNaviosRestantes - 1;
                return true;
            } else if (tabuleiro[coordenada.getColuna()][coordenada.getLinha()] == ' ') {
                System.out.println("O tiro acertou a água.");
                tabuleiro[coordenada.getColuna()][coordenada.getLinha()] = 'M';
                return true;
            } else if (tabuleiro[coordenada.getColuna()][coordenada.getLinha()] == 'M' || tabuleiro[coordenada.getColuna()][coordenada.getLinha()] == 'H') {
                mensagemVermelha("Jogada repetida. Tente novamente.", true);
                return false;
            } else {
                throw new IllegalArgumentException("O tabuleiro possui valores inesperados. Por favor, verifique o arquivo de configuração e reinicie o jogo.");
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Um tiro foi dado em uma posição inválida: " + coordenada.getString());
            return false;
        }
    }

    private void mensagemVermelha(String mensagem, boolean quebraLinha) {
        String ANSI_RED = "\u001B[31m";
        String ANSI_RESET = "\u001B[0m";
        if (quebraLinha) {
            System.out.println(ANSI_RED + mensagem + ANSI_RESET);
        } else {
            System.out.print(ANSI_RED + mensagem + ANSI_RESET);
        }
    }

    public char[][] getTabuleiro() {
        return this.tabuleiro;
    }

    public void adicionaPedacoNavio(int coluna, int linha) {
//        if(coluna<tabuleiro.length & linha > tabuleiro[0].length)
        this.tabuleiro[coluna][linha] = 'N';
        this.pedacosNaviosRestantes = this.pedacosNaviosRestantes + 1;
    }

    public boolean restamNavios() {
        if (this.pedacosNaviosRestantes > 0) {
            return true;
        }
        return false;
    }

    public int getPedacosRestantes() {
        return this.pedacosNaviosRestantes;
    }

    public boolean isCoordenadaDisponivel(Coordenada coordenada) {
        if (isCoordenadaNoTabuleiro(coordenada)) {
            if (tabuleiro[coordenada.getColuna()][coordenada.getLinha()] == ' ') {
                return true;
            } else {
                return false;
            }
        } else {
            System.err.println("Essa coordenada não está no tabuleiro");
            return false;
        }
    }

    public boolean isCoordenadaNoTabuleiro(Coordenada coordenada) {
        boolean xValido = false;
        boolean yValido = false;

        if (coordenada.getColuna() >= 0 && coordenada.getColuna() < tabuleiro.length) { // verifica o numero de linhas
            xValido = true;
        }

        if (coordenada.getLinha() >= 0 && coordenada.getLinha() < tabuleiro[0].length) { // verifica o numero de coluna
            yValido = true;
        }

        return xValido && yValido; // isso evita um if, Ela retorna verdadeiro se os dois forem true.
    }

    // Converte uma letra para sua posição no alfabeto (A=1, B=2, etc.)
    private int letraParaNumero(char letter) {
        return Character.toUpperCase(letter) - 'A';
    }

    // Converte um número para a letra correspondente no alfabeto (1=A, 2=B, etc.)
    private char numeroParaLetra(int number) {
        return (char) (number + 'A');
    }

}
