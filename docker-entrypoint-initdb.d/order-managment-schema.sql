CREATE DATABASE IF NOT EXISTS ordermanagementdb;

CREATE TABLE IF NOT EXISTS products
(
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    price DOUBLE NOT NULL
);

CREATE TABLE IF NOT EXISTS orders
(
    id INT AUTO_INCREMENT PRIMARY KEY,
    order_date DATE NOT NULL,
    status VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS order_items
(
    id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL,
    price DOUBLE NOT NULL,
    CONSTRAINT fk_order_item_order FOREIGN KEY(order_id) REFERENCES orders(id) ON DELETE CASCADE,
    CONSTRAINT fk_order_item_product FOREIGN KEY(product_id) REFERENCES products(id) ON DELETE CASCADE
);

-- Inserção de produtos iniciais
INSERT INTO products (name, description, price) VALUES
('Product A', 'Description A', 9.99),
('Product B', 'Description B', 19.99),
('Product C', 'Description C', 29.99);