<<<<<<< HEAD
Create table Konto (
KontoID int identity primary key not null, 
Mejladress Nvarchar(100) unique not null, 
L�senord Nvarchar(100) not null,
Notis bit,
AdminFunktionalitet bit
);


Create table Kategori (
KategoriID int identity primary key not null,
Kategorinamn varchar(100)
);

Create table Blogginl�gg (
Inl�ggID int identity primary key not null,
Rubrik nvarchar(100),
BInneh�ll text, 
Kategori int,
foreign key (Kategori) References Kategori(KategoriID)
);

Create table M�te (
M�tesID int identity primary key not null, 
Tid time, 
Datum date,
M�tesledare int,
Deltagare int,
foreign key (M�tesledare) references Konto(KontoID),
foreign key (Deltagare) references Konto(KontoID)
);

Create table Anslag (
AnslagID int identity primary key not null, 
ARubrik nvarchar(100),
AInneh�ll text,
Kategori int,
foreign key (Kategori) references Kategori(KategoriID)
);

Create table AnAn (
AnAn int identity not null,
Anv�ndare int not null,
Anslag int not null,
foreign key (Anv�ndare) references Konto(KontoID),
foreign key (Anslag) references Anslag(AnslagID)
);

Create table KoBl (
KoBlID int identity not null,
Inl�gg int,
Skribent int, 
foreign key (Inl�gg) references Blogginl�gg(Inl�ggID),
foreign key (Skribent) references Konto(KontoID)
);

Create table AnM� (
AnM� int identity not null,
M�te int,
M�tesledare int,
foreign key (M�te) references M�te(M�tesID),
foreign key (M�tesledare) references Konto(KontoID)
);


=======
Create table Konto (
KontoID int identity primary key not null, 
Mejladress Nvarchar(100) unique not null, 
L�senord Nvarchar(100) not null,
Notis bit,
AdminFunktionalitet bit
);


Create table Kategori (
KategoriID int identity primary key not null,
Kategorinamn varchar(100)
);

Create table Blogginl�gg (
Inl�ggID int identity primary key not null,
Rubrik nvarchar(100),
BInneh�ll text, 
Kategori int,
foreign key (Kategori) References Kategori(KategoriID)
);

Create table M�te (
M�tesID int identity primary key not null, 
Tid time, 
Datum date,
M�tesledare int,
Deltagare int,
foreign key (M�tesledare) references Konto(KontoID),
foreign key (Deltagare) references Konto(KontoID)
);

Create table Anslag (
AnslagID int identity primary key not null, 
ARubrik nvarchar(100),
AInneh�ll text,
Kategori int,
foreign key (Kategori) references Kategori(KategoriID)
);

Create table AnAn (
AnAn int identity not null,
Anv�ndare int not null,
Anslag int not null,
foreign key (Anv�ndare) references Konto(KontoID),
foreign key (Anslag) references Anslag(AnslagID)
);

Create table KoBl (
KoBlID int identity not null,
Inl�gg int,
Skribent int, 
foreign key (Inl�gg) references Blogginl�gg(Inl�ggID),
foreign key (Skribent) references Konto(KontoID)
);

Create table AnM� (
AnM� int identity not null,
M�te int,
M�tesledare int,
foreign key (M�te) references M�te(M�tesID),
foreign key (M�tesledare) references Konto(KontoID)
);

Create table KoM� (
Int m�te,
Int deltagare,
Foreign key (m�te) references m�te(m�tesID),
Foreign key (deltagare) references konto(kontoID)
);


>>>>>>> origin/master

ALTER TABLE Anslag (
ADD Filnamn VARCHAR(15),
Fil varbinary(max),
Filformat VARCHAR(15)
);