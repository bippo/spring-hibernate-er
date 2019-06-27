package bippotraining.dao;

import bippotraining.model.SalesOrder;

import java.util.List;

public interface SalesOrderDAO {
    SalesOrder getById(Integer salesOrderId);
    void addSalesOrder(SalesOrder salesOrder);
    void removeSalesOrder(SalesOrder salesOrder);
    void updateSalesOrder(SalesOrder salesOrder);
    List<SalesOrder> getSalesOrders();
}
