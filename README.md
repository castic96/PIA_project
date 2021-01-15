# PIA_project
Semestrální práce z předmětu KIV/PIA, 2020/2021. Cílem práce bylo implementovat online verzi Piškvorek (https://github.com/osvetlik/pia2020/tree/master/semester-project).

## Sestavení a spuštění aplikace
Pro sestavení a spuštění aplikace je nutné provést následující kroky:
1. provést `checkout` projektu z GITu
2. spustit Docker
3. v root složce projektu zadat příkaz přes Příkazový řádek: `docker build -t pia/zcastora .` (pro build aplikace)
4. v root složce projektu zadat příkaz přes Příkazový řádek: `docker-compose up` (pro spuštění aplikace a databáze)

Aplikace bude přístupná na adrese: http://localhost:8080/

Příkaz pro odstranění docker containeru s aplikací a databází: `docker-compose down` 

(v případě odstranění DB volume s daty: `docker-compose down -v`)

## Uživatelské účty
Po prvním spuštění aplikace jsou v databázi vytvořeny následující uživatelské účty:

|   Login             | Heslo    |
| :-----------------: | :------: |
|  admin@example.com  |  admin   |
| user1@example.com   |  user1   |
| user2@example.com   |  user2   |

## Realizace
### Povinná část
V rámci implementace byly realizovány všechny body povinné části. V konstantách programu je možné nastavit velikost hracího pole a počet symbolů vítězné řady. Výchozí nastavení: pole 5 x 5, 4 symboly vítězné řady.

### Rozšiřující část
Z rošiřující části bylo implementováno:
* in-game chat
* generování kódu přes OpenAPI/Swagger
