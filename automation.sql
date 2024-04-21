create table UserAutomations
(
    Username varchar(255) not null
        primary key
);

-- auto-generated definition
create table Automation
(
    Id                   int auto_increment
        primary key,
    ParentUsername       varchar(255)         null,
    Type                 int        default 0 not null,
    DeleteAfterExecution tinyint(1) default 0 not null,
    constraint FK_Automation_UserAutomations_ParentUsername
        foreign key (ParentUsername) references UserAutomations (Username)
);

-- auto-generated definition
create table PriceThresholdAction
(
    Id              int           not null
        primary key,
    ThresholdPrice  double        not null,
    TransactionType int           not null,
    ThresholdType   int           not null,
    Ticker          longtext      not null,
    Quantity        int default 0 not null,
    constraint FK_PriceThresholdAction_Automation_Id
        foreign key (Id) references Automation (Id)
            on delete cascade
);

-- auto-generated definition
create table Dca
(
    Id              int           not null
        primary key,
    Ticker          longtext      not null,
    LastBuyTime     datetime(6)   null,
    BuyQuantity     int           not null,
    Frequency       int           not null,
    TransactionType int default 0 not null,
    constraint FK_Dca_Automation_Id
        foreign key (Id) references Automation (Id)
            on delete cascade
);



create index IX_Automation_ParentUsername
    on Automation (ParentUsername);