package bippotraining.service;

import bippotraining.dao.SalesOrderDAO;
import bippotraining.model.SalesOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class SalesOrderServiceImpl implements SalesOrderService {

    @Autowired
    private SalesOrderDAO salesOrderDAO;

    @Override
    @Transactional(readOnly = false)
    public void addSalesOrder(SalesOrder salesOrder) {
        salesOrderDAO.addSalesOrder(salesOrder);    
    }

    @Override
    @Transactional(readOnly = false)
    public void removeSalesOrder(SalesOrder salesOrder) {
        salesOrderDAO.removeSalesOrder(salesOrder);
    }

    @Override
    @Transactional(readOnly = false)
    public void updateSalesOrder(SalesOrder salesOrder) {
        salesOrderDAO.updateSalesOrder(salesOrder);
    }

    @Override
    public SalesOrder getById(Integer salesOrderId) {
        return salesOrderDAO.getById(salesOrderId);
    }

    @Override
    public List<SalesOrder> getSalesOrders() {
        return salesOrderDAO.getSalesOrders();
    }
}
