package bippotraining.controller;

import bippotraining.model.SalesOrder;
import bippotraining.model.SalesOrderItem;
import bippotraining.service.SalesOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/salesorder")
public class SalesOrderController {
    @Autowired
    private SalesOrderService salesOrderService;

    @GetMapping(path = "/", produces = "application/json")
    public List<SalesOrder> getAllSalesOrder() {
        return salesOrderService.getSalesOrders();
    }

    @GetMapping(path = "/{id}", consumes = "application/json", produces = "application/json")
    public SalesOrder getById(@PathVariable("id") Integer id) {
        return salesOrderService.getById(id);
    }

    @PostMapping(path = "/", consumes = "application/json", produces = "application/json")
    public SalesOrder addSalesOrder(@RequestBody SalesOrder salesOrder) {
        BigDecimal totalOrder = BigDecimal.ZERO;
        for (SalesOrderItem item : salesOrder.getSalesOrderItems()) {
            item.setSalesOrder(salesOrder);
            totalOrder = totalOrder.add(item.getUnitPrice().multiply(new BigDecimal(item.getOrderQty())));
        }
        salesOrder.setTotalOrder(totalOrder);
        salesOrderService.addSalesOrder(salesOrder);

        return salesOrder;
    }

    @PutMapping(path = "/", consumes = "application/json", produces = "application/json")
    public void updateSalesOrder(@RequestBody SalesOrder salesOrder) {
        salesOrderService.updateSalesOrder(salesOrder);
    }

    @DeleteMapping(path = "/{id}", consumes = "application/json", produces = "application/json")
    public void deleteSalesOrder(@PathVariable("id") Integer id) {
        SalesOrder salesOrder = salesOrderService.getById(id);
        salesOrderService.removeSalesOrder(salesOrder);
    }

}
