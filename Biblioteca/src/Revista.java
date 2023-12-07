public class Revista extends Material {
    private int numeroEdicao;
    private String dataPublicacao;

    public Revista(String titulo, int numeroEdicao, String dataPublicacao) {
        super(titulo);
        this.numeroEdicao = numeroEdicao;
        this.dataPublicacao = dataPublicacao;
    }

    // Getters e Setters
    // ...

    @Override
    public void emprestar() {
        // Implementação para emprestar uma revista
    }
}
