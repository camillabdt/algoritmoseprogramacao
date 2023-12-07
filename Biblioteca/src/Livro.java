public class Livro extends Material {
    private String autor;
    private int numPaginas;
    private int numCopias;

    public Livro(String titulo, String autor, int numPaginas, int numCopias) {
        super(titulo);
        this.autor = autor;
        this.numPaginas = numPaginas;
        this.numCopias = numCopias;
    }

    // Getters e Setters
    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public int getNumPaginas() {
        return numPaginas;
    }

    public void setNumPaginas(int numPaginas) {
        this.numPaginas = numPaginas;
    }

    public int getNumCopias() {
        return numCopias;
    }

    public void setNumCopias(int numCopias) {
        this.numCopias = numCopias;
    }

    @Override
    public void emprestar() {
        if (numCopias > 0) {
            numCopias--;
        }
    }

    public void devolver() {
        numCopias++;
    }
}
