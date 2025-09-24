-- ToyProject > script.sql

-- 회원 테이블
drop table tblUser;

create table tblUser (
    id varchar2(50) primary key,                    --아이디(PK)
    pw varchar2(50) not null,                       --암호
    name varchar2(50) not null,                     --이름
    email varchar2(100) not null,                   --이메일
    lv number(1) not null,                          --등급(1-일반, 2-관리자)
    pic varchar2(100) default 'pic.png' not null,   --프로필사진
    intro varchar2(500) null,                       --자기소개
    regdate date default sysdate not null,          --가입날짜
    ing number(1) default 1 not null                --활동유무(1-활동,0-탈퇴)
);

-- 일반 계정
insert into tblUser (id, pw, name, email, lv, pic, intro, regdate, ing)
    values ('hong', '1111', '홍길동', 'hong@gmail.com', 1, default, '반갑습니다.'
            , default, default);

-- 관리자 계정
insert into tblUser (id, pw, name, email, lv, pic, intro, regdate, ing)
    values ('tiger', '1111', '호랑이', 'tiger@gmail.com', 2, default
            , '관리자입니다.', default, default);

select * from tblUser;

commit;


-- 게시판 테이블
create table tblBoard (
    seq number primary key,                         --번호(PK)
    subject varchar2(300) not null,                 --제목
    content varchar2(4000) not null,                --내용
    id varchar2(50) not null references tblUser(id),--아이디(FK)
    regdate date default sysdate not null,          --작성날짜
    readcount number default 0 not null             --조회수
);
create sequence seqBoard;

select * from tblBoard;





create or replace view vwBoard
as
select 
    seq, subject, id, readcount, regdate, content,
    (select name from tblUser where id = tblBoard.id) as name,
<<<<<<< HEAD
    (sysdate - regdate) as isnew,
    (select count(*) from tblComment where bseq = tblBoard.seq) as commentCount,
    secret, notice
from tblBoard
    where notice = 0
        --order by notice desc, seq desc;
        order by seq desc;


create or replace view vwNotice
as
select 
    seq, subject, id, readcount, regdate, content,
    (select name from tblUser where id = tblBoard.id) as name,
    (sysdate - regdate) as isnew,
    (select count(*) from tblComment where bseq = tblBoard.seq) as commentCount,
    secret, notice
from tblBoard
    where notice = 1
        order by seq desc;


select * from vwBoard;
select * from vwNotice;

-- SQL > 식별자
select commentCount from vwBoard;
select commentcount from vwBoard;


=======
    (sysdate - regdate) as isnew
from tblBoard 
    order by seq desc;


select * from vwBoard;
>>>>>>> 3a4bd4c01b2e9baccffede7fea274ecf8637b0ac

update tblBoard set regdate = regdate - 5 where seq = 1;
update tblBoard set regdate = regdate - 3.5 where seq = 2;
update tblBoard set regdate = regdate - 2.3 where seq = 3;
update tblBoard set regdate = regdate - 1.4 where seq = 4;
commit;

select count(*) from tblBoard; --262

<<<<<<< HEAD
select * from (select a.*, rownum as rnum from vwBoard a)
    where rnum between 1 and 10;

delete from tblBoard where seq > 45;    
commit;



-- 댓글 테이블
create table tblComment (
    seq number primary key,                         --번호(PK)
    content varchar2(2000) not null,                --댓글
    id varchar2(50) not null references tblUser(id),--아이디(FK)
    regdate date default sysdate not null,          --작성날짜
    bseq number not null references tblBoard(seq)   --부모글(FK)
);
create sequence seqComment;

select * from tblComment;
=======
select * from vwBoard;





>>>>>>> 3a4bd4c01b2e9baccffede7fea274ecf8637b0ac




<<<<<<< HEAD
drop table tblComment;
drop table tblBoard;
drop table tblUser;


