import com.google.gson.Gson;

import java.io.*;
import java.util.Arrays;

public class Basket {
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
            for (String product : products) {
                out.print(product + " ");
            }
            out.println();
            for (int price : prices) {
                out.print(price + " ");
            }
            out.println();
            for (int quantity : quantities) {
                out.print(quantity + " ");
            }
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

    public void saveJSON(File file) {
        try (PrintWriter writer = new PrintWriter(file)) {
            Gson gson = new Gson();
            String json = gson.toJson(this);
            writer.print(json);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Basket loadFromJSONFile(File file) {
        Basket basket = null;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder builder = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            Gson gson = new Gson();
            basket = gson.fromJson(builder.toString(), Basket.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return basket;
    }

}


