drop table if exists device_info;
drop table if exists report;

create table device_info(
    device_id VARCHAR(50) primary key ,
    nickname VARCHAR(50) unique
);

create table report(
   reporter_did varchar(50),
   chat_id VARCHAR(50),
   content VARCHAR(100),
   primary key (reporter_did,chat_id),
   foreign key (reporter_did) references device_info(device_id) on update cascade
);

CREATE TABLE IF NOT EXISTS Prefixes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    prefix VARCHAR(255) NOT NULL
);