select * from (select 
    tblComment.*, 
    (select name from tblUser where id = tblComment.id) as name
from tblComment 
    where bseq = 321 
        order by seq desc) where rownum <= 10;


select * from (select a.*, rownum as rnum from (select 
    tblComment.*, 
    (select name from tblUser where id = tblComment.id) as name
from tblComment 
    where bseq = 321 
        order by seq desc) a ) where rnum between 1 and 1 + 4;




drop table tblComment;
drop table tblBoard;


-- 게시판 테이블
create table tblBoard (
    seq number primary key,                         --번호(PK)
    subject varchar2(300) not null,                 --제목
    content varchar2(4000) not null,                --내용
    id varchar2(50) not null references tblUser(id),--아이디(FK)
    regdate date default sysdate not null,          --작성날짜
    readcount number default 0 not null,            --조회수
    attach varchar2(300) null,                      --첨부파일
    secret number(1) not null,                      --비밀글(0-공개, 1-비밀)
    notice number(1) not null                       --공지글(0-일반, 1-공지)
);

select * from tblBoard;

alter table tblBoard 
    add (secret number(1) default 0 not null);


alter table tblBoard 
    add (notice number(1) default 0 not null);
    

-- 해시 태그
create table tblHashtag (
    seq number primary key,                 --번호(PK)
    hashtag varchar2(100) unique not null   --해시태그(UQ)
);
create sequence seqHashtag;

-- 연결
create table tblTagging (
    seq number primary key,                         --번호(PK)
    bseq number not null references tblBoard(seq),  --글번호(FK)
    hseq number not null references tblHashtag(seq) --태그번호(FK)
);
create sequence seqTagging;


select * from tblHashtag;

select * from tblTagging;




select h.hashtag
from tblBoard b 
    inner join tblTagging t
        on b.seq = t.bseq
            inner join tblHashtag h
                on h.seq = t.hseq
    where b.seq = 403;

select * 
from (select a.*, rownum as rnum 
            from vwBoard a ) b
                inner join tblTagging t
                    on b.seq = t.bseq
                        inner join tblHashtag h
                            on h.seq = t.hseq
                                where rnum between 1 and 100 and h.hashtag = '우중충';



create table tblScrapBook (
    seq number primary key,                         --번호(PK)
    bseq number not null references tblBoard(seq),  --게시물 번호(FK)
    id varchar2(50) not null references tblUser(id),--아이디(FK)
    regdate date default sysdate not null           --날짜
);
create sequence seqScrapBook;

select * from tblScrapBook;



select 
    tblBoard.*, (select name from tblUser where id = tblBoard.id) as name,
    (select count(*) from tblScrapBook 
        where bseq = tblBoard.seq and id = ?) as scrapbook
from tblBoard;



select * from vwBoard where 내가 즐겨찾기한 글만;

select * from vwBoard 
    where seq in (select bseq from tblScrapBook where id = 'dog');


-- 로그 테이블
create table tblLog (
    seq number primary key,                         --번호(PK)
    id varchar2(50) not null references tblUser(id),--아이디(FK)
    regdate date default sysdate not null,          --방문시각
    url varchar2(300) not null                      --주소    
);
create sequence seqLog;

select * from tblLog;



-- 한달간 > 로그인한 날짜? 날짜(글쓴 횟수), 날짜(댓글쓴 횟수)

select
    to_char(regdate, 'yyyy-mm-dd' ) as regdate,
    count(*) as cnt, -- 로그인 기록
    (select count(*) from tblBoard
        where to_char(regdate, 'yyyy-mm-dd')
            = to_char(l.regdate, 'yyyy-mm-dd') and id ='hong') as bcnt,
    (select count(*) from tblBoard
        where to_char(regdate, 'yyyy-mm-dd')
            = to_char(l.regdate, 'yyyy-mm-dd') and id ='hong') as ccnt
from tblLog l
    where to_char(regdate, 'yyyy-mm') = '2025-09' and id = 'hong'
        group by to_char(regdate, 'yyyy-mm-dd');

