## Hibernate ER

Tutorial untuk mapping beberapa tabel di PostgreSQL yang saling terhubung (melalui foreign-key) ke dalam entity di Hibernate.

### Struktur Data
```sql
CREATE TABLE sales_order
(
    sales_order_id serial,
    customer_name character varying(128) NOT NULL,
    order_date date NOT NULL,
    total_order numeric(15, 2) NOT NULL,
    PRIMARY KEY (sales_order_id)
);

CREATE TABLE product
(
    product_code character varying(12),
    product_name character varying(128) NOT NULL,
    unit_of_measure character varying(16) NOT NULL,
    unit_price numeric(12, 2) NOT NULL,
    PRIMARY KEY (product_code)
);

CREATE TABLE sales_order_item
(
    sales_order_item_id serial,
    sales_order_id integer NOT NULL,
    product_code character varying(12) NOT NULL,
    order_qty real NOT NULL,
    unit_price numeric(12, 2) NOT NULL,
    PRIMARY KEY (sales_order_item_id),
    CONSTRAINT sales_order_id_fkey FOREIGN KEY (sales_order_id)
        REFERENCES sales_order (sales_order_id,
    CONSTRAINT produc_code_fkey FOREIGN KEY (product_code)
        REFERENCES product (product_code)
);
```

## Entity Class
### model.SalesOrder
Entity ini berasosiasi dengan tabel **`sales_order`**
Hal yang perlu diperhatikan pada class khususnya pada anotasi ***@GeneratedValue, unique, nullable***
```java
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
@Column(name = "sales_order_item_id", unique = true, nullable = false)
public Integer getSalesOrderItemId() {
    return salesOrderItemId;
}
```
* **GeneratedValue**: field tersebut dibuat otomatis oleh PostgreSQL
* **unique=true**: field dalam id tersebut unik (tidak boleh ada sales_order_id bernilai sama di tabel sales_order)
* **nullable = false**: field tersebut tidak boleh kosong
# Hibernate ER

