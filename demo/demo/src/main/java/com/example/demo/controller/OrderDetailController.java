package com.example.demo.controller;

import com.example.demo.entity.OrderDetail;
import com.example.demo.service.OrderDetailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/orderDetails")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:3000"})
public class OrderDetailController {

    private final OrderDetailService orderDetailService;

    public OrderDetailController(OrderDetailService orderDetailService) {
        this.orderDetailService = orderDetailService;
    }


    @GetMapping("/all")
    public Iterable<OrderDetail> getAllOrderDetails() {
        return orderDetailService.findAll();
    }


    @GetMapping("/getById/{id}")
    public ResponseEntity<OrderDetail> getOrderDetailById(@PathVariable Integer id) {
        Optional<OrderDetail> orderDetail = orderDetailService.findById(id);
        return orderDetail.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(404).body(null));
    }


    @PostMapping("/create")
    public ResponseEntity<String> createOrderDetail(@RequestBody OrderDetail orderDetail) {
        try {
            orderDetailService.save(orderDetail);
            return ResponseEntity.ok("Chi tiết đơn hàng đã được tạo thành công với ID: " + orderDetail.getId());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Lỗi khi tạo chi tiết đơn hàng: " + e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateOrderDetail(@PathVariable Integer id, @RequestBody OrderDetail orderDetail) {
        if (orderDetailService.findById(id).isPresent()) {
            orderDetail.setId(id);
            orderDetailService.save(orderDetail);
            return ResponseEntity.ok("Chi tiết đơn hàng với ID " + id + " đã được cập nhật thành công.");
        } else {
            return ResponseEntity.status(404).body("Không tìm thấy chi tiết đơn hàng với ID " + id + ".");
        }
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteOrderDetail(@PathVariable Integer id) {
        if (orderDetailService.findById(id).isPresent()) {
            orderDetailService.deleteById(id);
            return ResponseEntity.ok("Chi tiết đơn hàng với ID " + id + " đã được xóa thành công.");
        } else {
            return ResponseEntity.status(404).body("Không tìm thấy chi tiết đơn hàng với ID " + id + ".");
        }
    }
}
