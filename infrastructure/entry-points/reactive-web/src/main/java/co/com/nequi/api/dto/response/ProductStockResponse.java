package co.com.nequi.api.dto.response;

public record ProductStockResponse(Long branchId, String branchName, Long productId,
                                   String productName, Integer stock) {
}

