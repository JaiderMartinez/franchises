package co.com.nequi.r2dbc.entities;

public record TopStockProductRow(
        Long branchId,
        String branchName,
        Long productId,
        String productName,
        Integer stock
) {
}
