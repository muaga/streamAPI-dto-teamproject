package com.example.kakao.order;

import java.util.List;
import java.util.stream.Collectors;

import com.example.kakao.cart.Cart;
import com.example.kakao.order.item.Item;
import com.example.kakao.product.Product;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

public class OrderResponse {

    // (기능4) 주문상품 정보조회 (유저별)
    @ToString
    @Getter
    @Setter
    public static class FindAllByUserDTO {

    }

    // (기능5) 주문결과 확인

    // 주문목록 DTO
    @ToString
    @Getter
    @Setter
    public static class FindByIdDTO { // orderId + totalPrice

        private int orderId;
        private List<ItemDTO> items;
        private int totalPrice;

        public FindByIdDTO(Order order, List<Product> products, List<Item> items, int totalPrice) {
            this.orderId = order.getId();
            this.items = products.stream()
                    .distinct()
                    .map(p -> new ItemDTO(p, items))
                    .collect(Collectors.toList());
            this.totalPrice = totalPrice;
        }

        // 아이템 DTO
        @ToString
        @Getter
        @Setter
        public class ItemDTO { // items
            private int productId;
            private String productName;
            private List<OptionDTO> options;

            public ItemDTO(Product product, List<Item> items) {
                this.productId = product.getId();
                this.productName = product.getProductName();
                this.options = items.stream()
                        .filter(a -> a.getOption().getProduct().getId() == productId) // option의 productId = productId
                                                                                      // -> true
                        .map(i -> new OptionDTO(i))
                        .collect(Collectors.toList());
            }

            // 옵션 DTO
            @ToString
            @Getter
            @Setter
            public class OptionDTO {
                private int optionId;
                private String optionName;
                private int quantity;
                private int price;

                public OptionDTO(Item item) {
                    this.optionId = item.getOption().getId();
                    this.optionName = item.getOption().getOptionName();
                    this.quantity = item.getQuantity();
                    this.price = item.getPrice();
                }
            }
        }

    }
}
