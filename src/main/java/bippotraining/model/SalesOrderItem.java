package bippotraining.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "sales_order_item")
public class SalesOrderItem {
    private Integer salesOrderItemId;
    private Float orderQty;
    private BigDecimal unitPrice;

    private SalesOrder salesOrder;
    private Product product;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sales_order_item_id", unique = true, nullable = false)
    public Integer getSalesOrderItemId() {
        return salesOrderItemId;
    }

    public void setSalesOrderItemId(Integer salesOrderItemId) {
        this.salesOrderItemId = salesOrderItemId;
    }

    @Column(name = "order_qty")
    public Float getOrderQty() {
        return orderQty;
    }

    public void setOrderQty(Float orderQty) {
        this.orderQty = orderQty;
    }

    @Column(name = "unit_price")
    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    @ManyToOne
    @JoinColumn(name = "sales_order_id")
    @JsonIgnore
    public SalesOrder getSalesOrder() {
        return salesOrder;
    }

    public void setSalesOrder(SalesOrder salesOrder) {
        this.salesOrder = salesOrder;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_code")
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
