package com.example.demo.controller;

import com.example.demo.dto.OrderDto;
import com.example.demo.dto.OrderDetailDto;
import com.example.demo.entity.Order;
import com.example.demo.entity.OrderDetail;
import com.example.demo.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/orders")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:3000"})
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }


    @GetMapping("/all")
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = (List<Order>) orderService.findAll();
        return ResponseEntity.ok(orders);
    }


    @PostMapping("/create")
    public ResponseEntity<String> createOrder(@RequestBody OrderDto orderDto) {
        try {

            if (orderDto.getCustomerName() == null || orderDto.getCustomerName().isEmpty()) {
                return ResponseEntity.badRequest().body("Tên khách hàng là bắt buộc.");
            }
            if (orderDto.getCustomerPhone() == null || orderDto.getCustomerPhone().isEmpty()) {
                return ResponseEntity.badRequest().body("Số điện thoại khách hàng là bắt buộc.");
            }
            if (orderDto.getCustomerAddress() == null || orderDto.getCustomerAddress().isEmpty()) {
                return ResponseEntity.badRequest().body("Địa chỉ khách hàng là bắt buộc.");
            }
            if (orderDto.getOrderTotal() == null || orderDto.getOrderTotal().compareTo(BigDecimal.ZERO) <= 0) {
                return ResponseEntity.badRequest().body("Tổng đơn hàng phải lớn hơn 0.");
            }

            // Chuyển đổi OrderDto thành Order
            Order order = new Order();
            order.setCustomerName(orderDto.getCustomerName());
            order.setCustomerPhone(orderDto.getCustomerPhone());
            order.setCustomerAddress(orderDto.getCustomerAddress());
            order.setOrderTotal(orderDto.getOrderTotal());
            order.setOrderDate(LocalDateTime.now());

            // Lưu đơn hàng trước
            Order savedOrder = orderService.save(order);

            // Lưu chi tiết đơn hàng
            List<OrderDetail> orderDetails = new ArrayList<>();
            for (OrderDetailDto detailDto : orderDto.getOrderDetails()) {
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setOrderID(savedOrder.getOrderID()); // Liên kết đơn hàng với chi tiết đơn hàng
                orderDetail.setProductID(detailDto.getProductID());
                orderDetail.setQuantity(detailDto.getQuantity());
                orderDetail.setPrice(detailDto.getPrice());
                orderDetails.add(orderDetail);
            }


            savedOrder.setOrderDetails(orderDetails); // Thiết lập chi tiết đơn hàng cho đơn hàng
            orderService.save(savedOrder); // Lưu lại để duy trì chi tiết

            return ResponseEntity.ok("Đơn hàng đã được tạo thành công với ID: " + savedOrder.getOrderID());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Lỗi khi tạo đơn hàng: " + e.getMessage());
        }
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateOrder(@PathVariable Integer id, @RequestBody OrderDto orderDto) {
        if (orderService.findById(id).isPresent()) {
            // Chuyển đổi OrderDto thành Order để cập nhật
            Order order = new Order();
            order.setOrderID(id);
            order.setCustomerName(orderDto.getCustomerName());
            order.setCustomerPhone(orderDto.getCustomerPhone());
            order.setCustomerAddress(orderDto.getCustomerAddress());
            order.setOrderTotal(orderDto.getOrderTotal());
            order.setOrderDate(LocalDateTime.now()); // Cập nhật ngày đơn hàng nếu cần

            orderService.save(order);
            return ResponseEntity.ok("Đơn hàng với ID " + id + " đã được cập nhật thành công.");
        } else {
            return ResponseEntity.status(404).body("Không tìm thấy đơn hàng với ID " + id + ".");
        }
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable Integer id) {
        if (orderService.findById(id).isPresent()) {
            orderService.deleteById(id);
            return ResponseEntity.ok("Đơn hàng với ID " + id + " đã được xóa thành công.");
        } else {
            return ResponseEntity.status(404).body("Không tìm thấy đơn hàng với ID " + id + ".");
        }
    }
}
