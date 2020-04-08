# 302 Group Project DataBase SQL record Note

## Coffee Shop Database (All coffee shop database will have CE_ in front of the table name)

### shop
```
create table ce_shop(
s_id number(3) not null,
s_a varchar(100) not null,
s_azh varchar2(100) not null,
s_f char(1) not null,
CONSTRAINT ce_shop_pk primary key(s_id)
);

insert into ce_shop values (1, 'Tequila Kola, 1/F, Horizon Plaza, 2 Lee Wing Street, Ap Lei Chau',
'新海怡廣場,鴨脷洲利榮街2號新海怡廣場1樓Tequila Kola','1');
insert into ce_shop values (2, 'BRITISH COUNCIL,1/F British Council, 3 Supreme Court Road, Admiralty',
'英國文化協會,金鐘法院道3號英國文化協會1樓','1');
insert into ce_shop values (3, 'LIPPO CENTRE,Shop 2AC, 1/F, Lippo Centre, 89 Queensway, Admiralty',
'力寶中心,金鐘金鐘道89號力寶中心1樓2AC號鋪','1');
insert into ce_shop values (4, '8 WYNDHAM STREET,Portion of Lobby, G/F, 8 Wyndham St, Central',
'雲咸街,中環雲咸街8號地下大堂','1');
```

### Staff
```
create table ce_staff (
s_id number(5) not null,
s_pw varchar2(50) not null,
s_n varchar(20) not null,
s_nzh varchar2(10) not null,
s_lv char(1) not null,
CONSTRAINT ce_staff_pk primary key(s_id)
);

create SEQUENCE staffid_increase
START WITH 1
INCREMENT BY 1;

insert into ce_staff values (staffid_increase.nextval,'qwe','陳大文','jacky','1');
insert into ce_staff values (staffid_increase.nextval,'qwe','陳大文','jacky','2');
insert into ce_staff values (staffid_increase.nextval,'qwe','陳大文','jason','1');
```

### Food Categories
```
create table ce_cat (
c_id varchar(5) not null,
c_n varchar(20) not null,
c_nzh varchar2(20) not null,
CONSTRAINT ce_cat_pk primary key (c_id)
);

insert into ce_cat values ('ce','coffee','咖啡');
insert into ce_cat values ('ie','icy','凍飲');
insert into ce_cat values ('sh','sandwich','三文治');
insert into ce_cat values ('sd','salad','沙律');
```

### Food
```
create table ce_food(
f_id number(3) not null,
f_cat varchar(10) not null,
f_n varchar(50) not null,
f_nzh varchar2(50) not null,
f_p float not null,
f_f char(1) not null,
CONSTRAINT ce_food_pk primary key(f_id)
);

create SEQUENCE fid_increase 
start with 1
increment by 1;

insert into ce_food values (fid_increase.nextval,'ce','Caffe Mocha','朱古力牛奶咖啡',37,'1');
insert into ce_food values (fid_increase.nextval,'ce','Caramel Latte','焦糖牛奶咖啡',37,'1');
insert into ce_food values (fid_increase.nextval,'ce','Hazelnut Latte','榛子牛奶咖啡',37,'1');
insert into ce_food values (fid_increase.nextval,'ie','Iced Caffe Mocha','凍朱古力牛奶咖啡',37,'1');
insert into ce_food values (fid_increase.nextval,'ie','Iced Caramel Latte','凍焦糖牛奶咖啡',37,'1');
insert into ce_food values (fid_increase.nextval,'sh','Butcher Ham and Cheddar Cheese Sandwich','傳統火腿芝士三文治',36,'1');
insert into ce_food values (fid_increase.nextval,'sh','Cheese Supreme Sandwich','特選芝士三文治',36,'1');
insert into ce_food values (fid_increase.nextval,'sd','Caesar Salad with Chicken','凱撒雞肉沙律',45,'1');
insert into ce_food values (fid_increase.nextval,'sd','Honey Mustard Potato Salad with Soft-boiled Egg','蜜糖芥末薯仔沙律配流心蛋',45,'1');

```

### Set
```
create table ce_set (
s_id number(3) not null,
s_n varchar(20) not null,
s_nzh varchar2(20) not null,
s_fl varchar2(100) not null,
s_p float not null,
s_f char(1) not null,
CONSTRAINT ce_set_pk primary key (s_id)
);

create SEQUENCE set_increase
start with 1
increment by 1;

insert into ce_set values (set_increase.nextval,'Nice Morning Set','醒神套餐','3_6',55.5,'1');
insert into ce_set values (set_increase.nextval,'Healthy Lunch Set','健康午餐套餐','1_8',55.5,'1');

create SEQUENCE setCall_increase
start with 1
increment by 1;
```

### Order
```
create table ce_order (
o_id number(5) not null,
o_sid number(3) not null,
o_a char(1) not null,
o_d date not null,
o_t varchar2(30) not null,
o_p float not null,
o_f char(1) not null,
CONSTRAINT ce_order_pk primary key (o_id)
);

create SEQUENCE oid_increase
start with 1
increment by 1;
```

### Order Item
```
create table ce_orderitem (
oi_id number(3) not null,
oi_oid number(3) not null,
oi_sid number(3) not null,
oi_a char(1) not null,
oi_dt varchar2(30) not null,
oi_p float not null,
oi_f char(1) not null,
oi_fid number(3) not null,
oi_sf char(1) not null,
CONSTRAINT ce_orderitem_pk primary key (oi_id)
);

create SEQUENCE oiid_increase
start with 1
increment by 1;
```

### Special Item
```
create table ce_special (
s_id number(3) not null,
s_cat varchar(10) not null,
s_fid number(3) not null,
s_n varchar(20) not null,
s_nzh varchar2(20) not null,
s_p float not null,
s_f char(1) not null,
CONSTRAINT ce_special_pk primary key (s_id) 
);

create SEQUENCE special_increase
start with 1
increment by 1 ;

insert into ce_special values (special_increase.nextval,'ce',0,'Grande','大',5,'1');
insert into ce_special values (special_increase.nextval,'ce',0,'Alto','特大',10,'1');
insert into ce_special values (special_increase.nextval,'ie',0,'小冰','Less Ice',0,'1');
insert into ce_special values (special_increase.nextval,'ie',0,'多冰','Extra Ice',0,'1');
insert into ce_special values (special_increase.nextval,'ie',0,'Grande','大',5,'1');
insert into ce_special values (special_increase.nextval,'ie',0,'Alto','特大',10,'1');
insert into ce_special values (special_increase.nextval,'com',0,'Plastic Bag','膠袋',1,'1');
```

### Special item order
```
create table ce_sitem (
si_id number(3) not null,
si_sid number(3) not null,
si_oiid number(5) not null,
si_p float not null,
CONSTRAINT ce_sitem_pk primary key (si_id)
);

create SEQUENCE sitem_increase 
start with 1
increment by 1;
```





































