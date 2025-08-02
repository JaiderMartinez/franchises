package co.com.nequi.model.branch;

public record Branch(Long id, String name, Long franchiseId) {
    public Branch(String name, Long franchiseId) {
        this(null, name, franchiseId);
    }
}
