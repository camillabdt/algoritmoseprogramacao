public class Biblioteca {
    public static void main(String[] args) {
        Livro livro1 = new Livro("Livro Exemplo", "Autor Exemplo", 300, 5);
        MembroBiblioteca membro = new MembroBiblioteca("João", "12345");

        membro.emprestarMaterial(livro1);
        System.out.println("Número de cópias disponíveis do livro: " + livro1.getNumCopias());
        livro1.devolver();
        System.out.println("Número de cópias disponíveis após devolução: " + livro1.getNumCopias());
    }
}
