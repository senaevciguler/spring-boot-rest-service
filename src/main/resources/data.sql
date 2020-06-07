insert into product (id, name, description, price) values (12001, 'phone', 'electronic', 1000.0);
insert into product (id, name, description, price) values (12002, 'notebook', 'electronic', 2000.0);
insert into customer (id, name, lastname, mail, gsm, address) values (13001, 'sena', 'guler', 'senaevciguler@gmail.com', 0111111111, 'Tallinn/Estonia');
insert into orders (id, quantity, subtotal, note, product_id, customer_id) values (14001, 5, 5000.0, 'test', 12001, 13001);
insert into orders (id, quantity, subtotal, note, product_id, customer_id) values (14002, 2, 4000.0, 'test', 12002, 13001);