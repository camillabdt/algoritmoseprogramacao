public class Revista extends Material {
    private int numeroEdicao;
    private String dataPublicacao;

    public Revista(String titulo, int numeroEdicao, String dataPublicacao) {
        super(titulo);
        this.numeroEdicao = numeroEdicao;
        this.dataPublicacao = dataPublicacao;
    }


    public void emprestar() {
        // Implementação para emprestar uma revista
    }
}
