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
        BatalhaNaval jogo = new BatalhaNaval();
        Scanner scanner = new Scanner(System.in);
        int escolha;

        // Inicializa o jogador automático (computador)
        ((Computador) jogo.computador).lerArquivoEProcessar("computador.txt");
        // jogo.getComputador().imprimirTabuleiro();

        escolha = -1;
        while (escolha != 1 && escolha != 2) {
            System.out.println("Bem vindo ao Batalha Naval da Bdt!");
            System.out.println("Escolha o seu modo de jogo:");
            System.out.println("1. Automático");
            System.out.println("2. Manual");
            try {
                escolha = scanner.nextInt();
                if (escolha == 1) {
                    jogo.setModoAutomatico(true);// retorna o valor de modoAutomático
                } else if (escolha == 2) {
                    jogo.setModoAutomatico(false);
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
        jogo.inicializarTabuleiroHumano(jogo.isModoAutomatico());
        // jogo.getHumano().imprimirTabuleiro();

        // Par ou Impar (decide quem começa)
        int numeroAleatorio = new Random().nextInt(5) + 1;
        System.out.println("Vou jogar o dado! Se der um número par você começa, se der ímpar o computador começa! ");

        if (numeroAleatorio % 2 == 0) {
            System.out.println("O dado mostrou o número: " + numeroAleatorio + ", então o COMPUTADOR começa!");
            jogo.comecaBatalha(jogo.getComputador(), jogo.getHumano());
        } else {
            System.out.println("O dado mostrou o número: " + numeroAleatorio + ", então VOCÊ começa!");
            jogo.comecaBatalha(jogo.getHumano(), jogo.getComputador());
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
            while (humano.getnaviosRestantes() < computador.getnaviosRestantes()) { // enquanto o humano tiver menos peça que o computador,
                // o humano precisa inserir peças
                humano.imprimirTabuleiro();// perguntar para a brenda se pode mostrar aqui o tabuleiro no
                // momento de inserir navios
                try {
                    insereNavioManualmente();
                } catch (IllegalArgumentException execao) {
                    String mensagemDeErro = execao.getLocalizedMessage();
                    System.out.println("Aconteceu o seguinte erro: " + mensagemDeErro);
                }
            }
        } else {
            while (humano.getnaviosRestantes() < computador.getnaviosRestantes()) {
                insereNavioAleatoriamente();
            }
        }

    }

    private void insereNavioAleatoriamente() { //  faz randon e colocar posição inicial e final - usa om mesmo do computador
        Coordenada coordenadasIniciais = coordenadaAleatoria();
        Coordenada coordenadasFinais = getCoordenadaFinalAleatoria(coordenadasIniciais);
        if (!humano.adicionarNavio(new Navio(coordenadasIniciais, coordenadasFinais)) && humano.getnaviosRestantes() < computador.getnaviosRestantes()) {
            throw new IllegalArgumentException("O limite de navios foi atingido mas o computador possui mais navios que o usuário.");
        } else if (humano.getnaviosRestantes() > computador.getnaviosRestantes()) {
            throw new IllegalArgumentException("O usuário possui mais peças do que o computador.");
        }
    }
    // quantidade de navios é o numero de linhas entre dois espaços

    private Coordenada getCoordenadaFinalAleatoria(Coordenada coordenadasIniciais) {
        Random aleatorio = new Random();
        boolean horizontal = (aleatorio.nextInt() % 2 == 0); // se o numero aleatorio for par, então é horizontal
        int tamanhoNavio = aleatorio.nextInt(tamanhosDeNavio.length - 1);
        Coordenada coordenadasFinais;
        if (horizontal) {
            coordenadasFinais = new Coordenada(coordenadasIniciais.getX(), coordenadasIniciais.getY() + tamanhoNavio);
        } else {
            coordenadasFinais = new Coordenada(coordenadasIniciais.getX() + tamanhoNavio, coordenadasIniciais.getY());
        }
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
                    System.out.println("Essa coordenada não está disponivel");
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
                        boolean faltaHumanoAdicionar = humano.getnaviosRestantes() < computador.getnaviosRestantes();
                        boolean adicionouDemais = humano.getnaviosRestantes() > computador.getnaviosRestantes();

                        if (!adicionouNavio && faltaHumanoAdicionar) {
                            throw new IllegalArgumentException("O limite de navios foi atingido mas o computador possui mais navios que o usuário.");
                        } else if (adicionouDemais) {
                            throw new IllegalArgumentException("O usuário possui mais navios do que o computador.");
                        }
                    } else {
                        System.out.println("O tamanho do navio não corresponde a um tamanho válido."); // solicitara novamente a coordenada final
                    }
                } else {
                    System.out.println("A coordenada final já está ocupada ou é inválida."); // solicitara novamente a coordenada final
                }
            }
        }
    }

    private boolean isTamanhoNavioValido(Navio navio, Jogador jogador) {
        int tamanhohorizontal = navio.getCoordenadasFinais().getX() - navio.getCoordenadasIniciais().getX() + 1;
        int tamanhovertical = navio.getCoordenadasFinais().getY() - navio.getCoordenadasIniciais().getY() + 1;
        for (int i = 0; i < tamanhosDeNavio.length; i++) {
            if (tamanhosDeNavio[i] == tamanhovertical || tamanhosDeNavio[i] == tamanhohorizontal) {
                int tamanho =Math.abs(tamanhohorizontal - tamanhovertical)+1; // abs retorna o modulo (sem sinal)
                if (jogador.isTamanhoDisponivel(tamanho)) {
                    return true; // o tamanho vertical ou horizontal tem um tamanho válido!
                }

            }
        }
        System.out.println("O navio tem tamanho INVÁLIDO: " + tamanhohorizontal + "x" + tamanhovertical);
        return false; // se percorrer o vetor de tamanhos e nao encontrar um tamanho valido, retorna false
    }


    private void comecaBatalha(Jogador atirador, Jogador alvo) {
        Coordenada coordenadaTiro;
        int rodada = 0;

        while (atirador.getTabuleiro().restamNavios() && alvo.getTabuleiro().restamNavios()) {
            System.out.print(" ## RODADA " + (++rodada));

            // Identifica quem atira e localiza as coordenadas
            if (atirador instanceof Computador) {
                System.out.println(" (computador) ");
                coordenadaTiro = ((Computador) atirador).proximoTiro();

                // se acabar as coordenadas de tiros do computador no arquivo, sorteia uma nova coordenada
                if (coordenadaTiro == null){
                    coordenadaTiro = coordenadaAleatoria();
                }
            } else {
                System.out.println(" (Você)");
                if (modoAutomatico) {
                    coordenadaTiro = coordenadaAleatoria();
                    System.out.println("Tiro em: "+coordenadaTiro.getX()+","+coordenadaTiro.getY());
                } else {
                    coordenadaTiro = tiroManual(atirador);
                }
            }

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
        System.out.println("Fim de jogo! ");
        if (atirador.getTabuleiro().restamNavios()) {
            if (atirador instanceof Computador) {
                System.out.println("Você perdeu!");
            } else {
                System.out.println("Parabéns, você ganhou!");
            }
        }
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
                System.out.println("Aconteceu um erro ao processar a coordenada: " + e.getLocalizedMessage() + ". Por favor, use o padrão 'L#', onde L é a letra da linha (A a T) e N é o número da coluna de 1 a " + computador.getTabuleiro().getTabuleiro()[1].length);
            }
            if (!valido) {
                System.out.println("Use o padrão 'L#', onde L é a letra da linha (A a T) e N é o número da coluna de 0 a " + computador.getTabuleiro().getTabuleiro()[1].length);
            }
        }
        return null;
    }

    private Coordenada coordenadaAleatoria() {
        Coordenada aleatoria;
        int x = new Random().nextInt(computador.getTabuleiro().getTabuleiro().length - 1);
        int y = new Random().nextInt(computador.getTabuleiro().getTabuleiro()[0].length - 1);
        aleatoria = new Coordenada(x, y);
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


}
