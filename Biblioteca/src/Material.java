public abstract class Material {
    protected String titulo;

    public Material(String titulo) {
        this.titulo = titulo;
    }

    // Getters e Setters
    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    // Método abstrato para emprestar o material
    public abstract void emprestar();
}

