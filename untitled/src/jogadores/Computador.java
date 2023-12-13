package jogadores;

import trabalho2.Coordenada;
import trabalho2.navios.Navio;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Computador extends Jogador {

    private int numFibonacci;
    private int maxTiros;
    private Coordenada[] tirosComputador;
    private int[] sequenciaFibonacci;

    public Computador(int[] qtdsMaximaDeNavio) {
        super(qtdsMaximaDeNavio);
        maxTiros = linhas * colunas * 3; // assumimos que a quantidade máxima de tiros é três vezes a quantidade de posições disponíveis no tabuleiro
    }

    // Método para ler o arquivo e processar os dados
    public boolean lerArquivoEProcessar(String nomeArquivo) {
        try (BufferedReader br = new BufferedReader(new FileReader(nomeArquivo))) {

            //Lê a primeira linha do arquivo para obter a quantidade de termos da sequência de Fibonacci
            numFibonacci = lerNumFibonacci(br);

            System.out.println("Fibonacci: " + numFibonacci);
            sequenciaFibonacci = fibonacci(numFibonacci);
            imprimeVetorFibonacci(sequenciaFibonacci);

            // Pula a linha em branco
            br.readLine();

            // Lê as coordenadas dos navios enquanto houverem linhas que não estejam vazias (empty)
            frota = lerPosicoesNavios(br);

            // Posiciona a frota de navios no tabuleiro
            posicionaNavios(frota);

            // Lê as coordenadas dos tiros
            tirosComputador = lerPosicoesTiros(br, maxTiros);

        } catch (FileNotFoundException e) {
            System.err.println("O arquivo informado não existe.");
            return false;
        } catch (IOException e) {
            System.err.println("Ocorreu um problema ao ler o arquivo.");
            return false;
        }
        return true;
    }

    private void imprimeVetorFibonacci(int[] sequenciaFibonacci) {
        for (int i = 0; i < sequenciaFibonacci.length; i++) {
            System.out.print(sequenciaFibonacci[i] + " ");
        }
        System.out.println(); // Pula uma linha após imprimir a sequência
    }



    // Lê as coordenadas dos tiros
    private Coordenada[] lerPosicoesTiros(BufferedReader br, int maxTiros) throws IOException {
        Coordenada[] tirosComputador = new Coordenada[maxTiros];
        String linha;
        int tiros = 0;
        while ((linha = br.readLine()) != null) {
            int tiroLinha = tabuleiro.retornaPosicao(linha.charAt(0));
            int tiroColuna = Integer.parseInt(linha.substring(1));
            tirosComputador[tiros++] = new Coordenada(tiroLinha, tiroColuna);
        }
        return tirosComputador;
    }

    // Lê as coordenadas dos navios enquanto houverem linhas que não estejam vazias (empty)
    private Navio[] lerPosicoesNavios(BufferedReader br) throws IOException {
        String linha;
        Navio[] frota = new Navio[5];
        int pos = 0; // posicao do navio no vetor da frota de navios
        while ((linha = br.readLine()) != null && !linha.trim().isEmpty()) { // trim() remove espaços em branco
            String[] partes = linha.trim().split(",");
            if (partes.length != 2) {
                throw new IllegalArgumentException("Verifique o arquivo de entrada. A linha que deveria ter as coordeadas do navio " + pos + "está mal formatada.");
            } else {
                Coordenada coordenadasIniciais = tabuleiro.processarCoordenadas(partes[0]);
                Coordenada coordenadasFinais = tabuleiro.processarCoordenadas(partes[1]);
                frota[pos++] = new Navio(coordenadasIniciais, coordenadasFinais);
            }
        }
        return frota;
    }

    // Lê a primeira linha do arquivo para obter a quantidade de termos da sequência de Fibonacci
  //TODO arrumar parte do fibonnacci -
     private int lerNumFibonacci(BufferedReader br) throws IOException {
        String linha;
        if ((linha = br.readLine()) != null) {
            numFibonacci = Integer.parseInt(linha);

            // f[0] - 0 / f[1] 1 , para [2] e [n] // f[i]= f[i-1] + f[i-2]
            return numFibonacci;
        } else {
            throw new IllegalArgumentException("A primeira linha do arquivo não contém a quantidade de termos da sequência de Fibonacci.");
        }
    }


    public Coordenada proximoTiro() {
        if (tirosComputador.length > tirosDados +1){
            return  tirosComputador[tirosDados++];
        }

        return null;
    }

    public static int[] fibonacci(int N) {
        if (N <= 0) {
            return new int[0]; // se N for menor ou igual a 0, retorna vazio e nem calcula a sequencia...
        }

        int[] vetorFibonacci = new int[N]; // inicializa o vetor com o numero de posições igual a N
        vetorFibonacci[0] = 1; // a primeira posição é sempre 1

        if (N > 1) { // se tiver mais do que uma posição (N maior que 1)
            vetorFibonacci[1] = 1; // a segunda posição da sequencia vai ser sempre 1 também
            for (int i = 2; i < N; i++) {
                // formula do da sequencia de fibonacci (o valor da sequencia é sempre a soma dos dois numeros anteriores, ou seja, que estao nas duas posições do vetor que precedem a posiçao atual chamdada de i)
                vetorFibonacci[i] = vetorFibonacci[i - 1] + vetorFibonacci[i - 2];
            }
        }
        return vetorFibonacci;
    }

    public int[] getSequenciaFb() {
        return sequenciaFibonacci;
    }
}
