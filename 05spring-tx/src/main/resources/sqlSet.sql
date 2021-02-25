create databases spring_tx;

use spring_tx;

create table `bookNum`(`id` int(11) auto_increment, `num` int(11), primary key(`id`) );

create table `bookAmount`(`id` int(11) auto_increment, `amount` decimal(20, 2), primary key(`id`) );