insert into article(idx, subject, contents, `count`) values (1, 'init subject1', 'init contents1', 0);
insert into article(idx, subject, contents, `count`) values (2, 'init subject2', 'init contents2', 0);
insert into article(idx, subject, contents, `count`) values (3, 'init subject3', 'init contents3', 0);
insert into article(idx, subject, contents, `count`) values (4, 'init subject4', 'init contents4', 0);
insert into article(idx, subject, contents, `count`) values (5, 'init subject5', 'init contents5', 0);
insert into article(idx, subject, contents, `count`) values (6, 'init subject6', 'init contents6', 0);
insert into article(idx, subject, contents, `count`) values (7, 'init subject7', 'init contents7', 0);

insert into tag(idx, article_idx, tag, `count`) values (1, 5, 'name1', 0);
insert into tag(idx, article_idx, tag, `count`) values (2, 5, 'name2', 0);
insert into tag(idx, article_idx, tag, `count`) values (3, 5, 'name3', 0);

insert into tag(idx, article_idx, tag, `count`) values (4, 5, 'name1', 0);
insert into tag(idx, article_idx, tag, `count`) values (5, 5, 'name2', 0);
insert into tag(idx, article_idx, tag, `count`) values (6, 5, 'name3', 0);


insert into tag(idx, article_idx, tag, `count`) values (7, 5, 'name1', 0);
insert into tag(idx, article_idx, tag, `count`) values (8, 5, 'name2', 0);
insert into tag(idx, article_idx, tag, `count`) values (9, 5, 'name3', 0);

insert into article_info(article_idx, `data`) values (1, 'data1-1');
insert into article_info(article_idx, `data`) values (1, 'data1-2');
insert into article_info(article_idx, `data`) values (1, 'data1-3');
insert into article_info(article_idx, `data`) values (1, 'data1-4');
insert into article_info(article_idx, `data`) values (1, 'data1-5');
insert into article_info(article_idx, `data`) values (1, 'data1-6');

insert into article_info(article_idx, `data`) values (2, 'data2-1');
insert into article_info(article_idx, `data`) values (2, 'data2-2');
insert into article_info(article_idx, `data`) values (2, 'data2-3');
insert into article_info(article_idx, `data`) values (2, 'data2-4');
insert into article_info(article_idx, `data`) values (2, 'data2-5');
insert into article_info(article_idx, `data`) values (2, 'data2-6');