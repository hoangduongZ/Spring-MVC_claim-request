package mock.claimrequest;

import java.util.*;
import java.util.stream.Collectors;

public class Test {
    public static void main(String[] args) {
        List<Item> dbData = Arrays.asList(
                new Item(1, "Value1"),
                new Item(2, "Value2"),
                new Item(3, "Value3")
        );

        List<Item> newData = Arrays.asList(
                new Item(1, "Value1"), // Không thay đổi
                new Item(2, "NewValue2"), // Đã thay đổi
                new Item(4, "Value4") // Phần tử mới
        );

        // Tìm các phần tử thay đổi hoặc mới
        List<Item> changedItems = newData.stream()
                .filter(newItem -> {
                    Optional<Item> matchingDbItem = dbData.stream()
                            .filter(dbItem -> dbItem.getId() == newItem.getId())
                            .findFirst();
                    return matchingDbItem
                            .map(dbItem -> !dbItem.getValue().equals(newItem.getValue()))
                            .orElse(true); // true nếu không tìm thấy trong dbData (phần tử mới)
                })
                .collect(Collectors.toList());

        // In ra kết quả
        System.out.println("Changed or new items: ");
        changedItems.forEach(System.out::println);
    }
}

class Item {
    private int id;
    private String value;

    public Item(int id, String value) {
        this.id = id;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Item{id=" + id + ", value='" + value + "'}";
    }
}