select * from tblLog;

--더미(고양이)
--로그인 기록
insert into tblLog (seq, id, regdate, url)
    values (seqLog.nextVal, 'cat', sysdate - 35, '.toy.index.do');
insert into tblLog (seq, id, regdate, url)
    values (seqLog.nextVal, 'cat', sysdate - 33, '.toy.index.do');
insert into tblLog (seq, id, regdate, url)
    values (seqLog.nextVal, 'cat', sysdate - 30, '.toy.index.do');
insert into tblLog (seq, id, regdate, url)
    values (seqLog.nextVal, 'cat', sysdate - 27, '.toy.index.do');
insert into tblLog (seq, id, regdate, url)
    values (seqLog.nextVal, 'cat', sysdate - 20, '.toy.index.do');
insert into tblLog (seq, id, regdate, url)
    values (seqLog.nextVal, 'cat', sysdate - 19, '.toy.index.do');
insert into tblLog (seq, id, regdate, url)
    values (seqLog.nextVal, 'cat', sysdate - 14, '.toy.index.do');
insert into tblLog (seq, id, regdate, url)
    values (seqLog.nextVal, 'cat', sysdate - 12, '.toy.index.do');
insert into tblLog (seq, id, regdate, url)
    values (seqLog.nextVal, 'cat', sysdate - 8, '.toy.index.do');
insert into tblLog (seq, id, regdate, url)
    values (seqLog.nextVal, 'cat', sysdate - 2, '.toy.index.do');


commit;

select * from tblBoard;

select * from tblComment;

insert into tblBoard
    (seq, subject, content, id, regdate, readcount, attach, secret, notice)
values
    (seqBoard.nextVal, '게시판입니다.', '내용', 'cat', sysdate - 27, 0, null, 0, 0);
insert into tblBoard
    (seq, subject, content, id, regdate, readcount, attach, secret, notice)
values
    (seqBoard.nextVal, '게시판입니다.', '내용', 'cat', sysdate - 20, 0, null, 0, 0);
insert into tblBoard
    (seq, subject, content, id, regdate, readcount, attach, secret, notice)
values
    (seqBoard.nextVal, '게시판입니다.', '내용', 'cat', sysdate - 18, 0, null, 0, 0);
insert into tblBoard
    (seq, subject, content, id, regdate, readcount, attach, secret, notice)
values
    (seqBoard.nextVal, '게시판입니다.', '내용', 'cat', sysdate - 14, 0, null, 0, 0);
insert into tblBoard
    (seq, subject, content, id, regdate, readcount, attach, secret, notice)
values
    (seqBoard.nextVal, '게시판입니다.', '내용', 'cat', sysdate - 14, 0, null, 0, 0);
insert into tblBoard
    (seq, subject, content, id, regdate, readcount, attach, secret, notice)
values
    (seqBoard.nextVal, '게시판입니다.', '내용', 'cat', sysdate - 8, 0, null, 0, 0);
insert into tblBoard
    (seq, subject, content, id, regdate, readcount, attach, secret, notice)
values
    (seqBoard.nextVal, '게시판입니다.', '내용', 'cat', sysdate - 8, 0, null, 0, 0);
insert into tblBoard
    (seq, subject, content, id, regdate, readcount, attach, secret, notice)
values
    (seqBoard.nextVal, '게시판입니다.', '내용', 'cat', sysdate - 8, 0, null, 0, 0);
insert into tblBoard
    (seq, subject, content, id, regdate, readcount, attach, secret, notice)
values
    (seqBoard.nextVal, '게시판입니다.', '내용', 'cat', sysdate - 8, 0, null, 0, 0);
insert into tblBoard
    (seq, subject, content, id, regdate, readcount, attach, secret, notice)
values
    (seqBoard.nextVal, '게시판입니다.', '내용', 'cat', sysdate - 2, 0, null, 0, 0);
    
