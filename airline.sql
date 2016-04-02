drop table if exists baggages, reserves;
drop table if exists flights, passengers;
drop table if exists airplanes, airports, states_provinces_countries;
drop table if exists airplane_capacities;

create table states_provinces_countries 
(province_state char(2),
	country varchar(20),
	primary key (province_state));
    
create table airports 
(airport_id char(3),
	city varchar(20),
	province_state char(2),
	primary key (airport_id),
	foreign key (province_state) references states_provinces_countries(province_state));
    
create table passengers
(passport_no char(7),
	passenger_id int,
	first_name varchar(100),
	last_name varchar(100),
	password varchar(30),
	primary key (passport_no));
    
create table baggages 
(baggage_id int,
 	weight int,
  	type char(20),
  	passport_no char(7),
  	primary key (baggage_id),
  	foreign key (passport_no) references passengers(passport_no) ON DELETE CASCADE);
    
create table airplane_capacities
(manufacturer varchar(20),
  	model varchar(20),
  	capacity int,
  	primary key (manufacturer, model));
    
create table airplanes
(airplane_id varchar(20),
  	manufacturer varchar(20),
  	model varchar(20),
  	primary key (airplane_id),
  	foreign key (manufacturer, model) references airplane_capacities(manufacturer, model));
    
create table flights
(flight_no char(7),
	available_seats int,
	cost float,
	airplane_id varchar(20),
	airportid_depart char(3),
	airportid_arrive char(3),
	date_depart date,
	time_depart time,
	date_arrive date,
	time_arrive time,
	primary key (flight_no),
	foreign key (airplane_id) references airplanes(airplane_id)
		ON DELETE NO ACTION
		ON UPDATE CASCADE,
	foreign key (airportid_depart) references airports(airport_id)
		ON UPDATE CASCADE,
	foreign key (airportid_arrive) references airports(airport_id)
		ON DELETE CASCADE
		ON UPDATE CASCADE);    
    
create table reserves
(passport_no char(7),
	flight_no char(7),
	seat_no int,
	confirmation_no int,
	primary key (passport_no, flight_no),
	foreign key (passport_no) references passengers(passport_no)
		ON DELETE CASCADE
		ON UPDATE CASCADE,
	foreign key (flight_no) references flights(flight_no)
		ON DELETE NO ACTION
		ON UPDATE CASCADE);
                
insert into states_provinces_countries
values ('BC', 'Canada');

insert into states_provinces_countries
values ('AB', 'Canada');

insert into states_provinces_countries
values ('ON', 'Canada');

insert into airports
values('YVR', 'Vancouver', 'BC');

insert into airports
values('YEG', 'Edmonton', 'AB');

insert into airports
values('YYC', 'Calgary', 'AB');

insert into airports
values('YYT', 'Toronto', 'ON');

insert into airports
values('YUL', 'Montreal', 'ON');

insert into passengers
values('M142335','12345', 'Ben','Gee', 'bengee');

insert into passengers
values('M100502','16786','Qingzhou','Song', 'qingzhousong');

insert into passengers
values('N300531','10000','Yoony','Ok', 'yoonyok');

insert into passengers
values('P023945','19244','Daniel','Chen', 'danielchen');

insert into passengers
values('N239942','28442','Stella','Fang', 'stellafang');

insert into baggages
values('14','50','Checked', 'M142335');

insert into baggages
values('1','20','Carry-on','M142335');

insert into baggages
values('100','30','Checked','N300531');

insert into baggages
values('90','40','Checked','P023945');

insert into baggages
values('98','20','Carry-on','N239942');

insert into airplane_capacities 
values('Boeing', 'a100', 200);

insert into airplane_capacities 
values('Airbus', 'c5d100', 150);

insert into airplane_capacities 
values('Boeing', 'a200', 100);

insert into airplanes 
values('AZ120', 'Boeing', 'a100');

insert into airplanes 
values('BY120', 'Boeing', 'a100');

insert into airplanes 
values('CX320', 'Airbus', 'c5d100');

insert into airplanes 
values('DW420', 'Airbus', 'c5d100');

insert into airplanes 
values('EV520', 'Boeing', 'a200');

insert into flights
values ('100ABC', 200, 1000.00, 'AZ120', 'YVR', 'YYC', 
	'2015-02-12', '11:00:00', '2015-02-12', '12:30:00');
    
insert into flights
values ('200DEF', 200, 1000.00, 'BY120', 'YEG', 'YUL', 
	'2015-02-20', '14:00:00', '2015-02-20', '17:30:00');

insert into flights
values ('300GHI', 150, 800.00, 'CX320', 'YEG', 'YYT', 
	'2015-02-24', '10:00:00', '2015-02-24', '14:20:00');
    
insert into flights
values ('400JKL', 150, 500.00, 'DW420', 'YYC', 'YVR', 
	'2015-02-24', '09:00:00', '2015-02-24', '10:20:00');
		 	 	 					 		
insert into flights
values ('500MNO', 100, 1200.00, 'EV520', 'YUL', 'YEG', 
'2015-03-01', '06:00:00', '2015-03-01', '09:40:00');

insert into flights
values ('500MMM', 100, 1200.00, 'EV520', 'YUL', 'YEG', 
'2015-03-01', '06:00:00', '2015-03-01', '09:40:00');

insert into reserves
values('M142335', '100ABC', 45, 1);

insert into reserves
values('M142335', '200DEF', 45, 6);

insert into reserves
values('M142335', '300GHI', 45, 7);

insert into reserves
values('M142335', '400JKL', 45, 8);

insert into reserves
values('M142335', '500MNO', 45, 9);


insert into reserves
values('M100502', '200DEF', 90, 2);

insert into reserves
values('M100502', '100ABC', 90, 2);

insert into reserves
values('M100502', '300GHI', 90, 2);

insert into reserves
values('M100502', '400JKL', 90, 2);

insert into reserves
values('M100502', '500MNO', 90, 2);


insert into reserves
values('N300531', '300GHI', 3, 3);

insert into reserves
values('N300531', '100ABC', 3, 3);


insert into reserves
values('N239942', '200DEF', 78, 4);

insert into reserves
values('N239942', '100ABC', 78, 4);

insert into reserves
values('P023945', '100ABC', 60, 5);




