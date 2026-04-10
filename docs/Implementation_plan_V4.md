### 1. [HOTOVO] Databáza a základ backendu

- DB pripojenie
- konfigurácia JPA
- návrh entít a repozitárov
- otestovanie pripojenia

### 2. [HOTOVO] Klient modul

- registrácia klienta
- CRUD operácie hotové
- vyhľadávanie podľa mena, priezviska, emailu
- detail klienta
- update klienta
- delete klienta
- validácie
- Postman testy
- Frontend napojenie klientského modulu

### 3. [HOTOVO] Používateľský modul a bezpečnosť

- UserEntity
- UserRepository
- ClientEntity pre väzbu na používateľský účet
- väzba User ↔ Client
- registrácia používateľa
- registrácia zamestnanca
- admin inicializácia
- generovanie dočasného hesla (SecureRandom)
- hashovanie hesla
- reset hesla
- bezpečnostná otázka + hashovaná odpoveď
- overenie odpovede pri resete hesla
- roly ADMIN / EMPLOYEE / CLIENT
- login
- logout
- session autentifikácia
- Spring Security konfigurácia
- základné prístupové pravidlá

### 3.1 [ROZPRACOVANÉ] Access control a ownership pravidlá

#### ADMIN:

- vidí všetkých klientov
- spravuje klientov, permanentky, vstupy
- má plný prístup

#### EMPLOYEE:

- vidí všetkých klientov
- spravuje prevádzkové veci (klienti, vstupy, permanentky)
- nemá prístup k admin-only funkciám (napr. správa používateľov)

#### CLIENT:

- vidí iba svoj profil
- vidí iba svoju permanentku
- vidí iba svoje vstupy
- vidí iba svoje štatistiky

- doladenie security pravidiel pre ďalšie moduly

### 4. [HOTOVO / DOLADENIE] Exception handling a chybové hlásenia

- vlastné výnimky
- globálne spracovanie výnimiek
- jednotná error response
- doladenie chybových odpovedí pre security a API

### 5. [ROZPRACOVANÉ] React frontend

- inicializácia projektu cez Vite
- základná štruktúra frontend projektu
- App rozdelená podľa dashboardov
- zobrazenie podľa role
- login obrazovka
- logout
- obnova session po refreshi
- klientský management vo frontende
- vyčlenenie view logiky dashboardov

### 5.1 [HOTOVO / ROZPRACOVANÉ] React ↔ backend integrácia

- prepojenie React ↔ Spring Boot
- API login
- načítanie aktuálne prihláseného používateľa
- role-based zobrazenie
- klientská časť CRUD/search/detail/update/delete
- session flow pre frontend

### 6. [PLÁN] Modul permanentiek

- vytvorenie permanentky
- prepojenie na klienta
- platnosť od / do
- stav permanentky
- kontrola aktívnosti
- predĺženie permanentky
- príprava na kontrolu pri vstupe



