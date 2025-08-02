package co.com.nequi.model.franchise;

public record ProductStock(Long branchId, String branchName, Long productId,
                           String productName, Integer stock) {
}
