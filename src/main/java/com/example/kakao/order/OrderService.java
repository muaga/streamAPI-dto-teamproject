package com.example.kakao.order;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.kakao._core.errors.exception.Exception404;
import com.example.kakao._core.errors.exception.Exception500;
import com.example.kakao.cart.Cart;
import com.example.kakao.cart.CartJPARepository;
import com.example.kakao.order.OrderResponse.FindAllByUserDTO;
import com.example.kakao.order.OrderResponse.FindByIdDTO;
import com.example.kakao.order.item.Item;
import com.example.kakao.order.item.ItemJPARepository;
import com.example.kakao.product.Product;
import com.example.kakao.user.User;

import lombok.RequiredArgsConstructor;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class OrderService {
    private final ItemJPARepository itemJPARepository;
    private final OrderJPARepository orderJPARepository;
    private final CartJPARepository cartJPARepository;

    // (기능4) 주문상품 정보조회 (유저별) - CartJPARepository의 정보를 조회해야함
    public OrderResponse.FindAllByUserDTO findAllByUser(int sessionId) {
        // 1. 유저 장바구니 조회
        List<Cart> carts = cartJPARepository.findAllByUserId(sessionId);
        if (carts.size() == 0) {
            throw new Exception404("장바구니에 아무 내역도 존재하지 않습니다.");
        }
        OrderResponse.FindAllByUserDTO responseDTO = new FindAllByUserDTO(carts);
        return responseDTO;
    }

    // (기능5) 주문결과 확인
    public OrderResponse.FindByIdDTO findById(int orderId, int sessionId) {

        // 1. 유저 주문 목록 조회
        Order orderPS = orderJPARepository.findByOrderIdAndUserId(orderId, sessionId)
                .orElseThrow(() -> new Exception404("해당 주문을 찾을 수 없습니다 : " + orderId));

        // 2. 유저 주문 아이템 조회
        List<Item> items = itemJPARepository.findAllById(orderId);
        if (items == null) {
            throw new Exception404("해당 주문 아이템을 찾을 수 없습니다 : " + orderId);
        }

        // 3. product List 만들기
        List<Product> products = new ArrayList<>();
        for (Item item : items) {
            Product product = item.getOption().getProduct();
            products.add(product);
        }

        // 4. 주문 총 금액
        int totalPrice = 0;
        for (Item item : items) {
            totalPrice += item.getPrice();
        }

        // 5. 주문 -> 결과 DTO
        OrderResponse.FindByIdDTO responseDTO = new FindByIdDTO(orderPS, products, items, totalPrice);

        return responseDTO;
    }

    @Transactional
    public void saveOrder(User sessionUser) {
        // 1. 유저 장바구니 조회
        List<Cart> cartListPS = cartJPARepository.findAllByUserId(sessionUser.getId());
        if (cartListPS.size() == 0) {
            throw new Exception404("장바구니에 아무 내역도 존재하지 않습니다");
        }

        // 2. 주문 생성
        Order orderPS = orderJPARepository.save(Order.builder().user(sessionUser).build());

        // 3. 주문 아이템 저장
        List<Item> itemList = new ArrayList<>();
        for (Cart cartPS : cartListPS) {
            Item item = Item.builder()
                    .option(cartPS.getOption())
                    .order(orderPS)
                    .quantity(cartPS.getQuantity())
                    .price(cartPS.getOption().getPrice() * cartPS.getQuantity())
                    .build();
            itemList.add(item);
        }
        try {
            itemJPARepository.saveAll(itemList);
        } catch (Exception e) {
            throw new Exception500("결재 실패 : " + e.getMessage());
        }

        // 4. 유저 장바구니 초기화 (결재가 끝나면 장바구니가 초기화 됨)
        try {
            cartJPARepository.deleteByUserId(sessionUser.getId());
        } catch (Exception e) {
            throw new Exception500("장바구니 초기화 실패 : " + e.getMessage());
        }
    }

}
