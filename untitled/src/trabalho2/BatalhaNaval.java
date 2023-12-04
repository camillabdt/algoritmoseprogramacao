package trabalho2;


import jogadores.Computador;
import jogadores.Jogador;
import trabalho2.navios.Navio;

import java.util.Random;
import java.util.Scanner;

public class BatalhaNaval {
    Jogador computador;
    Jogador humano;

    private int[] tamanhosDeNavio;
    private boolean modoAutomatico = false;

    public BatalhaNaval() {
        this.computador = new Computador();
        this.humano = new Jogador();
        tamanhosDeNavio = new int[]{2, 3, 5, 7};
    }

    public static void main(String[] args) {
        BatalhaNaval jogo = new BatalhaNaval();

        // Inicializa o jogador automático (computador)
        ((Computador) jogo.computador).lerArquivoEProcessar("computador.txt");
        jogo.getComputador().imprimirTabuleiro();

        // Inicializa o jogador humano
        jogo.inicializarTabuleiroHumano(jogo.modoAutomatico);
        jogo.getHumano().imprimirTabuleiro();

        // Par ou Impar (decide quem começa)
        int numeroAleatorio = new Random().nextInt(10);
        if (numeroAleatorio % 2 == 0) {
            jogo.comecaBatalha(jogo.computador, jogo.humano);
        } else {
            jogo.comecaBatalha(jogo.humano, jogo.computador);
        }
    }

    private void inicializarTabuleiroHumano(boolean modoAutomatico) {
        if (!modoAutomatico) {
            while (humano.getPedacosRestantesNavios() < computador.getPedacosRestantesNavios()) {
                humano.imprimirTabuleiro();// perguntar para a brenda se pode mostrar aqui o tabuleiro no momento de inserir navios
                insereNavioManualmente();
            }
        } else {
            while (humano.getPedacosRestantesNavios() < computador.getPedacosRestantesNavios()) {
                insereNavioAleatoriamente();
            }
        }

    }

    private void insereNavioAleatoriamente() {
        Coordenada coordenadasIniciais = coordenadaAleatoria();
        Coordenada coordenadasFinais = getCoordenadaFinalAleatoria(coordenadasIniciais);
        if (!humano.adicionarNavio(new Navio(coordenadasIniciais, coordenadasFinais)) &&
                humano.getPedacosRestantesNavios() < computador.getPedacosRestantesNavios()) {
            throw new IllegalArgumentException("O limite de navios foi atingido mas o computador possui mais navios que o usuário.");
        } else if (humano.getPedacosRestantesNavios() > computador.getPedacosRestantesNavios()) {
            throw new IllegalArgumentException("O usuário possui mais peças do que o computador.");
        }
    }

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

    private void insereNavioManualmente() {
        // Solicita coordenadas iniciais
        String mensagem = "Informe as coordenadas iniciais do seu navio:";
        Coordenada coordenadasIniciais = solicitaCoordenadasUsuario(humano, mensagem);

        // Solicita coordenadas finais
        mensagem = "Informe as coordenadas finais do seu navio:";
        Coordenada coordenadasFinais = solicitaCoordenadasUsuario(humano, mensagem);

        if (!humano.adicionarNavio(new Navio(coordenadasIniciais, coordenadasFinais)) &&
                humano.getPedacosRestantesNavios() < computador.getPedacosRestantesNavios()) {
            throw new IllegalArgumentException("O limite de navios foi atingido mas o computador possui mais peças que o usuário.");
        } else if (humano.getPedacosRestantesNavios() > computador.getPedacosRestantesNavios()) {
            throw new IllegalArgumentException("O usuário possui mais peças do que o computador.");
        }
    }

    private void comecaBatalha(Jogador atirador, Jogador alvo) {
        Coordenada coordenadaTiro;
        while (atirador.getTabuleiro().restamNavios() && alvo.getTabuleiro().restamNavios()) {
            // Identifica quem atira e localiza as coordenadas
            if (atirador instanceof Computador) {
                coordenadaTiro = ((Computador) atirador).proximoTiro();
            } else {
                if (modoAutomatico) {
                    coordenadaTiro = coordenadaAleatoria();
                } else {
                    coordenadaTiro = tiroManual(atirador);
                }
            }

            // Dispara o tiro
            atirador.atirar(alvo, coordenadaTiro);

            // Inverte o alvo e o atirador para a próxima rodada
            Jogador aux = atirador; // variavel auxiliar para guardar temporariamente o atirador
            atirador = alvo;
            alvo = aux;
        }
        System.out.println("Fim de jogo! ");
        if (atirador.getTabuleiro().restamNavios()){
            if (atirador instanceof Computador){
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
                valido = true;
                return coordenadaInformada;
            } catch (Exception e) {
                System.out.println("Aconteceu um erro ao processar a coordenada: " + e.getLocalizedMessage() + ". Por favor, use o padrão 'L#', onde L é a letra da linha (A a T) e N é o número da coluna de 1 a " + computador.getTabuleiro().getTabuleiro()[1].length);
            }
            if (!valido) {
                System.out.println("Use o padrão 'L#', onde L é a letra da linha (A a T) e N é o número da coluna de 1 a " + computador.getTabuleiro().getTabuleiro()[1].length);
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


}
