package ru.knyazev.lesson10.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.knyazev.lesson10.controller.NotFoundException;
import ru.knyazev.lesson10.persist.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CartServiceImpl implements CartService {

    private final Map<Long, Map<LineItem, Integer>> lineItemsMap = new ConcurrentHashMap<>();

    private final ProductService productService;

    private final UserService userService;

    @Autowired
    public CartServiceImpl(ProductService productService, UserService userService) {
        this.productService = productService;
        this.userService = userService;
    }

    @Override
    public void addProductForUserQty(long productId, long userId, int qty) {
        Map<LineItem, Integer> itemsForUser = lineItemsMap.computeIfAbsent(userId, k -> new HashMap<>());

        Product product = productService.findById(productId).orElseThrow(NotFoundException::new);
        UserDTO user = userService.findById(userId).orElseThrow(NotFoundException::new);
        LineItem key = new LineItem(product, user, qty);

        itemsForUser.merge(key, qty, Integer::sum);
    }

    @Override
    public void removeProductForUser(long productId, long userId, int qty) {
        Map<LineItem, Integer> itemsForUser = lineItemsMap.getOrDefault(userId, new HashMap<>());
        if (itemsForUser == null) {
            return;
        }

        LineItem key = new LineItem(productId, userId);
        Integer count = itemsForUser.get(key);
        if (count != null) {
            if (count < qty) {
                itemsForUser.remove(key);
            } else {
                itemsForUser.put(key, count - qty);
            }
        }
    }

    @Override
    public void removeAllForUser(long userId) {
        lineItemsMap.remove(userId);
    }

    @Override
    public List<LineItem> findAllItemsForUser(long userId) {
        return new ArrayList<>(lineItemsMap.get(userId).keySet());
    }
}