commit;

insert into tblComment (seq, content, id, regdate, bseq)
    values (seqComment.nextVal, '댓글 내용', 'cat', sysdate - 30, 360);

insert into tblComment (seq, content, id, regdate, bseq)
    values (seqComment.nextVal, '댓글 내용', 'cat', sysdate - 27, 360);
    
insert into tblComment (seq, content, id, regdate, bseq)
    values (seqComment.nextVal, '댓글 내용', 'cat', sysdate - 27, 360);
    
insert into tblComment (seq, content, id, regdate, bseq)
    values (seqComment.nextVal, '댓글 내용', 'cat', sysdate - 20, 360);
    
insert into tblComment (seq, content, id, regdate, bseq)
    values (seqComment.nextVal, '댓글 내용', 'cat', sysdate - 20, 360);
    
insert into tblComment (seq, content, id, regdate, bseq)
    values (seqComment.nextVal, '댓글 내용', 'cat', sysdate - 19, 360);

insert into tblComment (seq, content, id, regdate, bseq)
    values (seqComment.nextVal, '댓글 내용', 'cat', sysdate - 14, 360);

insert into tblComment (seq, content, id, regdate, bseq)
    values (seqComment.nextVal, '댓글 내용', 'cat', sysdate - 14, 360);

insert into tblComment (seq, content, id, regdate, bseq)
    values (seqComment.nextVal, '댓글 내용', 'cat', sysdate - 12, 360);

insert into tblComment (seq, content, id, regdate, bseq)
    values (seqComment.nextVal, '댓글 내용', 'cat', sysdate - 12, 360);

insert into tblComment (seq, content, id, regdate, bseq)
    values (seqComment.nextVal, '댓글 내용', 'cat', sysdate - 8, 360);
    
insert into tblComment (seq, content, id, regdate, bseq)
    values (seqComment.nextVal, '댓글 내용', 'cat', sysdate - 2, 360);

--더미(홍길동)
--로그인 기록
insert into tblLog (seq, id, regdate, url)
    values (seqLog.nextVal, 'hong', sysdate - 30, '.toy.index.do');
insert into tblLog (seq, id, regdate, url)
    values (seqLog.nextVal, 'hong', sysdate - 29, '.toy.index.do');
insert into tblLog (seq, id, regdate, url)
    values (seqLog.nextVal, 'hong', sysdate - 27, '.toy.index.do');
insert into tblLog (seq, id, regdate, url)
    values (seqLog.nextVal, 'hong', sysdate - 24, '.toy.index.do');
insert into tblLog (seq, id, regdate, url)
    values (seqLog.nextVal, 'hong', sysdate - 22, '.toy.index.do');
insert into tblLog (seq, id, regdate, url)
    values (seqLog.nextVal, 'hong', sysdate - 19, '.toy.index.do');
insert into tblLog (seq, id, regdate, url)
    values (seqLog.nextVal, 'hong', sysdate - 15, '.toy.index.do');
insert into tblLog (seq, id, regdate, url)
    values (seqLog.nextVal, 'hong', sysdate - 12, '.toy.index.do');
insert into tblLog (seq, id, regdate, url)
    values (seqLog.nextVal, 'hong', sysdate - 9, '.toy.index.do');
insert into tblLog (seq, id, regdate, url)
    values (seqLog.nextVal, 'hong', sysdate - 2, '.toy.index.do');


select * from tblBoard;

insert into tblBoard
    (seq, subject, content, id, regdate, readcount, attach, secret, notice)
values
    (seqBoard.nextVal, '게시판입니다.', '내용', 'hong', sysdate - 30, 0, null, 0, 0);
insert into tblBoard
    (seq, subject, content, id, regdate, readcount, attach, secret, notice)
values
    (seqBoard.nextVal, '게시판입니다.', '내용', 'hong', sysdate - 27, 0, null, 0, 0);
