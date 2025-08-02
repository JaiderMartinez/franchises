package co.com.nequi.model.product;

public record Product(Long id, String name, Long branchId) {
    public Product(String name, Long branchId) {
        this(null, name, branchId);
    }
}
