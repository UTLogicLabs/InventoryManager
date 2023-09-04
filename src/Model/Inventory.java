package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.UUID;

public class Inventory {
    private static final ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static final ObservableList<Product> allProducts = FXCollections.observableArrayList();

    public static int generateUniqueId() {
        UUID idOne = UUID.randomUUID();
        String str=""+idOne;
        int uid=str.hashCode();
        String filterStr=""+uid;
        str=filterStr.replaceAll("-", "");
        return Integer.parseInt(str);
    }
    public static void addPart(Part partToAdd) {
        allParts.add(partToAdd);
    }
    public static void addProduct(Product productToAdd) {
        allProducts.add(productToAdd);
    }
    public static Part lookupPart(int partId) {
        if(!allParts.isEmpty()) {
            for (Part part : allParts) {
                if (part.getId() == partId)
                    return part;
            }
        }
        return null;
    }
    public static Product lookupProduct (int productId) {
        if(!allProducts.isEmpty()) {
            for (Product product : allProducts) {
                if (product.getId() == productId)
                    return product;
            }
        }
        return null;
    }
    public static ObservableList<Part> lookupPart(String partName) {
        ObservableList<Part> found = FXCollections.observableArrayList();
        for(Part part: getAllParts()) {
            if(part.getName().contains(partName)) {
                found.add(part);
            }
        }
        return found;
    }
    public static ObservableList<Product> lookupProduct(String productName) {
        ObservableList<Product> found = FXCollections.observableArrayList();
        for(Product product : Inventory.getAllProducts()) {
            if(product.getName().contains(productName)) {
                found.add(product);
            }
        }
        return found;
    }
    public static void updatePart(int index, Part updatedPart) {
        allParts.set(index, updatedPart);
    }
    public static void updateProduct(int index, Product updatedProduct) {
        allProducts.set(index, updatedProduct);
    }
    public static boolean deletePart(Part selectedPart) {
        if(allParts.contains(selectedPart)) {
            return allParts.remove(selectedPart);
        }

        return false;
    }
    public static boolean deleteProduct(Product selectedProduct) {
        if(allProducts.contains(selectedProduct)) {
            return allProducts.remove(selectedProduct);
        }

        return false;
    }
    public static ObservableList<Part> getAllParts() {
        return allParts;
    }
    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }
}