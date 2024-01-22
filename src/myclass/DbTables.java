package myclass;

public enum DbTables {
    INVENTORYTABLE("inventorytable"),PURCHASEDTABLE("purchasedtable");
    
    private final String value;
    
    DbTables(String value){
        this.value =  value;
    }
    
    public String getValue(){
        return value;
    }
}