Tutorial untuk mapping beberapa tabel di PostgreSQL yang saling terhubung (melalui foreign-key) ke dalam entity di Hibernate. Tutorial ini merupakan lanjutan dari tutorial [Spring Rest](https://github.com/bippo/spring-rest).

Struktur direktori dan file pada akhir tutorial kita sebagai berikut (file yang khusus dibuat pada tutorial ini diberi tanda *):
```
├── pom.xml
├── readme.md
└── src
    └── main
        ├── java
        │   └── bippotraining
        │       ├── Application.java
        │       ├── HibernateXMLConf.java
        │       ├── controller
        │       │   ├── EmployeeController.java
        │       │   └── SalesOrderController.java *
        │       ├── dao
        │       │   ├── EmployeeDAO.java
        │       │   ├── EmployeeDAOImpl.java
        │       │   ├── SalesOrderDAO.java *
        │       │   └── SalesOrderDAOImpl.java *
        │       ├── model
        │       │   ├── Employee.java
        │       │   ├── Product.java *
        │       │   ├── SalesOrder.java *
        │       │   └── SalesOrderItem.java *
        │       └── service
        │           ├── EmployeeService.java
        │           ├── EmployeeServiceImpl.java
        │           ├── SalesOrderService.java *
        │           └── SalesOrderServiceImpl.java *
        └── resources
            ├── application.properties
            └── hibernate5Configuration.xml
```
## 1. Skema Database dan Contoh Data
Pada tutorial kali ini kita akan menambahkan 3 tabel tambahan dengan data produk yang sudah diisi dengan skema sebagai berikut:
```sql
CREATE TABLE sales_order
(
    sales_order_id serial,
    customer_name character varying(128) NOT NULL,
    order_date date NOT NULL,
    total_order numeric(15, 2) NOT NULL,
    PRIMARY KEY (sales_order_id)
);

CREATE TABLE product
(
    product_code character varying(12),
    product_name character varying(128) NOT NULL,
    unit_of_measure character varying(16) NOT NULL,
    unit_price numeric(12, 2) NOT NULL,
    PRIMARY KEY (product_code)
);

CREATE TABLE sales_order_item
(
    sales_order_item_id serial,
    sales_order_id integer NOT NULL,
    product_code character varying(12) NOT NULL,
    order_qty real NOT NULL,
    unit_price numeric(12, 2) NOT NULL,
    PRIMARY KEY (sales_order_item_id),
    CONSTRAINT sales_order_id_fkey FOREIGN KEY (sales_order_id)
        REFERENCES sales_order (sales_order_id),
    CONSTRAINT produc_code_fkey FOREIGN KEY (product_code)
        REFERENCES product (product_code)
);

INSERT  INTO product (product_code, product_name, unit_of_measure, unit_price) VALUES ('49384434', 'Oreo', 'pcs', 5000.00);
INSERT  INTO product (product_code, product_name, unit_of_measure, unit_price) VALUES ('59459454', 'Susu Segar', 'liter', 25000.00);
INSERT  INTO product (product_code, product_name, unit_of_measure, unit_price) VALUES ('85946856', 'Roti Tawar', 'pcs', 15000.00);
INSERT  INTO product (product_code, product_name, unit_of_measure, unit_price) VALUES ('76998459', 'Kapas', 'pcs', 12000.00);
INSERT  INTO product (product_code, product_name, unit_of_measure, unit_price) VALUES ('95948986', 'Pasta Gigi', 'pcs', 9000.00);
INSERT  INTO product (product_code, product_name, unit_of_measure, unit_price) VALUES ('12345', 'Oroe', 'pcs', 6000.00);
INSERT  INTO product (product_code, product_name, unit_of_measure, unit_price) VALUES ('12346', 'Susu Segar', 'Liter', 18000.00);
INSERT  INTO product (product_code, product_name, unit_of_measure, unit_price) VALUES ('12347', 'Roti Tawar', 'pcs', 15000.00);
```

## 2. Entity Class
### model.SalesOrder
Entity ini berasosiasi dengan tabel **`sales_order`**
Hal yang perlu diperhatikan pada class khususnya pada anotasi ***@GeneratedValue, unique, nullable***
```java
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
@Column(name = "sales_order_item_id", unique = true, nullable = false)
public Integer getSalesOrderItemId() {
    return salesOrderItemId;
}
```
* **GeneratedValue**: field tersebut dibuat otomatis oleh PostgreSQL
* **unique=true**: field dalam id tersebut unik (tidak boleh ada sales_order_id bernilai sama di tabel sales_order)
* **nullable = false**: field tersebut tidak boleh kosong

### model.Product
Entity ini berasosiasi dengan tabel **`product`**

### model.SalesOrderItem
Entity ini berasosiasi dengan tabel **`sales_order_item`**, hal yang perlu diperhatikan pada Class ini:
#### Relasi 2 arah antara `SalesOrder` dan `SalesOrderItem`
Perhatikan pada class `SalesOrder`:
```java
@OneToMany(mappedBy = "salesOrder", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
public List<SalesOrderItem> getSalesOrderItems() {
    return salesOrderItems;
}
```
dan juga pada class `SalesOrderItem`:
```java
@ManyToOne
@JoinColumn(name = "sales_order_id")
@JsonIgnore
public SalesOrder getSalesOrder() {
    return salesOrder;
}
```

Class `SalesOrder` mempunyai property `salesOrderItems` yang bertipe `List<SalesOrderItem>` (karena satu row di `sales_order` bisa mempunyai banyak `sales_order_item`), sedangkan pada Class `SalesOrderItem` mempunyai property `salesOrder` bertipe `SalesOrder` (karena satu row di `sales_order_item` hanya bisa bisa mempunyai satu `sales_order`). Perhatikan anotasi `@JoinColumn` pada field di kedua class tersebut yang mendefinisikan hubungan relasi kedua Class tersebut.

Pada anotasi `@OneToMany` terdapat property:
1. `orphanRemoval = true`, jika salesOrderItem diremove di object salesOrder, maka juga akan ikut di-remove di database.
2. `cascade = CascadeType.ALL`, jika salesOrder disimpan/update/hapus, maka object salesOrderItem juga akan disimpan/update/hapus.
3. `fetch = FetchType.EAGER`, menandakan jika kita mengambil data `sales_order`, maka secara hibernate juga akan ikut mengambil data `sales_order_item`.

Anotasi `@JsonIgnore` pada `SalesOrderItem.getSalesOrder()` menandakan bahwa pada saat mengubah object tersebut ke JSON, maka property salesOrder tidak akan disertakan pada dokumen JSON.

## 3. DAO dan Service
Untuk DAO dan Service, kita hanya akan membuat DAO dan Service untuk Entity SalesOrder

## 4. Controller
### 4.1. Menyimpan Data (POST)
```java
    @PostMapping(path = "/", consumes = "application/json", produces = "application/json")
    public SalesOrder addSalesOrder(@RequestBody SalesOrder salesOrder) {
        BigDecimal totalOrder = BigDecimal.ZERO;
        for (SalesOrderItem item : salesOrder.getSalesOrderItems()) {
            item.setSalesOrder(salesOrder);
            totalOrder = totalOrder.add(item.getUnitPrice().multiply(new BigDecimal(item.getOrderQty())));
        }
        salesOrder.setTotalOrder(totalOrder);
        salesOrderService.addSalesOrder(salesOrder);
    }
```
Perhatikan pada kode di atas, kita melakukan kalkulasi total order dan menyimpannya di Entity `SalesOrder`. Pada Entity `SalesOrderItem`, kita perlu set property `salesOrder` secara manual, karena pada saat konversi dari JSON ke object `SalesOrder`, property `salesOrder` di entity `SalesOrderItem` masih bernilai null. Jika kita biarkan null, maka hibernate akan melakukan insert ke tabel `sales_order_item` dengan nilai `sales_order_item.sales_order_id` bernilai null, hal ini akan menyebabkan error karena di definisi tabel kita `sales_order_item.sales_order_id` tidak boleh null.

### 4.2. Mengambil Data (GET)
 Kita akan mencoba mengambil data dari SalesOrder berdasarkan id, method yang kita gunakan yaitu:
 ```java
 @GetMapping(path = "/{id}", consumes = "application/json", produces = "application/json")
public SalesOrder getById(@PathVariable("id") Integer id) {
    return salesOrderService.getById(id);
}
 ```

## 5. Pengujian
### 5.1. Menyimpan data
 ```
$curl -v POST -H "Content-Type: application/json" -d '{
        "customerName": "mahendra",
        "orderDate": "2019-06-20",
        "totalOrder": 15000,
        "salesOrderItems": [
            {
                "orderQty": 1,
                "unitPrice": 15000,
                "product": {
                    "productCode": "85946856"
                }
            },
            {
                "orderQty": 2,
                "unitPrice": "5000.00",
                "product": {
                    "productCode": "49384434"
                }
            }
        ]
    }' localhost:8080/salesorder/
 ```

### 5.2. Mengambil data
Berikut adalah script jika kita ingin mengambil data dengan kriteria `sales_order_id=7`
 ```
 $ curl -v -H "Content-Type: application/json" localhost:8080/salesorder/7
*   Trying ::1...
* TCP_NODELAY set
* Connected to localhost (::1) port 8080 (#0)
> GET /salesorder/7 HTTP/1.1
> Host: localhost:8080
> User-Agent: curl/7.54.0
> Accept: */*
> Content-Type: application/json
>
< HTTP/1.1 200
< Content-Type: application/json;charset=UTF-8
< Transfer-Encoding: chunked
< Date: Thu, 27 Jun 2019 04:51:16 GMT
<
* Connection #0 to host localhost left intact
{"salesOrderId":7,"customerName":"mahendra","orderDate":"2019-06-19T17:00:00.000+0000","totalOrder":25000.00,"salesOrderItems":[{"salesOrderItemId":8,"orderQty":1.0,"unitPrice":15000.00,"product":{"productCode":"85946856","productName":"Roti Tawar","unitOfMeasure":"pcs","unitPrice":15000.00}},{"salesOrderItemId":9,"orderQty":2.0,"unitPrice":5000.00,"product":{"productCode":"49384434","productName":"Oreo","unitOfMeasure":"pcs","unitPrice":5000.00}}]}
 ```






### model.Product
Entity ini berasosiasi dengan tabel **`product`**

### model.SalesOrderItem
Entity ini berasosiasi dengan tabel **`sales_order_item`**, hal yang perlu diperhatikan pada Class ini:
#### Relasi 2 arah antara `SalesOrder` dan `SalesOrderItem`
Perhatikan pada class `SalesOrder`:
```java
@OneToMany(mappedBy = "salesOrder", orphanRemoval = true, cascade = CascadeType.ALL)
public List<SalesOrderItem> getSalesOrderItems() {
    return salesOrderItems;
}
```
dan juga pada class `SalesOrderItem`:
```java
@ManyToOne
@JoinColumn(name = "sales_order_id")
public SalesOrder getSalesOrder() {
    return salesOrder;
}
```

Class `SalesOrder` mempunyai property `salesOrderItems` yang bertipe `List<SalesOrderItem>` (karena satu row di `sales_order` bisa mempunyai banyak `sales_order_item`), sedangkan pada Class `SalesOrderItem` mempunyai property `salesOrder` bertipe `SalesOrder` (karena satu row di `sales_order_item` hanya bisa bisa mempunyai satu `sales_order`). Perhatikan anotasi `@JoinColumn` pada field di kedua class tersebut yang mendefinisikan hubungan relasi kedua Class tersebut.