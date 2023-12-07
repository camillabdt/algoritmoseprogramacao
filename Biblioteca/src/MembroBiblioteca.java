import java.util.ArrayList;
import java.util.List;

public class MembroBiblioteca {
    private String nome;
    private String numIdentificacao;
    private List<Material> materiaisEmprestados;

    public MembroBiblioteca(String nome, String numIdentificacao) {
        this.nome = nome;
        this.numIdentificacao = numIdentificacao;
        this.materiaisEmprestados = new ArrayList<>();
    }

    public void emprestarMaterial(Material material) {
        material.emprestar();
        materiaisEmprestados.add(material);
    }

    // Getters e Setters
    // ...
}
