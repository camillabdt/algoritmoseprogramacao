public abstract class Material {
    protected String titulo;
    // Outras propriedades comuns podem ser adicionadas aqui

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

