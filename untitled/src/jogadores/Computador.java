package jogadores;

import trabalho2.Coordenada;
import trabalho2.Tabuleiro;
import trabalho2.navios.Navio;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Computador extends Jogador {

    private int numFibonacci;
    private int maxTiros;
    private int tirosDados;
    private Coordenada[] tirosComputador;

    public Computador() {
        maxTiros = linhas * colunas;
        tirosDados = 0;
    }

    // Método para ler o arquivo e processar os dados
    public void lerArquivoEProcessar(String nomeArquivo) {
        try (BufferedReader br = new BufferedReader(new FileReader(nomeArquivo))) {
            // Lê a primeira linha do arquivo para obter a quantidade de termos da sequência de Fibonacci
            numFibonacci = lerNumFibonacci(br);
            System.out.println("Fibonacci: " + numFibonacci);

            // Pula a linha em branco
            br.readLine();

            // Lê as coordenadas dos navios enquanto houverem linhas que não estejam vazias (empty)
            frota = lerPosicoesNavios(br);

            // Posiciona a frota de navios no tabuleiro
            posicionaNavios(frota);

            // Lê as coordenadas dos tiros
            tirosComputador = lerPosicoesTiros(br, maxTiros);

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
    private int lerNumFibonacci(BufferedReader br) throws IOException {
        String linha;
        if ((linha = br.readLine()) != null) {
            numFibonacci = Integer.parseInt(linha);
            return numFibonacci;
        } else {
            throw new IllegalArgumentException("A primeira linha do arquivo não contém a quantidade de termos da sequência de Fibonacci.");
        }
    }


    public Coordenada proximoTiro() {
        return tirosComputador[tirosDados++];
    }
}
