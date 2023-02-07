package com.food.ordering.system.order.domain.entity;

import com.food.ordering.system.entity.BaseEntity;
import com.food.ordering.system.valueobject.Money;
import com.food.ordering.system.valueobject.ProductId;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true )
public class Product extends BaseEntity<ProductId> {
    private String name;
    private Money price;
    public Product(ProductId productId, String name, Money price) {
        super.setId(productId);
        this.name = name;
        this.price = price;
    }
    public Product(ProductId productId) {
        super.setId(productId);

    }



    public void updateWithConfirmNameAndPrice(String name, Money price) {
        this.name = name;
        this.price = price;
    }
}
