import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    static Scanner scanner = new Scanner(System.in);
    static String[] products = {"Хлеб", "Яблоки", "Молоко"};
    static int[] prices = {100, 200, 300};
    static File saveFile = new File("basket.json");


    public static void main(String[] args) throws FileNotFoundException {

        Basket basket = null;
        if (saveFile.exists()) {
            basket = Basket.loadFromJSONFile(saveFile);
        } else {
            basket = new Basket(products, prices);
        }

        System.out.println("Список необходимых товаров для покупки:");
        for (int i = 0; i < products.length; i++) {
            System.out.println(products[i] + " " + prices[i] + " руб/шт.");
        }

        ClientLog log = new ClientLog();
        while (true) {
            System.out.println("Выберите товар и количество или введите `end`");
            String input = scanner.nextLine();
            if ("end".equals(input)) {
                log.exportAsCSV(new File("log.csv"));
                break;
            }

            String[] parts = input.split(" ");
            if (Integer.parseInt(parts[0]) <= products.length && Integer.parseInt(parts[0]) > 0) {
                int productNumber = Integer.parseInt(parts[0]) - 1;
                int productCount = Integer.parseInt(parts[1]);
                basket.addToCart(productNumber, productCount);
                log.log(productNumber, productCount);
                basket.saveJSON(saveFile);
            } else {
                System.out.println("Некорректный ввод!");
            }
        }
        basket.printCart();
    }
}