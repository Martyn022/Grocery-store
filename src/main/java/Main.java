import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static Scanner scanner = new Scanner(System.in);
    public static String[] products = {"Хлеб", "Яблоки", "Молоко"};
    protected static int[] prices = {100, 200, 300};

    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {

        XMLSetReader settings = new XMLSetReader(new File("shop.xml"));
        File loadFile = new File(settings.loadFile);
        File saveFile = new File(settings.saveFile);
        File logFile = new File(settings.logFile);

        Basket basket = createBasket(loadFile, settings.isLoad, settings.loadFormat);
        ClientLog log = new ClientLog();

        System.out.println("Список необходимых товаров для покупки:");
        for (int i = 0; i < products.length; i++) {
            System.out.println(products[i] + " " + prices[i] + " руб/шт.");
        }
        while (true) {
            System.out.println("Выберите товар и количество или введите `end`");
            String input = scanner.nextLine();
            if ("end".equals(input)) {
                if (settings.isLog) {
                    log.exportAsCSV(logFile);
                }
                break;
            }
            String[] parts = input.split(" ");
            if (Integer.parseInt(parts[0]) <= products.length && Integer.parseInt(parts[0]) > 0) {
                int productNumber = Integer.parseInt(parts[0]) - 1;
                int productCount = Integer.parseInt(parts[1]);
                basket.addToCart(productNumber, productCount);
                if (settings.isLog) {
                    log.log(productNumber, productCount);
                }
                if (settings.isSave) {
                    switch (settings.saveFormat) {
                        case "json" -> basket.saveJSON(saveFile);
                        case "txt" -> basket.saveTxt(saveFile);
                    }
                }
            } else {
                System.out.println("Некорректный ввод!");
            }
        }
        basket.printCart();
    }

    private static Basket createBasket(File loadFile, boolean isLoad, String loadFormat) {
        Basket basket;
        if (isLoad && loadFile.exists()) {
            basket = switch (loadFormat) {
                case "json" -> Basket.loadFromJSONFile(loadFile);
                case "txt" -> Basket.loadFromTxtFile(loadFile);
                default -> new Basket(products, prices);
            };
        } else {
            basket = new Basket(products, prices);
        }
        return basket;
    }
}