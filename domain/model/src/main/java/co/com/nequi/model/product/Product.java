package co.com.nequi.model.product;

public record Product(Long id, String name, Long branchId, Integer stock) {
    public Product(String name, Long branchId) {
        this(null, name, branchId);
    }

    public Product(Long id, String name, Long branchId) {
        this(id, name, branchId, null);
    }

    public Product(Product product, Integer stock) {
        this(product.id, product.name, product.branchId, stock);
    }
}
