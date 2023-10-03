package com.example.kakao.product;

import java.util.List;
import java.util.stream.Collectors;

import com.example.kakao.product.option.Option;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

public class ProductResponse {

    // (기능1) 상품 목록보기
    @ToString
    @Getter
    @Setter
    public static class FindAllDTO {
        private int productId;
        private int productDelivery;
        private String productimage;
        private String productName;
        private int productPrice;

        public FindAllDTO(Product product, int productDelivery) {
            this.productId = product.getId();
            this.productDelivery = productDelivery;
            this.productimage = product.getImage();
            this.productName = product.getProductName();
            this.productPrice = product.getPrice();
        }

    }

    // (기능2) 상품 상세보기
    @ToString
    @Getter
    @Setter
    public static class FindByIdDTO {
        private int productId;
        private String productName;
        private String productImage;
        private int productPrice;
        private int productStarCount;
        private List<OptionDTO> options;

        public FindByIdDTO(List<Option> options, int productStarCount) {
            this.productId = options.get(0).getProduct().getId();
            this.productName = options.get(0).getProduct().getProductName();
            this.productImage = options.get(0).getProduct().getImage();
            this.productPrice = options.get(0).getProduct().getPrice();
            this.productStarCount = productStarCount;
            this.options = options.stream().map(option -> new OptionDTO(option)).collect(Collectors.toList());
        }

        @ToString
        @Getter
        @Setter
        class OptionDTO {
            private int optionId;
            private String optionName;
            private int optionPrice;

            public OptionDTO(Option option) {
                this.optionId = option.getId();
                this.optionName = option.getOptionName();
                this.optionPrice = option.getPrice();
            }

        }

    }
}