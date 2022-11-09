package ru.knyazev.lesson10.service;

import lombok.Getter;
import lombok.Setter;
import ru.knyazev.lesson10.persist.Product;

import java.util.Objects;

@Getter
@Setter
public class LineItem {

    private Product product;

    private UserDTO user;

    private Integer qty;

    public LineItem() {
    }

    public LineItem(long productId, long userId) {
        this.product = new Product();
        this.product.setId(productId);
        this.user = new UserDTO();
        this.user.setId(userId);
    }

    public LineItem(Product product, UserDTO user, Integer qty) {
        this.product = product;
        this.user = user;
        this.qty = qty;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LineItem lineItem = (LineItem) o;
        return product.getId().equals(lineItem.product.getId()) && user.getId().equals(lineItem.user.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(product.getId(), user.getId());
    }
}
