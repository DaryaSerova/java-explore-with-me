drop table IF EXISTS PARTICIPATION_REQUEST_T;
drop table IF EXISTS COMPILATION_EVENT_T;
drop table IF EXISTS EVENT_T;
drop table IF EXISTS CATEGORY_T;
drop table IF EXISTS LOCATION_T;
drop table IF EXISTS USER_T;
drop table IF EXISTS COMPILATION_T;
drop table IF EXISTS COMMENT_T;


create TABLE IF NOT EXISTS LOCATION_T (
	ID BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
	LAT FLOAT4,
	LON FLOAT4,
	CONSTRAINT LOCATION_T_PK PRIMARY KEY (ID)
);

create TABLE IF NOT EXISTS USER_T (
	ID BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
	NAME CHARACTER VARYING NOT NULL,
	EMAIL CHARACTER VARYING NOT NULL,
	CONSTRAINT USER_T_PK PRIMARY KEY (ID)
);

create TABLE IF NOT EXISTS COMMENT_T (
    ID BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
	CONTENT CHARACTER VARYING NOT NULL,
	WRITER_ID BIGINT NOT NULL,
	EVENT_ID BIGINT NOT NULL,
	CREATED_ON TIMESTAMP WITHOUT TIME ZONE,
	CONSTRAINT COMMENT_T_PK PRIMARY KEY (ID),
	CONSTRAINT COMMENT_T_FK FOREIGN KEY (WRITER_ID) REFERENCES USER_T(ID) ON delete CASCADE ON update CASCADE,
	CONSTRAINT COMMENT_T_FK_1 FOREIGN KEY (EVENT_ID) REFERENCES EVENT_T(ID) ON delete CASCADE ON update CASCADE
);

create TABLE IF NOT EXISTS CATEGORY_T (
    ID BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
	NAME CHARACTER VARYING NOT NULL,
	CONSTRAINT CATEGORY_T_PK PRIMARY KEY (ID)
);

create TABLE IF NOT EXISTS EVENT_T (

    ID BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    ANNOTATION CHARACTER VARYING(2000) NOT NULL,
    CATEGORY_ID BIGINT,
    CONFIRMED_REQUESTS NUMERIC,
    CREATED_ON TIMESTAMP WITHOUT TIME ZONE,
    DESCRIPTION CHARACTER VARYING(7000),
    EVENT_DATE TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    INITIATOR_ID BIGINT NOT NULL,
    LOCATION_ID BIGINT,
    PAID BOOLEAN NOT NULL,
    PARTICIPANT_LIMIT NUMERIC,
    PUBLISHED_ON TIMESTAMP WITHOUT TIME ZONE,
    REQUEST_MODERATION BOOLEAN,
    STATE CHARACTER VARYING,
    TITLE CHARACTER VARYING NOT NULL,
    VIEWS NUMERIC,
    CONSTRAINT EVENT_T_PK PRIMARY KEY (ID),
    CONSTRAINT EVENT_T_FK FOREIGN KEY (CATEGORY_ID) REFERENCES CATEGORY_T(ID) ON delete CASCADE ON update CASCADE,
    CONSTRAINT EVENT_T_FK_1 FOREIGN KEY (INITIATOR_ID) REFERENCES USER_T(ID) ON delete CASCADE ON update CASCADE,
    CONSTRAINT EVENT_T_FK_2 FOREIGN KEY (LOCATION_ID) REFERENCES LOCATION_T(ID) ON delete CASCADE ON update CASCADE
);

create TABLE IF NOT EXISTS COMPILATION_T (

    ID BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    PINNED BOOL,
    TITLE CHARACTER VARYING,
    CONSTRAINT COMPILATION_T_PK PRIMARY KEY (ID)
);

create TABLE IF NOT EXISTS COMPILATION_EVENT_T (
    ID BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    COMPILATION_ID BIGINT NOT NULL,
    EVENT_ID BIGINT NOT NULL,
    CONSTRAINT COMPILATION_EVENT_T_PK PRIMARY KEY (ID),
    CONSTRAINT COMPILATION_EVENT_T_FK FOREIGN KEY (COMPILATION_ID) REFERENCES COMPILATION_T(ID)
    ON delete CASCADE ON update CASCADE,
    CONSTRAINT COMPILATION_EVENT_T_FK_1 FOREIGN KEY (EVENT_ID) REFERENCES EVENT_T(ID)
    ON delete CASCADE ON update CASCADE
);

create TABLE IF NOT EXISTS PARTICIPATION_REQUEST_T (

     ID BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
     CREATED TIMESTAMP WITHOUT TIME ZONE,
     EVENT_ID BIGINT NOT NULL,
     REQUESTER_ID BIGINT NOT NULL,
     STATUS CHARACTER VARYING NOT NULL,
     CONSTRAINT PARTICIPATION_REQUEST_T_PK PRIMARY KEY (ID),
     CONSTRAINT PARTICIPATION_REQUEST_T_FK FOREIGN KEY (EVENT_ID) REFERENCES EVENT_T(ID) ON delete CASCADE ON update CASCADE,
     CONSTRAINT PARTICIPATION_REQUEST_T_FK_1 FOREIGN KEY (REQUESTER_ID) REFERENCES USER_T(ID) ON delete CASCADE ON update CASCADE
);


