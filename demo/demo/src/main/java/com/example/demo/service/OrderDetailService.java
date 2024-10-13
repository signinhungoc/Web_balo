package com.example.demo.service;

import com.example.demo.entity.OrderDetail;
import com.example.demo.repository.OrderDetailRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrderDetailService {

    private final OrderDetailRepository orderDetailRepository;

    public OrderDetailService(OrderDetailRepository orderDetailRepository) {
        this.orderDetailRepository = orderDetailRepository;
    }

    public Iterable<OrderDetail> findAll() {
        return orderDetailRepository.findAll();
    }

    public Optional<OrderDetail> findById(Integer id) {
        return orderDetailRepository.findById(id);
    }

    public OrderDetail save(OrderDetail orderDetail) {
        return orderDetailRepository.save(orderDetail);
    }

    public void deleteById(Integer id) {
        orderDetailRepository.deleteById(id);
    }
}
