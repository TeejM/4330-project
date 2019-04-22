package documentanalyzer;

public class Tester {
    
    DocumentDatabase db;
    
    public Tester(DocumentDatabase db) {
        this.db = db;
    }
    
    public void testAdd() {
        db.addFolder("C:/Users/TJ/Desktop/test/");
        
        if(db.getDocument(1) != null) {
            System.out.println("Add Test passed.");
        }
        else
            System.out.println("Add Test failed.");
    }
    
    public void testClear() {
        if(db.clearData())
            System.out.println("Clear Test passed.");
        else
            System.out.println("Clear Test failed.");
    }
}
