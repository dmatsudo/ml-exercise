drop table if exists human_info;
drop table if exists human_info_stats;

create table human_info(
	dna varchar(1000)
);

create table human_info_stats(
	count_mutant_dna bigint unsigned,
	count_human_dna bigint unsigned
);

insert into human_info_stats values (0,0);
