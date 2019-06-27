package bippotraining.dao;

import bippotraining.model.SalesOrder;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SalesOrderDAOImpl implements SalesOrderDAO {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public SalesOrder getById(Integer salesOrderId) {
        return sessionFactory.getCurrentSession().get(SalesOrder.class, salesOrderId);
    }

    @Override
    public void addSalesOrder(SalesOrder salesOrder) {
        sessionFactory.getCurrentSession().save(salesOrder);
    }

    @Override
    public void removeSalesOrder(SalesOrder salesOrder) {
        sessionFactory.getCurrentSession().remove(salesOrder);
    }

    @Override
    public void updateSalesOrder(SalesOrder salesOrder) {
        sessionFactory.getCurrentSession().update(salesOrder);
    }

    @Override
    public List<SalesOrder> getSalesOrders() {
        return sessionFactory.getCurrentSession().createQuery("from SalesOrder", SalesOrder.class).list();
    }
}
