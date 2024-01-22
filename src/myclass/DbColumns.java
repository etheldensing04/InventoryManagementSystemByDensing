package myclass;

public enum DbColumns {
    IVENTORYCOLUMNS(new String[]{"productID","Category","ProductName",
        "Description","Quantity","RetailPrice","DateOfPurchase"}),
        PURCHASEDCOLUMNS(new String[]{"invoiceNumber","product","discountPercent","quantity","subtotal","total","purchasedDate","sellerfname","sellerlname"}),
        REPORTOPTIONS(new String[]{"Sales Report", "Inventory Report", "Out of Stocks", "Top Sales", "Top Sellers"});
    
    private final String[] values;
    
    DbColumns(String[] values){
        this.values = values;
    }
    
    public String[] getValues(){
        return values;
    }    
}
