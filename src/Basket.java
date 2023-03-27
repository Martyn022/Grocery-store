import java.io.*;
import java.util.Arrays;

public class Basket implements Serializable {
    private static final long serialVersionUID = 1L;
    private String[] products;
    private int[] prices;
    private int[] quantities;

    public Basket() {
    }

    public Basket(String[] products, int[] prices) {
        this.products = products;
        this.prices = prices;
        this.quantities = new int[products.length];
    }

    public void addToCart(int productNumber, int productCount) {
        quantities[productNumber] += productCount;
    }

    public void printCart() {
        int sumProducts = 0;
        System.out.println("Ваша корзина:");
        for (int i = 0; i < products.length; i++) {
            if (quantities[i] > 0) {
                int currentPrice = prices[i] * quantities[i];
                sumProducts += currentPrice;
                System.out.println(products[i] + " " +
                        quantities[i] + " шт " +
                        prices[i] + " руб/шт " +
                        currentPrice + " руб в сумме. \n");
            }
        }
        System.out.println("Итого: " + sumProducts + " руб.");
    }

    public void saveTxt(File textFile) throws FileNotFoundException {
        try (PrintWriter out = new PrintWriter(textFile)) {
            out.println(String.join("", products));
            out.println(String.join("", Arrays.stream(prices)
                    .mapToObj(String::valueOf)
                    .toArray(String[]::new)));
            out.println(String.join("", Arrays.stream(quantities)
                    .mapToObj(String::valueOf)
                    .toArray(String[]::new)));
        }
    }

    static Basket loadFromTxtFile(File textFile) {
        Basket basket = new Basket();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(textFile))) {
            String productsStr = bufferedReader.readLine();
            String pricesStr = bufferedReader.readLine();
            String quantitiesStr = bufferedReader.readLine();

            basket.products = productsStr.split(" ");
            basket.prices = Arrays.stream(pricesStr.split(" "))
                    .map(Integer::parseInt)
                    .mapToInt(Integer::intValue)
                    .toArray();
            basket.quantities = Arrays.stream(quantitiesStr.split(" "))
                    .map(Integer::parseInt)
                    .mapToInt(Integer::intValue)
                    .toArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return basket;
    }

    public void saveBin(File file) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Basket loadFromBinFile(File file) {
        Basket basket = null;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            basket = (Basket) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return basket;
    }

}


