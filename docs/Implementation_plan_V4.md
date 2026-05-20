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

### 3.1 [HOTOVO] Access control a ownership pravidlá

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

### 4. [HOTOVO / DOLADENIE] Exception handling a chybové hlásenia

- vlastné výnimky
- globálne spracovanie výnimiek
- jednotná error response
- doladenie chybových odpovedí pre security a API

### 5. [HOTOVO] React frontend

- inicializácia projektu cez Vite
- základná štruktúra frontend projektu
- App rozdelená podľa dashboardov
- zobrazenie podľa role
- login obrazovka
- logout
- obnova session po refreshi
- klientský management vo frontende
- vyčlenenie view logiky dashboardov

### 5.1 [HOTOVO] React ↔ backend integrácia

- prepojenie React ↔ Spring Boot
- API login
- načítanie aktuálne prihláseného používateľa
- role-based zobrazenie
- klientská časť CRUD/search/detail/update/delete
- session flow pre frontend

### 6. [HOTOVO] Modul permanentiek

- MembershipEntity
- MembershipRepository
- MembershipService
- MembershipController
- MembershipCreateRequest
- MembershipResponse
- MembershipStatus
- MembershipDuration
- vytvorenie alebo predĺženie permanentky
- prepojenie permanentky na klienta
- platnosť od / do
- stav permanentky
- načítanie permanentky podľa klienta
- načítanie vlastnej permanentky pre CLIENT rolu
- príprava permanentky na kontrolu pri vstupe
- frontend API vrstva pre permanentky
- frontend správa permanentiek

### 7. [HOTOVO] Modul vstupov a odchodov

- EntryEntity
- EntryRepository
- EntryService
- EntryController
- EntryStatus
- Reason
- EntryMethod
- evidencia príchodu klienta
- evidencia odchodu klienta
- kontrola platnej permanentky pri vstupe
- evidencia povolených aj zamietnutých vstupov
- ochrana pred viacerými aktívnymi vstupmi naraz
- evidencia spôsobu vstupu a odchodu
- načítanie aktuálne prítomných klientov
- frontend API vrstva pre vstupy
- frontend zobrazenie aktuálne prítomných klientov

### 8. [ROZPRACOVANÉ] QR modul

- QR token v ClientEntity
- automatické generovanie QR tokenu pri registrácii klienta
- QrService
- QrController
- QrCodeResponse
- QrCodeShowRequest
- QrTokenResetRequest
- získanie QR údajov klienta podľa ID
- zobrazenie QR po overení bezpečnostnej otázky
- reset QR tokenu administrátorom
- frontend API vrstva pre QR
- komponent na zobrazenie QR kódu
- zobrazenie QR kódu v administrátorskej správe klientov
- zobrazenie QR kódu v zamestnaneckej správe klientov
- získanie vlastného QR kódu pre CLIENT rolu

### 9. [ROZPRACOVANÉ] QR vstup / odchod a scan logika

- EntryQrRequest
- EntryScanController
- EntryScanService
- vstup klienta cez QR token
- odchod klienta cez QR token
- napojenie QR tokenu na existujúcu entry logiku
- rozhodovanie medzi príchodom a odchodom podľa aktívneho vstupu
- ošetrenie neplatného QR tokenu
- ošetrenie klienta bez platnej permanentky
- Postman testy QR vstupu a odchodu
- neskôr frontend simulácia QR scanovania

### 10. [PLÁN] Štatistiky a história

- história vstupov klienta
- história permanentiek
- štatistiky návštevnosti
- štatistiky podľa klienta
- prehľad aktívnych a expirovaných permanentiek
- vlastné štatistiky pre CLIENT rolu
- štatistiky pre ADMIN / EMPLOYEE

### 11. [PLÁN] UI / UX doladenie frontend aplikácie

- Bootstrap rozloženie stránok
- zjednotenie kariet, tabuliek a tlačidiel
- responzívne zobrazenie
- lepšie rozdelenie dashboardov
- vizuálne oddelenie sekcií
- úprava formulárov
- doladenie detailov klienta, permanentky, vstupov a QR kódu
- zlepšenie používateľskej prehľadnosti aplikácie



