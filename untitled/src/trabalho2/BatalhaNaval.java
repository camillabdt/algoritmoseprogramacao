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

    public BatalhaNaval() { // metodo construtor
        this.computador = new Computador();
        this.humano = new Jogador();
        tamanhosDeNavio = new int[]{2, 3, 5, 7};
        //TODO:  numero de navios pode ser diferente? Por exemplo, posso ter 5 navios menores que os 4 do PC se no total o numero de peças for o mesmo? - focar no numero de navios do computador
    }

    public static void main(String[] args) {
        BatalhaNaval jogo = new BatalhaNaval();
        Scanner scanner = new Scanner(System.in);
        int escolha;

        // Inicializa o jogador automático (computador)
        ((Computador) jogo.computador).lerArquivoEProcessar("computador.txt");
        // jogo.getComputador().imprimirTabuleiro();

        escolha = -1;
        while
        (escolha != 1 && escolha !=2) {
            System.out.println("Bem vindo ao Batalha Naval da Bdt!");
                    System.out.println("Escolha o seu modo de jogo:");
            System.out.println("1. Automático");
            System.out.println("2. Manual");
            escolha = scanner.nextInt();
            if (escolha ==1) {
                jogo.setModoAutomatico(true);// retorna o valor de modoAutomático
            } else if (escolha == 2) {
                jogo.setModoAutomatico(false);
            } else {
                System.out.println("Escolha inválida");

            }
        }

// Inicializa o jogador humano
        jogo.inicializarTabuleiroHumano(jogo.isModoAutomatico());
        // jogo.getHumano().imprimirTabuleiro();

        // Par ou Impar (decide quem começa)
        int numeroAleatorio = new Random().nextInt(10);
        if (numeroAleatorio % 2 == 0) {
            jogo.comecaBatalha(jogo.getComputador(), jogo.getHumano());
        } else {
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
            while (humano.getNaviosRestantes() < computador.getNaviosRestantes()) { // enquanto o humano tiver menos peça que o computador,
                // o humano precisa inserir peças
                humano.imprimirTabuleiro();// perguntar para a brenda se pode mostrar aqui o tabuleiro no
                // momento de inserir navios
                try {
                    insereNavioManualmente();
                } catch (IllegalArgumentException execao){
                    String mensagemDeErro = execao.getLocalizedMessage();
                    System.out.println("Aconteceu o seguinte erro: " + mensagemDeErro);
                }
            }
        } else {
            while (humano.getNaviosRestantes() < computador.getNaviosRestantes()) {
                insereNavioAleatoriamente();
            }
        }

    }

    private void insereNavioAleatoriamente() { //  faz randon e colocar posição inicial e final - usa om mesmo do computador
        Coordenada coordenadasIniciais = coordenadaAleatoria();
        Coordenada coordenadasFinais = getCoordenadaFinalAleatoria(coordenadasIniciais);
        if (!humano.adicionarNavio(new Navio(coordenadasIniciais, coordenadasFinais)) &&
                humano.getNaviosRestantes() < computador.getNaviosRestantes()) {
            throw new IllegalArgumentException("O limite de navios foi atingido mas o computador possui mais navios que o usuário.");
        } else if (humano.getNaviosRestantes() > computador.getNaviosRestantes()) {
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
        String mensagem = "Informe as coordenadas iniciais do seu navio:";
        Coordenada coordenadasIniciais = solicitaCoordenadasUsuario(humano, mensagem);

        // Solicita coordenadas finais (@TODO: TRATAR TAMANHO DO NAVIO)

        boolean valido = false;
        while (!valido) {
            mensagem = "Informe as coordenadas finais do seu navio:";
            Coordenada coordenadasFinais = solicitaCoordenadasUsuario(humano, mensagem);
            Navio navio = new Navio(coordenadasIniciais, coordenadasFinais);

            int tamanhohorizontal = coordenadasFinais.getX() - coordenadasIniciais.getX();
            int tamanhovertical = coordenadasFinais.getY() - coordenadasIniciais.getY();
// se for diferente de ' ' não coloca. - mensagem de errro e digite novamente

            for (int i = 0; i < tamanhosDeNavio.length; i++) ;
            {
                if (valido = true) {
                } else {
                    System.out.println("O tamanho do navio não corresponde a um tamanho válido");
                }
}           // checagens
            boolean adicionouNavio = humano.adicionarNavio(navio);
            boolean faltaHumanoAdicionar = humano.getNaviosRestantes() < computador.getNaviosRestantes();
            boolean adicionouDemais = humano.getNaviosRestantes() > computador.getNaviosRestantes();

            if (!adicionouNavio && faltaHumanoAdicionar) {
                throw new IllegalArgumentException("O limite de navios foi atingido mas o computador possui mais peças que o usuário.");
            } else if (adicionouDemais) {
                throw new IllegalArgumentException("O usuário possui mais peças do que o computador.");
            }
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
