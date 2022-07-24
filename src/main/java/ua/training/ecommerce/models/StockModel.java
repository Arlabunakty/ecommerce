package ua.training.ecommerce.models;

public class StockModel {

    String productName = "";
    String variantName = "";
    String stock = "0";

    public StockModel(String productName, String variantName, String stock) {
        this.setProductName(productName);
        this.setVariantName(variantName);
        this.setStock(stock);
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getVariantName() {
        return variantName;
    }

    public void setVariantName(String variantName) {
        this.variantName = variantName;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }
}