insert into tblBoard
    (seq, subject, content, id, regdate, readcount, attach, secret, notice)
values
    (seqBoard.nextVal, '게시판입니다.', '내용', 'hong', sysdate - 27, 0, null, 0, 0);
insert into tblBoard
    (seq, subject, content, id, regdate, readcount, attach, secret, notice)
values
    (seqBoard.nextVal, '게시판입니다.', '내용', 'hong', sysdate - 27, 0, null, 0, 0);
insert into tblBoard
    (seq, subject, content, id, regdate, readcount, attach, secret, notice)
values
    (seqBoard.nextVal, '게시판입니다.', '내용', 'hong', sysdate - 19, 0, null, 0, 0);
insert into tblBoard
    (seq, subject, content, id, regdate, readcount, attach, secret, notice)
values
    (seqBoard.nextVal, '게시판입니다.', '내용', 'hong', sysdate - 15, 0, null, 0, 0);
insert into tblBoard
    (seq, subject, content, id, regdate, readcount, attach, secret, notice)
values
    (seqBoard.nextVal, '게시판입니다.', '내용', 'hong', sysdate - 15, 0, null, 0, 0);
insert into tblBoard
    (seq, subject, content, id, regdate, readcount, attach, secret, notice)
values
    (seqBoard.nextVal, '게시판입니다.', '내용', 'hong', sysdate - 9, 0, null, 0, 0);
insert into tblBoard
    (seq, subject, content, id, regdate, readcount, attach, secret, notice)
values
    (seqBoard.nextVal, '게시판입니다.', '내용', 'hong', sysdate - 9, 0, null, 0, 0);
insert into tblBoard
    (seq, subject, content, id, regdate, readcount, attach, secret, notice)
values
    (seqBoard.nextVal, '게시판입니다.', '내용', 'hong', sysdate - 2, 0, null, 0, 0);


insert into tblComment (seq, content, id, regdate, bseq)
    values (seqComment.nextVal, '댓글 내용', 'hong', sysdate - 30, 360);

insert into tblComment (seq, content, id, regdate, bseq)
    values (seqComment.nextVal, '댓글 내용', 'hong', sysdate - 29, 360);
    
insert into tblComment (seq, content, id, regdate, bseq)
    values (seqComment.nextVal, '댓글 내용', 'hong', sysdate - 27, 360);
    
insert into tblComment (seq, content, id, regdate, bseq)
    values (seqComment.nextVal, '댓글 내용', 'hong', sysdate - 27, 360);
    
insert into tblComment (seq, content, id, regdate, bseq)
    values (seqComment.nextVal, '댓글 내용', 'hong', sysdate - 20, 360);
    
insert into tblComment (seq, content, id, regdate, bseq)
    values (seqComment.nextVal, '댓글 내용', 'hong', sysdate - 15, 360);

insert into tblComment (seq, content, id, regdate, bseq)
    values (seqComment.nextVal, '댓글 내용', 'hong', sysdate - 15, 360);

insert into tblComment (seq, content, id, regdate, bseq)
    values (seqComment.nextVal, '댓글 내용', 'hong', sysdate - 12, 360);

insert into tblComment (seq, content, id, regdate, bseq)
    values (seqComment.nextVal, '댓글 내용', 'hong', sysdate - 12, 360);

insert into tblComment (seq, content, id, regdate, bseq)
    values (seqComment.nextVal, '댓글 내용', 'hong', sysdate - 12, 360);

insert into tblComment (seq, content, id, regdate, bseq)
    values (seqComment.nextVal, '댓글 내용', 'hong', sysdate - 9, 360);
    
insert into tblComment (seq, content, id, regdate, bseq)
    values (seqComment.nextVal, '댓글 내용', 'hong', sysdate - 2, 360);


select 
    count(*) as cnt,
    url
from tblLog
    where id = 'hong'
        group by url
            order by count(*) desc;
