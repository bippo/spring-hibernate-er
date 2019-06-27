package bippotraining.service;

import bippotraining.model.SalesOrder;

import java.util.List;

public interface SalesOrderService {
    void addSalesOrder(SalesOrder salesOrder);
    void removeSalesOrder(SalesOrder salesOrder);
    void updateSalesOrder(SalesOrder salesOrder);
    SalesOrder getById(Integer salesOrderId);
    List<SalesOrder> getSalesOrders();
}
