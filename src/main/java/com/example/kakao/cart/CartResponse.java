package com.example.kakao.cart;

import java.util.List;
import java.util.stream.Collectors;

import com.example.kakao.product.Product;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

public class CartResponse {

    // (기능3) 장바구니 조회
    @ToString
    @Getter
    @Setter
    public static class FindAllByUserDTO {
        private List<ProductDTO> products;
        private int totalPrice;

        public FindAllByUserDTO(List<Cart> carts) {
            this.products = carts.stream()
                    .map(cart -> cart.getOption().getProduct()).distinct()
                    // cart의 중복되는 product를 제거
                    .map(product -> new ProductDTO(product, carts))
                    // 중복이 없는 product 객체를 ProductDTO에 사용, carts는 그대로 전달
                    .collect(Collectors.toList());
            this.totalPrice = carts.stream().mapToInt(cart -> cart.getPrice()).sum();
        }

        /*
         * map을 사용하면, List의 객체를 마음대로 가공해서 다시 list화 시킬 수 있다.
         */

        @ToString
        @Getter
        @Setter
        class ProductDTO {
            private int productId;
            private String productName;
            private List<OptionDTO> options;

            public ProductDTO(Product product, List<Cart> carts) {
                this.productId = product.getId();
                this.productName = product.getProductName();
                this.options = carts.stream()
                        .filter(cart -> cart.getOption().getProduct().getId() == productId)
                        .map(cart -> new OptionDTO(cart))
                        .collect(Collectors.toList());
            }

            @ToString
            @Getter
            @Setter
            class OptionDTO {
                private int optionId;
                private String optionName;
                private int cartQuantity;
                private int cartPrice;

                public OptionDTO(Cart cart) {
                    this.optionId = cart.getOption().getId();
                    this.optionName = cart.getOption().getOptionName();
                    this.cartQuantity = cart.getQuantity();
                    this.cartPrice = cart.getPrice();
                }
            }

        }
    }
}
