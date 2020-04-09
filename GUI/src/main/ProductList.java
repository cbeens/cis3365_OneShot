package GUI.Class;

public class ProductList {
    private String product;
    private String productDescription;
    private String useCount;
    private String lastUsed;

    //constructor...
    public ProductList(String product, String description, String count, String last) {
        this.product = product;
        this.productDescription = description;
        this.useCount = count;
        this.lastUsed = last;
    }


    public String getProduct() {
        return product;
    }
    public void setProduct(String product) {
        this.product = product;
    }

    public String getProductDescription() {return productDescription;}
    public void setProductDescription(String description) {
        this.productDescription = description;
    }

    public String getUseCount() {return useCount;}
    public void setUseCount(String count) {
        this.useCount = count;
    }

    public String getLastUsed() {return lastUsed;}
    public void setLastUsed(String last) {this.lastUsed = last;}

    @Override
    public String toString() {
        return "Product{" +
                ", Product ='" + product + '\'' +
                ", Product Description ='" + productDescription + '\'' +
                ", Use Count ='" + useCount + '\'' +
                ", Last Used ='" + lastUsed + '\'' +
                '}';
    }



}
