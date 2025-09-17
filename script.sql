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
    seq, subject, id, readcount, regdate,
    (select name from tblUser where id = tblBoard.id) as name,
    (sysdate - regdate) as isnew
from tblBoard 
    order by seq desc;


select * from vwBoard;









