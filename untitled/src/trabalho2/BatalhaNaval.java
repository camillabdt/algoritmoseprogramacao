package trabalho2;


import jogadores.Computador;
import jogadores.Jogador;
import trabalho2.navios.Navio;

import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class BatalhaNaval {
    Jogador computador;
    Jogador humano;
    private final int[] tamanhosDeNavio; // é final porque nunca vai mudar o valor dos tamanhos de navio durante a execução do jogo
    private final int[] qtdsMaximaDeNavio; // é final porque nunca vai mudar o valor dos tamanhos de navio durante a execução do jogo


    private boolean modoAutomatico = false;

    public BatalhaNaval() { // metodo construtor
        tamanhosDeNavio = new int[]{2, 3, 5, 7};
        qtdsMaximaDeNavio = new int[]{0, 0, 1, 2, 0, 1, 0, 1};
        this.computador = new Computador(qtdsMaximaDeNavio);
        this.humano = new Jogador(qtdsMaximaDeNavio);

    }

    public static void main(String[] args) {

        String escolha = "S";
        while (escolha.equals("S")) {
            System.out.println("Bem vindo ao Batalha Naval da Bdt!");

            // Inicia um novo jogo
            BatalhaNaval jogo = new BatalhaNaval();
            jogo.inicializarJogo();
            System.out.println("Deseja jogar novamente? Tecle (S) para sim, ou qualquer tecla para sair.");
            escolha = new Scanner(System.in).nextLine();
        }
    }

    private void inicializarJogo() {
        Scanner scanner = new Scanner(System.in);
        int escolha;

        // Inicializa o jogador automático (computador)
        String arquivo = "computador.txt";
        boolean leitura = false;
        while (!leitura) {
            leitura = ((Computador) computador).lerArquivoEProcessar(arquivo);
            if (!leitura) {
                System.out.println("Por favor, informe o nome do arquivo novamente:");
                arquivo = new Scanner(System.in).nextLine();
            }
        }
//        getComputador().imprimirTabuleiro();


        escolha = -1;
        while (escolha != 1 && escolha != 2) {
            System.out.println("Escolha o seu modo de jogo:");
            System.out.println("1. Automático");
            System.out.println("2. Manual");
            try {
                escolha = scanner.nextInt();
                if (escolha == 1) {
                    setModoAutomatico(true);// retorna o valor de modoAutomático
                } else if (escolha == 2) {
                    setModoAutomatico(false);
                } else {
                    throw new IllegalArgumentException("O número digitado é inválido, por favor, digite apenas o número 1 ou 2.");
                }
            } catch (IllegalArgumentException illegalArgumentException) {
                // essa parte é necessária porque o nextInt não remove a string do scanner (que geraria um loop infinito)
                System.out.println(illegalArgumentException.getLocalizedMessage());
            } catch (InputMismatchException inputMismatchException) {
                System.out.println("Entrada inválida:'" + scanner.nextLine() + "'. Por favor, digite apenas o número 1 ou 2.");
            }
        }

        // Inicializa o jogador humano
        inicializarTabuleiroHumano(isModoAutomatico());

        // jogo.getHumano().imprimirTabuleiro();

        // Par ou Impar (decide quem começa)
        int numeroAleatorio = new Random().nextInt(5) + 1;
        System.out.println("Vou jogar o dado! Se der um número par você começa, se der ímpar o computador começa! ");

        if (numeroAleatorio % 2 == 0) {
            System.out.println("O dado mostrou o número: " + numeroAleatorio + ", então o COMPUTADOR começa!");
            comecaBatalha(getComputador(), getHumano());
        } else {
            System.out.println("O dado mostrou o número: " + numeroAleatorio + ", então VOCÊ começa!");
            comecaBatalha(getHumano(), getComputador());
        }
    }

    public boolean isModoAutomatico() {
        return this.modoAutomatico;
    }

    public void setModoAutomatico(boolean modoAutomatico) {
        this.modoAutomatico = modoAutomatico;
    }

    private void inicializarTabuleiroHumano(boolean modoAutomatico) {
        if (!modoAutomatico) {
            while (humano.getNaviosRestantes() < computador.getNaviosRestantes()) { // enquanto o humano tiver menos peça que o computador,
//                humano.imprimirTabuleiro();// perguntar para a brenda se pode mostrar aqui o tabuleiro no
                // momento de inserir navios
                try {
                    insereNavioManualmente();
                } catch (IllegalArgumentException execao) {
                    String mensagemDeErro = execao.getLocalizedMessage();
                    System.out.println("Aconteceu o seguinte erro: " + mensagemDeErro);
                }
            }
        } else { // modo automatico, sorteia as posições de navios
            while (humano.getNaviosRestantes() < computador.getNaviosRestantes()) {
                insereNavioAutomaticamente();
            }
            humano.imprimirTabuleiro();
        }

    }

    private Coordenada getCoordenadaFinalAleatoria(Coordenada coordenadasIniciais, int tamanhoNavio) {
        Random aleatorio = new Random();
        boolean horizontal = (aleatorio.nextInt() % 2 == 0); // se o numero aleatorio for par, então é horizontal
        // esse método retorna o próximo navio, mas varia se começa do inico ou do fim da lista de navios
        Coordenada coordenadasFinais;
        if (horizontal) {
            coordenadasFinais = new Coordenada(coordenadasIniciais.getColuna(), coordenadasIniciais.getLinha() + tamanhoNavio - 1);
        } else {
            coordenadasFinais = new Coordenada(coordenadasIniciais.getColuna() + tamanhoNavio - 1, coordenadasIniciais.getLinha());
        }
        System.out.println(coordenadasIniciais.getString());
        System.out.println(coordenadasFinais.getString());

        return coordenadasFinais;
    }

    public void insereNavioManualmente() {
        // Solicita coordenadas iniciais
        boolean coordenadaInicialDisponivel = false;
        Coordenada coordenadasIniciais = null;
        String mensagem = "Informe as coordenadas iniciais do seu navio:";
        while (!coordenadaInicialDisponivel) {
            coordenadasIniciais = solicitaCoordenadasUsuario(humano, mensagem);
            if (coordenadasIniciais != null) {
                coordenadaInicialDisponivel = humano.getTabuleiro().isCoordenadaDisponivel(coordenadasIniciais);
                if (!coordenadaInicialDisponivel) {
                    System.err.println("Informe uma coordenada que esteja disponível, dentro do tabuleiro e sem navios.");
                }
            } else {
                System.out.println("Coordenadas inválidas.");
            }
        }

        boolean coorodenadaFinalDisponivel = false;
        while (!coorodenadaFinalDisponivel) {
            mensagem = "Informe as coordenadas finais do seu navio:";
            Coordenada coordenadasFinais = solicitaCoordenadasUsuario(humano, mensagem);

            if (coordenadasFinais != null) {

                // Verifica se a posição da coordenada está disponível
                coorodenadaFinalDisponivel = humano.getTabuleiro().isCoordenadaDisponivel(coordenadasFinais);
                if (coorodenadaFinalDisponivel) {

                    Navio navio = new Navio(coordenadasIniciais, coordenadasFinais);
                    boolean tamanhoValido = isTamanhoNavioValido(navio, humano); // Verifica se o navio tem um tamanho válido

                    if (tamanhoValido) {

                        // Verifica se a quantidade de navios está consistente
                        boolean adicionouNavio = humano.adicionarNavio(navio);
                        boolean faltaHumanoAdicionar = humano.getNaviosRestantes() < computador.getNaviosRestantes();
                        boolean adicionouDemais = humano.getNaviosRestantes() > computador.getNaviosRestantes();

                        if (!adicionouNavio && faltaHumanoAdicionar) {
                            throw new IllegalArgumentException("O limite de navios foi atingido mas o computador possui mais navios que o usuário.");
                        } else if (adicionouDemais) {
                            throw new IllegalArgumentException("O usuário possui mais navios do que o computador.");
                        }
                    } else {
                        System.err.println("O tamanho do navio não corresponde a um tamanho válido."); // solicitara novamente a coordenada final
                    }
                } else {
                    System.out.println("A coordenada final já está ocupada ou é inválida."); // solicitara novamente a coordenada final
                }
            }
        }
    }
//    private void insereNavioAleatoriamente() { //  faz randon e colocar posição inicial e final - usa om mesmo do computador
//        Coordenada coordenadasIniciais = coordenadaAleatoria();
//        Coordenada coordenadasFinais = getCoordenadaFinalAleatoria(coordenadasIniciais);
//        if (!humano.adicionarNavio(new Navio(coordenadasIniciais, coordenadasFinais)) && humano.getNaviosRestantes() < computador.getNaviosRestantes()) {
//            throw new IllegalArgumentException("O limite de navios foi atingido mas o computador possui mais navios que o usuário.");
//        } else if (humano.getNaviosRestantes() > computador.getNaviosRestantes()) {
//            throw new IllegalArgumentException("O usuário possui mais peças do que o computador.");
//        }
//    }

    public void insereNavioAutomaticamente() {
        System.out.println("Metodo de inserir automaticamente...");
        // Solicita coordenadas iniciais
        boolean coordenadaInicialDisponivel = false;
        Coordenada coordenadasIniciais = null;
        int tamanhoNavio = getHumano().getProximoTamanhoNavioDisponivel();
        System.out.println("Tamanho de navio:");

        while (!coordenadaInicialDisponivel) {
            coordenadasIniciais = coordenadaAleatoria();
            boolean cabeHorizontal = coordenadasIniciais.getColuna() + tamanhoNavio < humano.getTabuleiro().getTabuleiro().length;
            boolean cabeVertical = coordenadasIniciais.getColuna() + tamanhoNavio < humano.getTabuleiro().getTabuleiro()[0].length;
            if (cabeHorizontal || cabeVertical) { // esse if garante que o navio cabe no tabuleiro em pelo menos um sentido a partir da coordenada inicial escolhida
                if (coordenadasIniciais != null) {
                    coordenadaInicialDisponivel = humano.getTabuleiro().isCoordenadaDisponivel(coordenadasIniciais);
                    if (!coordenadaInicialDisponivel) {
                        System.err.println("Informe uma coordenada que esteja disponível, dentro do tabuleiro e sem navios.");
                    }
                } else {
                    System.out.println("Coordenadas inválidas.");
                }
            }
        }

        boolean coorodenadaFinalDisponivel = false;
        while (!coorodenadaFinalDisponivel) {
            Coordenada coordenadasFinais = getCoordenadaFinalAleatoria(coordenadasIniciais, tamanhoNavio);

            if (coordenadasFinais != null) {

                // Verifica se a posição da coordenada está disponível
                coorodenadaFinalDisponivel = humano.getTabuleiro().isCoordenadaDisponivel(coordenadasFinais);
                if (coorodenadaFinalDisponivel) {

                    Navio navio = new Navio(coordenadasIniciais, coordenadasFinais);
                    boolean tamanhoValido = isTamanhoNavioValido(navio, humano); // Verifica se o navio tem um tamanho válido

                    if (tamanhoValido) {

                        // Verifica se a quantidade de navios está consistente
                        boolean adicionouNavio = humano.adicionarNavio(navio);
                        boolean faltaHumanoAdicionar = humano.getNaviosRestantes() < computador.getNaviosRestantes();
                        boolean adicionouDemais = humano.getNaviosRestantes() > computador.getNaviosRestantes();

                        if (!adicionouNavio && faltaHumanoAdicionar) {
                            throw new IllegalArgumentException("O limite de navios foi atingido mas o computador possui mais navios que o usuário.");
                        } else if (adicionouDemais) {
                            throw new IllegalArgumentException("O usuário possui mais navios do que o computador.");
                        }
                    } else {
                        System.err.println("O tamanho do navio não corresponde a um tamanho válido."); // solicitara novamente a coordenada final
                    }
                } else {
                    System.out.println("A coordenada final já está ocupada ou é inválida."); // solicitara novamente a coordenada final
                    System.exit(0);
                }
            }
        }
    }

    private boolean isTamanhoNavioValido(Navio navio, Jogador jogador) {
        int tamanhohorizontal = navio.getCoordenadasFinais().getColuna() - navio.getCoordenadasIniciais().getColuna() + 1;
        int tamanhovertical = navio.getCoordenadasFinais().getLinha() - navio.getCoordenadasIniciais().getLinha() + 1;
        for (int i = 0; i < tamanhosDeNavio.length; i++) {
            if (tamanhosDeNavio[i] == tamanhovertical || tamanhosDeNavio[i] == tamanhohorizontal) {
                int tamanho = Math.abs(tamanhohorizontal - tamanhovertical) + 1; // abs retorna o modulo (sem sinal)
                if (jogador.isTamanhoDisponivel(tamanho)) {
                    return true; // o tamanho vertical ou horizontal tem um tamanho válido!
                }

            }
        }
        System.err.println("O navio tem tamanho INVÁLIDO: " + tamanhohorizontal + "x" + tamanhovertical);
        jogador.printTamanhosDeNaviosDisponiveis();
        return false; // se percorrer o vetor de tamanhos e nao encontrar um tamanho valido, retorna false
    }


    private void comecaBatalha(Jogador atirador, Jogador alvo) {
        Coordenada coordenadaTiro = null;
        int rodada = 0;

        while (atirador.getTabuleiro().restamNavios() && alvo.getTabuleiro().restamNavios()) {
            System.out.print(" ## RODADA " + (++rodada));
            // se o numero da rodada tiver dentro do vetor fibonacci - deixa jogar mais duas vezes
            int tiros = 1;
            Coordenada[] filaDeTiros = new Coordenada[tiros];
            if (estaNaSequenciaFibonnacio(rodada)) {
                tiros = 3;
                filaDeTiros = new Coordenada[tiros];
                System.out.println(" Você está na sequencia de fibonacci, terá a chance de jogar 3x");
            }

            // Identifica quem atira e localiza as coordenadas
            for (int tiro = 0; tiro < filaDeTiros.length; tiro++) {
                if (atirador instanceof Computador) {
                    System.out.println(" (computador) ");
                    coordenadaTiro = ((Computador) atirador).proximoTiro();

                    // se acabar as coordenadas de tiros do computador no arquivo, sorteia uma nova coordenada
                    if (coordenadaTiro == null) {
                        coordenadaTiro = coordenadaAleatoria();
                    }
                } else {
                    System.out.println(" (Você)");
                    if (modoAutomatico) {
                        coordenadaTiro = coordenadaAleatoria();
                        System.out.println("Tiro em: " + coordenadaTiro.getColuna() + "," + coordenadaTiro.getLinha());
                        // Descomentar a linha a seguir para acompanhar o progresso (ver os M onde tem tiro no tabuleiro)
//                    getComputador().imprimirTabuleiro();

                    } else {
                        coordenadaTiro = tiroManual(atirador);
                    }
                }
                filaDeTiros[tiro] = coordenadaTiro;
            }

            for (int tiro = 0; tiro < filaDeTiros.length; tiro++) {
                // Dispara o tiro
                boolean repetido = atirador.atirar(alvo, coordenadaTiro);
                if (!repetido) {
                    // se a coordenada não for repetida inverte o alvo e o atirador para a próxima rodada
                    Jogador aux = atirador; // variavel auxiliar para guardar temporariamente o atirador
                    atirador = alvo;
                    alvo = aux;
                } else {
                    rodada--;
                }
            }
        }
        System.out.println("Fim de jogo! ");
        if (alvo.getTabuleiro().

                restamNavios()) {
            if (alvo instanceof Computador) {
                mensagemVermelha("Você perdeu!");
            } else {
                mensagemVermelha("Parabéns, você ganhou!");
            }
        }
        System.out.println("######### TABULEIRO DO COMPUTADOR:");
        computador.imprimirTabuleiro();// perguntar para a brenda se pode mostrar aqui o tabuleiro no

        System.out.println("######### SEU TABULEIRO");
        humano.imprimirTabuleiro();// perguntar para a brenda se pode mostrar aqui o tabuleiro no

    }

    private boolean estaNaSequenciaFibonnacio(int rodada) {
        int[] sequenciaFB = getComputador().getSequenciaFb();
        for (int i = 0; i < sequenciaFB.length; i++) {
            if (sequenciaFB[i] == rodada) {
                return true;
            }
        }
        return false;
    }

    private Coordenada tiroManual(Jogador atirador) {
        return solicitaCoordenadasUsuario(atirador, "Informe as coordenadas do tiro:");
    }

    private Coordenada solicitaCoordenadasUsuario(Jogador usuario, String mensagem) {
        boolean valido = false;
        Scanner coordenadas = new Scanner(System.in);
        System.out.println(mensagem);
        while (!valido) {
            try {
                String coordenadasStr = coordenadas.nextLine();
                Coordenada coordenadaInformada = usuario.getTabuleiro().processarCoordenadas(coordenadasStr);
                if (coordenadaInformada != null) {
                    return coordenadaInformada;
                }
            } catch (Exception e) {
                System.err.println("Aconteceu um erro ao processar a coordenada: " + e.getLocalizedMessage() + ". Por favor, use o padrão 'L#', onde L é a letra da linha (A a T) e N é o número da coluna de 1 a " + computador.getTabuleiro().getTabuleiro()[1].length);
            }
            if (!valido) {
                System.err.println("Use o padrão 'L#', onde L é a letra da linha (A a T) e N é o número da coluna de 0 a " + computador.getTabuleiro().getTabuleiro()[1].length);
            }
        }
        return null;
    }

    private Coordenada coordenadaAleatoria() {
        Coordenada aleatoria;
        int coluna = new Random().nextInt(computador.getTabuleiro().getTabuleiro().length);
        int linha = new Random().nextInt(computador.getTabuleiro().getTabuleiro()[0].length);
        aleatoria = new Coordenada(coluna, linha);
        return aleatoria;
    }

    private Computador getComputador() {
        return (Computador) this.computador;
    }

    private Jogador getHumano() {
        return this.humano;
    }

    public void setComputador(Jogador computador) {
        this.computador = computador;
    }

    public void setHumano(Jogador humano) {
        this.humano = humano;
    }

    public int[] getTamanhosDeNavio() {
        return tamanhosDeNavio;
    }

    private void mensagemVermelha(String mensagem) {
        String ANSI_RED = "\u001B[31m";
        String ANSI_RESET = "\u001B[0m";
        System.out.println(ANSI_RED + mensagem + ANSI_RESET);
    }


}
