# Viikkotehtävä 5: Sääsovellus (Retrofit + Room Cache + Compose)

Tämä projekti laajentaa aiempaa sääsovellusta lisäämällä siihen **Room-tietokannan**, jota käytetään säädatan välimuistina ja hakuhistorian tallentamiseen.

## Room-arkkitehtuuri ja projektin rakenne

Sovellus noudattaa Googlen suosittelemaa arkkitehtuuria:
- **Entity (`WeatherEntity.kt`)**: Määrittää tietokantataulun rakenteen (kaupunki, lämpötila, aikaleima jne.).
- **DAO (`WeatherDao.kt`)**: Interface, joka sisältää SQL-kyselyt (haku, lisäys, poisto).
- **Database (`AppDatabase.kt`)**: Room-tietokannan pääpiste ja singleton-alustus.
- **Repository (`WeatherRepository.kt`)**: Toimii sovelluksen tietolähteenä. Se päättää, haetaanko data verkosta (Retrofit) vai tietokannasta (Room).
- **ViewModel (`WeatherViewModel.kt`)**: Hallitsee UI-tilaa ja tarjoaa säädatan Flow-muodossa.
- **UI (Jetpack Compose)**: Reagoi ViewModelin tilamuutoksiin ja näyttää datan käyttäjälle.

## Miten datavirta kulkee?

1. Käyttäjä syöttää kaupungin ja painaa "Hae sää".
2. **ViewModel** kutsuu **Repositorya**.
3. **Repository** tarkistaa ensin **Room-välimuistin**:
   - Jos data löytyy ja se on alle 30 minuuttia vanhaa -> palautetaan välimuisti.
   - Jos dataa ei ole tai se on vanhaa -> haetaan uusi data **Retrofitin** avulla API:sta.
4. Uusi API-data tallennetaan Roomiin (korvataan vanha tai lisätään uusi).
5. **WeatherDao** tarjoaa Flow-listauksen kaikista tallennetuista kaupungeista.
6. **ViewModel** kerää tämän Flow'n ja Compose UI päivittää **Hakuhistoria**-listan automaattisesti.

## Välimuistilogiikka (Caching)

Sovellukseen on toteutettu yksinkertainen mutta tehokas välimuisti:
- Kun sää haetaan, tallennamme nykyisen ajan (`System.currentTimeMillis()`) `WeatherEntity`-olioon.
- Ennen verkkokutsua tarkistamme: `currentTime - cachedTime < 30 minuuttia`.
- Jos ehto täyttyy, vältämme turhan API-kutsun ja käytämme paikallista dataa.
- Jos verkkoa ei ole saatavilla, sovellus yrittää näyttää viimeisimmän välimuistissa olevan datan virheen sijaan.

## Tekniikat yhteenvetona
- **Retrofit & Gson**: Rajapintapyynnöt ja JSON-muunnos.
- **Room**: Paikallinen SQLite-tietokanta.
- **Coroutines & Flow**: Asynkroninen datan käsittely ja reaaliaikainen UI-päivitys.
- **Jetpack Compose**: Moderni ja reaktiivinen käyttöliittymä.
- **BuildConfig**: API-avaimen turvallinen hallinta.

---
