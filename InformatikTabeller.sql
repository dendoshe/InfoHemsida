<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> origin/devGoled
Create table Konto (
KontoID int identity primary key not null, 
Mejladress Nvarchar(100) unique not null, 
Lösenord Nvarchar(100) not null,
Notis bit,
AdminFunktionalitet bit
);


Create table Kategori (
KategoriID int identity primary key not null,
Kategorinamn varchar(100)
);

Create table Blogginlägg (
InläggID int identity primary key not null,
Rubrik nvarchar(100),
BInnehåll text, 
Kategori int,
foreign key (Kategori) References Kategori(KategoriID)
);

Create table Möte (
MötesID int identity primary key not null, 
Tid time, 
Datum date,
Mötesledare int,
Deltagare int,
foreign key (Mötesledare) references Konto(KontoID),
foreign key (Deltagare) references Konto(KontoID)
);

Create table Anslag (
AnslagID int identity primary key not null, 
ARubrik nvarchar(100),
AInnehåll text,
Kategori int,
foreign key (Kategori) references Kategori(KategoriID)
);

Create table AnAn (
AnAn int identity not null,
Användare int not null,
Anslag int not null,
foreign key (Användare) references Konto(KontoID),
foreign key (Anslag) references Anslag(AnslagID)
);

Create table KoBl (
KoBlID int identity not null,
Inlägg int,
Skribent int, 
foreign key (Inlägg) references Blogginlägg(InläggID),
foreign key (Skribent) references Konto(KontoID)
);

Create table AnMö (
AnMö int identity not null,
Möte int,
Mötesledare int,
foreign key (Möte) references Möte(MötesID),
foreign key (Mötesledare) references Konto(KontoID)
);


<<<<<<< HEAD
=======
Create table Konto (
KontoID int identity primary key not null, 
Mejladress Nvarchar(100) unique not null, 
Lösenord Nvarchar(100) not null,
Notis bit,
AdminFunktionalitet bit
);


Create table Kategori (
KategoriID int identity primary key not null,
Kategorinamn varchar(100)
);

Create table Blogginlägg (
InläggID int identity primary key not null,
Rubrik nvarchar(100),
BInnehåll text, 
Kategori int,
foreign key (Kategori) References Kategori(KategoriID)
);

Create table Möte (
MötesID int identity primary key not null, 
Tid time, 
Datum date,
Mötesledare int,
Deltagare int,
foreign key (Mötesledare) references Konto(KontoID),
foreign key (Deltagare) references Konto(KontoID)
);

Create table Anslag (
AnslagID int identity primary key not null, 
ARubrik nvarchar(100),
AInnehåll text,
Kategori int,
foreign key (Kategori) references Kategori(KategoriID)
);

Create table AnAn (
AnAn int identity not null,
Användare int not null,
Anslag int not null,
foreign key (Användare) references Konto(KontoID),
foreign key (Anslag) references Anslag(AnslagID)
);

Create table KoBl (
KoBlID int identity not null,
Inlägg int,
Skribent int, 
foreign key (Inlägg) references Blogginlägg(InläggID),
foreign key (Skribent) references Konto(KontoID)
);

Create table AnMö (
AnMö int identity not null,
Möte int,
Mötesledare int,
foreign key (Möte) references Möte(MötesID),
foreign key (Mötesledare) references Konto(KontoID)
);

Create table KoMö (
Int möte,
Int deltagare,
Foreign key (möte) references möte(mötesID),
Foreign key (deltagare) references konto(kontoID)
);


>>>>>>> origin/master
=======
>>>>>>> origin/devGoled
